import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class Connect {
    private static final String url = "jdbc:mysql://127.0.0.1/students_tms";
    private static final String usr = "cos7els";
    private static final String pwd = "0123456789";

    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(url, usr, pwd);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from students");
            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                int id = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String city = rs.getString(4);
                result.append(String.format("%d, %s %s, %s\n", id, firstName, lastName, city));
            }
            System.out.println(result);
            Files.write(Path.of("students.txt"), result.toString().getBytes());
            result = new StringBuilder();
            rs.close();
            rs = stmt.executeQuery("select * from cities");
            while (rs.next()) {
                int id = rs.getInt(1);
                String city = rs.getNString(2);
                result.append(String.format("%d, %s\n", id, city));
            }
            System.out.println(result);
            Files.write(Path.of("cities.txt"), result.toString().getBytes());
            result = new StringBuilder();
            rs.close();
            rs = stmt.executeQuery("select students.*, cities.* from students inner join cities on students.city = cities.city");
            while (rs.next()) {
                int studentId = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String studentCity = rs.getString(4);
                int cityId = rs.getInt(5);
                String citiesCity = rs.getString(6);
                result.append(String.format("%d, %s %s, %s, %d, %s\n", studentId, firstName, lastName, studentCity, cityId, citiesCity));
            }
            System.out.println(result);
            Files.write(Path.of("students_and_cities.txt"), result.toString().getBytes());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
