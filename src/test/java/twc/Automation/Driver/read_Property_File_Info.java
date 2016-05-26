package twc.Automation.Driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class read_Property_File_Info {
	
	protected static FileInputStream fileInput;
	protected static Properties properties = new Properties();

	public static void property() throws IOException
	
	{
		String dataFilePath="/Users/monocept/Documents/workspace_luna/TWC.Automation.FrameWork/Project.Properties";
		
		File file = new File(dataFilePath);
		try {
			fileInput = new FileInputStream(file);
			properties.load(fileInput);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		

	}

}
