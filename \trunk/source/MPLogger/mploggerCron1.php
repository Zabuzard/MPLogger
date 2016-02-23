<?php
include("mploggerController.php");

set_time_limit(0);

$logging = FALSE;

if ($logging) echo "Starting mploggerCron1...<br />";
mplogger(FALSE);
if ($logging) echo "Ending mploggerCron1<br />";
?>