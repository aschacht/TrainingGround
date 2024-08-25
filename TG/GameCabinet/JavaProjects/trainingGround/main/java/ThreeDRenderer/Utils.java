package ThreeDRenderer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {

	public static String loadResource(String fileName) throws Exception {
		String result = "";

		File initialFile = new File(fileName);
		InputStream in = new FileInputStream(initialFile);
		Scanner scanner = new Scanner(in, "UTF-8");
		result = scanner.useDelimiter("\\A").next();
		scanner.close();
		return result;

	}
	
	public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        try {
        	File initialFile = new File(fileName);
    		InputStream in = new FileInputStream(initialFile);
        	System.out.println("stream: "+in);
        	InputStreamReader input = new InputStreamReader(in);
        	BufferedReader br = new BufferedReader(input);
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
        }catch (Exception e){
        	e.printStackTrace();
        }
        return list;
    }

	public static float[] listToArray(List<Float> list) {
		int size = list != null ? list.size():0;
		float[] floatArr = new float[size];
		for(int i =0;i<size;i++){
			floatArr[i]=list.get(i);
		}
		return floatArr;
		
	}

}
