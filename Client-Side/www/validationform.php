<?php 
	include('session.php');
?>
<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
<div id="main">
	<h1>Two Factor Validation</h1>
<div id="login">
	<h2>Please enter your code</h2>
	<form action="/tfvalidate.php" method="post">
		<label>Code :</label>
		<input id="name" name="code" placeholder="code" type="text">
		<input name="submit" type="submit" value="validate">
	</form>
<?php
	if($_SESSION['valid_code'] == "FALSE")
	{
		print("<span>Invalid code, please try again.</span>");
	}
?>
</div>
</div>
</body>
</html>