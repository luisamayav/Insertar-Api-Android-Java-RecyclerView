<?php
header("Content-Type: application/json");

$host = "localhost";
$user = "root";
$pass = "usbw";
$db   = "test";

$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    echo json_encode(["error" => "Conexión fallida"]);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = $_POST['name'] ?? '';
    $email = $_POST['email'] ?? '';

    if (!empty($name) && !empty($email)) {
        $stmt = $conn->prepare("INSERT INTO students (name, email) VALUES (?, ?)");
        $stmt->bind_param("ss", $name, $email);
        $stmt->execute();
        $stmt->close();
    }

    $result = $conn->query("SELECT id, name, email FROM students ORDER BY id DESC");

    $datos = [];
    while ($fila = $result->fetch_assoc()) {
        $datos[] = $fila;
    }

    echo json_encode($datos);
} else {
    echo json_encode(["error" => "Método no permitido"]);
}
$conn->close();
?>
