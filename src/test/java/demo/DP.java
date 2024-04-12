package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;


public class DP {
    
    @DataProvider(name = "search")
    public static Object[] getData() throws IOException{
        File filename = new File("src/test/resources/search.xlsx");
        FileInputStream file = new FileInputStream(filename); 

        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet("Sheet1");
        
        List<String> testData = new ArrayList<>();

        for(Row row:sheet){
            for(Cell cell: row){
                switch(cell.getCellType()){
                    case STRING:
                    String text = cell.getStringCellValue();
                    if(text.contains("searched")){
                        break;
                    }
                    testData.add(text);
                    default:
                    break;
                }
            }
        }
        file.close();
        workbook.close();
        return testData.toArray();
    }
}
