<?php

error_reporting(E_ALL);
ini_set('display_errors', 'on');

chdir("./php-wp");

/** Include the bootstrap for setting up WordPress environment */
require('wp-config.php');

if (function_exists('date_default_timezone_set'))
	date_default_timezone_set('America/Chicago');


/*
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
*/

$authname = "xxxxxxxxxxxxxxxxxxxx";
$authkey = "666666666666666666666666666";
$authendpoint = "https://apitest.authorize.net/xml/v1/request.api";

dc_debug("C1: " . $authendpoint);

$reqbody = "{
		\"authenticateTestRequest\": {
			\"merchantAuthentication\": {
					\"name\": \"{$authname}\",
					\"transactionKey\": \"{$authkey}\"
			}
		}
}";

dc_debug("C2: " . $reqbody);

dc_debug("C3: " . function_exists("wp_remote_post"));

$response = wp_remote_post($authendpoint, array(
	'headers' => array(
		'Accept' => 'application/json',
		'Content-Type' => 'application/json'
	),
	'data_format' => 'body',
	'body' => $reqbody
));

$body = trim(substr($response['body'],1));

dc_debug("D: " . $body);

$resrec = json_decode($body);

var_dump($resrec);

echo $resrec->messages->message[0]->code;

echo "done";

?>
