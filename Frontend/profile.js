// profile.js

document.addEventListener("DOMContentLoaded", () => {
    displaySelectedPreferences();

    // Redirects to the add more preferences page when "Add More" is clicked
    document.querySelector("#add-more-btn").addEventListener("click", () => {
        window.location.href = "add_more_preferences.html";
    });
});

// Function to load and display user-selected preferences
function displaySelectedPreferences() {
    // Mock data representing user-selected preferences (replace with actual data from backend if available)
    const userPreferences = [
        { category: "Technology", subcategory: "AI & Machine Learning" },
        { category: "Health", subcategory: "Mental Health" },
        { category: "Sports", subcategory: "Football" }
    ];

    const preferencesContainer = document.querySelector("#account-preferences .card-body");
    preferencesContainer.innerHTML = ""; // Clear any previous content

    userPreferences.forEach(pref => {
        const preferenceElement = document.createElement("div");
        preferenceElement.classList.add("alert", "alert-info", "mt-3");
        preferenceElement.textContent = `Category: ${pref.category} | Subcategory: ${pref.subcategory}`;
        preferencesContainer.appendChild(preferenceElement);
    });
}
