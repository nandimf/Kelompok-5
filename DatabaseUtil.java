/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uas;
import java.sql.*;

/**
 *
 * @author user
 */
public class DatabaseUtil {
 
    static Connection conn;
    static ResultSet result;
    static Statement stmt;
    
    static String database = "praktikum_pbo";
    static String port = "3306";
    static String url = "jdbc:mysql://localhost:"+port+"/"+database;
    static String user = "root";
    static String pass = "";
    
    public static void CheckConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,pass);
            stmt = conn.createStatement();
        } catch(ClassNotFoundException | SQLException e) {
            throw new DatabaseError("Tidak bisa konek database: "+e.getMessage());
        }
    }
    
    public static void CloseConnection() {
        try {
            stmt.close();
            conn.close();
        } catch(SQLException e) {
            throw new DatabaseError("Ada kesalahan dengan database: "+e.getMessage());
        }
    }
        
}
