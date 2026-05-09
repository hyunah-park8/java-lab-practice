

public class Lab1 {
		// Returns the string created by adding 's2' after positioning 'index' of 's1'.
		static String addString(String s1, int index, String s2) {
			s1 = s1.substring(0, index + 1) + s2 + s1.substring(index + 1, s1.length());
			return s1;
		}
		
		// Returns reversed string of 's'
		static String reverse(String s) {
			int i;
			String s3 = new String ("");
			for (i = s.length() - 1; i >= 0; i--) {
			s3 = s3 + s.charAt(i);
			} 
			return s3;
		}
		
		// Returns a string with all 's2's removed from 's1'
		static String removeString (String s1, String s2) {
			return s1.replace(s2, "");
		}
		
		
		public static void main(String[] args) {
			System.out.println(addString("0123456", 3, "-"));
			System.out.println(reverse("abc"));
			System.out.println(removeString("01001000", "00"));
		}
	}
