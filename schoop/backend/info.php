<?php
session_start();
header('Content-Type: application/json');

error_reporting(E_ALL);
ini_set('display_errors', 1);

include '../backend/db.php';

// Redirect to login if not logged in
if (!isset($_SESSION['loggedInUser'])) {
    echo json_encode(['success' => false, 'message' => 'Not logged in']);
    exit();
}

$loggedInUser = $_SESSION['loggedInUser'];

// Check if the user is a teacher (by email) or a student (by LRN)
if (isset($_SESSION['role']) && 
    ( $_SESSION['role'] === 'teacher' || $_SESSION['role'] === 'admin' ) && 
    isset($_SESSION['teacherID'])) {

    $teacherID = $_SESSION['teacherID'];

    // Use LEFT JOIN so that even if no subjects or sections exist, you still get the teacher's info.
    $sql = "SELECT t.TeacherID, t.LastName, t.MiddleName, t.FirstName, t.Email, t.Role, 
                   s.SubjectName, sec.SectionName
            FROM teachers t
            LEFT JOIN teacher_subjects ts ON t.TeacherID = ts.TeacherID
            LEFT JOIN subject s ON ts.SubjectID = s.SubjectID
            LEFT JOIN sections sec ON ts.SectionID = sec.SectionID
            WHERE t.TeacherID = ?";

    $stmt = $conn->prepare($sql);
    if (!$stmt) {
        echo json_encode(['success' => false, 'message' => 'SQL prepare error: ' . $conn->error]);
        exit();
    }
    $stmt->bind_param("s", $teacherID);
    $userType = 'teacher';

} else {
    // Query for student information
    $sql = "SELECT LRN, FirstName, MiddleName, LastName, GradeLevel, Section, Strand 
            FROM information WHERE LRN = ?";

    $stmt = $conn->prepare($sql);
    if (!$stmt) {
        echo json_encode(['success' => false, 'message' => 'SQL prepare error: ' . $conn->error]);
        exit();
    }
    $stmt->bind_param("s", $loggedInUser);
    $userType = 'student';
}

$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    if ($userType === 'teacher') {
        // For teachers/admin, you might have multiple rows if they have more than one subject.
        $teacherData = [];
        while ($row = $result->fetch_assoc()) {
            // Group by teacher basic info. Optionally, merge subjects into an array.
            $teacherData[] = [
                'TeacherID'   => $row['TeacherID'],
                'FirstName'   => $row['FirstName'],
                'MiddleName'  => $row['MiddleName'],
                'LastName'    => $row['LastName'],
                'Email'       => $row['Email'],
                'Role'        => $row['Role'],
                'SubjectName' => $row['SubjectName'],   // May be null if not assigned
                'SectionName' => $row['SectionName']      // May be null if not assigned
            ];
        }
        echo json_encode(['success' => true, 'data' => $teacherData]);
    } else {
        // For student, fetch a single record
        $row = $result->fetch_assoc();
        echo json_encode([
            'success'    => true,
            'LRN'        => $row['LRN'],
            'FirstName'  => $row['FirstName'],
            'MiddleName' => $row['MiddleName'],
            'LastName'   => $row['LastName'],
            'GradeLevel' => $row['GradeLevel'],
            'Section'    => $row['Section'],
            'Strand'     => $row['Strand']
        ]);
    }
} else {
    echo json_encode(['success' => false, 'message' => 'User not found']);
}

$stmt->close();
$conn->close();
?>
