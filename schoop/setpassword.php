<?php
session_start();
error_reporting(E_ALL);
ini_set('display_errors', 1);

include 'db.php';

// Ensure it's a POST request
if ($_SERVER["REQUEST_METHOD"] != "POST") {
    echo json_encode(['success' => false, 'message' => 'Invalid request method.']);
    exit;
}

// Capture and validate inputs
$userinfo = $_POST['userinfo'] ?? '';
$password = $_POST['password'] ?? '';

if (empty($userinfo) || empty($password)) {
    echo json_encode(['success' => false, 'message' => 'Missing required fields.']);
    exit;
}

// Ensure password is strong enough
if (strlen($password) < 6) {
    echo json_encode(['success' => false, 'message' => 'Password must be at least 6 characters long.']);
    exit;
}

// Hash password securely
$hashedPassword = password_hash($password, PASSWORD_BCRYPT);

// Try updating the teacher's password first
$stmt = $conn->prepare("UPDATE teachers SET Password = ? WHERE Email = ?");
$stmt->bind_param("ss", $hashedPassword, $userinfo);
$stmt->execute();

// If no teacher was updated, try updating the student
if ($stmt->affected_rows === 0) {
    $stmt = $conn->prepare("UPDATE information SET Password = ? WHERE LRN = ?");
    $stmt->bind_param("ss", $hashedPassword, $userinfo);
    $stmt->execute();
}

// Check if any update was successful
if ($stmt->affected_rows > 0) {
    // Fetch user data to log them in
    if (filter_var($userinfo, FILTER_VALIDATE_EMAIL)) {
        $sql = "SELECT * FROM teachers WHERE Email = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $userinfo);
        $userType = 'teacher';
    } else {
        $sql = "SELECT * FROM information WHERE LRN = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $userinfo);
        $userType = 'student';
    }

    $stmt->execute();
    $result = $stmt->get_result();
    $row = $result->fetch_assoc();

    // Create session after setting the password
    if ($userType === 'teacher') {
        $_SESSION['loggedInUser'] = $row['Email'];
        $_SESSION['role'] = $row['Role'] ?? '';
        $_SESSION['teacherID'] = $row['TeacherID'];
        $_SESSION['subject'] = $row['Subject'] ?? '';
    } else {
        $_SESSION['loggedInUser'] = $row['LRN'];
        $_SESSION['role'] = $row['Strand'] ?? '';
        $_SESSION['studentID'] = $row['LRN'];
    }

    echo json_encode(['success' => true, 'message' => 'Password updated successfully.']);
} else {
    echo json_encode(['success' => false, 'message' => 'User not found or password unchanged.']);
}

$stmt->close();
$conn->close();
?>
