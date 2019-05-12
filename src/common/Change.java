package common;

public class Change implements BaseEncryption{
	
	public Change() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String encrypt(String line) {
		String First=line.substring(0, 1);
		String End=line.substring(line.length()-1);
		String Middle=line.substring(1, (line.length()-1));
		String encrypt=End+Middle+First;
		/*System.out.println(End + Middle+First);
		for (int i=0;i<encrypt.length();i++) {
			System.out.println("Anzahl:"+encrypt.charAt(i));}*/
		//System.out.println("Verschlüsselt: "+encrypt);
		return encrypt;
	}
	@Override
	public String decrypt(String line) {
		String First=line.substring(0, 1);
		String End=line.substring(line.length()-1);
		String Middle=line.substring(1, (line.length()-1));
		String encrypt=End+Middle+First;
		return encrypt;
	}
}
