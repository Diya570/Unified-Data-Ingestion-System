import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.*;

public class XLSReader {

    public static String readXLS(String filePath)throws Exception {
        StringBuilder content=new StringBuilder();
        FileInputStream fis= new FileInputStream(filePath);
            Workbook workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheetAt(0);
                for(Row row:sheet)
                {
                    for(Cell cell:row)
                    {
                        content.append(cell.toString()).append("\t");

                    }
                    content.append("\n");
                }
                workbook.close();
                fis.close();
                return content.toString();
    }
}
