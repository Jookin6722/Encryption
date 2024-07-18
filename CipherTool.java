import java.util.*;
import java.util.concurrent.*;
import java.io.*;

class CipherTool {

	//Clear console method
	public static void clear() {  
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
	} 

	//Prints string in a cool way
	public static void slowPrint(String output) {
    for (int i = 0; i<output.length(); i++) {
      char c = output.charAt(i);
      System.out.print(c);
      try {
        TimeUnit.MILLISECONDS.sleep(30);
      }
      catch (Exception e) {
      }
    }
  }

	//Caesar cipher method
	public static String caesar(String text, int shift){
		StringBuilder ciptext = new StringBuilder();
		char[] letters = text.toCharArray();
		for(char c: letters){
			if((int)c == 32){
				ciptext.append(" ");
			}
			if((int)c > 96 && (int)c < 123){
				int distance = c - 'a';
				int newpos = (distance + shift) % 26;
				ciptext.append((char)('a' + newpos));
			}
			if((int)c > 64 && (int)c < 91){
				int distance = c - 'A';
				int newpos = (distance + shift) % 26;
				ciptext.append((char)('A' + newpos));
			}
		}
		return ciptext.toString();
	}

	//Caesar cipher decode method
	public static String dcaesar(String text, int shift){
		return caesar(text, 26 - (shift % 26));
	}

	//Method aiding the vigenerre cipher
	static String LowerToUpper(String s){
		StringBuffer str =new StringBuffer(s);
		
		for(int i = 0; i < s.length(); i++){
			if(Character.isLowerCase(s.charAt(i))){
				str.setCharAt(i, Character.toUpperCase(s.charAt(i)));
			}
		}
		s = str.toString();
		return s;
	}

	//Vigenerre cipher method
	public static String vig(String text, String key){
		text = text.replaceAll("[^a-zA-Z0-9]", "");
		key = key.replaceAll("[^a-zA-Z0-9]", "");
		text = LowerToUpper(text);
		key = LowerToUpper(key);
		
		if(key.length() < text.length()){
			for(int i = 0; ; i++){
				if(key.length() == text.length())
					break;
				key += key.charAt(i);
			}
		}
		if(key.length() > text.length()){
			key = key.substring(0, text.length());
		}

		StringBuilder output = new StringBuilder();

		for(int i = 0; i < text.length(); i++){
			int x = (text.charAt(i) + key.charAt(i)) % 26;

			x += 'A';
			output.append((char)x);
		}
		return output.toString();
	}

	//Vigenerre cipher decode method
	public static String dvig(String text, String key){
		text = text.replaceAll("[^a-zA-Z0-9]", "");
		key = key.replaceAll("[^a-zA-Z0-9]", "");
		text = LowerToUpper(text);
		key = LowerToUpper(key);

		if(key.length() < text.length()){
			for(int i = 0; ; i++){
				if(key.length() == text.length())
					break;
				key += key.charAt(i);
			}
		}
		if(key.length() > text.length()){
			key = key.substring(0, text.length());
		}
		
		StringBuilder output = new StringBuilder();
		
		for (int i = 0 ; i < text.length(); i++){
    	int x = (text.charAt(i) - key.charAt(i) + 26) % 26;
      x += 'A';
      output.append((char)x);
    }
    return output.toString();
	}

	//Alphanumeric cipher method
	public static String alphanum(String text){
		char[] letters = text.toCharArray();
		StringBuilder output = new StringBuilder();
		for(char c: letters){
			if((int)c > 96 && (int)c < 123){
				output.append(((int)c - 96) + " ");
			}
			if((int)c > 64 && (int)c < 91){
				output.append(((int)c - 64) + " ");
			}
		}
		return output.toString();
	}

	//Alphanumeric decoder method
	public static String dalphanum(String text){
		StringBuilder output = new StringBuilder();
		String[] split = text.split(" ");
		int[] num = new int[split.length];
		for(int i = 0; i < num.length; i++){
			num[i] = Integer.parseInt(split[i]);
		}
		for(int i = 0; i < num.length; i++){
			output.append((char)(num[i] + 96));
		}
		return output.toString();
	}

	//Binary cipher method 
	public static String binary(String text){
		char[] letters = text.toCharArray();
		int[] ctoi = new int[letters.length];
		for(int i = 0; i < letters.length; i++){
			ctoi[i] = (int)letters[i];
		}
		StringBuilder output = new StringBuilder();
		for(int i: ctoi){
			output.append(Integer.toBinaryString(i) + " ");
		}
		return output.toString();
	}

	//Binary decoder method
	public static String dbinary(String text){
		StringBuilder output = new StringBuilder();
		String[] split = text.split(" ");
		for(int i = 0; i < split.length; i++){
			output.append((char)Integer.parseInt(split[i], 2));
		}
		return output.toString();
	}
	
  public static void main(String[] args) {

		Scanner ssc = new Scanner(System.in);
		Scanner isc = new Scanner(System.in);

		slowPrint("Welcome to the Cipher Encoder/Decoder!\n");

		//Menu system
		while(true){
			slowPrint("How would you like to cipher?\n");
			System.out.println("(1) Caesar Cipher\n(2) Vigenerre Cipher\n(3) AlphaNumeric Cipher\n(4) Binary Cipher\n(5) Exit Program\n");
			
			switch(isc.nextInt()){
				case 1:
					clear();
					slowPrint("\033[1mEnter text to encode/decode: \n\033[0m");
					String textc = ssc.nextLine();
					slowPrint("\033[1mEnter the shift: \n\033[0m");
					int shift = isc.nextInt();

					clear();
					System.out.println("\033[1mNote the program encodes and decodes at the same time regardless if it's cleartext or ciphertext\033[0m\n");
					System.out.println("Inputted Text: " + textc);
					System.out.println("Encoded Text: " + caesar(textc, shift));
					System.out.println("Decoded Text: " + dcaesar(textc, shift));
					System.out.println("\n");

					break;
					
				case 2:
					clear();
					slowPrint("\033[1mEnter text to encode/decode: \n\033[0m");
					String textv = ssc.nextLine();
					slowPrint("\033[1mEnter the key: \n\033[0m");
					String key = ssc.nextLine();
					
					clear();
					System.out.println("\033[1mNote the program encodes and decodes at the same time regardless if it's cleartext or ciphertext\033[0m\n");
					System.out.println("Inputted Text: " + textv);
					System.out.println("Encoded Text: " + vig(textv, key));
					System.out.println("Decoded Text: " + dvig(textv, key));
					System.out.println("\n");

					break;

				case 3:
					clear();
					slowPrint("\033[1mEnter text to encode/decode: \n\033[0m");
					String texta = ssc.nextLine();

					clear();
					System.out.println("\033[1mNote the program encodes and decodes at the same time regardless if it's cleartext or ciphertext\033[0m\n");
					System.out.println("Inputted Text: " + texta);
					try{
						System.out.println("Encoded Text: " + alphanum(texta));
					}catch(Exception e){
						System.out.println("Encoded Text: \033[1mCan't encode an integer string.\033[0m");
					}
					try{
						System.out.println("Decoded Text: " + dalphanum(texta));
					}catch(Exception e){
						System.out.println("Decoded Text: \033[1mCan't decode non-integer string.\033[0m");
					}
					System.out.println("\n");

					break;

				case 4:
					clear();
					slowPrint("\033[1mEnter text to encode/decode: \n\033[0m");
					String textb = ssc.nextLine();

					clear();
					System.out.println("\033[1mNote the program encodes and decodes at the same time regardless if it's cleartext or ciphertext\033[0m\n");
					System.out.println("Inputted Text: " + textb);
					try{
						System.out.println("Encoded Text: " + binary(textb));
					}catch(Exception e){
						System.out.println("Encoded Text: \033[1mCan't encode binary string.\033[0m");
					}
					try{
						System.out.println("Decoded Text: " + dbinary(textb));
					}catch(Exception e){
						System.out.println("Decoded Text: \033[1mCan't decode non-binary string.\033[0m");
					}
					System.out.println("\n");

					break;

				case 5:
					for(int i = 0; i < 3; i++){
						clear();
						slowPrint("Logging off please wait...");
					}
					System.exit(0);

					break;
			}
		}
  }
}