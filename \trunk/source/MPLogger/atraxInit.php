<?php
include("Alert/autoload.php");
include("aatrox/autoload.php");
use Artax\Client,
    Artax\ClientException,
    Artax\Ext\Cookies\CookieExtension,
    Artax\Ext\Cookies\FileCookieJar;
$client = new Artax\Client;
//$client->setOption('verboseSend', TRUE);
(new CookieExtension)->extend($client);
 
function getUrl($url){
    global $client;
    $request = requestHandle();
    $request->setUri($url);
    $request->setMethod('GET');
    $response = $client->request($request);
    return $response;
}
 
function getUrlSrc($url){
        global $client;
        $req = getUrl($url);
        $body = $req->getBody();
        return $body;
}
 
function requestHandle(){
    $request = new Artax\Request;
    $request->setHeader('User-Agent', 'Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0');
    return $request;
}
?>