package fr.leloubil.lotawarp;

import com.sun.media.sound.InvalidFormatException;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

class Database {


    //Classe de la base de données
    private Statement statement;
    private Connection connection;

    private final String database;


    public Database(JavaPlugin main) {
        this.database = "SmallPlugins";

        try {
            this.openConnection();
            this.statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
    private void createNewStatementIfClosed(){
        boolean cclosed = true;
        boolean sclosed = true;
        try {
            cclosed = this.connection.isClosed();
            sclosed = this.statement.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (cclosed) {
                this.openConnection();
            } if (sclosed) {
                this.statement = connection.createStatement();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public ResultSet getFromKey(String TableName,String key, String value) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM `" + TableName + "` WHERE `" + key +"` = ? ;");
        ps.setString(1,value);
        return ps.executeQuery();
    }

    private ArrayList<HashMap<String,String>> getFromQuery(String sqlQuery) throws InvalidFormatException {
        createNewStatementIfClosed();
        ResultSet result = null;
        if (!sqlQuery.contains("SELECT")){
            throw new InvalidFormatException();
        }
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        try {
            result = this.statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HashMap<String,String> Results;
        try {

            assert result != null;
            ResultSetMetaData rm = result.getMetaData();
            Integer i = 0;
            Integer j;
            while (result.next()){
                Results = new HashMap<>();
                //on ajoute l'id pour differencier les iles
                j = rm.getColumnCount();
                while ( j!=0){
                    Results.put(rm.getColumnName(j),result.getString(j));
                    j--;
                }
                list.add(Results);
                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<HashMap<String,String>> getFromQuerys(String sqlQuery) throws InvalidFormatException {
        createNewStatementIfClosed();
        ResultSet result = null;
        if (!sqlQuery.contains("SELECT")){
            throw new InvalidFormatException();
        }
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        try {
            result = this.statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HashMap<String,String> Results;
        try {

            assert result != null;
            ResultSetMetaData rm = result.getMetaData();
            Integer i = 0;
            Integer j;
            while (result.next()){
                Results = new HashMap<>();
                //on ajoute l'id pour differencier les iles
                j = rm.getColumnCount();
                while ( j!=0){
                    Results.put(rm.getColumnName(j),result.getString(j));
                    j--;
                }
                list.add(Results);
                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean DeletefromKey(String TableName, String key, String value){
        String query = "DELETE FROM " + TableName + " WHERE " + key + " = '" + value + "';";
        createNewStatementIfClosed();
        try {
            return this.statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<HashMap<String,String>> getFromKey(String TableName, String key, operator op, String value) throws InvalidFormatException {
        String query = "SELECT * FROM " + TableName+ " WHERE " + key  +  op.toString() + " " + value + ";";
        return this.getFromQuery(query);
    }




    public void AddValues(String TableName, HashMap<String,String> data) throws InvalidFormatException{
        createNewStatementIfClosed();
        String sqlQuery = "INSERT INTO " + TableName + " (";
        final String[] sqlEnd = {") VALUES ("};
        final String[] finalSqlQuery = {sqlQuery};
        final String[] finalSqlEnd = {sqlEnd[0]};
        data.forEach((k, v) -> {
            finalSqlQuery[0] =  finalSqlQuery[0].concat(k + ", ");
            finalSqlEnd[0] = finalSqlEnd[0].concat( "'" + v + "', ");
        });
        sqlQuery = finalSqlQuery[0];
        sqlEnd[0] = finalSqlEnd[0];
        sqlQuery = sqlQuery.substring(0,sqlQuery.length() - 2);
        sqlEnd[0] = sqlEnd[0].substring(0, sqlEnd[0].length() - 2);
        sqlQuery = sqlQuery.concat(sqlEnd[0] + ");");
        AddFromQuery(sqlQuery);
    }
    private void AddFromQuery(String Query) throws InvalidFormatException {
        createNewStatementIfClosed();
        if (!Query.contains("INSERT") && !Query.contains("UPDATE")){
            throw new InvalidFormatException();
        }
        try {

            this.statement.executeUpdate(Query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean ExecuteQuery(String Query){
        createNewStatementIfClosed();
        try {

            this.statement.executeUpdate(Query);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
            int port = 3306;
            String password = "sT8NUousVzOxU0h5";
            String username = "SmallPlugins";
            String host = "127.0.0.1";
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + this.database, username, password);
        }
    }

    public ResultSet getResult(String sql) {
        createNewStatementIfClosed();
        try {
            if (this.connection.isClosed()) {
                try {
                    this.openConnection();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            Statement st = this.connection.createStatement();
            st.executeQuery(sql);
            return st.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}