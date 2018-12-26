import java.io.File;
import java.io.IOException;

public class XMLTesting {
	public static File file = new File("res/Test");
	
	public static void main(String[] args) {
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		write();
	}
	
	public static void read() {
		
	}

	public static void write() {
		
	}
}
