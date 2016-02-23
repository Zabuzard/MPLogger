<?php
include("connectdb.php");

function save($user, $item, $price, $world) {
    global $linkLogs;
	global $logging;
	$query = "SELECT `ts` FROM `mploggerwelt" . $world . "` WHERE `item` = '$item' AND `ts` > DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH)";
	$res = mysql_query($query, $linkLogs);
	echo $res.mysql_error()."s<br />";
	$out = mysql_fetch_assoc($res);
	if ($out == "") {
		$query = "INSERT INTO `zabuza`.`mploggerwelt" . $world . "` (`user`, `item`, `price`, `ts`) VALUES ('$user', '$item', '$price', CURRENT_TIMESTAMP);";
		echo mysql_query($query, $linkLogs).mysql_error()."i <br />";
		if ($logging) echo "World $world: INSERTED ('$user', '$item', '$price) <br />";
	}
}

function routine($client, $world){
	$buffer = array();
	
	$preWorld = "welt";
	$serverip = "$preWorld$world.freewar.de";
	$serverlocation = "/freewar/internal";
	$urlChat = "http://$serverip$serverlocation/chattext.php";
	$content = getUrl($urlChat)->getBody();
	
	$exploded = explode("</p>",$content);
	foreach($exploded as $line) {
		//<p class="chattext"><i><b>Exorzist</b> kauft 53x das Item <b>Ölfass</b> für jeweils <b>244</b> Goldmünzen</b></i></p>
		if(preg_match("/<p class=\"chattext\">.*?<b>(.*?)<\/b> kauft.*?das Item <b>(.*?)<\/b> f.*?<b>(\d+)</", $line, $found)) {
			array_shift($found);
			if (!in_array($found, $buffer)) {
				array_push($buffer, $found);
			}
		}
	}
	foreach ($buffer as $entry) {
		save($entry[0], $entry[1], $entry[2], $world);
	}
}
?>