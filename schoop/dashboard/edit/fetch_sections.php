<?php
session_start();
error_reporting(E_ALL);
ini_set('display_errors', 1);

header('Content-Type: application/json');
include '../../backend/db.php';

// Fetch all sections
$query = "SELECT SectionID, SectionName FROM sections";
$result = $conn->query($query);

if (!$result) {
    die(json_encode(["error" => "Database query failed: " . $conn->error]));
}

$sections = [];
while ($row = $result->fetch_assoc()) {
    $sections[] = $row;
}

echo json_encode(["sections" => $sections]);
$conn->close();