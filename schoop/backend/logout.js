(() => {
    // Determine the correct path based on the location
    let basePath;

    if (window.location.pathname.includes('/dashboard/edit/')) {
        basePath = '../../';
    } else if (window.location.pathname.includes('/dashboard/')) {
        basePath = '../';
    } else {
        basePath = '';
    }    

    document.addEventListener("DOMContentLoaded", function() {
        const logoutBtn1 = document.getElementById("logoutLink");
        const logoutBtn2 = document.getElementById("logoutdrop");

        function addLogoutEvent(button) {
            if (button) {
                button.addEventListener("click", function(event) {
                    event.preventDefault(); // Stop default behavior

                    fetch(`${basePath}logout.php`, { method: 'GET' })
                        .then(response => response.json())
                        .then(data => {
                            if (data.success) {
                                sessionStorage.removeItem("loggedInUser"); // Optional: Remove frontend session
                                window.location.href = `${basePath}login.html`; // Redirect to login page
                            } else {
                                console.error("Logout failed:", data.message);
                            }
                        })
                        .catch(error => console.error('Error:', error));
                });
            } else {
                console.error("Logout button not found!");
            }
        }

        // Apply to both buttons if they exist
        addLogoutEvent(logoutBtn1);
        addLogoutEvent(logoutBtn2);
    });
})();
