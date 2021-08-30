<?php

error_reporting(E_ALL);
ini_set('display_errors', 'on');

chdir("./php-wp");

/** Include the bootstrap for setting up WordPress environment */
require('wp-config.php');

if (function_exists('date_default_timezone_set'))
	date_default_timezone_set('America/Chicago');


global $wpdb;

$sql = $wpdb->prepare("SELECT * FROM ws_config WHERE id = %d", 1);

$row = $wpdb->get_row($sql);

echo "id: " . $row->id . "\n";
echo "me: " . $row->maxentries . "\n";
echo "note: " . $row->titlenotice . "\n";

$wpdb->update(
	'ws_config',
	array(
		'maxentries' => 45
	),
	array('id' => 1),
	array('%d'),
	array('%d')
);


echo "done";

?>
