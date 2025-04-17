<?php
session_start();
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

header('Content-Type: application/json');
include '../../backend/db.php';

// Ensure only admins can access this
if (!isset($_SESSION['role']) || $_SESSION['role'] !== 'admin') {
    echo json_encode(["error" => "Unauthorized access. Admins only."]);
    exit;
}

$action = $_GET['action'] ?? '';
$teacherId = $_GET['id'] ?? '';

// Handle adding/editing a teacher
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $teacherID = trim($_POST['TeacherID'] ?? '');
    $firstName = trim($_POST['FirstName'] ?? '');
    $middleName = trim($_POST['MiddleName'] ?? '');
    $lastName = trim($_POST['LastName'] ?? '');
    $email = trim($_POST['Email'] ?? '');
    $role = ($_POST['Role'] === 'admin') ? 'admin' : 'teacher';
    $sectionId = intval($_POST['sectionId'] ?? 0);
    $subjectIds = $_POST['subjectId'] ?? []; // Array of selected subject IDs

    // Validate required fields
    if (empty($teacherID) || empty($firstName) || empty($lastName) || empty($email) || !$sectionId || empty($subjectIds)) {
        echo json_encode(["error" => "All fields are required."]);
        exit;
    }

    // Check for duplicate TeacherID or Email (excluding the current teacher during update)
    $action = $_GET['action'] ?? '';
    $teacherIdToExclude = ($action === 'update') ? $_GET['id'] : null;

    $checkQuery = "SELECT * FROM teachers WHERE (TeacherID = ? OR Email = ?)";
    if ($teacherIdToExclude) {
        $checkQuery .= " AND TeacherID != ?";
    }
    $stmt = $conn->prepare($checkQuery);
    if ($teacherIdToExclude) {
        $stmt->bind_param("sss", $teacherID, $email, $teacherIdToExclude);
    } else {
        $stmt->bind_param("ss", $teacherID, $email);
    }
    $stmt->execute();
    $result = $stmt->get_result();
    if ($result->num_rows > 0) {
        echo json_encode(["error" => "TeacherID or Email already exists."]);
        exit;
    }
    $stmt->close();

    // Insert or update teacher
    if ($action === 'update' && $teacherIdToExclude) {
        $updateQuery = "UPDATE teachers SET FirstName = ?, MiddleName = ?, LastName = ?, Email = ?, Role = ? WHERE TeacherID = ?";
        $stmt = $conn->prepare($updateQuery);
        $stmt->bind_param("ssssss", $firstName, $middleName, $lastName, $email, $role, $teacherIdToExclude);
    } else {
        $insertQuery = "INSERT INTO teachers (TeacherID, FirstName, MiddleName, LastName, Email, Password, Role) 
                        VALUES (?, ?, ?, ?, ?, NULL, ?)";
        $stmt = $conn->prepare($insertQuery);
        $stmt->bind_param("ssssss", $teacherID, $firstName, $middleName, $lastName, $email, $role);
    }

    if ($stmt->execute()) {
        // Assign subjects to the teacher
        foreach ($subjectIds as $subjectId) {
            $insertSubjectQuery = "
                INSERT INTO teacher_subjects (TeacherID, SubjectID, SectionID) 
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE SubjectID = ?, SectionID = ?
            ";
            $stmt = $conn->prepare($insertSubjectQuery);
            $stmt->bind_param("siiii", $teacherID, $subjectId, $sectionId, $subjectId, $sectionId);
            $stmt->execute();
        }

        echo json_encode(["success" => "Teacher updated successfully."]);
    } else {
        echo json_encode(["error" => "Error updating teacher: " . $stmt->error]);
    }

    $stmt->close();
    exit;
}

// Fetch a single teacher
if ($action === 'fetch' && $teacherId) {
    $query = "SELECT * FROM teachers WHERE TeacherID = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("s", $teacherId);
    $stmt->execute();
    $result = $stmt->get_result();
    $teacher = $result->fetch_assoc();
    echo json_encode($teacher);
    exit;
}

// Update a teacher
if ($action === 'update' && $teacherId) {
    $firstName = trim($_POST['FirstName'] ?? '');
    $middleName = trim($_POST['MiddleName'] ?? '');
    $lastName = trim($_POST['LastName'] ?? '');
    $email = trim($_POST['Email'] ?? '');
    $role = ($_POST['Role'] === 'admin') ? 'admin' : 'teacher';

    $query = "UPDATE teachers SET FirstName = ?, MiddleName = ?, LastName = ?, Email = ?, Role = ? WHERE TeacherID = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("ssssss", $firstName, $middleName, $lastName, $email, $role, $teacherId);

    if ($stmt->execute()) {
        echo json_encode(["success" => "Teacher updated successfully."]);
    } else {
        echo json_encode(["error" => "Error updating teacher: " . $stmt->error]);
    }
    exit;
}

// Delete a teacher
if ($action === 'delete' && $teacherId) {
    $query = "DELETE FROM teachers WHERE TeacherID = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("s", $teacherId);

    if ($stmt->execute()) {
        echo json_encode(["success" => "Teacher deleted successfully."]);
    } else {
        echo json_encode(["error" => "Error deleting teacher: " . $stmt->error]);
    }
    exit;
}

// Handle adding a new teacher
if ($_SERVER['REQUEST_METHOD'] === 'POST' && $action !== 'update') {
    $teacherID = trim($_POST['TeacherID'] ?? '');
    $firstName = trim($_POST['FirstName'] ?? '');
    $middleName = trim($_POST['MiddleName'] ?? '');
    $lastName = trim($_POST['LastName'] ?? '');
    $email = trim($_POST['Email'] ?? '');
    $role = ($_POST['Role'] === 'admin') ? 'admin' : 'teacher';

    // Validate required fields
    if (empty($teacherID) || empty($firstName) || empty($lastName) || empty($email)) {
        echo json_encode(["error" => "TeacherID, First Name, Last Name, and Email are required."]);
        exit;
    }

    // Check for duplicate TeacherID or Email
    $checkQuery = "SELECT * FROM teachers WHERE TeacherID = ? OR Email = ?";
    $stmt = $conn->prepare($checkQuery);
    $stmt->bind_param("ss", $teacherID, $email);
    $stmt->execute();
    $result = $stmt->get_result();
    if ($result->num_rows > 0) {
        echo json_encode(["error" => "TeacherID or Email already exists."]);
        exit;
    }
    $stmt->close();

    // Insert teacher with NULL password
    $insertQuery = "INSERT INTO teachers (TeacherID, FirstName, MiddleName, LastName, Email, Password, Role) 
                    VALUES (?, ?, ?, ?, ?, NULL, ?)";
    $stmt = $conn->prepare($insertQuery);
    $stmt->bind_param("ssssss", $teacherID, $firstName, $middleName, $lastName, $email, $role);

    if ($stmt->execute()) {
        echo json_encode(["success" => "Teacher added successfully."]);
    } else {
        echo json_encode(["error" => "Error adding teacher: " . $stmt->error]);
    }

    $stmt->close();
    $conn->close();
    exit;
}

// Fetch all teachers (for display)
$fetchQuery = "
    SELECT 
        t.TeacherID, 
        t.FirstName, 
        t.MiddleName, 
        t.LastName, 
        t.Email, 
        t.Role, 
        t.created_at,
        GROUP_CONCAT(DISTINCT s.SubjectName ORDER BY s.SubjectName SEPARATOR ', ') AS Subjects
    FROM teachers t
    LEFT JOIN teacher_subjects ts ON t.TeacherID = ts.TeacherID
    LEFT JOIN subject s ON ts.SubjectID = s.SubjectID
    GROUP BY t.TeacherID
    ORDER BY t.created_at DESC
";
$result = $conn->query($fetchQuery);

$teachers = [];
while ($row = $result->fetch_assoc()) {
    $teachers[] = $row;
}

echo json_encode(["teachers" => $teachers]);
$conn->close();