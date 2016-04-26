<?php
function sendMessage($world, $option) {
	global $linkLogs;
	
	//If true bot does not stand at correct location, if false bot has a message
	if ($option) {
		// Only send those messages if not already done today
		$query = "SELECT `ts` FROM `messages` WHERE `world` = '$world' AND `ts` > DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY)";
		$res = mysql_query($query, $linkLogs);
		echo $res.mysql_error()."s<br />";
		$out = mysql_fetch_assoc($res);
		if ($out == "") {
			// Decide to send the message
			$query = "INSERT INTO `zabuza`.`messages` (`world`, `ts`) VALUES ('$world', CURRENT_TIMESTAMP);";
			echo mysql_query($query, $linkLogs).mysql_error()."i <br />";
		} else {
			// There was already a message send today, abort.
			return;
		}
	}
	$message = "";
	if ($option) {
		$message = "Freewar - Marktpreisbot\nBot aus Welt $world steht nicht auf der Markthalle.";
	} else {
		$message = "Freewar - Marktpreisbot\nBot aus Welt $world hat eine Nachricht bekommen.";
	}
	$message = wordwrap($message, 70);
	
	$subject = "";
	if ($option) {
		$subject = 'Freewar - Marktpreisbot - Ort';
	} else {
		$subject = 'Freewar - Marktpreisbot - Post';
	}
	mail('exampleadress@gmail.de', $subject, $message);
}
?>
