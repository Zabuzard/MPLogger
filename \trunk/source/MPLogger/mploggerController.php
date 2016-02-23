<?php
include("atraxInit.php");
include("login.php");
include("logout.php");
include("routine.php");
include("sendMessage.php");

function mplogger($logout) {
	global $client;
	global $logging;
	
	if ($logging) echo "Starting mplogger...<br />";
	
	$preWorld = "welt";
	$worldAmount = 14;

	for($world = 1; $world <= $worldAmount; $world++) {
		// Handle world bans
		if ($world == 2 || $world == 4 || $world == 14) {
			continue;
		}
		
		/*
		//Single world test
		if ($world != 12) {
			continue;
		}
		*/
		
		$serverip = "$preWorld$world.freewar.de";
		$serverlocation = "/freewar/internal";
		$urlMain = "http://$serverip$serverlocation/main.php";
		if ($logging) echo "World $world: Login...<br />";
		login($client, $world);
		$main = getUrl($urlMain)->getBody();
		
		$searchmaskDummy = "Dummy";
		$searchmaskLocation = "Markthalle";
		$searchmaskPost = "Brief";
		$searchmaskMod = "Moderator";
	
		//Start routine if at correct location
		if (strpos($main, $searchmaskLocation) !== false) {
			if ($logging) echo "World $world: Routine...<br />";
			routine($client, $world);
		} else {
			//Send message if not at correct location
			if ($logging) echo "World $world: Location message...<br />";
			sendMessage($world, TRUE);
		}
		//Send message if there is post
		if (strpos($main, $searchmaskPost) !== false || strpos($main, $searchmaskMod) !== false) {
			if ($logging) echo "World $world: Post message...<br />";
			sendMessage($world, FALSE);
		}
		
		if ($logout) {
			if ($logging) echo "World $world: Logout...<br />";
			logout($client, $world);
		}
	}
	if ($logging) echo "Ending mplogger<br />";
}
?>