<?php

echo "1: " . getcwd() . "\n";
//echo "1: " . dirname(__FILE__) . "\n";

chdir("./temp");

echo "2: " . getcwd() . "\n";

require('./funcs/scommon.php');

//var_dump(ini_get('foo'));

echo "A" . getFoo() . "B";

mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

$db = mysqli_connect("localhost", "writes", "GbKbJBuvTuj7", "write_test");

echo "s" . $db . "t";

if (!$db) {
    die('bad connection: ' . mysql_error());
}

$result = mysqli_query($db, 'SELECT * FROM ws_config WHERE id=1');

if (!$result) {
    die('Invalid query: ' . mysql_error());
}
else {
  echo "Returned rows are: " . mysqli_num_rows($result);
  // Free result set

  while ($row = mysqli_fetch_row($result)) {
    printf("%s - %s\n", $row[0], $row[1]);
  }

  mysqli_free_result($result);
}

?>