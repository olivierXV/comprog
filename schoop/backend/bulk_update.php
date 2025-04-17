<?php
session_start();
error_reporting(E_ALL);
ini_set('display_errors', 1);
header('Content-Type: application/json');
include 'db.php';

// Capture incoming JSON data
$data = json_decode(file_get_contents('php://input'), true);

if (!$data || !isset($data['changes']) || !is_array($data['changes'])) {
    echo json_encode(["error" => "Invalid input."]);
    exit;
}

$changes = $data['changes'];
$successCount = 0;

// Get TeacherID from session, as string (if available)
$teacherID = isset($_SESSION['teacherID']) ? strval($_SESSION['teacherID']) : '';

foreach ($changes as $change) {
    // Required fields: lrn, subjectID, quarter, schoolYear, grade
    $required = ['lrn', 'subjectID', 'quarter', 'schoolYear', 'grade'];
    foreach ($required as $field) {
        if (!isset($change[$field]) || $change[$field] === '') {
            echo json_encode(["error" => "Missing required field: $field"]);
            exit;
        }
    }
    
    $lrn = $change['lrn'];
    $subjectID = intval($change['subjectID']);
    $quarter = $change['quarter'];
    $schoolYear = $change['schoolYear'];
    $grade = strval($change['grade']);
    
    $allowedFields = [
        'ww1', 'ww2', 'ww3', 'ww4', 'ww5', 'ww6', 'ww7', 'ww8', 'ww9', 'ww10',
        'qz1', 'qz2', 'qz3', 'qz4', 'qz5', 'qz6', 'qz7', 'qz8', 'qz9', 'qz10',
        'pt1', 'pt2', 'pt3', 'pt4', 'qa'
    ];
        
    // Initialize arrays for fields and values
    $fields = ['LRN', 'TeacherID', 'SubjectID', 'quarter', 'SchoolYear', 'Grade'];
    $values = [$lrn, $teacherID, $subjectID, $quarter, $schoolYear, $grade];
    $types = 'ssisss'; // Types for required fields: s (string), i (integer)

    // Add allowed fields that are provided in the change
    $updateFields = [];
    foreach ($allowedFields as $field) {
        if (isset($change[$field])) {
            $fields[] = $field;
            $values[] = intval($change[$field]);
            $types .= 'i'; // Allowed fields are integers
            $updateFields[] = "$field = VALUES($field)";
        }
    }
    
    // Build the query
    $query = "
        INSERT INTO grades (" . implode(', ', $fields) . ")
        VALUES (" . implode(', ', array_fill(0, count($fields), '?')) . ")
        ON DUPLICATE KEY UPDATE " . implode(', ', $updateFields) . "
    ";
    
    $stmt = $conn->prepare($query);
    if (!$stmt) {
        echo json_encode(["error" => "Prepare failed: " . $conn->error]);
        exit;
    }
    
    // Bind parameters using the spread operator
    $stmt->bind_param($types, ...$values);
    
    if ($stmt->execute()) {
        $successCount++;
    } else {
        echo json_encode(["error" => "Execution failed: " . $stmt->error]);
        exit;
    }
    
    $stmt->close();
}

$conn->close();
echo json_encode(["success" => "$successCount records updated."]);
?>