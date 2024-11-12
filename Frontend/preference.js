const apiUrl = "http://localhost:8080/api";

// Load categories and populate dropdown
function loadCategories() {
    fetch(`${apiUrl}/categories`)
        .then(response => response.json())
        .then(categories => {
            const categoryDropdown = document.getElementById("category");
            categories.forEach(category => {
                const option = document.createElement("option");
                option.value = category.categoryId;
                option.textContent = category.categoryName;
                categoryDropdown.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading categories:", error));
}

// Load subcategories based on selected category
function loadSubcategories() {
    const categoryId = document.getElementById("category").value;
    if (!categoryId) return;

    fetch(`${apiUrl}/subcategories/${categoryId}`)
        .then(response => response.json())
        .then(subcategories => {
            const subcategoryDropdown = document.getElementById("subcategory");
            subcategoryDropdown.innerHTML = "";
            subcategories.forEach(subcategory => {
                const option = document.createElement("option");
                option.value = subcategory.subcategoryId;
                option.textContent = subcategory.subcategoryName;
                subcategoryDropdown.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading subcategories:", error));
}

// Save user preferences to backend
function savePreferences() {
    const userId = localStorage.getItem("userId");
    const subcategoryDropdown = document.getElementById("subcategory");
    const selectedPreferences = [{
        subcategoryId: subcategoryDropdown.value,
        priority: 1
    }];

    fetch(`${apiUrl}/preferences/${userId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(selectedPreferences)
    })
    .then(response => {
        if (!response.ok) throw new Error("Failed to save preferences");
        document.getElementById("message").textContent = "Preference saved successfully!";
    })
    .catch(error => {
        document.getElementById("message").textContent = "Error saving preference. Please try again.";
        console.error("Error saving preference:", error);
    });
}

// Redirect to news feed page
function redirectToNewsFeed() {
    window.location.href = "newsFeed.html";
}

// Load categories on page load
document.addEventListener("DOMContentLoaded", loadCategories);
