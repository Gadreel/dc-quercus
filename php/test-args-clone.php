<?php

echo "1: " . getcwd() . "\n";

chdir("./php");

echo "2: " . getcwd() . "\n";

require('./funcs/scommon.php');

echo "A" . getFoo() . "B\n";

echo "1 - " . $dc_args["title"] . "\n";
echo "2 - " . $dc_args["number"] . "\n";
echo "3 - " . $dc_args["p"]["title"] . "\n";

echo "done and 🙏 over \n";

return 9.95;

?>
