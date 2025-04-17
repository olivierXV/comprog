<?php
session_start();
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Only allow admins (adjust this check as needed)
if (!isset($_SESSION['loggedInUser']) || $_SESSION['role'] !== 'admin') {
    header("Location: dashboard.php");
    exit;
}

include '../../backend/db.php';

$message = '';

// Handle form submission for adding/updating announcements
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id = $_POST['id'] ?? '';
    $announcement = trim($_POST['announcement'] ?? '');
    $type = $_POST['type'] ?? 'announcement';
    $start_date = trim($_POST['start_date'] ?? '');
    $end_date = trim($_POST['end_date'] ?? '');

    if (empty($announcement)) {
        $message = "Announcement text cannot be empty.";
    } else {
        if ($id) {
            // Update an existing announcement
            $sql = "UPDATE announcements 
                    SET announcement = ?, type = ?, start_date = ?, end_date = ? 
                    WHERE id = ?";
            $stmt = $conn->prepare($sql);
            $stmt->bind_param("ssssi", $announcement, $type, $start_date, $end_date, $id);
            if ($stmt->execute()) {
                $message = "Announcement updated successfully.";
            } else {
                $message = "Error updating announcement: " . $stmt->error;
            }
            $stmt->close();
        } else {
            // Insert a new announcement
            $sql = "INSERT INTO announcements (announcement, type, start_date, end_date)
                    VALUES (?, ?, ?, ?)";
            $stmt = $conn->prepare($sql);
            $stmt->bind_param("ssss", $announcement, $type, $start_date, $end_date);
            if ($stmt->execute()) {
                $message = "Announcement added successfully.";
            } else {
                $message = "Error adding announcement: " . $stmt->error;
            }
            $stmt->close();
        }
    }
}

// Handle announcement deletion
if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['delete'])) {
    $delete_id = intval($_GET['delete']);
    $sql = "DELETE FROM announcements WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $delete_id);
    if ($stmt->execute()) {
        $message = "Announcement deleted successfully.";
    } else {
        $message = "Error deleting announcement: " . $stmt->error;
    }
    $stmt->close();
}

// Fetch all announcements for display
$sql = "SELECT * FROM announcements ORDER BY updated_at DESC";
$result = $conn->query($sql);
$announcements = [];
if ($result) {
    while ($row = $result->fetch_assoc()) {
        $announcements[] = $row;
    }
}
$conn->close();
?>
<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Dashboard - Edit mode</title>
  <link rel="stylesheet" href="../../grades.css">
  <link rel="stylesheet" href="../../hover.css">
  <link rel="stylesheet" href="../../colours.css">
  <style>
    .navbar ul.admin,
    .navbar ul.teacher{
      background-image: radial-gradient(circle, #ffa600,#aa6f00) !important;
    }
    .message { text-align: center; font-weight: bold; margin-bottom: 20px; }
        form { max-width: 100%; margin: 0 auto; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input, textarea, select { min-width: 100%; padding: 5px; margin-bottom: 10px; }
        button { padding: 3px; cursor: pointer; }
        table { width: 100%; border-collapse: collapse; margin-top: 3vh; }
        table, th, td { border: 1px solid #ccc; table-layout: fixed;}
        th, td { padding: 2px; text-align: left; }
        .delete-button { color: white; background-color: red; }
  </style>
  <link rel="icon" href="../../school.png">
  <script src="../../menu.js" defer></script>
  <script src="../../backend/logout.js" defer></script>
</head>

<body class="<?php echo isset($_SESSION['role']) ? $_SESSION['role'] : (isset($_SESSION['strand']) ? $_SESSION['strand'] : ''); ?>">
  <div class="errir"><p>Screen Size is too small</p></div>
  <div class="navbar">
  <ul class="<?php echo isset($_SESSION['role']) ? $_SESSION['role'] : (isset($_SESSION['strand']) ? $_SESSION['strand'] : ''); ?>">
  <div style="display: flex;">
  <li><a href="../../about.html"><img src="../../school.png"></a></li>
      <li class="title"><a>Edit Announcements</a></li>
      </div>
      <div class="link" style="float: right;">
        <div class="dropdown">
        <button class="dropbut" href="javascript:void(0);" class="icon" onclick="menu()">≡</button>
        <div id="dropdown-content">
            <a href="../../index.html">Home</a>
            <a href="../../dashboard.php">Dashboard</a>
            <a href="../edit.php" id="teachersOnly">Grading Sheet</a>
            <a href="teachers.php" id="adminOnly">Edit Teachers</a>
            <a href="students.php" id="teachersOnly">Edit Students</a>
            <a href="#" id="logoutdrop">Log out</a>
          </div>
        </div>
        <div class="deskLink">
        <li><a style="float: right; font-size: large; color: white;" href="../../index.html" >Home</a></li>
        <li><a style="float: right; font-size: large; color: white;" href="../../dashboard.php" >Dashboard</a></li>
        <li><a style="float: right; font-size: large; color: white;" href="../edit.php" id="teachersOnly">Grading Sheet</a></li>
        <li><a style="float: right; font-size: large; color: white;" class="active" href="" id="adminOnly">Edit Announcements</a></li>
        <li><a style="float: right; font-size: large; color: white;" href="teachers.php" id="adminOnly">Edit Teachers</a></li>
        <li><a style="float: right; font-size: large; color: white;" href="students.php" id="teachersOnly">Edit Students</a></li>
        <li><a style="float: right; font-size: large; color: white;" href="#" id="logoutLink" >Log out</a></li>
        </div>
      </div>
    </ul>
  </div>
  <div class="body">
    <div class="left">
      <div class="box">
        <div style="padding:5%; margin-left: -3%;">
        <form method="POST" action="">
        <input type="hidden" name="id" id="announcementId" value="">
        
        <label for="announcement">Announcement:</label>
        <textarea name="announcement" id="announcement" required></textarea>
        
        <label for="type">Type:</label>
        <select name="type" id="type">
            <option value="announcement">Announcement</option>
            <option value="holiday">Holiday</option>
        </select>
        
        <label for="start_date">Start Date (optional):</label>
        <input type="date" name="start_date" id="start_date">
        
        <label for="end_date">End Date (optional):</label>
        <input type="date" name="end_date" id="end_date">
        
        <button type="submit">Save Announcement</button>
    </form>
        </div>
      </div>
    </div>
    <div class="right">
      <div class="box">

      <div style="padding: 1px;">
      <input type="text" id="searchBar" placeholder="Search" style="padding: 5px; width: 100%;">
      </div>

      <table id="announcetable">
        <thead>
            <tr>
                <th>ID</th>
                <th>Announcement</th>
                <th>Type</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Updated At</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <?php if (empty($announcements)): ?>
                <tr><td colspan="7">No announcements found.</td></tr>
            <?php else: ?>
                <?php foreach ($announcements as $ann): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($ann['id']); ?></td>
                        <td><?php echo nl2br(htmlspecialchars($ann['announcement'])); ?></td>
                        <td><?php echo htmlspecialchars($ann['type']); ?></td>
                        <td><?php echo htmlspecialchars($ann['start_date']); ?></td>
                        <td><?php echo htmlspecialchars($ann['end_date']); ?></td>
                        <td><?php echo htmlspecialchars($ann['updated_at']); ?></td>
                        <td style="display: flex; justify-content: center; padding: 2px;">
                            <button onclick="editAnnouncement(<?php echo $ann['id']; ?>, '<?php echo addslashes($ann['announcement']); ?>', '<?php echo $ann['type']; ?>', '<?php echo $ann['start_date']; ?>', '<?php echo $ann['end_date']; ?>')">Edit</button>
                            <a href="?delete=<?php echo $ann['id']; ?>" onclick="return confirm('Are you sure you want to delete this announcement?');">
                                <button class="delete-button">Delete</button>
                            </a>
                        </td>
                    </tr>
                <?php endforeach; ?>
            <?php endif; ?>
        </tbody>
    </table>
      </div>
    </div>
  </div>

<script>
document.addEventListener('DOMContentLoaded', () => {
    const searchBar = document.getElementById('searchBar');
    const announcetable = document.getElementById('announcetable');

    // Search functionality
    searchBar.addEventListener('input', () => {
        const filter = searchBar.value.toUpperCase();
        const rows = announcetable.getElementsByTagName('tr');

        for (let i = 1; i < rows.length; i++) { // Start from 1 to skip the header row
            const row = rows[i];
            const cells = row.getElementsByTagName('td');
            let match = false;

            // Check all columns except the last one (Actions)
            for (let j = 0; j < cells.length - 1; j++) {
                const cell = cells[j];
                if (cell) {
                    const textValue = cell.textContent || cell.innerText;
                    if (textValue.toUpperCase().indexOf(filter) > -1) {
                        match = true;
                        break;
                    }
                }
            }

            // Show/hide row based on match
            row.style.display = match ? '' : 'none';
        }
    });
});

// Populate form for editing an announcement
function editAnnouncement(id, announcement, type, startDate, endDate) {
    document.getElementById('announcementId').value = id;
    document.getElementById('announcement').value = announcement;
    document.getElementById('type').value = type;
    document.getElementById('start_date').value = startDate;
    document.getElementById('end_date').value = endDate;
    window.scrollTo(0, 0);
}
</script>
</body>
</html>
<!--olicierrv, quipp3r-->

