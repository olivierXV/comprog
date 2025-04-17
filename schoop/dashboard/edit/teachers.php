<?php
session_start();
error_reporting(E_ALL);
ini_set('display_errors', 1);
include '../../backend/db.php';

// Verify admin privileges
if ($_SESSION['role'] !== 'admin') {
  header("Location: dashboard.php");
  die("Unauthorized access");
}
?>
<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Edit Teachers</title>
  <link rel="stylesheet" href="../../grades.css">
  <link rel="stylesheet" href="../../hover.css">
  <link rel="stylesheet" href="../../colours.css">
  <style>
    .navbar ul{
      background-image: radial-gradient(circle, #ffa600,#aa6f00) !important;
    }
    label { display: block; margin-top: 10px; font-weight: bold; }
    input, select { width: 95%; padding: 5px; margin-top: 5px; }
    button { margin-top: 10px; padding: 5px; cursor: pointer; }
    .message { margin-top: 10px; font-weight: bold; }
    table { width: 100%; border-collapse: collapse; margin-top: 10px;}
    th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
    .actions { white-space: nowrap; }
    th, td {
        border: 1px solid #ccc;
        padding: 8px;
        text-align: left;
        word-wrap: break-word; /* Ensures long text wraps */
    }
    @media screen and (min-width:769px){
    th:nth-child(1), td:nth-child(1) { width: 10%; } /* Teacher ID */
    th:nth-child(2), td:nth-child(2) { width: 20%; } /* Name */
    th:nth-child(3), td:nth-child(3) { width: 23%; } /* Email */
    th:nth-child(4), td:nth-child(4) { width: 7%; } /* Role */
    th:nth-child(5), td:nth-child(5) { width: 15%; } /* Subjects */
    th:nth-child(6), td:nth-child(6) { width: 8%; } /* Created At */
    th:nth-child(7), td:nth-child(7) { width: 12%; } /* Actions */
    }
    #sectionSelect{
        display:flex;
    }
  </style>
  <link rel="icon" href="../../school.png">
  <script src="../../backend/logout.js" defer></script>
  <script src="../../menu.js" defer></script>
</head>

<body class="<?php echo isset($_SESSION['role']) ? $_SESSION['role'] : (isset($_SESSION['strand']) ? $_SESSION['strand'] : ''); ?>">
  <div class="errir"><p>Screen Size is too small</p></div>
  <div class="navbar">
  <ul class="<?php echo isset($_SESSION['role']) ? $_SESSION['role'] : (isset($_SESSION['strand']) ? $_SESSION['strand'] : ''); ?>">
  <div style="display: flex;">
  <li><a href="../../about.html"><img src="../../school.png"></a></li>
      <li class="title"><a>Edit Teachers</a></li>
      </div>
      <div class="link" style="float: right;">
        <div class="dropdown">
        <button class="dropbut" href="javascript:void(0);" class="icon" onclick="menu()">≡</button>
        <div id="dropdown-content">
            <a href="../../index.html">Home</a>
            <a href="../../dashboard.php">Dashboard</a>
            <a href="../edit.php" id="teachersOnly">Grading Sheet</a>
            <a href="announcements.php" id="adminOnly">Edit Announcements</a>
            <a href="students.php" id="teachersOnly">Edit Students</a>
            <a href="#" id="logoutdrop">Log out</a>
          </div>
        </div>

        <div class="deskLink">
        <li><a style="float: right; font-size: large; color: white;" href="../../index.html" >Home</a></li>
        <li><a style="float: right; font-size: large; color: white;" href="../../dashboard.php" >Dashboard</a></li>
        <li id="teachersOnly"><a style="float: right; font-size: large; color: white;" href="../edit.php" >Grading Sheet</a></li>
        <li id="adminOnly"><a style="float: right; font-size: large; color: white;" href="announcements.php" >Edit Announcements</a></li>
        <li id="adminOnly"><a style="float: right; font-size: large; color: white;" class="active" href="teachers.php" >Edit Teachers</a></li>
        <li id="teachersOnly"><a style="float: right; font-size: large; color: white;" href="students.php" >Edit Students</a></li>
        <li><a style="float: right; font-size: large; color: white;" href="#" id="logoutLink" >Log out</a></li>
</div>
      </div>
    </ul>
  </div>
  <div class="body">
  <div class="left">
  <div class="box">
    <div style="padding:5%; margin-left: -3%;">
      <form id="addTeacherForm">
        <!-- Existing fields for adding/editing teachers -->
        <label for="TeacherID">Teacher ID (7 Characters):</label>
        <input type="text" id="TeacherID" name="TeacherID" maxlength="7" required>

        <label for="FirstName">First Name:</label>
        <input type="text" id="FirstName" name="FirstName" required>

        <label for="MiddleName">Middle Name (Optional):</label>
        <input type="text" id="MiddleName" name="MiddleName">

        <label for="LastName">Last Name:</label>
        <input type="text" id="LastName" name="LastName" required>

        <label for="Email">Email Address:</label>
        <input type="email" id="Email" name="Email" required>

        <label for="Role">Role:</label>
        <select id="Role" name="Role">
          <option value="teacher" selected>Teacher</option>
          <option value="admin">Admin</option>
        </select>

        <!-- New fields for subject assignment -->
        <label for="sectionSelect">Section:</label>
        <select id="sectionSelect" name="sectionId">
          <!-- Populate with sections dynamically -->
        </select>

        <label for="subjectSelect">Subjects:</label>
        <select id="subjectSelect" name="subjectId" multiple>
        <!-- Populate with subjects dynamically -->
        </select>

        <button type="submit">Add Teacher</button>
        <button type="button" id="cancelEdit" style="display: none;">Cancel Edit</button>
      </form>
    </div>
  </div>
</div>
    <div class="right">
  <div class="box">

<div style="padding: 1px;">
<input type="text" id="searchBar" placeholder="Search" style="padding: 5px; width: 100%;">
</div>

    <p id="message" class="message"></p>
    <table id="teachersTable">
        <thead>
            <tr>
                <th>Teacher ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Subjects</th>
                <th>Created At</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody></tbody>
    </table>
  </div>
</div>
  </div>

  <script>
    const form = document.getElementById('addTeacherForm');
    const message = document.getElementById('message');
    const teachersTable = document.getElementById('teachersTable').querySelector('tbody');
    const cancelEditButton = document.getElementById('cancelEdit');
    const submitButton = form.querySelector('button[type="submit"]');

    let editingTeacherId = null;

    function filterTable() {
    const searchInput = document.getElementById('searchBar');
    const filter = searchInput.value.toUpperCase();
    const table = document.getElementById('teachersTable');
    const rows = table.getElementsByTagName('tr');

    for (let i = 1; i < rows.length; i++) { // Start from 1 to skip the header row
        const row = rows[i];
        const cells = row.getElementsByTagName('td');
        let match = false;

        for (let j = 0; j < cells.length; j++) {
            const cell = cells[j];
            if (cell) {
                const textValue = cell.textContent || cell.innerText;
                if (textValue.toUpperCase().indexOf(filter) > -1) {
                    match = true;
                    break;
                }
            }
        }

        if (match) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    }
}

document.getElementById('searchBar').addEventListener('input', filterTable);

    // Fetch and display teachers
    async function fetchTeachers() {
        try {
            const response = await fetch(`edit_teachers.php`);
            const data = await response.json();
            teachersTable.innerHTML = data.teachers.map(teacher => `
                <tr>
                    <td>${teacher.TeacherID}</td>
                    <td>${teacher.FirstName} ${teacher.MiddleName || ''} ${teacher.LastName}</td>
                    <td>${teacher.Email}</td>
                    <td>${teacher.Role}</td>
                    <td>${teacher.Subjects || 'No subjects assigned'}</td>
                    <td>${teacher.created_at}</td>
                    <td class="actions">
                        <button onclick="editTeacher('${teacher.TeacherID}')">Edit</button>
                        <button onclick="deleteTeacher('${teacher.TeacherID}')">Delete</button>
                    </td>
                </tr>
            `).join('');
        } catch (error) {
            console.error('Error fetching teachers:', error);
        }
    }

    // Handle form submission (Add/Edit Teacher)
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Confirmation before proceeding
        const action = editingTeacherId ? 'update' : 'add';
        const confirmationMessage = editingTeacherId 
            ? 'Are you sure you want to update this teacher?' 
            : 'Are you sure you want to add this teacher?';

        if (!confirm(confirmationMessage)) {
            return; // Stop if the user cancels
        }

        const formData = new FormData(form);
        const url = editingTeacherId 
            ? `edit_teachers.php?action=update&id=${editingTeacherId}` 
            : `edit_teachers.php`;

        try {
            const response = await fetch(url, {
                method: 'POST',
                body: formData
            });
            const result = await response.json();

            message.textContent = result.success || result.error;
            if (result.success) {
                form.reset();
                fetchTeachers();
                cancelEdit();
            }
        } catch (error) {
            message.textContent = 'Error processing request.';
            console.error('Error:', error);
        }
    });

    // Edit teacher
    async function editTeacher(teacherId) {
        if (confirm('Are you sure you want to edit this teacher?')) {
            try {
                const response = await fetch(`edit_teachers.php?action=fetch&id=${teacherId}`);
                const teacher = await response.json();

                document.getElementById('TeacherID').value = teacher.TeacherID;
                document.getElementById('FirstName').value = teacher.FirstName;
                document.getElementById('MiddleName').value = teacher.MiddleName || '';
                document.getElementById('LastName').value = teacher.LastName;
                document.getElementById('Email').value = teacher.Email;
                document.getElementById('Role').value = teacher.Role;

                editingTeacherId = teacherId;
                cancelEditButton.style.display = 'inline';
                submitButton.textContent = 'Edit Teacher'; // Change button text
            } catch (error) {
                console.error('Error fetching teacher:', error);
            }
        }
    }

    // Delete teacher
    async function deleteTeacher(teacherId) {
        if (confirm('Are you sure you want to delete this teacher?')) {
            try {
                const response = await fetch(`edit_teachers.php?action=delete&id=${teacherId}`, {
                    method: 'POST'
                });
                const result = await response.json();

                message.textContent = result.success || result.error;
                if (result.success) {
                    fetchTeachers();
                }
            } catch (error) {
                console.error('Error deleting teacher:', error);
            }
        }
    }

    // Cancel edit
    function cancelEdit() {
        form.reset();
        editingTeacherId = null;
        cancelEditButton.style.display = 'none';
        submitButton.textContent = 'Add Teacher'; // Reset button text
    }

    cancelEditButton.addEventListener('click', cancelEdit);

    // Initialize
    document.addEventListener('DOMContentLoaded', fetchTeachers);

// Fetch sections and populate the dropdown
async function populateSectionDropdown() {
    try {
        const response = await fetch(`fetch_sections.php`);
        const data = await response.json();
        console.log("Fetched sections data:", data); // Log the fetched data

        const sectionSelect = document.getElementById('sectionSelect');

        // Clear existing options
        sectionSelect.innerHTML = '';

        // Add new options
        if (data.sections && Array.isArray(data.sections)) {
            data.sections.forEach(section => {
                const option = document.createElement('option');
                option.value = section.SectionID;
                option.textContent = section.SectionName;
                sectionSelect.appendChild(option);
            });

            // Trigger subject dropdown population when a section is selected
            sectionSelect.addEventListener('change', populateSubjectDropdown);
        } else {
            console.error("Invalid sections data:", data);
        }
    } catch (error) {
        console.error('Error fetching sections:', error);
    }
}

// Fetch subjects based on the selected section
async function populateSubjectDropdown() {
    const sectionId = document.getElementById('sectionSelect').value;

    try {
        const response = await fetch(`fetch_subjects.php?sectionId=${sectionId}`);
        const data = await response.json();
        console.log("Fetched subjects data:", data); // Log the fetched data

        const subjectSelect = document.getElementById('subjectSelect');

        // Clear existing options
        subjectSelect.innerHTML = '';

        // Add new options
        if (data.subjects && Array.isArray(data.subjects)) {
            data.subjects.forEach(subject => {
                const option = document.createElement('option');
                option.value = subject.SubjectID;
                option.textContent = subject.SubjectName;
                subjectSelect.appendChild(option);
            });
        } else {
            console.error("Invalid subjects data:", data);
        }
    } catch (error) {
        console.error('Error fetching subjects:', error);
    }
}

// Handle form submission (Add/Edit Teacher)
form.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Confirmation before proceeding
    const action = editingTeacherId ? 'update' : 'add';
    const confirmationMessage = editingTeacherId 
        ? 'Are you sure you want to update this teacher?' 
        : 'Are you sure you want to add this teacher?';

    if (!confirm(confirmationMessage)) {
        return; // Stop if the user cancels
    }

    const formData = new FormData(form);

    // Add all selected subjects to the FormData
    const subjectSelect = document.getElementById('subjectSelect');
    const selectedSubjects = Array.from(subjectSelect.selectedOptions).map(option => option.value);
    selectedSubjects.forEach((subjectId, index) => {
        formData.append(`subjectId[${index}]`, subjectId);
    });

    const url = editingTeacherId 
        ? `edit_teachers.php?action=update&id=${editingTeacherId}` 
        : `edit_teachers.php`;

    try {
        const response = await fetch(url, {
            method: 'POST',
            body: formData
        });
        const result = await response.json();

        message.textContent = result.success || result.error;
        if (result.success) {
            form.reset();
            fetchTeachers();
            cancelEdit();
        }
    } catch (error) {
        message.textContent = 'Error processing request.';
        console.error('Error:', error);
    }
});

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    fetchTeachers();
    populateSectionDropdown();
    populateSubjectDropdown();
});
    </script>
</body>
</html>