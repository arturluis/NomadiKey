package lockscreen.u2d.controller;

public class PasswordGenerator {
	
	public static String generatePassword(int length, boolean withStroke, int num){
		
		StringBuffer result = new StringBuffer();
		int key;
		int stroke;
		String strokeStr = "";
		
		for(int i = 0; i < length; ++i){
			
			key = (int) (Math.random() * 10);
			result.append(key);

			
			if(withStroke){
				stroke = (int) (Math.random() * num);
				switch(stroke){
				case 0:
					strokeStr = "↑";
					break;
				case 1:
					strokeStr = "→";
					break;
				case 2:
					strokeStr = "↓";
					break;
				case 3:
					strokeStr = "←";
					break;
				case 4:
					strokeStr = "↗";
					break;
				case 5:
					strokeStr = "↙";
					break;
				case 6:
					strokeStr = "↘";
					break;
				case 7:
					strokeStr = "↖";
					break;
				case 8:
					strokeStr = "◦";
				}
				result.append(strokeStr);
			}
			
			result.append(" ");
		}
		
		return result.toString();
		
	}

}
