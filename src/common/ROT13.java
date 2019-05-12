/* Source: https://introcs.cs.princeton.edu/java/31datatype/Rot13.java.html
*/
package common;

public class ROT13  implements BaseEncryption{
	
	
	public ROT13() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String encrypt(String line) {
		String result="";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            String g=Character.toString(c);
            result=result+g;
        //System.out.println(result);}
	}
        //System.out.println(result);
        return result;}


	@Override
	public String decrypt(String line) {
		String result="";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            String g=Character.toString(c);
            result=result+g;
        //System.out.println(result);}
	}
        //System.out.println(result);
        return result;}
}

