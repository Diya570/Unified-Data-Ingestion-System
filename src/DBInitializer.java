import java.sql.Connection;
import java.sql.Statement;

public class DBInitializer {

    public static void init() {
        try(Connection con=DBConnection.getConnection();
        Statement stmt=con.createStatement())
        {
        String sql=
            "CREATE TABLE IF NOT EXISTS raw_data ("+
                "id SERIAL PRIMARY KEY,"+
                "file_name TEXT,"+
                "file_type TEXT,"+
                "content TEXT,"+
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+")";

            stmt.execute(sql);
            System.out.println("Table raw_data is ready.");

        } catch (Exception e) {
            System.out.println("Error initializing database");
            e.printStackTrace();
        }
    }
}
