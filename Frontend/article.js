const apiUrl = "http://localhost:8080/api";

// Fetch articles based on user preferences
function fetchArticles() {
    const userId = localStorage.getItem("userId");

    fetch(`${apiUrl}/articles/preferences/${userId}`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch articles.");
        }
        return response.json();
    })
    .then(articles => {
        const newsContainer = document.getElementById("news-container");
        newsContainer.innerHTML = "";

        articles.forEach(article => {
            const articleElement = document.createElement("div");
            articleElement.classList.add("article");
            articleElement.innerHTML = `
                <h2>${article.title}</h2>
                <p>${article.description}</p>
                <a href="${article.url}" target="_blank">Read more</a>
            `;
            newsContainer.appendChild(articleElement);
        });
    })
    .catch(error => console.error("Fetch Articles Error:", error));
}

// Call fetchArticles on page load
document.addEventListener("DOMContentLoaded", fetchArticles);
