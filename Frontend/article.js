const apiUrl = "http://localhost:8080/api";
<<<<<<< HEAD

// Fetch articles based on user preferences
function fetchArticles() {
    const userId = localStorage.getItem("userId");

    fetch(`${apiUrl}/articles/preferences/${userId}`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`
=======
const userId = localStorage.getItem("userId");
const token = localStorage.getItem("token");
console.log(token)
function fetchArticles() {
    const token = localStorage.getItem("token"); // Retrieve token here

    if (!userId || !token) {
        console.error("No user ID or token found. Please log in.");
        return;
    }

    fetch(`${apiUrl}/articles/preferences/${userId}`, {
        headers: {
            "Authorization": `Bearer ${token}`
>>>>>>> main
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch articles.");
        }
        return response.json();
    })
    .then(articles => {
<<<<<<< HEAD
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
=======
        displayArticles(articles);
    })
    .catch(error => {
        console.error("Fetch Articles Error:", error);
        document.getElementById("news-container").innerHTML = "<p>Could not load articles. Please try again later.</p>";
    });
}


function displayArticles(articles) {
    const newsContainer = document.getElementById("news-container");
    newsContainer.innerHTML = ""; // Clear previous articles

    if (articles.length === 0) {
        newsContainer.innerHTML = "<p>No articles found for your preferences.</p>";
        return;
    }

    articles.forEach(article => {
        const articleCard = document.createElement("div");
        articleCard.classList.add("article-card");

        articleCard.innerHTML = `
            <h2>${article.title}</h2>
            <p><strong>Source:</strong> ${article.source ? article.source.name : "Unknown"}</p>
            <p><strong>Published At:</strong> ${new Date(article.publishedAt).toLocaleDateString()}</p>
            <p>${article.description}</p>
            <p><strong>Author:</strong> ${article.author || "Unknown"}</p>
            <a href="${article.url}" target="_blank">Read more</a>
        `;

        newsContainer.appendChild(articleCard);
    });
>>>>>>> main
}

// Call fetchArticles on page load
document.addEventListener("DOMContentLoaded", fetchArticles);
