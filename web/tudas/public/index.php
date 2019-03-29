<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

$config['displayErrorDetails'] = true;
$config['addContentLengthHeader'] = false;

// Define database connection

$config['db']['host'] = 'localhost';
$config['db']['user'] = 'tudas';
$config['db']['pass'] = 'AllesFuerDieErstis';
$config['db']['dbname'] = 'tudas';

$app = new \Slim\App(['settings' => $config]);

// Make the database connection available in the code

$container = $app->getContainer();
$container['db'] = function ($c) {
    $db = $c['settings']['db'];
    $pdo = new PDO('mysql:host='.$db['host'].';dbname='.$db['dbname'], $db['user'], $db['pass']);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
    return $pdo;
};

// Define app routes

/**
 * Returns all appointments with appointment contents at the given date and corresponding to the given labels.
 * If no labels are given, it returns all appointments at the given date.
 */
$app->get('/api/appointments/{date}[/{labels:.*}]', function (Request $request, Response $response, $args) {
    $labels = explode('/', $args['labels']);
    $date = $args['date'];
    $db = $this->db;
    $sql = "SELECT ac.id AS id,
            ac.title AS title, 
            ac.description AS description, 
            ac.abbreviation AS abbreviation, 
            ac.room AS room, 
            ac.color AS color, 
            a.start_date AS start_date, 
            a.end_date AS end_date FROM appointment_contents ac INNER JOIN appointments a ON ac.id = a.appointment_content_id INNER JOIN labels l ON ac.label_id = l.id
            WHERE (DATE(a.start_date) = '$date' OR DATE(a.end_date) = '$date' OR (DATE(a.start_date) < '$date' AND DATE(a.end_date) > '$date'))";
    if(count($labels) > 0)
        $sql .= "AND (l.abbreviation = '$labels[0]'";
    for($i = 1; $i < count($labels); $i++) {
        $sql .= " OR l.abbreviation = '$labels[$i]'";
    }
    $sql .= ")  ORDER BY a.start_date ASC, a.end_date DESC";
    $stmt = $db->query($sql);
    $from_database = $stmt->fetchAll();
    $output = [];
    foreach($from_database as $row) {
        $appointment['startDate'] = $row['start_date'];
        $appointment['endDate'] = $row['end_date'];
        $appointmentContent['title'] = $row['title'];
        $appointmentContent['description'] = $row['description'];
        $appointmentContent['abbreviation'] = $row['abbreviation'];
        $appointmentContent['room'] = $row['room'];
        $appointmentContent['color'] = "#".$row['color'];
        $appointment['appointmentContent'] = $appointmentContent;
        $output[] = $appointment;
    }
    return $response->getBody()->write(json_encode($output));
});


/**
 * Returns true if the given label exists in the database, otherwise it returns false.
 */
$app->get('/api/labelExists/{label}', function(Request $request, Response $response, $args) {
    $db = $this->db;
    $label = $args['label'];
    $sql = "SELECT * FROM labels WHERE abbreviation = '$label'";
    $stmt = $db->query($sql);
    $output = count($stmt->fetchAll()) == 0 ? array("exists" => false) : array("exists" => true);
    return $response->getBody()->write(json_encode($output));
});

// Run app
$app->run();