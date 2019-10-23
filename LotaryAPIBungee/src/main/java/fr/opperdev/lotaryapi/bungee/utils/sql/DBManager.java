package fr.opperdev.lotaryapi.bungee.utils.sql;

import fr.opperdev.lotaryapi.bungee.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * MySQL API
 * @author OpperDev
 * @version 3.6
 */
public class DBManager {

    private DBInitor initor;

    /**
     * Constructor #1
     * @param initor
     */
    public DBManager(DBInitor initor) {
        this.initor = initor;
        initor.init();
    }

    /**
     * @param column
     * @param data
     * @param table
     * @return boolean
     */
    public boolean exists(String column, String data, String table) {
        try (Connection con = this.initor.getConnection()){
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM "+table+" WHERE "+column+"="+data+";");
            if(result.next()){
                statement.close();
                result.close();
                return true;
            }
            statement.close();
            result.close();
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param table
     * @return ResultSet
     */
    public ArrayList<HashMap<String,String>> selectAllTable(String table) {
        try (Connection con = this.initor.getConnection()){
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM "+table+";");
            ArrayList<HashMap<String,String>> map = new ArrayList<>();
            while (result.next()){
                HashMap<String,String> dt = new HashMap<>();
                int max = result.getMetaData().getColumnCount();
                for (int i = 1; i <= max; i++) {
                    dt.put(result.getMetaData().getColumnName(i),result.getString(i));
                }
                map.add(dt);
            }
            statement.close();
            result.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param column
     * @param gate
     * @param data
     * @param table
     * @return ResultSet
     */
    public ArrayList<HashMap<String,String>> selectAll(String column, String gate, String data, String table) {
        try (Connection con = this.initor.getConnection()){
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM "+table+" WHERE "+column+gate+data+";");
            ArrayList<HashMap<String,String>> map = new ArrayList<>();
            while (result.next()){
                HashMap<String,String> dt = new HashMap<>();
                int max = result.getMetaData().getColumnCount();
                for (int i = 1; i <= max; i++) {
                    dt.put(result.getMetaData().getColumnName(i),result.getString(i));
                }
                map.add(dt);
            }
            statement.close();
            result.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * @param column
     * @param gate
     * @param data
     * @param toSelect
     * @param table
     * @return Object
     */
    public Object select(String column, String gate, String data, String toSelect, String table) {
        try (Connection con = this.initor.getConnection()){
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM "+table+" WHERE "+column+gate+data+";");
            if(result.next()) {
                return result.getObject(toSelect);
            }
            statement.close();
            result.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param columns
     * @param values
     * @param table
     */
    public void insert(String columns, String values, String table) {
        try (Connection con = this.initor.getConnection()){
            Statement statement = con.createStatement();
            String query = "INSERT INTO "+table+" ("+columns+") VALUES ("+values+")";
            Main.instance.log("QUERY : " + query);
            statement.executeUpdate(query);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param column
     * @param gate
     * @param data
     * @param table
     */
    public void delete(String column, String gate, String data, String table) {
        try (Connection con = this.initor.getConnection()){
            Statement statement = con.createStatement();
            statement.executeUpdate("DELETE FROM "+table+" WHERE "+column+gate+data+";");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param toSet
     * @param column
     * @param gate
     * @param data
     * @param table
     */
    public void update(String toSet, String column, String gate, String data, String table) {
        try (Connection con = this.initor.getConnection()){
            Statement statement = con.createStatement();
            statement.executeUpdate("UPDATE "+table+" SET "+toSet+" WHERE "+column+gate+data+";");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return DBInitor
     */
    public DBInitor getInitor() {
        return this.initor;
    }
}
