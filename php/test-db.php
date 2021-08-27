<?php

echo "1: " . getcwd() . "\n";
//echo "1: " . dirname(__FILE__) . "\n";

chdir("./php");

echo "2: " . getcwd() . "\n";

require('./funcs/scommon.php');

//var_dump(ini_get('foo'));

echo "A" . getFoo() . "B\n";

echo "#" . $runnumber . "\n";

echo "-" . funTest() . "*\n";

echo "next\n";

echo "-" . funTest("aa", "bbb") . "*\n";

echo "done\n";

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
  echo "Returned rows are: " . mysqli_num_rows($result) . "\n";
  // Free result set

    echo "result\n";

  while ($row = mysqli_fetch_row($result)) {
    printf("%s - %s\n", $row[0], $row[1]);
  }

  mysqli_free_result($result);
}

?>
