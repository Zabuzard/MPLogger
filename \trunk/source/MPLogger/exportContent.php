<?php
include('connectdb.php');

function printContent($world, $lastPrintDate) {
	global $linkLogs;
	global $logging;
	echo '>Entries since: '.$lastPrintDate."\n";
	
	$query = "SELECT * FROM `mploggerwelt" . $world . "` WHERE `ts` > '". $lastPrintDate . "'";
	$result = mysql_query($query, $linkLogs);
	
	echo ">Errors:\n";
	echo $result.mysql_error()."\n";
	
	echo ">Entries:\n";
	while ($row = mysql_fetch_assoc($result)) {
		echo $row["user"].';';
		echo $row["item"].';';
		echo $row["price"].';';
		echo $row["ts"]."\n";
	}
	mysql_free_result($result);
}

set_time_limit(0);

$world = trim($_GET['world']);
$d = trim($_GET['d']);
$m = trim($_GET['m']);
$y = trim($_GET['y']);

if (strlen($world) <= 0 || strlen($d) <= 0
	|| strlen($m) <= 0 || strlen($y) <= 0) {
	return;
}
$world = intval($world);
$d = intval($d);
$m = intval($m);
$y = intval($y);

if ($world < 1 || $world > 14 || $d < 1 || $d > 31 || $m < 1 || $m > 12 || $y < 1970 || $y > 3000) {
	return;
}

$lastPrintDate = date('Y-m-d H:i:s', mktime(0, 0, 0, $m, $d, $y));
printContent($world, $lastPrintDate);
?>