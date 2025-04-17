document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("gradingForm");
    const message = document.getElementById("message");
    const teacherSubjectsSelect = document.getElementById("teacherSubjects");
    const gradingTableBody = document.querySelector(".grading-settings tbody");

    /** ✅ Fetch and Populate Grading Settings */
    async function fetchGradingSettings() {
        const subjectID = teacherSubjectsSelect.value;
        console.log("Fetching grading settings for SubjectID:", subjectID); // Debugging
    
        if (!subjectID || isNaN(subjectID)) {
            console.warn("Invalid SubjectID:", subjectID);
            return;
        }
    
        try {
            const response = await fetch(`http://localhost/schoop/dashboard/edit/fetch_grading_settings.php?subjectID=${subjectID}`);
            const result = await response.json();
    
            console.log("API Response:", result); // Debugging
    
            if (!result.success) {
                console.warn("No grading settings found:", result.message);
                gradingTableBody.innerHTML = `<tr><td colspan="4">No grading settings found.</td></tr>`;
                return;
            }
    
            // Populate form fields and table
            document.getElementById('numWW').value = result.data.NumWW;
            document.getElementById('numQZ').value = result.data.NumQZ;
            document.getElementById('numPT').value = result.data.NumPT;
            form.dataset.id = result.data.SettingID; // Store ID for updates
    
            gradingTableBody.innerHTML = `
                <tr>
                    <td>${result.data.NumWW}</td>
                    <td>${result.data.NumQZ}</td>
                    <td>${result.data.NumPT}</td>
                    <td>
                        <button class="edit-btn" data-id="${result.data.SettingID}" 
                                data-numww="${result.data.NumWW}" 
                                data-numqz="${result.data.NumQZ}" 
                                data-numpt="${result.data.NumPT}">
                            Edit
                        </button>
                        <button class="delete-btn" data-id="${result.data.SettingID}">Delete</button>
                    </td>
                </tr>
            `;
        } catch (error) {
            console.error("Error fetching grading settings:", error);
        }
    }

    /** ✅ Fetch settings when subject changes */
    teacherSubjectsSelect.addEventListener('change', fetchGradingSettings);

    /** ✅ Edit Button Click */
    gradingTableBody.addEventListener("click", function (event) {
        if (event.target.classList.contains("edit-btn")) {
            const btn = event.target;
            document.getElementById("numWW").value = btn.dataset.numww;
            document.getElementById("numQZ").value = btn.dataset.numqz;
            document.getElementById("numPT").value = btn.dataset.numpt;
            form.dataset.id = btn.dataset.id; // Store ID for update
        }
    });

    /** ✅ Delete Button Click */
    gradingTableBody.addEventListener("click", function (event) {
        if (event.target.classList.contains("delete-btn")) {
            const id = event.target.dataset.id;
            if (confirm("Are you sure you want to delete this setting?")) {
                fetch(`http://localhost/schoop/dashboard/edit/delete_grading.php?id=${id}`, { method: "DELETE" })
                    .then(response => response.json())
                    .then(result => {
                        if (result.success) {
                            message.style.color = "green";
                            message.textContent = "Deleted successfully!";
                            fetchGradingSettings();
                        } else {
                            message.style.color = "red";
                            message.textContent = "Failed to delete setting.";
                        }
                    })
                    .catch(error => console.error("Error:", error));
            }
        }
    });

    /** ✅ Form Submission */
    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const subjectID = teacherSubjectsSelect.value;
        const numWW = document.getElementById("numWW").value;
        const numQZ = document.getElementById("numQZ").value;
        const numPT = document.getElementById("numPT").value;
        const id = form.dataset.id || ""; // If updating, this will have a value

        if (!subjectID) {
            message.style.color = "red";
            message.textContent = "Please select a subject.";
            return;
        }

        const requestData = {subjectID, numWW, numQZ, numPT };
        let url = id ? `http://localhost/schoop/dashboard/edit/update_grading.php?id=${id}` : `http://localhost/schoop/dashboard/edit/save_grading.php`;
        let method = id ? "PUT" : "POST";

        fetch(url, {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Server error: ${response.status} ${response.statusText}`);
            }
            return response.json();
        })
        .then(result => {
            if (result.success) {
                message.style.color = "green";
                message.textContent = result.message || "Grading settings updated successfully!";
                form.reset();
                delete form.dataset.id;
                fetchGradingSettings();
                fetchGrades();
            } else {
                message.style.color = "red";
                message.textContent = result.error || "Failed to update settings.";
            }
        })
        .catch(error => {
            console.error("Error:", error);
            message.style.color = "red";
            message.textContent = "An unexpected error occurred. Please try again.";
        });        
    });
});
