<?php
session_start();
include '../backend/db.php';

// Verify admin privileges
if ($_SESSION['role'] !== 'admin' && $_SESSION['role'] !== 'teacher') {
  header("Location: dashboard.php");
  die("Unauthorized access");
}
?>
<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Grading Sheet</title>
  <link rel="stylesheet" href="../grades.css">
  <link rel="stylesheet" href="../hover.css">
  <link rel="stylesheet" href="../colours.css">
  <script src="../backend/dashfetch.js" defer></script>
  <script src="../backend/dashcheck.js" defer></script>
  <script src="../backend/check.js" defer></script>
  <script src="../backend/info.js" defer></script>
  <script src="../backend/logout.js" defer></script>
  <script src="../js/fetchannouncements.js" defer></script>
  <script src="edit/set_grading.js" defer></script>
  <style>
    .navbar ul.admin,
    .navbar ul.teacher{
      background-image: radial-gradient(circle, #ffa600,#aa6f00) !important;
    }
  </style>
  <!-- Include jsPDF and html2canvas libraries -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>

  <link rel="icon" href="../school.png">
  <script src="../menu.js" defer></script>
</head>

<body class="<?php echo isset($_SESSION['role']) ? $_SESSION['role'] : (isset($_SESSION['strand']) ? $_SESSION['strand'] : ''); ?>">
  <div class="errir"><p>Screen Size is too small</p></div>
  <div class="navbar">
  <ul class="<?php echo isset($_SESSION['role']) ? $_SESSION['role'] : (isset($_SESSION['strand']) ? $_SESSION['strand'] : ''); ?>">
  <div style="display: flex;">
    <li><a href="../about.html"><img src="../school.png"></a></li>
      <li class="title"><a>Grading Sheet</a></li>
      </div>
      <div class="link" style="float: right;">
        <div class="dropdown">
        <button class="dropbut" href="javascript:void(0);" class="icon" onclick="menu()">≡</button>
        <div id="dropdown-content">
            <a href="../index.html">Home</a>
            <a href="../dashboard.php">Dashboard</a>
            <a href="edit/announcements.php">Edit Announcements</a>
            <a href="edit/teachers.php">Edit Teachers</a>
            <a href="edit/students.php">Edit Students</a>
            <a href="#" id="logoutdrop">Log out</a>
          </div>
        </div>
        <div class="deskLink">
        <li><a style="float: right; font-size: large; color: white;" href="../index.html" >Home</a></li>
        <li><a style="float: right; font-size: large; color: white;" href="../dashboard.php" >Dashboard</a></li>
        <li><a style="float: right; font-size: large; color: white;" class="active" href="" >Grading Sheet</a></li>
        <li id="adminOnly"><a style="float: right; font-size: large; color: white;" href="dashboard/edit/announcements.php">Edit Announcements</a></li>
        <li id="adminOnly"><a style="float: right; font-size: large; color: white;" href="dashboard/edit/teachers.php" >Edit Teachers</a></li>
        <li id="teachersOnly"><a style="float: right; font-size: large; color: white;" href="dashboard/edit/students.php" >Edit Students</a></li>
        <li><a style="float: right; font-size: large; color: white;" href="#" id="logoutLink" >Log out</a></li>
      </div>
  </div>
    </ul>
  </div>
  <div class="body">
    <div class="full">
      <div class="box">
        <div class="selectarea">
          <select id="grade" onchange="fetchGrades()">
              <option value="11">Grade 11</option>
            <option value="12" selected>Grade 12</option>
          </select>
          <select id="semester" onchange="fetchGrades()">
            <option value="1">Semester 1 (Q1 & Q2)</option>
            <option value="2" selected>Semester 2 (Q3 & Q4)</option>
          </select>
          <select id="quarter" onchange="fetchGrades()">
            <option value="sectionSelect">Quarter</option>
          </select>
          <select id="sectionSelect" onchange="fetchGrades()">
            <option value="sectionSelect">Section</option>
          </select>
          <select id="schoolYear" onchange="fetchGrades()">
            <option value="schoolYear">School Year</option>
          </select>
          <!-- Only available for teachers/admins when in teacher view -->
          <select id="teacherSubjects" onchange="fetchGrades()">
            <option value="">Select Subject</option>
          </select>
          <button id="saveGradesBtn">Save Changes</button>
          <a href="../dashboard.php"><button>Exit Exit Mode</button></a>
        </div>
        <a style="font-size: medium; margin-bottom:10px;">Double click to edit</a>


        <div style="padding: 1px;">
          <input type="text" id="searchBar" placeholder="Search" style="padding: 5px; width: 100%;">
        </div>
        
    <!-- Grade Table -->
        <div>
          <table id="gradesTable">
            <thead>
              <tr><th colspan="17">Loading...</th></tr>
            </thead>
            <tbody>
              <tr><td colspan="17">Select filters to load data</td></tr>
            </tbody>
          </table>
        </div>
        <div>
        <button onclick="exportTableToPDF()" style="margin-top: 10px;">Export Table to PDF</button>
        <a href="edit/students.php" style="margin-top: 10px;"><button>Add Students</button></a>
        </div>
        <div class="grading-settings">
    <h3>Grading Settings</h3>

    <form id="gradingForm">
        <label for="numWW">Number of Written Works:</label>
        <input type="number" id="numWW" name="numWW" min="1" max="10" required>
        
        <label for="numQZ">Number of Quizzes:</label>
        <input type="number" id="numQZ" name="numQZ" min="1" max="10" required>
        
        <label for="numPT">Number of Performance Tasks:</label>
        <input type="number" id="numPT" name="numPT" min="1" max="10" required>
        
        <button type="submit">Save Settings</button>
    </form>

    <p id="message"></p>

    <table>
        <thead>
            <tr>
                <th>Written Works</th>
                <th>Quizzes</th>
                <th>Performance Tasks</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <tr><td colspan="4">No grading settings found.</td></tr>
        </tbody>
    </table>
</div>

      </div>
    </div>
  </div>

</body>

</html>
<!--olicierrv, quipp3r-->
