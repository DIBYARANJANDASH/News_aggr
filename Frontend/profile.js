const apiUrl = "http://localhost:8080/api";

document.addEventListener('DOMContentLoaded', () => {
    const userId = localStorage.getItem("userId");  // Retrieve user ID from localStorage or session storage
    const token = localStorage.getItem("token");  // Retrieve JWT token for authorization

    console.log(token);
    console.log(userId);

    if (!userId || !token) {
        alert("User is not logged in");
        window.location.href = '/login.html';  // Redirect to login page if not authenticated
        return;
    }

    // Fetch and display user details
    fetchUserDetails(userId, token);
    fetchAndDisplayPreferences(userId, token);

    document.getElementById('save-general-info-btn').addEventListener('click', () => {
            updateUserInfo(userId, token);
        });
    // Update password form submission
    document.getElementById('update-password-btn').addEventListener('click', () => updatePassword(userId, token));

    // Redirect to add more preferences
    document.getElementById('add-more-btn').addEventListener('click', () => {
        window.location.href = './preferences.html';
    });

    document.getElementById('save-priority-btn').addEventListener('click', () => {
        console.log("Button hit");
        savePreferencePriority(userId, token);
    });
});

/// Function to fetch user details
function fetchUserDetails(userId, token) {
    fetch(`${apiUrl}/users/details/${userId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to fetch user details');
        return response.json();
    })
    .then(data => {
        // Populate user detail fields
        document.querySelector('#account-general input[name="username"]').value = data.username;
        document.querySelector('#account-general input[name="firstname"]').value = data.firstname;
        document.querySelector('#account-general input[name="lastname"]').value = data.lastname;
        document.querySelector('#account-general input[name="email"]').value = data.email;

        // Display user preferences
        displayPreferences(data.preferences);
    })
    .catch(error => console.error('Error fetching user details:', error));
}
// Update user's first name and last name
function updateUserInfo(userId, token) {
    const updatedFirstname = document.querySelector('input[name="firstname"]').value;
    const updatedLastname = document.querySelector('input[name="lastname"]').value;

    const requestBody = {
        firstname: updatedFirstname,
        lastname: updatedLastname
    };

    fetch(`${apiUrl}/users/update-info/${userId}`, {
        method: 'PATCH',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to update user info');
        alert('User info updated successfully');
        fetchUserDetails(userId, token); // Refresh user info without reloading the page
    })
    .catch(error => console.error('Error updating user info:', error));
}


function fetchAndDisplayPreferences(userId, token) {
    fetch(`${apiUrl}/preferences/${userId}`, {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
        },
    })
        .then((response) => {
            if (!response.ok) throw new Error("Failed to fetch preferences");
            return response.json();
        })
        .then((data) => {
            console.log("Fetched Preferences:", data);
            displayPreferences(data);
        })
        .catch((error) => console.error("Error fetching preferences:", error));
}




function displayPreferences(preferences) {
    if (!Array.isArray(preferences)) {
        console.error("Invalid preferences data:", preferences);
        return; // Exit if preferences is not an array
    }

    const preferencesContainer = document.getElementById("preferences-list");

    if (!preferencesContainer) {
        console.error("Element with ID 'preferences-list' not found");
        return; // Exit if container element is not found
    }

    preferencesContainer.innerHTML = ""; // Clear existing preferences

    // Sort preferences by priority in ascending order
    preferences.sort((a, b) => a.priority - b.priority);

    preferences.forEach((pref) => {
        const prefItem = document.createElement("li");
        prefItem.classList.add("preference-item");
        prefItem.draggable = true;
        prefItem.dataset.subcategoryId = pref.subCategoryId;

        prefItem.innerText = `${pref.categoryName} - ${pref.subCategoryName}`;

        prefItem.addEventListener("dragstart", handleDragStart);
        prefItem.addEventListener("dragover", handleDragOver);
        prefItem.addEventListener("drop", handleDrop);
        prefItem.addEventListener("dragleave", handleDragLeave);

        preferencesContainer.appendChild(prefItem);
    });
}

function savePreferencePriority(userId, token) {
    const preferenceItems = Array.from(document.querySelectorAll('#preferences-list .preference-item'));
    
    // Map the preferences to an array of objects with updated priority
    const preferences = preferenceItems.map((item, index) => ({
        subcategoryId: item.dataset.subcategoryId, // Use data-subcategory-id
        priority: index + 1, // Set priority based on the new order (index + 1 for ascending order)
    }));

    // Send the updated preferences to the backend
    fetch(`${apiUrl}/preferences/${userId}`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(preferences)
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to save preferences');
        alert('Preferences saved successfully');
    })
    .catch(error => console.error('Error saving preferences:', error));
}

// Drag and Drop Handlers
let draggedItem = null;

function handleDragStart(e) {
    draggedItem = e.target;
    e.target.style.opacity = '0.5';
}

function handleDragOver(e) {
    e.preventDefault();
    e.target.classList.add('over');
}

function handleDrop(e) {
    e.preventDefault();
    e.target.classList.remove('over');

    if (draggedItem !== e.target) {
        const parent = e.target.parentNode;
        parent.insertBefore(draggedItem, e.target);
    }
}

function handleDragLeave(e) {
    e.target.classList.remove('over');
}


// Function to update password
function updatePassword(userId, token) {
    const currentPassword = document.querySelector('#account-change-password input[name="currentPassword"]').value;
    const newPassword = document.querySelector('#account-change-password input[name="newPassword"]').value;
    const confirmPassword = document.querySelector('#account-change-password input[name="confirmPassword"]').value;

    if (newPassword !== confirmPassword) {
        alert('Passwords do not match');
        return;
    }

    const requestBody = {
        currentPassword: currentPassword,
        newPassword: newPassword
    };

    fetch(`${apiUrl}/users/change-password/${userId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (response.ok) {
            alert('Password updated successfully');
        } else {
            response.text().then(text => alert(text));
        }
    })
    .catch(error => console.error('Error updating password:', error));
}
function redirectToNewsFeed() {
    window.location.href = "newsFeed.html";
}



function redirectToNewsFeed() {
    window.location.href = "newsFeed.html";
}