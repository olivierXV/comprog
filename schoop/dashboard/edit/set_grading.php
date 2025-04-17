<?php
session_start();
include '../../backend/db.php';

// Ensure only teachers can access this page
if (!isset($_SESSION['teacherID'])) {
    header("Location: ../login.php");
    exit;
}

$teacherID = $_SESSION['teacherID'];

// Fetch the subjects assigned to this teacher
$query = "
    SELECT ts.SubjectID, s.SubjectName
    FROM teacher_subjects ts
    JOIN subject s ON ts.SubjectID = s.SubjectID
    WHERE ts.TeacherID = ?
    ORDER BY s.SubjectName;
";

$stmt = $conn->prepare($query);
$stmt->bind_param("s", $teacherID);
$stmt->execute();
$result = $stmt->get_result();

$subjects = [];
while ($row = $result->fetch_assoc()) {
    $subjects[] = $row;
}

$stmt->close();
$conn->close();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Set Grading Parameters</title>
    <link rel="stylesheet" href="../styles.css"> 
    <script defer src="set_grading.js"></script>
</head>
<body>
    <h2>Set Grading Parameters</h2>

    <form id="gradingForm">
        <label for="subject">Select Subject:</label>
        <select id="subject" name="subject" required>
            <option value="">Select a Subject</option>
            <?php foreach ($subjects as $subject): ?>
                <option value="<?php echo $subject['SubjectID']; ?>">
                    <?php echo htmlspecialchars($subject['SubjectName']); ?>
                </option>
            <?php endforeach; ?>
        </select>

        <label for="numWW">Number of Written Works:</label>
        <input type="number" id="numWW" name="numWW" min="1" max="10" required>

        <label for="numQZ">Number of Quizzes:</label>
        <input type="number" id="numQZ" name="numQZ" min="1" max="10" required>

        <label for="numPT">Number of Performance Tasks:</label>
        <input type="number" id="numPT" name="numPT" min="1" max="10" required>

        <button type="submit">Save Settings</button>
    </form>

    <p id="message"></p>

</body>
</html>
