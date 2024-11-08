const apiUrl = "http://localhost:8080/api";
const userId = localStorage.getItem("userId");
const token = localStorage.getItem("token");

console.log("Token:", token);

function fetchArticles() {
    if (!userId || !token) {
        console.error("No user ID or token found. Please log in.");
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
        document.getElementById("news-container").innerHTML = "<p>Could not load articles. Please try again later.</p>";
    });
}

function displayArticles(articles) {
    const newsContainer = document.getElementById("news-container");
    newsContainer.innerHTML = ""; // Clear previous content

    if (articles.length === 0) {
        newsContainer.innerHTML = "<p>No articles found for your preferences.</p>";
        return;
    }

    articles.forEach(article => {
        const articleCard = document.createElement("div");
        articleCard.classList.add("news-card");

        articleCard.innerHTML = `
            <div class="news-image-container">
                <img src="${article.image}" alt="News Image" />

            </div>
            <div class="news-content">
                <div class="news-title">
                    ${article.title}
                </div>
                <div class="news-description">
                    ${article.description || article.content || ""}
                </div>
                <a href="${article.url}" target="_blank" class="view-button">Read More</a>
            </div>
        `;

        newsContainer.appendChild(articleCard);
    });
}

// Call fetchArticles on page load
document.addEventListener("DOMContentLoaded", fetchArticles);
