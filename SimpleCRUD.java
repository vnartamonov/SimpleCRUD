import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class SimpleCRUD implements AutoCloseable {
    private final Connection connection;

    public SimpleCRUD(String url, String username, String password) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance(); // Подключаем драйвер MySql.
        connection = DriverManager.getConnection(url, username, password); // Инициализируем соединение.
    }

    // Добавляем студента. Поле id с автоинкрементом.
    public boolean add(Student s) throws SQLException {
        String sqlTemplate = "INSERT INTO Students(`Name`, `Surname`, `Patronymic`, `DateBirth`, `Group`) VALUES ('%s', '%s', '%s', '%tF', '%s');";
        String sqlCommand = String.format(sqlTemplate, s.name, s.surname, s.patronymic, s.dateBirth, s.group);

        Statement statement = connection.createStatement();
        int result = statement.executeUpdate(sqlCommand);
        return result == 1;
    }

    // Удаляем студента по id.
    public boolean delete(int id) throws SQLException {
        String sqlTemplate = "DELETE FROM Students WHERE id=%d;";
        String sqlCommand = String.format(sqlTemplate, id);

        Statement statement = connection.createStatement();
        int result = statement.executeUpdate(sqlCommand);
        return result == 1;
    }

    // Возвращаем список всех студентов.
    public ArrayList<Student> print() throws SQLException {
        String sqlCommand = "SELECT * FROM Students;";
        ArrayList<Student> students = new ArrayList<Student>();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlCommand);

        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            String patronymic = resultSet.getString("Patronymic");
            Date dateBirth = resultSet.getDate("DateBirth");
            String group = resultSet.getString("Group");

            students.add(new Student(id, name, surname, patronymic, dateBirth, group));
        }

        return students;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    public static class Student {
        int id;
        String name;
        String surname;
        String patronymic;
        Date dateBirth;
        String group;

        public Student(int id, String name, String surname, String patronymic, Date dateBirth, String group){
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.patronymic = patronymic;
            this.dateBirth = dateBirth;
            this.group = group;
        }

        @Override
        public String toString() {
            return  "id = " + id +
                    ", name = '" + name + '\'' +
                    ", surname = '" + surname + '\'' +
                    ", patronymic = '" + patronymic + '\'' +
                    ", dateBirth = " + dateBirth +
                    ", group = '" + group + '\'';
        }
    }
}
