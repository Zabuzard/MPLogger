<?php
function login($client, $world) {
	$name ="Freewaruser";
	$password = "password";
	$preWorld = "welt";
	$wordLogin = "Einloggen";
	
	$serverip = "$preWorld$world.freewar.de";
	$serverlocation = "/freewar/internal";
	$urlIndex = "http://$serverip$serverlocation/index.php";
	$urlFrset = "http://$serverip$serverlocation/frset.php";
	
	$body = new Artax\FormBody;
	$body->addField('name', "$name");
	$body->addField('password', "$password");
	$body->addField('submit', "$wordLogin");
	
	$request = requestHandle();
	$request->setBody($body);
	$request->setUri($urlIndex);
	$request->setMethod('POST');
	$response = $client->request($request);
	
	$frset = getUrl($urlFrset);
}
?>