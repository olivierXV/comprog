// Store pending changes
let pendingChanges = [];

// Dynamically determine the basePath based on the current location
const basePath = window.location.pathname.includes('/dashboard/') ? '../backend/' : 'backend/';

document.addEventListener('DOMContentLoaded', () => {
    fetchSchoolYears();
    fetchSections();
    updateQuarterOptions();
    studfetch();
    title();

    fetchTeacherSubjects().then(() => {
        // Now attach change listeners after subjects are loaded
        ['schoolYear', 'semester', 'quarter', 'grade', 'sectionSelect', 'teacherSubjects'].forEach(id => {
            document.getElementById(id)?.addEventListener('change', () => {
                fetchGrades();
                updateFullSection();
            });
        });

        if (document.body.classList.contains('admin')) {
            initializeAdminView();
        } else {
            initializeRegularView();
        }
    });

    // Attach saveAllChanges to the Save button
    const saveButton = document.getElementById('saveGradesBtn');
    if (saveButton) {
        saveButton.addEventListener('click', saveAllChanges);
    } else {
        console.error('Save button not found!');
    }




    
    function initializeAdminView() {
        const adminControls = document.getElementById('adminControls');
        if (!adminControls) return;
    
        let isAdminView = true; // Track view state
        
        // Elements to toggle
        const selectArea = document.querySelector('.selectarea');
        const gradesTable = document.getElementById('gradesTable');
        const toggleBtn = document.getElementById('toggleTeachersView');
    
        // Update UI based on view state
        const updateView = () => {
            if (isAdminView) {
                // Admin view - show masterlists
                adminControls.style.display = 'block';
                if (selectArea) selectArea.style.display = 'none';
                if (gradesTable) gradesTable.style.display = 'none';
                toggleBtn.textContent = "Show Teachers View";
                fetchTeachersMasterlist(); // Load default admin view
            } else {
                // Teacher view - show grade entry
                adminControls.style.display = 'block';
                if (selectArea) selectArea.style.display = 'block';
                if (gradesTable) gradesTable.style.display = 'table';
                toggleBtn.textContent = "Show Admin View";
            }
        };
    
        // Add event listeners
        const addAdminListener = (id, handler) => {
            const element = document.getElementById(id);
            if (element) element.addEventListener('click', handler);
        };
    
        addAdminListener('showTeachersMasterlist', fetchTeachersMasterlist);
        addAdminListener('showStudentsMasterlist', fetchStudentsMasterlist);
        
        addAdminListener('toggleTeachersView', () => {
            isAdminView = !isAdminView;
            updateView();
            
            if (!isAdminView) {
                fetchGrades(); // Only fetch grades when switching to teacher view
            }
        });
    
        // Initial setup
        updateView();
        fetchTeachersMasterlist(); // Load default admin view
    }
    
    // New functions for admin views
    async function fetchTeachersMasterlist() {
        try {
            const response = await fetch(`${basePath}dashfetch.php?fetch=teachersMasterlist`);
            const teachers = await response.json();
            
            const tableHTML = `
                <table class="masterlist">
                    <thead>
                        <tr>
                            <th>Last Name</th>
                            <th>First Name</th>
                            <th>Middle Name</th>
                            <th>Email</th>
                            <th>Role</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${teachers.map(teacher => `
                            <tr>
                                <td>${teacher.LastName}</td>
                                <td>${teacher.FirstName}</td>
                                <td>${teacher.MiddleName || ''}</td>
                                <td>${teacher.Email}</td>
                                <td>${teacher.Role || 'N/A'}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
            `;
            
            document.getElementById('gradesTable').innerHTML = tableHTML;
            document.getElementById('gradesTable').style.display = 'table';
            document.querySelector('.selectarea').style.display = 'none';
    
            // Attach the search functionality
            document.getElementById('searchBar').addEventListener('input', filterTable);
        } catch (error) {
            console.error('Error fetching teachers masterlist:', error);
        }
    }
    
    async function fetchStudentsMasterlist() {
        try {
            const response = await fetch(`${basePath}dashfetch.php?fetch=studentsMasterlist`);
            const students = await response.json();
    
            const tableHTML = `
                <table class="masterlist">
                    <thead>
                        <tr>
                            <th>Section</th>
                            <th>Last Name</th>
                            <th>First Name</th>
                            <th>Middle Name</th>
                            <th>LRN</th>
                            <th>Grade Level</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${students.map(student => `
                            <tr>
                                <td>${student.SectionName}</td>
                                <td>${student.LastName}</td>
                                <td>${student.FirstName}</td>
                                <td>${student.MiddleName || ''}</td>
                                <td>${student.LRN}</td>
                                <td>Grade ${student.GradeLevel}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
            `;
            
            document.getElementById('gradesTable').innerHTML = tableHTML;
            document.getElementById('gradesTable').style.display = 'table';
    
            // Attach the search functionality
            document.getElementById('searchBar').addEventListener('input', filterTable);
        } catch (error) {
            console.error('Error fetching students masterlist:', error);
            document.getElementById('gradesTable').innerHTML = `
                <div class="error">
                    Error loading student list. Please try again later.
                </div>
            `;
        }
    }
    
    function initializeRegularView() {
        // Existing initialization for teachers/students
        fetchSchoolYears();
        fetchSections();
        updateQuarterOptions();
        studfetch();
        title();
    
        ['schoolYear', 'semester', 'quarter', 'grade', 'sectionSelect'].forEach(id => {
            document.getElementById(id)?.addEventListener('change', () => {
                fetchGrades();
                updateFullSection();
            });
        });
    
        document.getElementById('semester')?.addEventListener('change', updateQuarterOptions);
    
        if (document.getElementById('teacherSubjects')) {
            fetchTeacherSubjects();
        }
    }

    document.getElementById('semester')?.addEventListener('change', updateQuarterOptions);

    // If the teacherSubjects select exists (for teachers/admins), fetch subjects.
    if (document.getElementById('teacherSubjects')) {
        fetchTeacherSubjects();
    }
});

async function fetchTeacherSubjects() {
    const subjectSelect = document.getElementById('teacherSubjects');
    if (!subjectSelect) return;

    try {
        const response = await fetch(`${basePath}dashfetch.php?fetch=teacherSubjects`);
        const data = await response.json();
        console.log("Subjects Data:", data); // Inspect the response

        subjectSelect.innerHTML = ''; // Clear default options
        subjectSelect.innerHTML += '<option value="">Select Subject</option>'; // Add default

        if (!Array.isArray(data) || data.length === 0) {
            subjectSelect.innerHTML = '<option>No subjects assigned</option>';
            return;
        }

        // Populate subjects with explicit value attributes
        data.forEach(sub => {
            const option = document.createElement('option');
            option.value = sub.SubjectID; // Ensure this is a number (e.g., 16)
            option.textContent = sub.SubjectName;
            subjectSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error fetching subjects:', error);
        subjectSelect.innerHTML = '<option>Error loading subjects</option>';
    }
}

async function title() {
    const setText = (id, text) => {
        const element = document.getElementById(id);
        if (element) element.textContent = text;
    };

    const isAdminOrTeacher = document.body.classList.contains('admin') || 
                             document.body.classList.contains('teacher');

    if (document.body.classList.contains('admin')) {
        setText('dashb', 'Admin Dashboard');
    } else if (document.body.classList.contains('teacher')) {
        setText('dashb', `Teacher's Dashboard`);
    } else {
        setText('dashb', `Student Dashboard`);
    }
}

// Function to filter table rows based on search input
function filterTable() {
    const searchInput = document.getElementById('searchBar');
    const filter = searchInput.value.toUpperCase();
    const table = document.getElementById('gradesTable'); // Reference the gradesTable
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

// Attach the filter function to the search bar's input event
document.getElementById('searchBar').addEventListener('input', filterTable);

// Fetch available school years
async function fetchSchoolYears() {
    const schoolYearSelect = document.getElementById('schoolYear');

    try {
        const response = await fetch(`${basePath}dashfetch.php?fetch=schoolYears`);
        const data = await response.json();

        if (!Array.isArray(data) || data.length === 0) {
            console.error('No school years available.');
            return;
        }

        schoolYearSelect.innerHTML = data.map(year =>
            `<option value="${year.SchoolYear}">${year.SchoolYear}</option>`
        ).join('');

        fetchGrades(); // Load grades after populating school years
    } catch (error) {
        console.error('Error fetching school years:', error);
    }
}

// Update the quarter options based on the semester
function updateQuarterOptions() {
    const semester = document.getElementById('semester')?.value;
    const quarterSelect = document.getElementById('quarter');

    quarterSelect.innerHTML = (semester === '1')
        ? `<option value="Q1">Quarter 1</option><option value="Q2">Quarter 2</option>`
        : `<option value="Q3">Quarter 3</option><option value="Q4">Quarter 4</option>`;

    fetchGrades(); // Update grades when semester changes
}

// Fetch sections
async function fetchSections() {
    const sectionSelect = document.getElementById('sectionSelect');

    try {
        const response = await fetch(`${basePath}dashfetch.php?fetch=sections`);
        const data = await response.json();

        sectionSelect.innerHTML = data.map(section =>
            `<option value="${section.SectionID}">${section.SectionName}</option>`
        ).join('');

        fetchGrades(); // Fetch grades after sections load
    } catch (error) {
        console.error('Error fetching sections:', error);
    }
}

// Store changes in pendingChanges array
function storePendingChange(lrn, field, value, subject, quarter, schoolYear, grade) {
    // Check if an entry already exists for this LRN, subject, quarter, and school year
    const existingChange = pendingChanges.find(change =>
        change.lrn === lrn &&
        change.subjectID === subject &&
        change.quarter === quarter &&
        change.schoolYear === schoolYear &&
        change.grade === grade
    );

    if (existingChange) {
        // Update existing entry
        existingChange[field] = value;
    } else {
        // Add a new entry with all fields (including empty ones)
        const newChange = {
            lrn,
            subjectID: subject,
            quarter,
            schoolYear,
            grade,
            [field]: value
        };
        pendingChanges.push(newChange);
    }

    console.log('Pending Changes:', pendingChanges);
}

// Fetch and display grades
async function fetchGrades() {
    // Common parameters
    const schoolYear = document.getElementById('schoolYear')?.value;
    const grade = document.getElementById('grade')?.value;
    const semester = document.getElementById('semester')?.value;
    const quarter = document.getElementById('quarter')?.value;
    const section = document.getElementById('sectionSelect')?.value;

    // Teacher-specific parameter
    const subjectID = document.getElementById('teacherSubjects')?.value;

    // Determine user type
    const isTeacher = document.body.classList.contains('teacher') || 
                     document.body.classList.contains('admin');
    const isStudent = !isTeacher;

    const table = document.getElementById('gradesTable');
    const tbody = table.querySelector('tbody');
    const thead = table.querySelector('thead');

    try {
        // Build URL based on user type
        let url;
        if (isTeacher) {
            url = `${basePath}dashfetch.php?schoolYear=${schoolYear}&grade=${grade}&` +
                 `semester=${semester}&quarter=${quarter}&section=${section}&subjectID=${subjectID}`;
        } else {
            url = `${basePath}dashfetch.php?schoolYear=${schoolYear}&grade=${grade}&` +
                 `semester=${semester}&quarter=${quarter}&section=${section}`;
        }

        const response = await fetch(url);
        if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);

        const data = await response.json();
        tbody.innerHTML = '';
        thead.innerHTML = '';

        if (!Array.isArray(data) || data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="17">No data available.</td></tr>';
            return;
        }

        // Update the table with the fetched data
        const settings = data[0] ? {
            NumWW: data[0].NumWW || 10, // Default to 10 if not provided
            NumQZ: data[0].NumQZ || 10, // Default to 10 if not provided
            NumPT: data[0].NumPT || 4 // Default to 4 if not provided
        } : { NumWW: 10, NumQZ: 10, NumPT: 4 };

        updateGradeTable(data, settings);
    } catch (error) {
        console.error('Error fetching grades:', error);
        tbody.innerHTML = '<tr><td colspan="17">Error loading data. Please try again later.</td></tr>';
    }
}


// Helper function to generate editable cells
function createEditableCell(item, field, quarter, schoolYear) {
    return `
        <td class="editable" 
            data-lrn="${item.LRN || ''}"
            data-subject="${item.SubjectID || ''}"
            data-quarter="${quarter || ''}"
            data-schoolyear="${schoolYear || ''}"
            data-field="${field}">
            ${item[field] ?? 'N/A'}
        </td>`;
}

function updateGradeTable(data, settings) {
    const table = document.getElementById('gradesTable');
    const tbody = table.querySelector('tbody');
    const thead = table.querySelector('thead');

    tbody.innerHTML = '';
    thead.innerHTML = '';

    if (!Array.isArray(data) || data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="17">No data available.</td></tr>';
        return;
    }

    const isTeacher = data[0].hasOwnProperty('LastName');
    
    if (isTeacher) {
        renderTeacherTable(data, settings, thead, tbody);
    } else {
        renderStudentTable(data, thead, tbody); // Pass thead here
    }
}

function renderTeacherTable(data, settings, thead, tbody) {
    // Generate headers based on settings
    let headerHTML = `<tr>
        <th>Last Name</th>
        <th>Middle Name</th>
        <th>First Name</th>`;

    // Add headers for Written Works (WW)
    for (let i = 1; i <= settings.NumWW; i++) {
        headerHTML += `<th>W${i}</th>`;
    }

    // Add headers for Quizzes (QZ)
    for (let i = 1; i <= settings.NumQZ; i++) {
        headerHTML += `<th>Q${i}</th>`;
    }

    // Add headers for Performance Tasks (PT)
    for (let i = 1; i <= settings.NumPT; i++) {
        headerHTML += `<th>PT${i}</th>`;
    }

    // Add headers for QA, Averages, and Quarter Grade
    headerHTML += `
        <th>QA</th>
        <th>WW Avg</th>
        <th>PT Avg</th>
        <th>Quarter Grade</th>
    </tr>`;

    thead.innerHTML = headerHTML;

    // Populate rows with data
    data.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.LastName || 'N/A'}</td>
            <td>${item.MiddleName || 'N/A'}</td>
            <td>${item.FirstName || 'N/A'}</td>
            ${generateEditableCells(item, 'ww', settings.NumWW)}
            ${generateEditableCells(item, 'qz', settings.NumQZ)}
            ${generateEditableCells(item, 'pt', settings.NumPT)}
            <td class="editable" 
                data-lrn="${item.LRN}"
                data-subject="${item.SubjectID}"
                data-quarter="${item.quarter}"
                data-schoolyear="${item.SchoolYear}"
                data-field="qa">
                ${item.qa ?? 'N/A'}
            </td>
            <td>${item.ww_qz_average || 'N/A'}</td>
            <td>${item.pt_average || 'N/A'}</td>
            <td>${item.quarter_grade || 'N/A'}</td>
        `;
        tbody.appendChild(row);
    });

    // Enable editing if on the edit page
    if (window.location.pathname.includes('/dashboard/edit.php')) {
        enableEditing();
    }
}

function generateEditableCells(item, prefix, count) {
    let cells = '';
    for (let i = 1; i <= count; i++) {
        const field = `${prefix}${i}`;
        cells += `
            <td class="editable" 
                data-lrn="${item.LRN}"
                data-subject="${item.SubjectID}"
                data-quarter="${item.quarter}"
                data-schoolyear="${item.SchoolYear}"
                data-field="${field}">
                ${item[field] ?? 'N/A'}
            </td>
        `;
    }
    return cells;
}

function renderStudentTable(data, thead, tbody) { // Accept thead as a parameter
    thead.innerHTML = `<tr><th>Subject</th><th>Quarter Grade</th></tr>`;
    data.forEach(item => {
        tbody.innerHTML += `
            <tr>
                <td>${item.SubjectName}</td>
                <td>${item.quarter_grade || 'N/A'}</td>
            </tr>
        `;
    });
}

// Enable editing on double-click (collects changes)
function enableEditing() {
    document.querySelectorAll('.editable').forEach(cell => {
        cell.addEventListener('dblclick', () => {
            const originalValue = cell.textContent.trim();
            const input = document.createElement('input');
            input.type = 'text';
            input.value = originalValue;

            cell.innerHTML = '';
            cell.appendChild(input);
            input.focus();

            // Save change only on blur or Enter key
            const saveChange = () => {
                const newValue = parseInt(input.value.trim(), 10);
                if (!isNaN(newValue)) {
                    const lrn = cell.dataset.lrn;
                    const field = cell.dataset.field;
                    const subject = cell.dataset.subject;
                    const quarter = cell.dataset.quarter;
                    const schoolYear = cell.dataset.schoolyear;
                    const grade = getGrade(); // Fetch the grade value

                    if (lrn && field && subject && quarter && schoolYear && grade) {
                        storePendingChange(lrn, field, newValue, subject, quarter, schoolYear, grade);
                    } else {
                        console.error('Missing data:', { lrn, field, subject, quarter, schoolYear, grade });
                    }
                }
                cell.textContent = newValue || 'N/A';
            };

            input.addEventListener('blur', saveChange);
            input.addEventListener('keydown', (e) => {
                if (e.key === 'Enter') saveChange();
            });
        });
    });
}

// Save all pending changes when Save button is clicked
async function saveAllChanges() {
    // Show a confirmation dialog using the native browser `confirm()`
    if (!confirm('Are you sure you want to save the edits?')) {
        return; // Exit if the user cancels
    }

    // Check if there are any pending changes
    if (pendingChanges.length === 0) {
        alert('No changes to save.'); // Notify the user if there are no changes
        return;
    }

    console.log('Saving changes:', pendingChanges); // Debugging

    try {
        // Send the changes to the server
        const response = await fetch(`${basePath}bulk_update.php`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ changes: pendingChanges })
        });

        const result = await response.json();
        console.log('Save response:', result); // Debugging

        // Check if the request was successful
        if (!response.ok) throw new Error(result.error || 'Failed to save grades');

        // Notify the user of success
        alert('Grades updated successfully.');

        // Clear pending changes and refresh the table
        pendingChanges.length = 0;
        fetchGrades();
    } catch (error) {
        console.error('Error saving grades:', error);
        alert('Error saving grades. Please try again.');
    }
}

// Attach the saveAllChanges function to the Save button
document.getElementById('saveGradesBtn').addEventListener('click', saveAllChanges);

// Fetch user info and store in global variables
async function studfetch() {
    try {
        const response = await fetch(`${basePath}info.php`);
        if (!response.ok) throw new Error(`Network error: ${response.statusText}`);
        const data = await response.json();
        console.log('User  Info:', data);
        if (!data.success) throw new Error('Error fetching user information');
        fullSection = `Grade ${data.GradeLevel || ''} ${data.Section || ''}`.trim();
    } catch (error) {
        console.error('Error fetching user data:', error);
    }
}

// Update the fullSection variable or DOM element
function updateFullSection() {
    const grade = document.getElementById('grade').value; // Get selected grade
    const sectionSelect = document.getElementById('sectionSelect');
    const sectionName = sectionSelect.options[sectionSelect.selectedIndex]?.text || '';
    
    // Update the fullSection variable or DOM element
    const fullSection = `Grade ${grade} ${sectionName}`;
    
    // If displaying in the UI (e.g., a <span>), update it:
    document.getElementById('full-section-display').textContent = fullSection;
}

// Helper function to get the grade value
function getGrade() {
    return document.getElementById('grade')?.value || '';
}



//code from online, jsPDF and html2canvas
// Helper function: load an image from a URL and return a Promise
function loadImage(url) {
    return new Promise((resolve, reject) => {
      const img = new Image();
      img.src = url;
      img.onload = () => resolve(img);
      img.onerror = (err) => reject(err);
    });
  }
  
  function exportTableToPDF() {
    const isAdminOrTeacher = document.body.classList.contains('admin') || 
                             document.body.classList.contains('teacher');

    let fullSection;
    if (isAdminOrTeacher) {
        // For admins/teachers: Construct from dropdowns
        const grade = document.getElementById('grade').value;
        const sectionName = document.getElementById('sectionSelect').selectedOptions[0]?.text || '';
        fullSection = `Grade ${grade} ${sectionName}`;
    } else {
        // For others: Fetch from a DOM element (e.g., a hidden field or displayed text)
        fullSection = document.getElementById('full-section')?.textContent.trim() || '';
    }

    // Get the info values from the DOM
    const fullName = document.getElementById('full-name')?.textContent.trim() || '';

    const schoolYearVal = document.getElementById('schoolYear')?.value || '';
    const gradeVal = document.getElementById('grade')?.value || '';
    const semesterVal = document.getElementById('semester')?.value || '';
    const quarterVal = (document.getElementById('quarter')?.value || '').replace('Q', '');
    
    // Create the info block as an array of lines
    const infoLines = [
      fullName,
      `School Year ${schoolYearVal}`,
      fullSection,
      `Computer Programming`,
      `Semester ${semesterVal} Quarter ${quarterVal}`,
      ``
    ];
  
    // Set font size and line height for the info block
    const infoFontSize = 10;
    const lineHeight = infoFontSize + 4; // e.g., 16 points per line
    const infoBlockHeight = infoLines.length * lineHeight;
  
    // Get the table element
    const table = document.getElementById('gradesTable');
    if (!table) {
      alert('Table not found!');
      return;
    }
  
    // Capture the table as an image using html2canvas
    html2canvas(table).then(canvas => {
      const tableImgData = canvas.toDataURL('image/png');
      const { jsPDF } = window.jspdf;
      // Create an A4 PDF in portrait (units in points)
      const pdf = new jsPDF('p', 'pt', 'a4');
      const pdfWidth = pdf.internal.pageSize.getWidth();   // ~595.28 pt
      const pdfHeight = pdf.internal.pageSize.getHeight(); // ~841.89 pt
  
      // Fixed header and footer heights in points
      const headerMaxHeight = 355.68; // 4.94 inches
      const footerMaxHeight = 287.28; // 3.99 inches
  
      // Side margin for the table: 1 inch = 72 points on each side
      const sideMargin = 48;
      const tableAvailableWidth = pdfWidth - 1 * sideMargin;
  
      // Load header and footer images from ../backend/pdf/
      Promise.all([
        loadImage(`${basePath}pdf/header.png`),
        loadImage(`${basePath}pdf/footer.png`)
      ]).then(([headerImg, footerImg]) => {
        // --- Header Image Scaling ---
        // Scale header image to fill full width and preserve aspect ratio; cap height at headerMaxHeight.
        const computedHeaderHeight = pdfWidth * (headerImg.naturalHeight / headerImg.naturalWidth);
        const headerImgHeight = computedHeaderHeight > headerMaxHeight ? headerMaxHeight : computedHeaderHeight;
        const headerImgWidth = pdfWidth; // fill full width
  
        // --- Footer Image Scaling ---
        const computedFooterHeight = pdfWidth * (footerImg.naturalHeight / footerImg.naturalWidth);
        const footerImgHeight = computedFooterHeight > footerMaxHeight ? footerMaxHeight : computedFooterHeight;
        const footerImgWidth = pdfWidth; // fill full width
  
        // --- Calculate available vertical space for info block + table ---
        const availableHeight = pdfHeight - headerImgHeight - footerImgHeight;
        // Deduct info block height to get available height for table image
        const tableAvailableHeight = availableHeight - infoBlockHeight;
  
        // --- Scale Table Image ---
        const conversionFactor = 72 / 96; // 0.75
        const tableImgOriginalWidth = canvas.width * conversionFactor;
        const tableImgOriginalHeight = canvas.height * conversionFactor;
        // Center the table image horizontally within the page, but ensure at least sideMargin on each side
        let tableImgWidth = tableImgOriginalWidth;
        let tableImgHeight = tableImgOriginalHeight;
        if (tableImgWidth > (pdfWidth - sideMargin * 2)) {
          tableImgWidth = pdfWidth - sideMargin * 2;
          tableImgHeight = canvas.height * (tableImgWidth / canvas.width) * conversionFactor;
        }
        const tableX = (pdfWidth - tableImgWidth) / 2;
        // Position the table image below the header + info block
        const tableY = headerImgHeight + infoBlockHeight;
  
        // --- Assemble PDF ---
        // Add header image at top, spanning full width
        pdf.addImage(headerImg, 'PNG', 0, 0, headerImgWidth, headerImgHeight);
  
        // Add info block text just below the header
        pdf.setFont("helvetica", "bold"); // Set font to bold
        pdf.setFontSize(infoFontSize);
        let textY = headerImgHeight + infoFontSize; // starting y for text
        
        infoLines.forEach(line => {
          // Left align the text using the sideMargin as the x-coordinate
          pdf.text(line, sideMargin, textY, { align: "left" });
          textY += lineHeight;
        });
  
        // Add table image, centered horizontally in the available space
        pdf.addImage(tableImgData, 'PNG', tableX, tableY, tableImgWidth, tableImgHeight);
  
        // Add footer image at bottom, spanning full width
        pdf.addImage(footerImg, 'PNG', 0, pdfHeight - footerImgHeight, footerImgWidth, footerImgHeight);
  
        // Save the PDF
        pdf.save('grades.pdf');
      }).catch(error => {
        console.error('Error loading header or footer image:', error);
      });
    }).catch(error => {
      console.error('Error exporting table to PDF:', error);
    });
  }
  