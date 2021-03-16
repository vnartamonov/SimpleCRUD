import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String [] argc) {

        // Учетные данные для подключения к базе данных.
        String url = "jdbc:mysql://localhost/Students";
        String username = "root";
        String password = "toor";


        try(SimpleCRUD simpleCRUD = new SimpleCRUD(url, username, password);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connection successful.\n");
            int input = 0;

            // В цикле выполняем интерактивный интерфейс.
            do {
                System.out.println("1. Print all students");
                System.out.println("2. Create student");
                System.out.println("3. Remove student by id");
                System.out.println("4. Exit");

                // Конструкция для проверки корректности ввода переменной input.
                do {
                    System.out.print("> ");
                    try {
                        input = Integer.parseInt(consoleReader.readLine());
                    } catch (NumberFormatException e) { input = 0;}
                } while (!(input > 0 && input < 5));

                switch (input){
                    case 1:
                        for(SimpleCRUD.Student student : simpleCRUD.print())
                            System.out.println(student);
                        break;
                    case 2:
                        if(simpleCRUD.add(inputStudent(consoleReader)))
                            System.out.println("Student added.");
                        else
                            System.out.println("Add error.");
                        break;
                    case 3:
                        if(simpleCRUD.delete(inputId(consoleReader)))
                            System.out.println("Student deleted.");
                        else
                            System.out.println("Delete error.");
                        break;
                }
            }while (input != 4);
        }
        catch (Exception e){
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }

    // Ввод данных студента с консоли с проверкой вводимых данных.
    private static SimpleCRUD.Student inputStudent(BufferedReader reader) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String name, surname, patronymic, group;
        Date dateBirth = new Date(0);

        do{
            System.out.print("Enter name: ");
            name = reader.readLine();
        }while (name.isEmpty());

        do{
            System.out.print("Enter surname: ");
            surname = reader.readLine();
        }while (surname.isEmpty());

        do{
            System.out.print("Enter patronymic: ");
            patronymic = reader.readLine();
        }while (patronymic.isEmpty());

        do {
            System.out.print("Enter date of birth day (dd.mm.yyyy): ");
            try { dateBirth = formatter.parse(reader.readLine()); }
            catch (ParseException e) {}
        }while (dateBirth.getTime() == 0);

        do{
            System.out.print("Enter study group: ");
            group = reader.readLine();
        }while (group.isEmpty());

        return new SimpleCRUD.Student(0, name, surname, patronymic, dateBirth, group);
    }

    // Ввод id с консоли с проверкой вводимых данных.
    private static int inputId(BufferedReader reader) throws IOException {
        int id = 0;
        do {
            System.out.print("Enter id: ");
            try {
                id = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {}
        } while (!(id>0));

        return id;
    }
}
