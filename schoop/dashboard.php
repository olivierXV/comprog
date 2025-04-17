<?php
session_start();
?>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Dashboard</title>
  <link rel="stylesheet" href="colours.css">
  <link rel="stylesheet" href="hover.css">
  <link rel="stylesheet" href="grades.css">
  <!-- Your existing scripts -->
  <script src="backend/dashfetch.js" defer></script>
  <script src="backend/dashcheck.js" defer></script>
  <script src="backend/check.js" defer></script>
  <script src="backend/info.js" defer></script>
  <script src="backend/logout.js" defer></script>
  <script src="js/fetchannouncements.js" defer></script>
  <!-- Include the dashboard toggle script for masterlists -->
  <script src="menu.js" defer></script>
  
  <!-- Include jsPDF and html2canvas libraries -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>
  <link rel="icon" href="school.png">
</head>
<body class="<?php echo isset($_SESSION['role']) ? $_SESSION['role'] : ''; ?>">
  <div class="errir"><p>Screen Size is too small</p></div>
  <div class="navbar">
    <ul class="<?php echo isset($_SESSION['role']) ? $_SESSION['role'] : ''; ?>">
    <div style="display: flex;">
        <li><a href="about.html"><img src="school.png"></a></li>
        <li class="title"><a id="dashb">Dashboard</a></li>
      </div>
      <div class="link" style="float: right;">

        <div class="desklink">
        <li><a style="float: right; font-size: large; color: white;" href="index.html">Home</a></li>
        <li><a style="float: right; font-size: large; color: white;" class="active" href="">Dashboard</a></li>
        <li id="teachersOnly"><a style="float: right; font-size: large; color: white;" href="dashboard/edit.php" >Grading Sheet</a></li>
        <li><a style="float: right; font-size: large; color: white;" href="#" id="logoutLink">Log out</a></li>
        </div>
        <div class="dropdown">
          <button class="dropbut" href="javascript:void(0);" class="icon" onclick="menu()">≡</button>
          <div id="dropdown-content">
          <a href="index.html">Home</a>
            <a href="">Dashboard</a>
            <a href="dashboard/edit.php" id="teachersOnly">Grading Sheet</a>
            <a href="dashboard/edit/announcements.php" id="adminOnly">Edit Announcements</a>
            <a href="dashboard/edit/teachers.php" id="adminOnly">Edit Teachers</a>
            <a href="dashboard/edit/students.php" id="teachersOnly">Edit Students</a>
            <a href="#" id="logoutdrop">Log out</a>
          </div>
        </div>
      </div>
    </ul>
  </div>
  
  <div class="body">
    <div class="left">
      <div class="box">
        <ul>
          <li><a id="full-name"></a></li>
          <li><a id="lrn"></a></li>
          <li><a id="full-section"></a></li>
          <hr style="height: 1px; width: 100%; background-color: #000;">
          <li><a id="announcements"></a></li>
          <?php if (isset($_SESSION['role']) && $_SESSION['role'] === 'admin'): ?>
          <a href="dashboard/edit/announcements.php" style="margin-top: 20px;" id="adminOnly">
            <button>Edit Announcements</button>
          </a>
          <?php endif; ?>
        </ul>
      </div>
    </div>
    
    <div class="right">
      <div class="box">
        <!-- Toggle buttons for Admin View -->
        <div id="adminControls" style="display: none; padding:1%;">
          <button id="toggleTeachersView">Show Teachers View</button>
          <button id="showTeachersMasterlist">Show Teachers Masterlist</button>
          <button id="showStudentsMasterlist">Show Students Masterlist</button>
          <div style="margin: 1%; display:flex;">
            <a href="dashboard/edit/students.php" id="adminOnly"><button>Edit Students</button></a>
            <a href="dashboard/edit/teachers.php" id="adminOnly"><button>Edit Teachers</button></a>
          </div>
        </div>
        
        <!-- Filters for grading sheet (for teachers and admins in teacher view) -->
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
        <div>
          <a href="dashboard/edit.php" id="edit" target="_blank">
            <button id="edit">Open Grading Sheet</button>
          </a>
          <button onclick="exportTableToPDF()" style="margin-top: 10px;">Export Table to PDF</button>
        </div>  
        </div>

        <div style="padding: 1px;">
        <input type="text" id="searchBar" placeholder="Search" style="padding: 5px; width: 100%;">
        </div>
        
        <!-- Table Container where masterlist or grading sheet will be displayed -->
        <div id="tableContainer" style="padding:2px">
          <table id="gradesTable">
            <thead>
              <tr><th colspan="17">Loading...</th></tr>
            </thead>
            <tbody>
              <tr><td colspan="17">Select filters to load data</td></tr>
            </tbody>
          </table>
        </div>
        
      </div>
    </div>
  </div>
  
</body>
</html>
