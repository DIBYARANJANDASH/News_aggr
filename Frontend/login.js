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

// Add event listener to the login form submission
document.getElementById("loginForm").addEventListener("submit", (e) => {
  e.preventDefault();  // Prevent default form submission behavior

  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  // Encrypt the password using the encryptPassword function
  const encryptedPassword = encryptPassword(password);
  console.log(encryptedPassword);

  // Send the username and encrypted password to the backend
  fetch(`${apiUrl}/users/loginUser`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      username,
      password: encryptedPassword  // Use the encrypted password here
    })
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Invalid username or password");
      }
      return response.json();
    })
    .then((data) => {
      // Store user ID in local storage and redirect to news feed
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


const encryptPassword = (plainText) => {
  const secretKey = "123"; // Ensure this matches the backend

  // Generate 16-byte salt and IV in hex format
  const salt = CryptoJS.lib.WordArray.random(128 / 8); // 16 bytes
  const iv = CryptoJS.lib.WordArray.random(128 / 8);   // 16 bytes

  // Derive the key using PBKDF2
  const key = CryptoJS.PBKDF2(secretKey, salt, {
    keySize: 256 / 32,  // 256-bit key
    iterations: 1000,
    hasher: CryptoJS.algo.SHA256, // Explicitly use SHA256
  });

  // Encrypt the plainText with AES using CBC mode and PKCS7 padding
  const encrypted = CryptoJS.AES.encrypt(plainText, key, {
    iv: iv,
    padding: CryptoJS.pad.Pkcs7,
  });

  // Format the output as: hex(salt):hex(iv):ciphertext (ciphertext remains in Base64)
  const cipherText = [
    salt.toString(CryptoJS.enc.Hex),   // Convert salt to hex
    iv.toString(CryptoJS.enc.Hex),     // Convert iv to hex
    encrypted.toString()               // ciphertext in Base64
  ].join(":");

  return cipherText;
};

