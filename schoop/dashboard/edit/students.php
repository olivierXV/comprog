<?php
session_start();
error_reporting(E_ALL);
ini_set('display_errors', 1);
include '../../backend/db.php';

// Verify admin privileges
if ($_SESSION['role'] !== 'admin' && $_SESSION['role'] !== 'teacher') {
  header("Location: dashboard.php");
  die("Unauthorized access");
}

// Query sections and students
$sections = [];
$studentsBySection = [];
$sectionResult = $conn->query("SELECT SectionID, SectionName FROM sections ORDER BY SectionName");
while ($section = $sectionResult->fetch_assoc()) {
    $sections[] = $section; // Store entire row
}

// Get all students sorted by section and name
$studentResult = $conn->query("
    SELECT i.*, s.SectionName 
    FROM information i
    LEFT JOIN sections s ON i.SectionID = s.SectionID
    ORDER BY s.SectionName, i.LastName, i.FirstName
");

while ($student = $studentResult->fetch_assoc()) {
    $sectionName = $student['SectionName'];
    if (!isset($studentsBySection[$sectionName])) {
        $studentsBySection[$sectionName] = [];
    }
    $studentsBySection[$sectionName][] = $student;
}

// Handle form submissions
$message = '';
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $action = $_POST['action'] ?? 'add';
    $lrn = trim($_POST['lrn'] ?? '');

    // Validate LRN length
    if (strlen($lrn) !== 12) {
        $message = "Error: LRN must be exactly 12 characters long.";
        header("Location: ".$_SERVER['PHP_SELF']."?message=".urlencode($message));
        exit();
    }

    // Check for duplicate LRN (only for 'add' action)
    if ($action === 'add') {
        $checkLrnQuery = "SELECT LRN FROM information WHERE LRN = ?";
        $stmt = $conn->prepare($checkLrnQuery);
        $stmt->bind_param("s", $lrn);
        $stmt->execute();
        $result = $stmt->get_result();
        if ($result->num_rows > 0) {
            $message = "Error: LRN already exists.";
            header("Location: ".$_SERVER['PHP_SELF']."?message=".urlencode($message));
            exit();
        }
    }

    try {
        // Common fields
        $firstName = trim($_POST['firstName']);
        $lastName = trim($_POST['lastName']);
        $middleName = trim($_POST['middleName'] ?? '');
        $strand = trim($_POST['strand']);
        $gradeLevel = trim($_POST['gradeLevel']);
        $sectionID = trim($_POST['sectionID']);
        $schoolYear = trim($_POST['schoolYear']);

        // Handle Specialization based on Strand
        $specialization = NULL; // Default to NULL
        if ($strand === 'HE' || $strand === 'ICT') {
            $specialization = trim($_POST['specialization'] ?? '');
            // Validate Specialization
            $allowedSpecializations = ['Programming', 'CSS', 'Combo 1', 'Combo 2'];
            if (!in_array($specialization, $allowedSpecializations)) {
                throw new Exception("Invalid specialization for the selected strand.");
            }
        }

        // Get section name
        $sectionName = $sections[$sectionID] ?? '';

        if ($action === 'update') {
            $stmt = $conn->prepare("UPDATE information SET
                FirstName = ?, LastName = ?, MiddleName = ?,
                Strand = ?, Specialization = ?, GradeLevel = ?,
                SectionID = ?, Section = ?, SchoolYear = ?
                WHERE LRN = ?");
            $stmt->bind_param("ssssssisss", 
                $firstName, $lastName, $middleName,
                $strand, $specialization, $gradeLevel,
                $sectionID, $sectionName, $schoolYear, $lrn
            );
        } else {
            $stmt = $conn->prepare("INSERT INTO information 
                (LRN, FirstName, LastName, MiddleName, Strand, 
                Specialization, GradeLevel, SectionID, Section, SchoolYear, Password)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '')");
            $stmt->bind_param("sssssssiss",
                $lrn, $firstName, $lastName, $middleName,
                $strand, $specialization, $gradeLevel,
                $sectionID, $sectionName, $schoolYear
            );
        }

        $stmt->execute();
        $message = "Student " . ($action === 'update' ? "updated" : "added") . " successfully";
    } catch (Exception $e) {
        $message = "Error: " . $e->getMessage();
    }

    // Refresh data after modification
    header("Location: ".$_SERVER['PHP_SELF']."?message=".urlencode($message));
    exit();
}
?>

<!doctype html>
<html lang="en">
<head>
  <title>Edit Students</title>
  <style>
    .student-table {
        width: 100%;
        border-collapse: collapse;
        margin: 20px 0;
    }
    .student-table th, .student-table td {
        border: 1px solid #ddd;
        text-align: left;
    }
    .student-table th {
        background-color: #f8f9fa;
    }
    .section-header {
        background-color: #e9ecef;
        padding: 10px;
        margin-top: 20px;
        font-weight: bold;
    }
    .action-buttons button {
        padding: 5px 10px;
        margin: 2px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }
    .edit-btn { background-color: #4CAF50; color: white; }
    .delete-btn { background-color: #f44336; color: white; }
    .navbar ul{
      background-image: radial-gradient(circle, #ffa600,#aa6f00) !important;
    }
    label { display: block; margin-top: 10px; font-weight: bold; }
    input, select { width: 100%; padding: 5px; margin-top: 5px; }
    button { margin-top: 10px; padding: 5px; cursor: pointer; }
    .message { margin-top: 10px; font-weight: bold; }
    table { width: 100%; border-collapse: collapse; margin-top: 10px;}
    th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
    form { min-width: 100%; margin: 0 auto; }
  </style>
  <link rel="stylesheet" href="../../grades.css">
  <link rel="stylesheet" href="../../hover.css">
  <link rel="stylesheet" href="../../colours.css">
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
        <li class="title"><a>Edit Students</a></li>
      </div>
      <div class="link" style="float: right;">
        <div class="dropdown">
          <button class="dropbut" href="javascript:void(0);" class="icon" onclick="menu()">≡</button>
          <div id="dropdown-content">
            <a href="../../index.html">Home</a>
            <a href="../../dashboard.php">Dashboard</a>
            <a href="../edit.php" id="teachersOnly">Grading Sheet</a>
            <a href="announcements.php" id="adminOnly">Edit Announcements</a>
            <a href="teachers.php" id="adminOnly">Edit Teachers</a>
            <a href="#" id="logoutdrop">Log out</a>
          </div>
        </div>
        <div class="deskLink">
          <li><a style="float: right; font-size: large; color: white;" href="../../index.html">Home</a></li>
          <li><a style="float: right; font-size: large; color: white;" href="../../dashboard.php">Dashboard</a></li>
          <li id="teachersOnly"><a style="float: right; font-size: large; color: white;" href="../edit.php">Grading Sheet</a></li>
        <li id="adminOnly"><a style="float: right; font-size: large; color: white;" href="dashboard/edit/announcements.php">Edit Announcements</a></li>
        <li id="adminOnly"><a style="float: right; font-size: large; color: white;" href="dashboard/edit/teachers.php" >Edit Teachers</a></li>
        <li id="teachersOnly"><a style="float: right; font-size: large; color: white;" class="active" href="dashboard/edit/students.php" >Edit Students</a></li>
          <li><a style="float: right; font-size: large; color: white;" href="#" id="logoutLink">Log out</a></li>
        </div>
      </div>
    </ul>
  </div>
  <div class="body">
    <div class="left">
      <div class="box">
        <?php if (isset($_GET['message'])): ?>
          <p class="message"><?= htmlspecialchars(urldecode($_GET['message'])) ?></p>
        <?php endif; ?>
        <form method="POST" action="">
          <input type="hidden" name="action" id="formAction" value="add">
          <input type="hidden" name="original_lrn" id="originalLrn" value="<?= htmlspecialchars($formData['original_lrn'] ?? '') ?>">

          <label for="lrn">LRN:</label>
          <input type="text" name="lrn" id="lrn" maxlength="12" required>

          <label for="firstName">First Name:</label>
          <input type="text" name="firstName" id="firstName" required>

          <label for="middleName">Middle Name:</label>
          <input type="text" name="middleName" id="middleName">

          <label for="lastName">Last Name:</label>
          <input type="text" name="lastName" id="lastName" required>

          <label for="strand">Strand:</label>
          <select name="strand" id="strand" required onchange="updateSpecialization()">
            <option value="">Select Strand</option>
            <option value="HUMSS">HUMSS</option>
            <option value="GAS">GAS</option>
            <option value="HE">HE</option>
            <option value="ICT">ICT</option>
          </select>

          <label for="specialization">Specialization (optional):</label>
          <select name="specialization" id="specialization">
            <option value="">None</option>
            <option value="Programming">Programming</option>
            <option value="CSS">CSS</option>
            <option value="Combo 1">Combo 1</option>
            <option value="Combo 2">Combo 2</option>
          </select>

          <label for="gradeLevel">Grade Level:</label>
          <select name="gradeLevel" id="gradeLevel" required>
            <option value="">Select Grade</option>
            <option value="11">Grade 11</option>
            <option value="12">Grade 12</option>
          </select>

          <label for="sectionID">Section:</label>
          <select name="sectionID" id="sectionID" required>
            <option value="">Select Section</option>
            <?php foreach ($sections as $sec): ?>
              <option value="<?= htmlspecialchars($sec['SectionID']) ?>">
                <?= htmlspecialchars($sec['SectionName']) ?>
              </option>
            <?php endforeach; ?>
          </select>

          <label for="schoolYear">School Year:</label>
          <select name="schoolYear" id="schoolYear" required>
            <option value="">Select School Year</option>
            <option value="2023-2024">2023-2024</option>
            <option value="2024-2025">2024-2025</option>
            <option value="2025-2026">2025-2026</option>
          </select>

          <button type="submit" id="submitBtn">Add Student</button>
          <button type="button" onclick="resetForm()">Cancel</button>
        </form>
      </div>
    </div>
    <div class="right">
      <div class="box">

<div style="padding: 1px;">
<input type="text" id="searchBar" placeholder="Search" style="padding: 5px; width: 100%;">
</div>
        <table class="student-table">
          <thead>
            <tr>
              <th>Section</th>
              <th>Last Name</th>
              <th>First Name</th>
              <th>Middle Name</th>
              <th>LRN</th>
              <th>Grade Level</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <?php foreach ($studentsBySection as $sectionName => $students): ?>
              <?php foreach ($students as $student): ?>
                <tr>
                  <td><?= htmlspecialchars($sectionName) ?></td>
                  <td><?= htmlspecialchars($student['LastName']) ?></td>
                  <td><?= htmlspecialchars($student['FirstName']) ?></td>
                  <td><?= htmlspecialchars($student['MiddleName']) ?></td>
                  <td><?= htmlspecialchars($student['LRN']) ?></td>
                  <td>Grade <?= htmlspecialchars($student['GradeLevel']) ?></td>
                  <td class="action-buttons">
                    <button class="edit-btn" 
                      data-lrn="<?= htmlspecialchars($student['LRN']) ?>"
                      data-firstname="<?= htmlspecialchars($student['FirstName']) ?>"
                      data-lastname="<?= htmlspecialchars($student['LastName']) ?>"
                      data-middlename="<?= htmlspecialchars($student['MiddleName']) ?>"
                      data-strand="<?= htmlspecialchars($student['Strand']) ?>"
                      data-specialization="<?= htmlspecialchars($student['Specialization']) ?>"
                      data-gradelevel="<?= htmlspecialchars($student['GradeLevel']) ?>"
                      data-sectionid="<?= htmlspecialchars($student['SectionID']) ?>"
                      data-schoolyear="<?= htmlspecialchars($student['SchoolYear']) ?>">
                      Edit
                    </button>
                    <form method="POST" style="display: inline;">
                      <input type="hidden" name="action" value="delete">
                      <input type="hidden" name="lrn" value="<?= htmlspecialchars($student['LRN']) ?>">
                      <button type="submit" class="delete-btn" 
                        onclick="return confirm('Are you sure you want to delete this student?')">
                        Delete
                      </button>
                    </form>
                  </td>
                </tr>
              <?php endforeach; ?>
            <?php endforeach; ?>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <script>
      document.addEventListener('DOMContentLoaded', () => {
    const editButtons = document.querySelectorAll('.edit-btn');
    const searchBar = document.getElementById('searchBar');
    const studentsTable = document.querySelector('.student-table tbody');

    // Search functionality
    searchBar.addEventListener('input', () => {
        const filter = searchBar.value.toUpperCase();
        const rows = studentsTable.getElementsByTagName('tr');

        for (let i = 0; i < rows.length; i++) {
            const row = rows[i];
            const cells = row.getElementsByTagName('td');
            let match = false;

            // Check all columns except the last one (Actions)
            for (let j = 0; j < cells.length - 1; j++) {
                const cell = cells[j];
                if (cell) {
                    const textValue = cell.textContent || cell.innerText;
                    if (textValue.toUpperCase().indexOf(filter) > -1) {
                        match = true;
                        break;
                    }
                }
            }

            // Show/hide row based on match
            row.style.display = match ? '' : 'none';
        }
    });
        
        editButtons.forEach(button => {
          button.addEventListener('click', () => {
            const student = {
              lrn: button.dataset.lrn,
              firstName: button.dataset.firstname,
              lastName: button.dataset.lastname,
              middleName: button.dataset.middlename,
              strand: button.dataset.strand,
              specialization: button.dataset.specialization,
              gradeLevel: button.dataset.gradelevel,
              sectionID: button.dataset.sectionid,
              schoolYear: button.dataset.schoolyear
            };

            // Populate form fields
            document.getElementById('lrn').value = student.lrn;
            document.getElementById('originalLrn').value = student.lrn;
            document.getElementById('lrn').readOnly = true; // Disable LRN editing
            document.getElementById('formAction').value = 'update';
            document.getElementById('submitBtn').textContent = 'Update Student';

            // Populate other fields
            document.getElementById('firstName').value = student.firstName;
            document.getElementById('lastName').value = student.lastName;
            document.getElementById('middleName').value = student.middleName;
            document.getElementById('strand').value = student.strand;
            document.getElementById('specialization').value = student.specialization;
            document.getElementById('gradeLevel').value = student.gradeLevel;
            document.getElementById('sectionID').value = student.sectionID;
            document.getElementById('schoolYear').value = student.schoolYear;

            // Update Specialization dropdown state
            updateSpecialization();
          });
        });
      });

      function updateSpecialization() {
        const strand = document.getElementById('strand').value;
        const specializationSelect = document.getElementById('specialization');

        if (strand === 'HUMSS' || strand === 'GAS') {
          specializationSelect.disabled = true;
          specializationSelect.value = ''; // Clear the value
        } else {
          specializationSelect.disabled = false;
        }
      }

      function resetForm() {
        document.querySelector('form').reset();
        document.getElementById('lrn').readOnly = false;
        document.getElementById('formAction').value = 'add';
        document.getElementById('submitBtn').textContent = 'Add Student';
        updateSpecialization(); // Reset Specialization dropdown state
      }
  </script>
</body>
</html>
<!--olicierrv, quipp3r-->
