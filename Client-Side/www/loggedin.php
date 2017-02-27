<?php
	include('session.php');
?>
<!DOCTYPE html>
<html>
<head>
<title>Login Form in PHP with Session</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="main">
<h1>Success Page</h1>
<div id="login">
<h2>You were logged in successfully!</h2>
<form action="logout.php">
<input name="submit" type="submit" value="Log Out">
</form>
</div>
</div>
</body>
</html>