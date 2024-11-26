

const container = document.querySelector(".container");

// GNews API endpoint
let requestURL = `https://gnews.io/api/v4/top-headlines?category=general&lang=en&country=us&max=10&apikey=f0340de4da4a90909cc6787aefbc2691`;

// Create news cards from data
const generateUI = (articles) => {
  container.innerHTML = ""; // Clear existing content

  articles.forEach(item => {
    let card = document.createElement("div");
    card.classList.add("news-card");
    card.innerHTML = `
      <div class="news-image-container">
        <img src="${item.image || "./newspaper.jpg"}" alt="News Image" />
      </div>
      <div class="news-content">
        <div class="news-title">
          ${item.title}
        </div>
        <div class="news-description">
          ${item.description || item.content || ""}
        </div>
        <!-- Redirect to login page on Read More click -->
        <a href="login.html" target="_self" class="view-button">Read More</a>
      </div>`;
    container.appendChild(card);
  });
};

// Fetch news from GNews API
const getNews = async () => {
  container.innerHTML = ""; // Clear existing content
  try {
    let response = await fetch(requestURL);
    if (!response.ok) {
      alert("Data unavailable at the moment. Please try again later.");
      return;
    }
    let data = await response.json();
    generateUI(data.articles); // GNews API returns articles under the "articles" key
  } catch (error) {
    console.error("Error fetching news:", error);
    alert("An error occurred while fetching news.");
  }
};

// Initialize by fetching news
const init = () => {
  getNews();
};

window.onload = init;

