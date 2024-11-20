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

// Function to fetch and display user preferences separately (if needed)
function fetchAndDisplayPreferences(userId, token) {
    fetch(`${apiUrl}/preferences/${userId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to fetch preferences');
        return response.json();
    })
    .then(data => {
        displayPreferences(data);  // Pass the data to displayPreferences
    })
    .catch(error => console.error('Error fetching preferences:', error));
}

// Function to display user preferences with draggable feature
function displayPreferences(preferences) {
    const preferencesContainer = document.getElementById('preferences-lsit');
    preferencesContainer.innerHTML = ''; // Clear existing preferences

    if (preferences !== undefined) {
        preferences.forEach(pref => {
            const prefItem = document.createElement('li');
            prefItem.classList.add('preference-item');
            prefItem.draggable = true; // Make the item draggable

            // Display category and subcategory names (ensure these match your DTO field names)
            prefItem.innerText = `${pref.categoryName} - ${pref.subCategoryName}`;

            // Add drag and drop event listeners
            prefItem.addEventListener('dragstart', handleDragStart);
            prefItem.addEventListener('dragover', handleDragOver);
            prefItem.addEventListener('drop', handleDrop);
            prefItem.addEventListener('dragleave', handleDragLeave);

            preferencesContainer.appendChild(prefItem);
        });
    }
}

// Drag and Drop Handlers
let draggedItem = null;

function handleDragStart(e) {
    draggedItem = e.target; // Set the dragged item
    e.target.style.opacity = '0.5'; // Visual feedback
    e.dataTransfer.effectAllowed = 'move'; // Indicate the type of operation
}

function handleDragOver(e) {
    e.preventDefault(); // Allow the drop
    e.target.classList.add('over'); // Add a visual highlight to the drop target
}

function handleDrop(e) {
    e.preventDefault();
    e.target.classList.remove('over'); // Remove the visual highlight

    if (draggedItem !== e.target) {
        const parent = e.target.parentNode;
        parent.insertBefore(draggedItem, e.target); // Move the dragged item to its new position
    }
}

function handleDragLeave(e) {
    e.target.classList.remove('over'); // Remove the visual highlight when leaving a drop target
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