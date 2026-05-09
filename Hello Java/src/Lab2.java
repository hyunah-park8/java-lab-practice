import java.util.Scanner;

public class Lab2 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scn = new Scanner(System.in);
		int deci = scn.nextInt();
		
		int tempDeci = deci;
		String bin = "";
		while (tempDeci > 0) {
			bin = (tempDeci % 2) + bin;
			tempDeci = tempDeci / 2;
		}
		
		System.out.println("b " + bin);
		
		tempDeci = deci;
		String oct = "";
		while (tempDeci > 0) {
			oct = (tempDeci % 8) + oct;
			tempDeci = tempDeci / 8;
		}
		
		System.out.println("o " + oct);
		
		// 내장함수를 활용했을 때
		String hex = Integer.toHexString(deci);
		System.out.println("h " + hex);
	}
}
