<?php
function logout($client, $world) {
	$preWorld = "welt";
	$serverip = "$preWorld$world.freewar.de";
	$serverlocation = "/freewar/internal";
	$urlLogout = "http://$serverip$serverlocation/logout.php";
	$content = getUrl($urlLogout);
}
?>