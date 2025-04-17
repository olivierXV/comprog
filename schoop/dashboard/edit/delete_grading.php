<?php
session_start();
include '../../backend/db.php';

if (!isset($_SESSION['teacherID'])) {
    echo json_encode(["success" => false, "error" => "Unauthorized"]);
    exit;
}

$id = $_GET['id'];

$query = "DELETE FROM grading_settings WHERE id = ?";
$stmt = $conn->prepare($query);
$stmt->bind_param("i", $id);
$success = $stmt->execute();

$stmt->close();
$conn->close();

echo json_encode(["success" => $success]);
?>
