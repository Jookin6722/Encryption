import java.util.*;
import java.io.*;
import java.io.IOException;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

class Encryption {

	public static void clear() {  
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
	} 

	public static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		return key;
		
	}

	public static IvParameterSpec generateIv(){
		
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
		
	}

	public static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] encryptText = cipher.doFinal(input.getBytes());
		return Base64.getEncoder().encodeToString(encryptText);
		
	}

	public static String decrypt(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.DECRYPT_MODE, key, iv);
    byte[] plainText = cipher.doFinal(Base64.getDecoder()
        .decode(cipherText));
    return new String(plainText);
}

	public static String XORcrypt(String text, String key){
		StringBuilder output = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {
      output.append((char) (text.charAt(i) ^ key.charAt(i % key.length())));
    }
    return output.toString();
	}
	
  public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
    BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeySpecException {
		
		Scanner ssc = new Scanner(System.in);
		Scanner isc = new Scanner(System.in);
		
		System.out.println("Welcome to Encrypto");

		String plaintext = "";
		String pass = "";
		String salt = "Encryption is great";
		String alg = "AES/CBC/PKCS5Padding";
		IvParameterSpec ivparamspec = generateIv();
		
		while(true){
			//Creates a key using the user entered password
			SecretKey key = getKeyFromPassword(pass, salt);

			//Menu options
			System.out.println("What would you like to do?\n1. Choose String to Encrypt\n2. Choose a password\n3. XOR Encrypt\n4. CBC Encrypt\n5. Exit Program");
			int choice = isc.nextInt();

			//Asks user for string to encrypt
			if(choice == 1){
				System.out.println("Enter your string.");
				plaintext = ssc.nextLine();
				clear();
			}
			
			//Asks user for a text password
			if(choice == 2){
				System.out.println("Enter a password.");
				pass = ssc.nextLine();
				clear();
			}

			//Encrypts the text using XOR
			if(choice == 3){
				clear();
				System.out.println("Unencrypted String:\n" + plaintext + "\n\nEncrypted String:");
				String encryptText = XORcrypt(plaintext, pass);
				System.out.println(encryptText + "\n");

				//Writes the password to a file
				try {
					File obj = new File("Password.txt");
					FileWriter writer = new FileWriter("Password.txt", false);
					writer.write("Password: " + pass);
					writer.close();
				} catch (IOException e){
					System.out.println("Error occurred.");
				}

				//Writes the encrypted text to a file
				try {
					File obj = new File("Encrypted.txt");
					FileWriter writer = new FileWriter("Encrypted.txt", false);
					writer.write("Encrypted Text: " + encryptText);
					writer.close();
				} catch (IOException e){
					System.out.println("Error occurred");
				}				

				//Asks the user if they would like to decrypt the text and puts the decrypted text into a file
				System.out.println("Would you like to decrypt the text?\n1. Yes   2. No");
				if(isc.nextInt() == 1){
					String decryptText = XORcrypt(encryptText, pass);
					System.out.println(decryptText + "\n");
					try {
						File obj = new File("Decrypted.txt");
						FileWriter writer = new FileWriter("Decrypted.txt", false);
						writer.write("Decrypted Text: " + decryptText);
						writer.close();
					} catch (IOException e){
						System.out.println("Error occurred");
					}
				}
				
			}
			
			//Encrypts the text using CBC
			if(choice == 4){
				clear();
				System.out.println("Unencrypted String:\n" + plaintext + "\n\nEncrypted String:");
				String encryptText = encrypt(alg, plaintext, key, ivparamspec);
				System.out.println(encryptText + "\n");

				//Writes the password to a file
				try {
					File obj = new File("Password.txt");
					FileWriter writer = new FileWriter("Password.txt", false);
					writer.write("Password: " + pass);
					writer.close();
				} catch (IOException e){
					System.out.println("Error occurred.");
				}

				//Writes the encrypted text to a file
				try {
					File obj = new File("Encrypted.txt");
					FileWriter writer = new FileWriter("Encrypted.txt", false);
					writer.write("Encrypted Text: " + encryptText);
					writer.close();
				} catch (IOException e){
					System.out.println("Error occurred");
				}

				//Asks the user if they would like to decrypt the text and puts the decrypted text into a file
				System.out.println("Would you like to decrypt the text?\n1. Yes   2. No");
				if(isc.nextInt() == 1){
					String decryptText = decrypt(alg, encryptText, key, ivparamspec);
					try {
						File obj = new File("Decrypted.txt");
						FileWriter writer = new FileWriter("Decrypted.txt", false);
						writer.write("Decrypted Text: " + decryptText);
						writer.close();
					} catch (IOException e){
						System.out.println("Error occurred");
					}
				}
			}
			
			if(choice == 5){
				break;
			}
		}
  }
}