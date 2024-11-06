/* //API Used: http://newsapi.org/s/india-news-api
const container = document.querySelector(".container");
const optionsContainer = document.querySelector(".options-container");
// "in" stands for India
const country = "in";
const options = [
  "general",
  "entertainment",
  "health",
  "science",
  "sports",
  "technology",
];
//100 requests per day
let requestURL;
//Create cards from data
const generateUI = (articles) => {
  for (let item of articles) {
    let card = document.createElement("div");
    card.classList.add("news-card");
    card.innerHTML = `<div class="news-image-container">
    <img src="${item.urlToImage || "./newspaper.jpg"}" alt="" />
    </div>
    <div class="news-content">
      <div class="news-title">
        ${item.title}
      </div>
      <div class="news-description">
      ${item.description || item.content || ""}
      </div>
      <a href="${item.url}" target="_blank" class="view-button">Read More</a>
    </div>`;
    container.appendChild(card);
  }
};
//News API Call
const getNews = async () => {
  container.innerHTML = "";
  let response = await fetch(requestURL);
  if (!response.ok) {
    alert("Data unavailable at the moment. Please try again later");
    return false;
  }
  let data = await response.json();
  generateUI(data.articles);
};
//Category Selection
const selectCategory = (e, category) => {
  let options = document.querySelectorAll(".option");
  options.forEach((element) => {
    element.classList.remove("active");
  });
  requestURL = `https://newsapi.org/v2/everything?q=subcategory &apiKey=c9ecc1e54c544dfa9ffef750dbcc6251`;
  e.target.classList.add("active");
  getNews();
};
//Options Buttons
const createOptions = () => {
  for (let i of options) {
    optionsContainer.innerHTML += `<button class="option ${
      i == "general" ? "active" : ""
    }" onclick="selectCategory(event,'${i}')">${i}</button>`;
  }
};
const init = () => {
  optionsContainer.innerHTML = "";
  getNews();
  createOptions();
}; 
window.onload = () => {
  requestURL = `http://localhost:8080/api/articles/fetch?category=sports&subcategory=cricket`;
  init();
}; */







const container = document.querySelector(".container");

// Specify the API endpoint
let requestURL = `https://newsapi.org/v2/everything?q=subcategory&apiKey=c9ecc1e54c544dfa9ffef750dbcc6251`;

// Create news cards from data
const generateUI = (articles) => {
  for (let item of articles) {
    let card = document.createElement("div");
    card.classList.add("news-card");
    card.innerHTML = `<div class="news-image-container">
      <img src="${item.urlToImage || "./newspaper.jpg"}" alt="" />
    </div>
    <div class="news-content">
      <div class="news-title">
        ${item.title}
      </div>
      <div class="news-description">
        ${item.description || item.content || ""}
      </div>
      <a href="../regstration form/login.html" target="_self" class="view-button">Read More</a>
    </div>`;
    container.appendChild(card);
  }
};

// Fetch news from API
const getNews = async () => {
  container.innerHTML = ""; // Clear existing content
  try {
    let response = await fetch(requestURL);
    if (!response.ok) {
      alert("Data unavailable at the moment. Please try again later.");
      return;
    }
    let data = await response.json();
    generateUI(data.articles);
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




/* // Show login footer prompt when user scrolls down
window.addEventListener("scroll", () => {
  const scrollTriggerHeight = window.innerHeight * 1.5; // Show after scrolling past 1.5x viewport height
  const loginFooter = document.getElementById("login-footer");

  if (window.scrollY > scrollTriggerHeight) {
    loginFooter.style.display = "block"; // Show the footer
  } else {
    loginFooter.style.display = "none"; // Hide the footer
  }
});
 */