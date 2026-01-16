import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/unified_data_ingestion";
    private static final String USER = "princel";
    private static final String PASSWORD = "princel123";

    public static Connection getConnection() throws Exception{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    }