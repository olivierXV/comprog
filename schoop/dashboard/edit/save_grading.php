<?php
session_start();
include '../../backend/db.php';

header('Content-Type: application/json');

// Ensure the teacher is logged in
if (!isset($_SESSION['teacherID'])) {
    echo json_encode(["success" => false, "error" => "Unauthorized"]);
    exit;
}

// Check database connection
if ($conn->connect_error) {
    echo json_encode(["success" => false, "error" => "Database connection failed: " . $conn->connect_error]);
    exit;
}

$input = file_get_contents("php://input");
error_log("Received input: " . $input); // Log the raw input

// Validate input
if (!$input) {
    echo json_encode(["success" => false, "error" => "No data received"]);
    exit;
}

$data = json_decode($input, true);
error_log("Decoded data: " . print_r($data, true)); // Log the decoded data

if (!$data || !isset($data['subjectID'], $data['numWW'], $data['numQZ'], $data['numPT'])) {
    echo json_encode(["success" => false, "error" => "Invalid input data"]);
    exit;
}

$teacherID = $_SESSION['teacherID'];
$subjectID = intval($data['subjectID']);
error_log("Processed SubjectID: " . $subjectID); // Log the processed SubjectID

// Validate Subject ID
if ($subjectID <= 0) {
    echo json_encode(["success" => false, "error" => "Invalid SubjectID"]);
    exit;
}

$numWW = intval($data['numWW']);
$numQZ = intval($data['numQZ']);
$numPT = intval($data['numPT']);

// Validate input values
if ($numWW < 0 || $numQZ < 0 || $numPT < 0) {
    echo json_encode(["success" => false, "error" => "Invalid values for numWW, numQZ, or numPT"]);
    exit;
}

// Insert new record
$insertQuery = "INSERT INTO grading_settings (SubjectID, TeacherID, NumWW, NumQZ, NumPT) VALUES (?, ?, ?, ?, ?)";
$stmt = $conn->prepare($insertQuery);
if (!$stmt) {
    echo json_encode(["success" => false, "error" => "SQL preparation failed: " . $conn->error]);
    exit;
}

$stmt->bind_param("isiii", $subjectID, $teacherID, $numWW, $numQZ, $numPT);
if (!$stmt->execute()) {
    error_log("Query execution failed: " . $stmt->error); // Log the error
    echo json_encode(["success" => false, "error" => "Query execution failed: " . $stmt->error]);
    exit;
}

$stmt->close();
$conn->close();

echo json_encode(["success" => true, "message" => "New grading settings added"]);
?>