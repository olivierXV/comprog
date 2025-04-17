<?php
session_start();
include '../../backend/db.php';

// Set JSON response header
header('Content-Type: application/json');

// Enable error reporting for debugging
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Ensure teacher is logged in
if (!isset($_SESSION['teacherID'])) {
    echo json_encode(["success" => false, "error" => "Unauthorized"]);
    exit;
}
error_log("Received SubjectID: " . $_GET['subjectID']); // Log the raw value

$teacherID = $_SESSION['teacherID'];
$subjectID = isset($_GET['subjectID']) ? intval($_GET['subjectID']) : 0;
error_log("Processed SubjectID: " . $subjectID); // Log the processed value

if ($subjectID <= 0) {
    echo json_encode(["success" => false, "error" => "Invalid SubjectID"]);
    exit;
}

// Query without SchoolYear
$query = "SELECT SettingID, NumWW, NumQZ, NumPT 
          FROM grading_settings 
          WHERE SubjectID = ? AND TeacherID = ?";

$stmt = $conn->prepare($query);
if (!$stmt) {
    echo json_encode(["success" => false, "error" => "SQL preparation failed: " . $conn->error]);
    exit;
}

$stmt->bind_param("is", $subjectID, $teacherID);
$executeResult = $stmt->execute();

if (!$executeResult) {
    echo json_encode(["success" => false, "error" => "Query execution failed: " . $stmt->error]);
    exit;
}

$result = $stmt->get_result();
$settings = $result->fetch_assoc();

$stmt->close();
$conn->close();

// Return JSON response
if ($settings) {
    echo json_encode(["success" => true, "data" => $settings]);
} else {
    echo json_encode(["success" => false, "message" => "No grading settings found"]);
}
?>
