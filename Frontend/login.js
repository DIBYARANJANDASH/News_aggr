const container = document.getElementById("container");
const registerBtn = document.getElementById("register");
const loginBtn = document.getElementById("login");
const apiUrl = "http://localhost:8080/api";

// Toggle Forms
registerBtn.addEventListener("click", () => {
  container.classList.add("active");
});

loginBtn.addEventListener("click", () => {
  container.classList.remove("active");
});

// Error Message Display
function displayError(elementId, message) {
  const errorMessageDiv = document.getElementById(elementId);
  errorMessageDiv.textContent = message;
}

// Login Function
document.getElementById("loginForm").addEventListener("submit", (e) => {
  e.preventDefault();

  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  fetch(`${apiUrl}/users/loginUser`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password })
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Invalid username or password");
      }
      return response.json();
    })
    .then((data) => {
      localStorage.setItem("userId", data.userId);
      window.location.href = "newsFeed.html";
    })
    .catch((error) => {
      console.error("Login Error:", error);
      displayError("error-message", error.message);
    });
});

// Signup Function
document.getElementById("signupForm").addEventListener("submit", (e) => {
  e.preventDefault();

  const username = document.getElementById("new-username").value;
  const email = document.getElementById("new-email").value;
  const password = document.getElementById("new-password").value;
  const firstname = document.getElementById("first-name").value;
  const lastname = document.getElementById("last-name").value;

  fetch(`${apiUrl}/users/createUser`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, firstname, lastname, email, password })
  })
    .then((response) => {
      if (response.status === 409) {
        throw new Error("Username or email already exists.");
      } else if (!response.ok) {
        throw new Error("Failed to create user. Please try again.");
      }
      return response.json();
    })
    .then((data) => {
      localStorage.setItem("userId", data.userId);
      window.location.href = "preferences.html";
    })
    .catch((error) => {
      displayError("error-message2", error.message);
      console.error("Signup Error:", error);
    });
});
