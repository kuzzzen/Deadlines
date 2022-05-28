package fit.biepjv.deadlines;

import java.sql.*;

public class SQLConnection {
    private static ResultSet result;
    private static Statement statement;
    private static Connection connection;

    public SQLConnection(String url) {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * FROM tasks;");
        } catch (SQLException e) {
            System.out.println("Error: could not connect to database");
            e.printStackTrace();
        }
    }

    public ResultSet getResult(java.sql.Date dateFrom, java.sql.Date dateTo) throws SQLException {
        String resultQuery = "SELECT * FROM tasks WHERE due_date BETWEEN ? and ? ORDER BY due_date ASC;";
        PreparedStatement resultStatement = connection.prepareStatement(resultQuery);
        resultStatement.setDate(1, dateFrom);
        resultStatement.setDate(2, dateTo);
        result = resultStatement.executeQuery();
        return result;
    }

    public void insertEventIntoDb(Event event) throws SQLException {
        String insertQuery = "INSERT INTO tasks (id, title, due_date, description) VALUES (?,?,?,?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setInt(1, event.getId());
        insertStatement.setString(2, event.getTitle());
        insertStatement.setDate(3, (Date) event.getDate());
        insertStatement.setString(4, event.getDescription());
        insertStatement.executeUpdate();
    }

    public void deleteEventFromDb(int eventId) throws SQLException {
        String deleteQuery = "DELETE FROM tasks WHERE id=" + eventId;
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.executeUpdate();
    }

    public int maxId() throws SQLException {
        return statement.executeQuery("SELECT MAX(id) id FROM tasks").getInt("id");
    }
}
