<?php
	include('session.php');
	$code = $_POST['code'];
	$secret = $_SESSION['secret'];
	$valid = file_get_contents("http://quantum-star-90200.appspot.com/TwoFactor?option=ValidateCode&secret=" . $secret . "&code=" . $code);
	$valid = trim($valid, "\t\n\r\0\x0B");
	print($valid);
	if($valid == "TRUE") {
		print("True");
		$_SESSION['valid_code'] = "TRUE";
		header("location: /loggedin.php");
	}
	else {
		print("False");
		$_SESSION['valid_code'] = "FALSE";
		header("location: /validationform.php");
	}
?>