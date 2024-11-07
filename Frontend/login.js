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
  errorMessageDiv.style.display = "block";
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

  // Get field values
  const username = document.getElementById("new-username").value.trim();
  const email = document.getElementById("new-email").value.trim();
  const password = document.getElementById("new-password").value.trim();
  const firstname = document.getElementById("first-name").value.trim();
  const lastname = document.getElementById("last-name").value.trim();

  // Check if all fields are filled
  if (!username || !email || !password || !firstname || !lastname) {
    displayError("error-message2", "Please fill in all fields.");
    return;
  }

  // Encrypt the password
  const encryptedPassword = encryptPassword(password);
  console.log(encryptedPassword);

  // Make API call to create user
  fetch(`${apiUrl}/users/createUser`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, firstname, lastname, email, password: encryptedPassword })
  })
    .then((response) => {
      if (response.status === 409) {
        // User already exists
        throw new Error("Username or email already exists.");
      } else if (!response.ok) {
        throw new Error("Error creating user.");
      }
      return response.json();
    })
    .then((data) => {
      // Save userId to localStorage and redirect to preferences page
      localStorage.setItem("userId", data.userId);
      window.location.href = "preferences.html";
    })
    .catch((error) => {
      displayError("error-message2", error.message);
      console.error("Signup Error:", error);
    });
});

// Password Encryption Function
//const encryptPassword = (plainText) => {
//  const secretKey = "123"; // Ensure this matches the backend
// console.log( typeof(secretKey))
//  const salt = CryptoJS.lib.WordArray.random(128 / 8); // 16 bytes salt
//  const key = CryptoJS.PBKDF2(secretKey, salt, { keySize: 256 / 32, iterations: 1000 });
//  const iv = CryptoJS.lib.WordArray.random(128 / 8); // 16 bytes IV
//  console.log("Secret Key:", secretKey);
//  console.log("Salt (Base64):", CryptoJS.enc.Base64.stringify(salt));
//  console.log("Key (Hex):", key.toString(CryptoJS.enc.Hex));
//  console.log("IV (Base64):", CryptoJS.enc.Base64.stringify(iv));
//
//  // Encrypt with AES-CBC using generated key and IV
//  const encrypted = CryptoJS.AES.encrypt(plainText, key, { iv: iv, padding: CryptoJS.pad.Pkcs7 });
//
//  // Format the output: salt:iv:ciphertext
//  const cipherText = CryptoJS.enc.Base64.stringify(salt) + ":" +
//                     CryptoJS.enc.Base64.stringify(iv) + ":" +
//                     encrypted.toString();
//  return cipherText;
//};
