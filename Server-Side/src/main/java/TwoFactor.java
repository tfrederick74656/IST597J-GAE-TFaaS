package com.tfrederick74656.twofactor;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

import com.google.common.io.BaseEncoding;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TwoFactor extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws IOException {
		
		byte[] secret = null;
		long code;
		String encodedSecret = request.getParameter("secret");
		PrintWriter writer;
		
		String option = request.getParameter("option");
		
		if(option.equals("CreateSecret")) {
			secret = createSecret();
			encodedSecret = encodeSecret(secret);

			writer = response.getWriter();
//			String htmlResponse = "<html>";
//			htmlRespone += "<h2>Encoded Secret (" + encodedSecret.length() + "): " + encodedSecret + "</h2>";
//			htmlRespone += "</html>";
			String htmlResponse = encodedSecret;
			writer.println(htmlResponse);
		}
		
		if(option.equals("GenerateQR")) {
			String QRURL = createQRCode(encodedSecret);

			writer = response.getWriter();
//			String htmlResponse = "<html>";
//			htmlResponse += "<img src=\"" + QRURL + "\" />";
//			htmlResponse += "</html>";
			String htmlResponse = "<img src=\"" + QRURL + "\" />";
			writer.println(htmlResponse);
		}
		
		if(option.equals("GetCode")) {
			code = 0;
			encodedSecret = request.getParameter("secret");
			secret = BaseEncoding.base32().decode(encodedSecret);
			try {
				code = getCode(secret, getTimeIndex());
			} catch (InvalidKeyException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			writer = response.getWriter();
//			String htmlResponse = "<html>";
//			htmlResponse += "<h2>Code: " + code + "</h2>";
//			htmlResponse += "</html>";
			writer.println(code);
		}
		
		if(option.equals("ValidateCode")) {
			code = 0;
			encodedSecret = request.getParameter("secret");
			secret = BaseEncoding.base32().decode(encodedSecret);
			try {
				code = getCode(secret, getTimeIndex());
			} catch (InvalidKeyException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String requestCode = request.getParameter("code");
			
			String htmlResponse = "";
			if(Long.parseLong(requestCode) == code)	{
				htmlResponse = "TRUE";
			} else {
				htmlResponse = "FALSE";
			}
			
			writer = response.getWriter();
//			String htmlResponse = "<html>";
//			htmlResponse += "<h2>Code: " + code + "</h2>";
//			htmlResponse += "</html>";
			writer.println(htmlResponse);
		}
		
//		      resp.setContentType("text/plain");
//		      resp.getWriter().println("Hello, this is a testing servlet. \n\n");
//		      Properties p = System.getProperties();
//		      p.list(resp.getWriter());
		  }
	
	public static void main(String[] args) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
		
		Scanner scanner = new Scanner(System.in);
		int command = 0;
		byte[] secret = null;
		long code;
		String encodedSecret = null;

		do {
			System.out.println();
			System.out.println("1. Create Secret");
			System.out.println("2. Get QR Code");
			System.out.println("3. Get Code");
			System.out.println("4. Verify Code");
			System.out.println("5. Exit");
			System.out.println();
			System.out.print("Command: ");
			
			try {
				command = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.err.println("You have entered an invalid command. Please try again.");
			}
			System.out.println();
			
			switch(command) {
			case 1:
				secret = createSecret();
				encodedSecret = encodeSecret(secret);
				System.out.println("Encoded Secret (" + encodedSecret.length() + "): " + encodedSecret);
				break;
			case 2:
				String QRURL = createQRCode(encodedSecret);
				System.out.println(QRURL);
				break;
			case 3:
				code = getCode(secret, getTimeIndex());
				System.out.println(String.valueOf(code));
				break;
			case 4:
				code = getCode(secret, getTimeIndex());
				System.out.print("Enter code: ");
				if (code == Integer.parseInt(scanner.nextLine())) {
					System.out.println();
					System.out.println("VALID Code");
				} else {
					System.out.println();
					System.out.println("INVALID Code");
				}
				break;
			case 5:
				System.exit(0);
				break;
			default:
				System.out.println("You have entered an invalid command. Please try again.");
				break;
			}
			
		} while (true);
	}

	static byte[] createSecret() {
        byte[] buffer = new byte[20]; // 5 bytes to 8 characters
        new SecureRandom().nextBytes(buffer);
        
        return buffer;
	}
	
	static String encodeSecret(byte[] secret) {
		return BaseEncoding.base32().encode(secret);		
	}
	
	static String createQRCode(String secret) {
//		secret = secret.substring(0,25);
		String OTPString = "otpauth://totp/IST597J:tfrederick74656@gmail.com?secret=" + secret + "&issuer=IST597J";
		String OTPURL = null;
		try {
			OTPURL = URLEncoder.encode(OTPString, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "https://chart.googleapis.com/chart?cht=qr&chs=200x200&choe=UTF-8&chld=H|0&chl=" + OTPURL + "";
	}
	
	static boolean verifyCode() {
		return true;
	}
	
	private static long getCode(byte[] secret, long timeIndex) 
	          throws NoSuchAlgorithmException, InvalidKeyException {
	    SecretKeySpec signKey = new SecretKeySpec(secret, "HmacSHA1");
	    ByteBuffer buffer = ByteBuffer.allocate(8);
	    buffer.putLong(timeIndex);
	    byte[] timeBytes = buffer.array();
	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(signKey);
	    byte[] hash = mac.doFinal(timeBytes);
	    int offset = hash[19] & 0xf;
	    long truncatedHash = hash[offset] & 0x7f;
	    for (int i = 1; i < 4; i++) {
	        truncatedHash <<= 8;
	        truncatedHash |= hash[offset + i] & 0xff;
	    }
	    return (truncatedHash %= 1000000);
	  }
	
	public static long getTimeIndex() {
		return System.currentTimeMillis()/1000/30;
	}
}