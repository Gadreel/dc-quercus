<?php

echo "#" . $runnumber . "\n";

mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

$db = mysqli_connect("localhost", "writes", "GbKbJBuvTuj7", "write_test");

if (!$db) {
    die('bad connection: ' . mysql_error());
}

$result = mysqli_query($db, 'SELECT * FROM ws_config WHERE id=1');

if (!$result) {
    die('Invalid query: ' . mysql_error());
}
else {
    $row = mysqli_fetch_row($result);

  if ($row) {
    printf("%s - %s\n", $row[0], $row[1]);
  }

  mysqli_free_result($result);
}

?>
