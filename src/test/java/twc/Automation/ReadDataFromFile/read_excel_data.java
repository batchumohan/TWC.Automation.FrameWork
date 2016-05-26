package twc.Automation.ReadDataFromFile;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import twc.Automation.Driver.Drivers;;

public class read_excel_data extends Drivers{
	
public static String[][] exceldataread(String Type) throws Exception {
		
		Drivers.property();
		
		//System.out.println("File path " + properties.getProperty("excel_file_path"));
		File f_validation= new File(properties.getProperty("excel_file_path"));
		
		FileInputStream fis_validation = new FileInputStream(f_validation);
		HSSFWorkbook wb_validation = new HSSFWorkbook(fis_validation);
		HSSFSheet ws = wb_validation.getSheet(Type);

		int rownum = ws.getLastRowNum() + 1;
		int colnum = ws.getRow(0).getLastCellNum();
		String data[][] = new String[rownum][colnum];

			for (int i = 0; i < rownum; i++) {
			    HSSFRow row = ws.getRow(i);
	
			    for (int j = 0; j < colnum; j++) {
				HSSFCell cell = row.getCell(j);
				String value = cell.toString();//Cell_To_String.celltostring(cell);
				data[i][j] = value;
				//System.out.println("Values are :" + value + " : data[" + i + "][" + j + "]");
			    }
			}
		return data;
		}
}
