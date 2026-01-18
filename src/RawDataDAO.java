import java.sql.Connection;
import java.sql.PreparedStatement;

public class RawDataDAO {

    public static void insertRawData(String sourceType, String fileName, String content)throws Exception {

        String sql = "INSERT INTO raw_data(file_type, file_name, content) VALUES (?, ?, ?)";

            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, sourceType);
            ps.setString(2, fileName);
            ps.setString(3, content);

            ps.executeUpdate();

            ps.close();
            con.close();
        }

    }

