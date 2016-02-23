<?php
function sendMessage($world, $option) {
	//If true bot does not stand at correct location, if false bot has a message
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