/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uas;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static uas.DatabaseUtil.*;

/**
 *
 * @author user
 */
public class Barang {
    String kode;
    String nama;
    String harga;
    
    public Barang(String paramKode, String paramNama, String paramHarga) {
       this.kode = paramKode;
       this.nama = paramNama;
       this.harga = paramHarga;
    }
    
    public static ResultSet getDataBarang() {
        try {
            String sql = "SELECT kode, nama, harga FROM barang";
            ResultSet result_data = stmt.executeQuery(sql);
            return result_data;
        } catch(SQLException e) {
            System.out.println("Gagal get data: "+ e.getMessage());
        }
        return null;
    }
    
    public static ResultSet getDataBarangByKode(String paramKode) {
        try {
            String sql = "SELECT kode, nama, harga FROM barang WHERE kode='%s'";
            sql = String.format(sql, paramKode);
            ResultSet result_data = stmt.executeQuery(sql);
            return result_data;
        } catch(SQLException e) {
            System.out.println("Gagal get data by kode: "+e.getMessage());
        }
        return null;
    }
    
    public static void showDataBarang(DefaultTableModel model) {
        try {
            model.getDataVector().removeAllElements();
            model.fireTableDataChanged();
            ResultSet result_data = getDataBarang();
                while(result_data.next()) {
                    Object[] object;
                    object = new Object[3];
                    object[0] = result_data.getString("kode");
                    object[1] = result_data.getString("nama");
                    object[2] = result_data.getString("harga");
                    model.addRow(object);
                }
        } catch(SQLException e) {
            System.out.println("Error show data: "+e.getMessage());
        }
    }
    
    public static void insertDataBarang(Barang object) {
        try {
            String sql = "INSERT INTO barang VALUES('%s', '%s', '%s')";
            sql = String.format(sql, object.kode, object.nama, object.harga);
            stmt.execute(sql);
            JOptionPane.showMessageDialog(null, "Berhasil input data");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error insert data: "+e.getMessage());
        }
        
    }    
    
    public static void updateDataBarang(String paramKode, Barang object) {
        try {
            String sql = "UPDATE barang SET nama='%s', harga='%s' WHERE kode='%s'";
            sql = String.format(sql, object.nama, object.harga, object.kode);
            stmt.execute(sql);
            JOptionPane.showMessageDialog(null, "Berhasil update data");
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error update data: "+e.getMessage());
        }
    }
    
    public static void deleteDataBarang(String paramKode) {
        try {
            String sql = "DELETE FROM barang WHERE kode='%s'";
            sql = String.format(sql, paramKode);
            stmt.execute(sql);
            JOptionPane.showMessageDialog(null, "Berhasil delete data");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error delete data: "+e.getMessage());
        }
    }
        
}
