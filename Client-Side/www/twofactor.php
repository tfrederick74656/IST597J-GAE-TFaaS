<?php
	include('session.php');
	include('dbconnect.php');
?>
<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
<?php
	$username = $_SESSION['login_user'];
	$query = mysqli_query($connection, "select secret from users where username='$username'");
	$secret = mysqli_fetch_array($query);
	if(strcmp("", $secret[0]) != 0)
	{
		print("evaluation true");
		print($secret[0]);
		$_SESSION['secret'] = $secret[0];
		header("location: validationform.php");
	}
	else
	{
		header("location: tfenroll.php");
	}
?>


</body>
</html>