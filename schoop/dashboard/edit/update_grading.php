<?php
session_start();
include '../../backend/db.php';

header('Content-Type: application/json');

// Ensure the teacher is logged in
if (!isset($_SESSION['teacherID'])) {
    error_log("Unauthorized access attempt.");
    echo json_encode(["success" => false, "error" => "Unauthorized"]);
    exit;
}

$id = $_GET['id'] ?? null;
$input = file_get_contents("php://input");

// Validate JSON input
if (!$input) {
    error_log("No data received in request.");
    echo json_encode(["success" => false, "error" => "No data received"]);
    exit;
}

$data = json_decode($input, true);
if (!$data || !isset($data['numWW'], $data['numQZ'], $data['numPT'])) {
    error_log("Invalid JSON input: " . json_encode($data));
    echo json_encode(["success" => false, "error" => "Invalid input data"]);
    exit;
}

// Validate ID
if (!$id || !is_numeric($id)) {
    error_log("Invalid ID received: " . $id);
    echo json_encode(["success" => false, "error" => "Invalid ID"]);
    exit;
}

$numWW = (int) $data['numWW'];
$numQZ = (int) $data['numQZ'];
$numPT = (int) $data['numPT'];

// Debug: Check if ID exists
$checkQuery = "SELECT SettingID FROM grading_settings WHERE SettingID = ?";
$checkStmt = $conn->prepare($checkQuery);
$checkStmt->bind_param("i", $id);
$checkStmt->execute();
$result = $checkStmt->get_result();
if ($result->num_rows === 0) {
    error_log("No matching SettingID found: " . $id);
    echo json_encode(["success" => false, "error" => "No matching record found"]);
    exit;
}
$checkStmt->close();

// Fix: Use the correct column name (SettingID)
$query = "UPDATE grading_settings SET NumWW = ?, NumQZ = ?, NumPT = ? WHERE SettingID = ?";
$stmt = $conn->prepare($query);
if (!$stmt) {
    error_log("SQL preparation failed: " . $conn->error);
    echo json_encode(["success" => false, "error" => "SQL preparation failed"]);
    exit;
}

$stmt->bind_param("iiii", $numWW, $numQZ, $numPT, $id);
$success = $stmt->execute();

if (!$success) {
    error_log("Query execution failed: " . $stmt->error);
    echo json_encode(["success" => false, "error" => "Query execution failed"]);
    exit;
}

$stmt->close();
$conn->close();

echo json_encode(["success" => true, "message" => "Grading settings updated successfully"]);
?>
