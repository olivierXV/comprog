<?php
session_start();
include '../backend/db.php';

error_reporting(E_ALL);
ini_set('display_errors', 1);

$isStudent = isset($_SESSION['studentID']);
$isTeacher = isset($_SESSION['teacherID']);
if (!$isStudent && !$isTeacher) {
    die(json_encode(["error" => "Unauthorized access: No valid session."]));
}

$grade = isset($_GET['grade']) ? htmlspecialchars($_GET['grade']) : null;
$semester = isset($_GET['semester']) ? intval($_GET['semester']) : 1;
$validQuarters = ($semester == 1) ? ['Q1', 'Q2'] : ['Q3', 'Q4'];
$quarter = (isset($_GET['quarter']) && in_array($_GET['quarter'], $validQuarters)) ? htmlspecialchars($_GET['quarter']) : $validQuarters[0];
$sectionID = isset($_GET['section']) ? intval($_GET['section']) : null;
$schoolYear = isset($_GET['schoolYear']) ? htmlspecialchars($_GET['schoolYear']) : null;

if (isset($_GET['fetch']) && $_GET['fetch'] === 'teacherSubjects' && $isTeacher) {
    $teacherID = $_SESSION['teacherID'];
    $query = "
       SELECT ts.SubjectID, s.SubjectName
       FROM teacher_subjects ts
       JOIN subject s ON ts.SubjectID = s.SubjectID
       WHERE ts.TeacherID = ?
       ORDER BY s.SubjectName;
    ";

    $stmt = $conn->prepare($query);
    if (!$stmt) {
        die(json_encode(["error" => "SQL Error (Teacher Subjects): " . $conn->error]));
    }
    $stmt->bind_param('s', $teacherID);
    $stmt->execute();
    $result = $stmt->get_result();

    $subjects = [];
    while ($row = $result->fetch_assoc()) {
        $subjects[] = $row;
    }

    $stmt->close();
    $conn->close();

    header('Content-Type: application/json');
    echo json_encode($subjects, JSON_PRETTY_PRINT);
    exit();
}

// Fetch available school years
if (isset($_GET['fetch']) && $_GET['fetch'] === 'schoolYears') {
    $query = "SELECT DISTINCT SchoolYear FROM grades ORDER BY SchoolYear DESC";
    $result = $conn->query($query);
    $schoolYears = [];
    while ($row = $result->fetch_assoc()) {
        $schoolYears[] = $row;
    }

    header('Content-Type: application/json');
    echo json_encode($schoolYears);
    exit();
}

// Fetch sections for teachers
if (isset($_GET['fetch']) && $_GET['fetch'] === 'sections' && $isTeacher) {
    $teacherID = $_SESSION['teacherID'];
    $query = "
        SELECT DISTINCT sec.SectionID, sec.SectionName
        FROM teacher_subjects ts
        JOIN sections sec ON ts.SectionID = sec.SectionID
        WHERE ts.TeacherID = ?
        ORDER BY sec.SectionName;
    ";

    $stmt = $conn->prepare($query);
    if (!$stmt) die(json_encode(["error" => "SQL Error (Sections): " . $conn->error]));
    $stmt->bind_param('s', $teacherID);
    $stmt->execute();
    $result = $stmt->get_result();

    $sections = [];
    while ($row = $result->fetch_assoc()) {
        $sections[] = $row;
    }

    $stmt->close();
    $conn->close();
    header('Content-Type: application/json');
    echo json_encode($sections);
    exit();
}

// Fetch grades for students
if ($isStudent) {
    $studentLRN = $_SESSION['studentID'];

    $query = "
            SELECT 
            s.SubjectName, sec.Track, ROUND(g.quarter_grade) AS quarter_grade,
            g.ww1, g.ww2, g.ww3, g.ww4, g.ww5, g.ww6, g.ww7, g.ww8, g.ww9, g.ww10,
            g.qz1, g.qz2, g.qz3, g.qz4, g.qz5, g.qz6, g.qz7, g.qz8, g.qz9, g.qz10,
            g.pt1, g.pt2, g.pt3, g.pt4, g.pt5, g.pt6, g.pt7, g.pt8, g.pt9, g.pt10,
            g.qa, g.ww_qz_average, g.pt_average,
            gs.NumWW, gs.NumQZ, gs.NumPT  -- Fetch the number of WW, QZ, and PT from grading settings
        FROM subject s
        JOIN sections sec ON s.Strand = sec.StrandName OR s.Strand = 'All'
        JOIN information i ON i.SectionID = sec.SectionID
        LEFT JOIN grades g ON g.SubjectID = s.SubjectID
                        AND g.LRN = i.LRN
                        AND g.quarter = ?
                        AND g.Grade = ?
        LEFT JOIN grading_settings gs ON gs.TeacherID = g.TeacherID AND gs.SubjectID = g.SubjectID  -- Fetch teacher grading settings
        WHERE i.LRN = ?
        AND (s.Strand = sec.StrandName OR s.Strand = 'All')
        AND (s.Track = sec.Track OR s.Track IS NULL)
        AND (s.Grade = ? OR s.Grade IS NULL)
        AND (s.Semester = ? OR s.Semester IS NULL)
        ORDER BY FIELD(s.Type, 'Core', 'Applied', 'Immersion', 'Specialized'), s.SubjectName;
    ";

    $stmt = $conn->prepare($query);
    if (!$stmt) die(json_encode(["error" => "SQL Error (Student Grades): " . $conn->error]));
    $stmt->bind_param('sssss', $quarter, $grade, $studentLRN, $grade, $semester);
}

// Fetch grades for teachers
if ($isTeacher) {
    $teacherID = $_SESSION['teacherID'];
    $subjectID = isset($_GET['subjectID']) ? intval($_GET['subjectID']) : null;

    // Fetch grading settings (unchanged)
    $gradingSettingsQuery = "SELECT NumWW, NumQZ, NumPT FROM grading_settings WHERE TeacherID = ? AND SubjectID = ? AND SchoolYear = ?";
    $stmtSettings = $conn->prepare($gradingSettingsQuery);
    $stmtSettings->bind_param('sii', $teacherID, $subjectID, $schoolYear);
    $stmtSettings->execute();
    $gradingSettings = $stmtSettings->get_result()->fetch_assoc();

    // Updated query with correct WHERE clause
    $query = "
        SELECT 
            i.LRN AS LRN,
            i.LastName, i.MiddleName, i.FirstName, 
            s.SubjectName, s.SubjectID AS SubjectID,
            g.quarter AS quarter,
            g.SchoolYear AS SchoolYear,
            g.qa,
            ROUND(g.quarter_grade) AS quarter_grade,
            g.ww1, g.ww2, g.ww3, g.ww4, g.ww5, g.ww6, g.ww7, g.ww8, g.ww9, g.ww10,
            g.qz1, g.qz2, g.qz3, g.qz4, g.qz5, g.qz6, g.qz7, g.qz8, g.qz9, g.qz10,
            g.pt1, g.pt2, g.pt3, g.pt4, g.pt5, g.pt6, g.pt7, g.pt8, g.pt9, g.pt10,
            gs.NumWW, gs.NumQZ, gs.NumPT
        FROM teacher_subjects ts
        JOIN subject s ON ts.SubjectID = s.SubjectID
        JOIN information i ON i.SectionID = ts.SectionID AND i.GradeLevel = ts.Grade
        LEFT JOIN grades g 
            ON g.SubjectID = s.SubjectID 
            AND g.LRN = i.LRN 
            AND g.quarter = ?
            AND g.SchoolYear = ?
        LEFT JOIN grading_settings gs ON gs.TeacherID = ts.TeacherID AND gs.SubjectID = s.SubjectID
        WHERE 
            ts.TeacherID = ? 
            AND ts.SectionID = ?
            AND ts.SubjectID = ?
        ORDER BY s.SubjectName, i.LastName, i.FirstName;
    ";

    $stmt = $conn->prepare($query);
    if (!$stmt) die(json_encode(["error" => "SQL Error (Teacher Grades): " . $conn->error]));
    
    // Bind parameters with correct types (sssii)
    $stmt->bind_param('sssii', $quarter, $schoolYear, $teacherID, $sectionID, $subjectID);
}

// In dashfetch.php
if (isset($_GET['fetch'])) {
    switch ($_GET['fetch']) {
        case 'teachersMasterlist':
            $query = "
                SELECT 
                    t.TeacherID,
                    t.LastName,
                    t.FirstName,
                    t.MiddleName,
                    t.Email,
                    t.Role
                FROM teachers t
                ORDER BY t.LastName, t.FirstName
            ";
        
            $result = $conn->query($query);
            if (!$result) {
                die(json_encode(["error" => "Teacher masterlist query failed: " . $conn->error]));
            }
        
            $teachers = [];
            while ($row = $result->fetch_assoc()) {
                $teachers[] = $row;
            }
        
            echo json_encode($teachers);
            exit();
            break;

            case 'studentsMasterlist':
                $query = "
                    SELECT 
                        s.SectionName,
                        i.LRN,
                        i.LastName,
                        i.FirstName,
                        i.MiddleName,
                        i.GradeLevel
                    FROM information i
                    JOIN sections s ON i.SectionID = s.SectionID
                    ORDER BY s.SectionName, i.LastName, i.FirstName
                ";
            
                $result = $conn->query($query);
                if (!$result) {
                    die(json_encode(["error" => "Student masterlist query failed: " . $conn->error]));
                }
            
                $students = [];
                while ($row = $result->fetch_assoc()) {
                    $students[] = $row;
                }
            
                echo json_encode($students);
                exit();
                break;
    }
}

// Execute and return results
$stmt->execute();
$result = $stmt->get_result();

$data = [];
while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

$stmt->close();
$conn->close();

header('Content-Type: application/json');
echo json_encode($data, JSON_PRETTY_PRINT);
?>
