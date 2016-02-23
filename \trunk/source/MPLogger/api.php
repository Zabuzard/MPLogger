<?php
include("connectdb.php");

function request($world, $ts) {
	global $linkLogs;
	$separator = ";";
	
	$tsAsDate = date("Y-m-d H:i:s", mysql_real_escape_string($ts));
	
	$query = "SELECT `item`, `ts` FROM `mploggerwelt" . mysql_real_escape_string($world) . "` WHERE `ts` > '$tsAsDate' ORDER BY `ts` ASC";
	$res = mysql_query($query, $linkLogs);
	
	while ($row = mysql_fetch_array($res, MYSQL_NUM)) {
		echo $world . $separator . $row[0] . $separator . strtotime($row[1]) . "\n";
	}
}

function isValidTimeStamp($timestamp) {
	return ((string) (int) $timestamp === $timestamp) 
		&& ($timestamp <= PHP_INT_MAX)
		&& ($timestamp >= ~PHP_INT_MAX);
}

$amountOfWorlds = 14;

if (isset($_GET['w']) && strlen($_GET['w']) > 0 && isset($_GET['ts']) && strlen($_GET['ts']) > 0) {
	$world = $_GET['w'];
	$ts = $_GET['ts'];
	if (is_numeric($world) && $world >= 1 && $world <= $amountOfWorlds && isValidTimeStamp($ts)) {
		request($world, $ts);
	}
}
?>