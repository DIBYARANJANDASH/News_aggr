const apiUrl = "http://localhost:8080/api";
const userId = localStorage.getItem("userId");
const token = localStorage.getItem("token");

console.log("Token:", token);

function fetchArticles() {
    if (!userId || !token) {
        console.error("No user ID or token found. Please log in.");
        window.location.href = "/login.html"; // Redirect if not authenticated
        return;
    }

    fetch(`${apiUrl}/articles/preferences/${userId}`, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch articles.");
            }
            return response.json();
        })
        .then(articles => {
        console.log(articles)
            displayArticles(articles);
        })
        .catch(error => {
            console.error("Fetch Articles Error:", error);
            document.getElementById("news-container").innerHTML = "<p>Could not load articles. Please try again later.</p>";
        });
}
// Map to store searchId against article title (or a unique key)
const searchIdMap = {};

function displayArticles(articles) {
            const newsContainer = document.getElementById("news-container");
            newsContainer.innerHTML = ""; // Clear previous content

            if (articles.length === 0) {
                newsContainer.innerHTML = "<p>No articles found for your preferences.</p>";
                return;
            }

            articles.forEach(article => {
                const searchId = generateSearchId(); // Generate a unique search ID for tracking

                const sanitizedTitle = article.title.replace(/'/g, "\\'");
                const sanitizedUrl = article.url.replace(/'/g, "\\'");
                const sanitizedDescription = (article.description || article.content || "").replace(/'/g, "\\'");

                const articleCard = document.createElement("div");
                articleCard.classList.add("news-card");

                articleCard.innerHTML = `
                    <div class="news-image-container">
                        <img src="${article.image}" alt = "News Image" />
                    </div>
                    <div class="news-content">
                        <div class="news-title">
                            ${sanitizedTitle}
                        </div>
                        <div class="news-description">
                            ${sanitizedDescription}
                        </div>
                        <a href="${sanitizedUrl}" target="_blank" class="view-button"
                           onclick="saveSearchHistory('${sanitizedTitle}', '${sanitizedUrl}', '${searchId}')">
                           Read More
                        </a>
                        <div class="feedback-buttons">
                            <button onclick="saveFeedback('${searchId}', true)">Like</button>
                            <button onclick="saveFeedback('${searchId}', false)">Dislike</button>
                        </div>
                    </div>
                `;

                newsContainer.appendChild(articleCard);
            });
        }


// Generate a unique search ID for each article view
function generateSearchId() {
    return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
}

function saveSearchHistory(title, url, searchId) {
     fetch(`${apiUrl}/users/${userId}/history`, {
         method: "POST",
         headers: {
             "Authorization": `Bearer ${token}`,
             "Content-Type": "application/json"
         },
         body: JSON.stringify({ title, url, searchId })
     })
     .then(response => {
         if (!response.ok) {
             throw new Error(`Failed to save search history. Status: ${response.status}`);
         }
         console.log("Search history saved successfully.");
     })
     .catch(error => console.error("Error saving search history:", error));
 }

 // Ensure search history is saved before feedback
 function handleFeedback(title, url, feedback) {
     const searchId = generateSearchId(); // Generate a new search ID

     saveSearchHistory(title, url, searchId); // Save history
     saveFeedback(searchId, feedback);        // Save feedback only after
 }


// Save Feedback
function saveFeedback(searchId, feedback) {
    console.log("Saving feedback with:", { searchId, feedback });

    fetch(`${apiUrl}/users/${userId}/favourites`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ searchId, feedback })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to save feedback. Status: ${response.status}`);
            }
            console.log("Feedback saved successfully.");
        })
        .catch(error => console.error("Error saving feedback:", error));
}


// Call fetchArticles on page load
document.addEventListener("DOMContentLoaded", fetchArticles);


document.addEventListener("DOMContentLoaded", () => {
    const logoutButton = document.getElementById("log-btn"); // Ensure the logout button has the correct ID

    if (logoutButton) {
        logoutButton.addEventListener("click", () => {
            logout(); // Call the logout function
        });
    }
});

function logout() {
    const token = localStorage.getItem("token");

    // Call backend logout API
    fetch(`${apiUrl}/logout`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.ok) {
            console.log("Logout successful");
        } else {
            console.error("Logout failed");
        }
    })
    .catch(error => console.error("Logout API Error:", error))
    .finally(() => {
        // Clear session and redirect
        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        redirectToHome();
    });
}

// Redirect to home page
function redirectToHome() {
    alert("Session expired or logged out. Redirecting to home page.");
    window.location.href = "home.html";
}

