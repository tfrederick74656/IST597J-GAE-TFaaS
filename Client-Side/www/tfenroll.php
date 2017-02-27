<?php
	include('dbconnect.php');
	include('session.php');
?>
<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>

<div id="main">
<h1>Two Factor Enrollment</h1>
<div id="login">
<h2></h2>
<?php
	$generatedsecret = file_get_contents("http://quantum-star-90200.appspot.com/TwoFactor?option=CreateSecret");
	$secret = trim($generatedsecret, "\t\n\r\0\x0B");
	$username = $_SESSION['login_user'];
	$_SESSION['secret'] = $secret;
	$qrcode = file_get_contents("http://quantum-star-90200.appspot.com/TwoFactor?option=GenerateQR&secret=" . $secret);
	print($secret);
	print($qrcode);
	$query = mysqli_query($connection, "UPDATE users SET secret='$secret' WHERE username='$username'");
?>
<form>
	<input type="button" onClick="history.go(0)" value="Refresh">
</form>
<form action="/validationform.php">
    <input type="submit" value="Enroll">
</form>
</div>
</div>
</body>
</html>