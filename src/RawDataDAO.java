import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RawDataDAO {

    public static void insertRawData(String type, String name, String content) throws Exception {
        String sql = "INSERT INTO raw_data(file_type, file_name, content) VALUES (?, ?, ?)";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, type);
        ps.setString(2, name);
        ps.setString(3, content);
        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public static Object[][] fetchPreviewData() throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "SELECT id, file_type, file_name FROM raw_data ORDER BY id DESC";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        List<Object[]> rows = new ArrayList<>();
        while (rs.next()) {
            rows.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("file_type"),
                    rs.getString("file_name")
            });
        }

        Object[][] data = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i);
        }

        rs.close();
        st.close();
        con.close();
        return data;
    }

    public static int countByType(String type) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "SELECT COUNT(*) FROM raw_data WHERE file_type=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, type);

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        con.close();
        return count;
    }
}