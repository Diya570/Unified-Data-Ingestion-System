import java.nio.file.Files;
import java.nio.file.Path;

public class TXTReader {

    public static String readTXT(String filePath)throws Exception
    {
        return Files.readString(Path.of(filePath));
    }
}