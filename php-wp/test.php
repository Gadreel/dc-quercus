<?php

error_reporting(E_ALL);
ini_set('display_errors', 'on');

chdir("./php-wp");

/** Include the bootstrap for setting up WordPress environment */
//require('wp-config.php');

if (function_exists('date_default_timezone_set'))
	date_default_timezone_set('America/Chicago');

$pdf = new PDF();

$pdf->begin_document("./something.pdf");
$pdf->begin_page(100, 100);

$pdf->set_info("Title", "testing aaa");

$pdf->show("hello there");

$pdf->end_page();
$pdf->end_document();


/*
require_once('./tcpdf_config.php');
require_once('./tcpdf/tcpdf.php');

$pdf = new TCPDF("P", PDF_UNIT, PDF_PAGE_FORMAT, false, 'cp1252', false);

dc_debug("b: " . PDF_AUTHOR);

// set document information
$pdf->SetCreator("apw");
$pdf->SetAuthor("dca");
$pdf->SetTitle('Packing List');

// remove default header/footer
$pdf->setPrintHeader(false);
$pdf->setPrintFooter(false);

// set default monospaced font
$pdf->SetDefaultMonospacedFont(PDF_FONT_MONOSPACED);

// set margins
$pdf->SetMargins(PDF_MARGIN_LEFT, PDF_MARGIN_TOP, PDF_MARGIN_RIGHT);

// set auto page breaks
$pdf->SetAutoPageBreak(FALSE, PDF_MARGIN_BOTTOM);

// set image scale factor
$pdf->setImageScale(PDF_IMAGE_SCALE_RATIO);


dc_debug("c");


$pdf->AddPage();

dc_debug("c1");

$pdf->SetFont('times', '', 16);
$pdf->SetTextColor(0);

dc_debug("c2");

// header
$pdf->StartTransform();
$pdf->SetAbsXY(8, 50);

dc_debug("c3");

$pdf->Cell(57, 8, "Kit", 1, 0, 'C', 1);
$pdf->StopTransform();


dc_debug("d");

$pdf->Output('./SchoolSummary_2021.pdf','F');



dc_debug("e");
*/


/*
$filename = "./temp-test.txt";
$fmode = 'ab+';
$data = "check out my friend: 🦝 - pretty cool!";

$f = @fopen($filename, $fmode);

fwrite($f, $data);
fclose($f);
*/

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

/*
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
*/




echo "done";

?>
