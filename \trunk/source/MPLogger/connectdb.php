<?php
function mysql_login($server, $user, $pass, $db){
$link=mysql_connect("$server", "$user","$pass") or die ("Keine Verbindung moeglich");
  mysql_select_db("$db") or die ("Die Datenbank existiert nicht.");
  return $link;
}

$server = "localhost";
$user = "Databaseuser";
$pass = "password";
$db = "Databasename";
$linkLogs=mysql_login($server, $user, $pass, $db);
?>