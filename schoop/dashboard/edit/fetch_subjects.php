<?php
session_start();
error_reporting(E_ALL);
ini_set('display_errors', 1);

header('Content-Type: application/json');
include '../../backend/db.php';

$sectionId = intval($_GET['sectionId'] ?? 0);

if (!$sectionId) {
    echo json_encode(["error" => "Section ID is required."]);
    exit;
}

// Fetch the section's track
$trackQuery = "SELECT StrandName FROM sections WHERE SectionID = ?";
$stmt = $conn->prepare($trackQuery);
$stmt->bind_param("i", $sectionId);
$stmt->execute();
$result = $stmt->get_result();
$section = $result->fetch_assoc();
$strand = $section['StrandName'] ?? '';

// Determine the track (Academic or TVL)
$track = in_array($strand, ['HUMSS', 'GAS']) ? 'Academic' : 'TVL';

// Fetch subjects based on track or null track
$subjectsQuery = "
    SELECT SubjectID, SubjectName 
    FROM subject 
    WHERE (Track = ? OR Track IS NULL) 
    AND Type = 'Applied'
";
$stmt = $conn->prepare($subjectsQuery);
$stmt->bind_param("s", $track);
$stmt->execute();
$subjects = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);

echo json_encode(["subjects" => $subjects]);
$conn->close();