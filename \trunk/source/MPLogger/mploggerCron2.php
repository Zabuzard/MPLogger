<?php
include("mploggerController.php");

set_time_limit(0);

$logging = FALSE;

if ($logging) echo "Starting mploggerCron2...<br />";
mplogger(TRUE);
if ($logging) echo "Ending mploggerCron2<br />";
?>