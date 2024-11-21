const apiUrl = "http://localhost:8080/api";
const userId = localStorage.getItem("userId");
const token = localStorage.getItem("token");

console.log("Token:", token);

// Utility function to clear container and set loading state
function clearContainer(message = "Loading...") {
    const newsContainer = document.getElementById("news-container");
    newsContainer.innerHTML = `<p>${message}</p>`;
}

// Fetch Articles
function fetchArticles() {
    clearContainer();
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
            displayArticles(articles);
        })
        .catch(error => {
            console.error("Fetch Articles Error:", error);
            clearContainer("Could not load articles. Please try again later.");
        });
}

// Fetch User History
function fetchUserHistory() {
    clearContainer();
    fetch(`${apiUrl}/users/${userId}/history`, {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch user history.");
            }
            return response.json();
        })
        .then(history => {
            displayUserHistory(history);
        })
        .catch(error => {
            console.error("Fetch History Error:", error);
            clearContainer("Could not load history. Please try again later.");
        });
}

// Fetch Favourites
function fetchFavourites() {
    clearContainer();
    fetch(`${apiUrl}/users/${userId}/favourites`, {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch favourites.");
            }
            return response.json();
        })
        .then(favourites => {
            const likedFavourites = favourites.filter(fav => fav.feedback === true);
            displayFavourites(likedFavourites);
        })
        .catch(error => {
            console.error("Fetch Favourites Error:", error);
            clearContainer("Could not load favourites. Please try again later.");
        });
}

// Display Articles
function displayArticles(articles) {
    const newsContainer = document.getElementById("news-container");
    newsContainer.innerHTML = "";

    if (articles.length === 0) {
        newsContainer.innerHTML = "<p>No articles found for your preferences.</p>";
        return;
    }

    articles.forEach(article => {
        const searchId = generateSearchId();
        const imageUrl = article.image || "https://via.placeholder.com/150?text=No+Image";

        const articleCard = document.createElement("div");
        articleCard.classList.add("news-card");

        articleCard.innerHTML = `
            <div class="news-image-container">
                <img src="${imageUrl}" alt="News Image" />
            </div>
            <div class="news-content">
                <div class="news-title">${article.title}</div>
                <div class="news-description">${article.description || article.content || ""}</div>
                <a href="${article.url}" target="_blank" class="view-button"
                   onclick="saveSearchHistory('${article.title}', '${article.url}', '${searchId}')">
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

// Display User History
function displayUserHistory(history) {
    const newsContainer = document.getElementById("news-container");
    newsContainer.innerHTML = "";

    if (history.length === 0) {
        newsContainer.innerHTML = "<p>No history found.</p>";
        return;
    }

    history.forEach(item => {
        const historyCard = document.createElement("div");
        historyCard.classList.add("news-card");

        historyCard.innerHTML = `
            <div class="news-content">
                <div class="news-title">${item.articleTitle}</div>
                <div class="news-description">
                    <a href="${item.articleUrl}" target="_blank" class="view-button">Read Again</a>
                </div>
            </div>
        `;

        newsContainer.appendChild(historyCard);
    });
}

// Display Favourites
function displayFavourites(favourites) {
    const newsContainer = document.getElementById("news-container");
    newsContainer.innerHTML = "";

    if (favourites.length === 0) {
        newsContainer.innerHTML = "<p>No liked articles found.</p>";
        return;
    }

    favourites.forEach(fav => {
        const favCard = document.createElement("div");
        favCard.classList.add("news-card");

        favCard.innerHTML = `
            <div class="news-content">
                <div class="news-title">${fav.searchHistory.articleTitle}</div>
                <div class="news-description">
                    <a href="${fav.searchHistory.articleUrl}" target="_blank" class="view-button">Read Again</a>
                </div>
            </div>
        `;

        newsContainer.appendChild(favCard);
    });
}

// Generate a unique search ID for each article view
function generateSearchId() {
    return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
}

// Save Search History
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
                throw new Error("Failed to save search history.");
            }
            console.log("Search history saved successfully.");
        })
        .catch(error => console.error("Error saving search history:", error));
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
                throw new Error("Failed to save feedback.");
            }
            console.log("Feedback saved successfully.");
        })
        .catch(error => console.error("Error saving feedback:", error));
}

// Event Listeners for Navigation Buttons
document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("news-btn").addEventListener("click", fetchArticles);
    document.getElementById("history-btn").addEventListener("click", fetchUserHistory);
    document.getElementById("favourites-btn").addEventListener("click", fetchFavourites);

    // Default view: News
    fetchArticles();
});

document.getElementById("log-btn").addEventListener("click", logout);

function logout() {
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
            localStorage.removeItem("token");
            localStorage.removeItem("userId");
            window.location.href = "home.html";
        });
}
