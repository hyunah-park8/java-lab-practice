import java.io.*;
import java.util.ArrayList;

public class Reverse {
    public static void main(String[] args) {
        String inputFilePath = "input.txt";
        String outputFilePath = "output.txt";

        ArrayList<String> lines = new ArrayList<>();

        try {
        	
            BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath));

            String data;
            
            while ((data = br.readLine()) != null) {
                lines.add(data);
            }

            for (int i = lines.size() - 1; i >= 0; i--) {
                bw.write(lines.get(i));
                bw.newLine();
            }

            br.close();
            bw.close();

            System.out.println("파일 변환 및 저장 완료!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}