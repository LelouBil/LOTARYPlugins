package fr.leloubil.shopapi;


import java.sql.*;

public class Database {

    private Connection connection;
    private final String host = Main.getInstance().getConfig().getString("sql.host");
    private final String database;
    private final String username = Main.getInstance().getConfig().getString("sql.user");
    private final String password = Main.getInstance().getConfig().getString("sql.password");
    private final int port = Main.getInstance().getConfig().getInt("sql.port");

    public Connection getConnection(){
        createNewStatementIfClosed();
        return connection;
    }
    public Database(String database) {
        this.database = database;
        try {
            this.openConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    private void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }


    public void createNewStatementIfClosed(){
        boolean cclosed = true;
        try {
            cclosed = this.connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (cclosed) {
                this.openConnection();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
