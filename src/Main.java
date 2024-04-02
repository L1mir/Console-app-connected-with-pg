import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    // Data Base table: task(columns: id, name, state)
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "1";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/IntellijIdeaDb";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        while (true) {
            System.out.println("1. Показать список всех комманд");
            System.out.println("2. Выполнить задачу");
            System.out.println("3. Создать комманду");
            System.out.println("4. Выйти");

            try {
                int command = scanner.nextInt();

                switch (command) {
                    case 1:
                        Statement statement = connection.createStatement();
                        String SQL_SELECT_TASKS = "select * from task order by id desc";
                        ResultSet result = statement.executeQuery(SQL_SELECT_TASKS);

                        while (result.next()) {
                            System.out.println(result.getInt("id") + " "
                                    + result.getString("name") + " " + result.getString("state"));
                        }
                        break;
                    case 2:
                        String sqlUpdate = "UPDATE task SET state = 'done' WHERE id = ?";
                        PreparedStatement preparedStatementUpdate = connection.prepareStatement(sqlUpdate);
                        System.out.println("Введите идентификатор задачи:");
                        int taskIdUpdate = scanner.nextInt();
                        preparedStatementUpdate.setInt(1, taskIdUpdate);
                        preparedStatementUpdate.executeUpdate();
                        break;
                    case 3:
                        String sqlInsert = "insert into task (name, state) values (?, 'in_process')";
                        PreparedStatement preparedStatementInsert = connection.prepareStatement(sqlInsert);
                        System.out.println("Введите название задачи:");
                        String taskNameInsert = scanner.next();
                        preparedStatementInsert.setString(1, taskNameInsert);
                        preparedStatementInsert.executeUpdate();
                        break;
                    case 4:
                        System.exit(0);
                        break;
                    default:
                        System.err.println("Команда не распознана");
                        break;
                }
            } catch (InputMismatchException e) {
                System.err.println("Неправильный формат ввода. Введите целое число.");
                scanner.next();
            }
        }
    }
}
