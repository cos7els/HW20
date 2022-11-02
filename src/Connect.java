import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Connect {
    private final Statement statement;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

    private Connect() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1/students";
        String user = "cos7els";
        String password = "0123456789";
        Connection connect = DriverManager.getConnection(url, user, password);
        statement = connect.createStatement();
    }

    public static Connect getInstance() {
        Connect instance;
        try {
            instance = new Connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    public String getAllStudents() {
        StringBuilder sbResult = new StringBuilder();
        String result;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT student_id, first_name, last_name, city " +
                    "FROM student INNER JOIN city ON student.city_id = city.city_id");
            while (resultSet.next()) {
                int student_id = resultSet.getInt(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                String city = resultSet.getString(4);
                sbResult.append(String.format("%d %s %s %s\n", student_id, firstName, lastName, city));
            }
            result = sbResult.toString();
            Path path = Path.of(String.format("results\\getAllStudents%s.txt", LocalDateTime.now().format(format)));
            Files.write(path, result.getBytes());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public String getAllCities() {
        StringBuilder sbResult = new StringBuilder();
        String result;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM city");
            while (resultSet.next()) {
                int city_id = resultSet.getInt(1);
                String city = resultSet.getString(2);
                sbResult.append(String.format("%d %s\n", city_id, city));
            }
            result = sbResult.toString();
            Path path = Path.of(String.format("results\\getAllCities%s.txt", LocalDateTime.now().format(format)));
            Files.write(path, result.getBytes());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void addStudent(String firstName, String lastName, String city) {
        try {
            int cityId = getCityId(city);
            if (cityId < 0) {
                cityId = insertCity(city);
            }
            statement.execute(String.format("INSERT INTO students.student (first_name, last_name, city_id) " +
                    "VALUES ('%s', '%s', '%d')", firstName, lastName, cityId));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeStudent(String firstName, String lastName, String city) {
        int studentId = getStudentId(firstName, lastName, city);
        if (studentId > 0) {
            deleteStudent(studentId);
        } else if (studentId < 0) {
            System.out.println("There is no this student in database");
        }
    }

    private void deleteStudent(int studentId) {
        try {
            statement.execute(String.format("DELETE FROM students.student WHERE student_id = '%d'", studentId));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getStudentId(String firstName, String lastName, String city) {
        int studentId = -1;
        try {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT student_id FROM student, city WHERE first_name = '%s' AND last_name = '%s' AND city = '%s'", firstName, lastName, city));
            if (resultSet.next()) {
                studentId = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return studentId;
    }

    public void addCity(String city) {
        int cityId = getCityId(city);
        if (cityId < 0) {
            insertCity(city);
        } else if (cityId > 0) {
            System.out.printf("The city '%s' is in the database on index %d\n", city, cityId);
        }
    }

    public void removeCity(String city) {
        int cityId = getCityId(city);
        if (cityId > 0) {
            deleteCity(cityId);
        } else if (cityId < 0) {
            System.out.println("There is no this city in database");
        }
    }

    private int getCityId(String city) {
        int cityId = -1;
        try {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT city.city_id FROM city WHERE city.city = '%s'", city));
            if (resultSet.next()) {
                cityId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cityId;
    }

    private int insertCity(String city) {
        int cityID;
        try {
            statement.execute(String.format("INSERT INTO students.city (city) VALUE ('%s')", city));
            cityID = getCityId(city);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cityID;
    }

    private void deleteCity(int cityId) {
        try {
            statement.execute(String.format("DELETE FROM students.city WHERE city_id = '%d'", cityId));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
