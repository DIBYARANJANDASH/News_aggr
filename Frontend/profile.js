const apiUrl = "http://localhost:8080/api";

document.addEventListener('DOMContentLoaded', () => {
    const userId = localStorage.getItem("userId");  // Retrieve user ID from localStorage
    const token = localStorage.getItem("token");  // Retrieve JWT token

    console.log("Token:", token);
    console.log("User ID:", userId);

    if (!userId || !token) {
        alert("User is not logged in");
        window.location.href = '/login.html';  // Redirect to login page if not authenticated
        return;
    }

    // Fetch and display user details and preferences
    fetchUserDetails(userId, token);

    // Event listener for saving user info (firstname, lastname)
    document.getElementById('save-changes-btn').addEventListener('click', () => {
        updateUserInfo(userId, token);
    });

    // Update password form submission
    document.getElementById('update-password-btn').addEventListener('click', updatePassword);

    // Redirect to add more preferences
    document.getElementById('add-more-btn').addEventListener('click', () => {
        window.location.href = '/preferences.html';
    });
});

// Function to fetch user details
function fetchUserDetails(userId, token) {
    fetch(`${apiUrl}/users/${userId}`, {
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
        // Set read-only fields
        document.querySelector('#account-general input[name="username"]').value = data.username;
        document.querySelector('#account-general input[name="email"]').value = data.email;

        // Set editable fields
        document.querySelector('#account-general input[name="firstname"]').value = data.firstname || "";
        document.querySelector('#account-general input[name="lastname"]').value = data.lastname || "";

        // Display preferences
        displayPreferences(data.preferences || []);  // Displaying preferences if available
    })
    .catch(error => console.error('Error fetching user details:', error));
}

// Function to display user preferences
function displayPreferences(preferences) {
    const preferencesContainer = document.getElementById('preferencesContainer');
    preferencesContainer.innerHTML = '';  // Clear existing preferences

    // Check if preferences is an array and has at least one item
    if (!Array.isArray(preferences) || preferences.length === 0) {
        preferencesContainer.innerHTML = "<p>No preferences set yet.</p>";
        return;
    }

    preferences.forEach(pref => {
        const prefItem = document.createElement('div');
        prefItem.classList.add('alert', 'alert-info', 'mt-2');
        prefItem.innerText = `${pref.category} - ${pref.subCategory} (Priority: ${pref.priority})`;
        preferencesContainer.appendChild(prefItem);
    });
}


// Function to update firstname and lastname
function updateUserInfo(userId, token) {
    const firstname = document.querySelector('#account-general input[name="firstname"]').value;
    const lastname = document.querySelector('#account-general input[name="lastname"]').value;

    const updateRequest = {
        firstname: firstname,
        lastname: lastname
    };

    fetch(`${apiUrl}/users/update-info/${userId}`, {
        method: 'PATCH',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updateRequest)
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to update user info');
        alert('User info updated successfully.');
    })
    .catch(error => console.error('Error updating user info:', error));
}

// Function to update password
function updatePassword() {
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

    fetch(`${apiUrl}/users/${userId}`, {
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
            throw new Error('Failed to update password');
        }
    })
    .catch(error => console.error('Error updating password:', error));
}
