// Determine base path
const basePath = window.location.pathname.includes('/dashboard/')
  ? '../backend/'
  : 'backend/';

// Function to load Teachers Masterlist (Admin View)
async function loadTeachersMasterlist() {
  try {
    const response = await fetch(`${basePath}teacher_masterlist.php`);
    if (!response.ok) throw new Error('Network error');
    const data = await response.json();
    let html = `<h3>Teachers Masterlist</h3>
      <table>
        <thead>
          <tr>
            <th>Teacher ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Subjects</th>
            <th>Grade &amp; Section</th>
          </tr>
        </thead>
        <tbody>`;
    data.forEach(teacher => {
      html += `<tr>
        <td>${teacher.TeacherID}</td>
        <td>${teacher.FirstName} ${teacher.MiddleName || ''} ${teacher.LastName}</td>
        <td>${teacher.Email}</td>
        <td>${teacher.Subjects}</td>
        <td>${teacher.Section}</td>
      </tr>`;
    });
    html += `</tbody></table>`;
    document.getElementById('tableContainer').innerHTML = html;
  } catch (err) {
    console.error('Error loading teacher masterlist:', err);
    document.getElementById('tableContainer').innerHTML = 'Error loading teacher masterlist.';
  }
}

// Function to load Students Masterlist (Admin View)
async function loadStudentsMasterlist() {
  try {
    const response = await fetch(`${basePath}student_masterlist.php`);
    if (!response.ok) throw new Error('Network error');
    const data = await response.json();
    let html = `<h3>Students Masterlist</h3>`;
    for (const section in data) {
      html += `<h4>Section: ${section}</h4>
      <table>
        <thead>
          <tr>
            <th>LRN</th>
            <th>Name</th>
          </tr>
        </thead>
        <tbody>`;
      data[section].forEach(student => {
        html += `<tr>
          <td>${student.LRN}</td>
          <td>${student.LastName}, ${student.FirstName} ${student.MiddleName || ''}</td>
        </tr>`;
      });
      html += `</tbody></table>`;
    }
    document.getElementById('tableContainer').innerHTML = html;
  } catch (err) {
    console.error('Error loading student masterlist:', err);
    document.getElementById('tableContainer').innerHTML = 'Error loading student masterlist.';
  }
}

// Function to load Grading Sheet (Teacher View)
async function loadGradingSheet() {
  try {
    const response = await fetch(`${basePath}grading_sheet.php`);
    if (!response.ok) throw new Error('Network error');
    const data = await response.json();
    let html = `<h3>Grading Sheet</h3>
      <table>
        <thead>
          <tr>
            <th>LRN</th>
            <th>Name</th>
            <th>Subject</th>
            <th>Grade</th>
          </tr>
        </thead>
        <tbody>`;
    data.forEach(item => {
      html += `<tr>
        <td>${item.LRN}</td>
        <td>${item.StudentName}</td>
        <td>${item.SubjectName}</td>
        <td>${item.Grade}</td>
      </tr>`;
    });
    html += `</tbody></table>`;
    document.getElementById('tableContainer').innerHTML = html;
  } catch (err) {
    console.error('Error loading grading sheet:', err);
    document.getElementById('tableContainer').innerHTML = 'Error loading grading sheet.';
  }
}

// Toggle functions for Admin view
function showAdminView() {
  // By default, load teacher masterlist
  loadTeachersMasterlist();
}

function switchToTeacherView() {
  loadGradingSheet();
}

// Attach event listeners when document loads
document.addEventListener('DOMContentLoaded', () => {
  const userRole = document.body.classList.contains('admin') ? 'admin' : 'teacher';

  if (userRole === 'admin') {
    document.getElementById('showTeachersBtn').addEventListener('click', loadTeachersMasterlist);
    document.getElementById('showStudentsBtn').addEventListener('click', loadStudentsMasterlist);
    document.getElementById('switchToTeacherBtn').addEventListener('click', switchToTeacherView);
    // Load the default admin view (teachers masterlist)
    showAdminView();
  } else {
    // For teacher users, load grading sheet by default
    loadGradingSheet();
  }
});
