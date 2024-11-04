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
      window.location.href = "../newsFeed.html";
    })
    .catch((error) => {
      console.error("Login Error:", error);
      displayError("error-message", error.message);
    });
});

// Signup Function
document.getElementById("signupForm").addEventListener("submit", (e) => {
  e.preventDefault();

  // Get field values
  const username = document.getElementById("new-username").value.trim();
  const email = document.getElementById("new-email").value.trim();
  const password = document.getElementById("new-password").value.trim();
  const firstname = document.getElementById("first-name").value.trim();
  const lastname = document.getElementById("last-name").value.trim();


  const encryptedPassword = encryptPassword(password);


  // Check if all fields are filled
  if (!username || !email || !password || !firstname || !lastname) {
    displayError("error-message2", "Please fill in all fields.");
    return;
  }

  // Make API call to create user
  fetch(`${apiUrl}/users/createUser`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, firstname, lastname, email, password})
  })
    .then((response) => {
      if (response.status === 409) {
        // User already exists
        throw new Error("Username or email already exists.");
      } else if (!response.ok) {
        throw new Error("User already exist");
      }
      return response.json();
    })
    .then((data) => {
      // Save userId to localStorage and redirect to preferences page
      localStorage.setItem("userId", data.userId);
      window.location.href = "../preference/preferences.html";
    })
    .catch((error) => {
      displayError("error-message2", error.message);
      console.error("Signup Error:", error);
    });
});

// function encryptPassword(password) {
//   const key = CryptoJS.enc.Utf8.parse("1234567812345678"); // Replace with a secure key
//   const iv = CryptoJS.enc.Utf8.parse("1234567812345678");  // Replace with a secure IV

//   const encrypted = CryptoJS.AES.encrypt(password, key, {
//     iv: iv,
//     mode: CryptoJS.mode.CBC,
//     padding: CryptoJS.pad.Pkcs7
//   });
//   console.log(encrypted);

//   return encrypted.toString(); // Returns the encrypted password as a string
// }

// Utility function to display error messages
function displayError(elementId, message) {
  const errorElement = document.getElementById(elementId);
  errorElement.textContent = message;
  errorElement.style.display = "block";
}

