/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uas;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import static uas.DatabaseUtil.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author user
 */
public class Penjualan {
    String id;
    String kodeBarang;
    String jumlah;
    String totalHarga;
    String tanggalJual;
    
    public Penjualan(String paramKodeBarang, String paramJumlah, String paramTotalHarga, String paramTanggalJual) {
        this.kodeBarang = paramKodeBarang;
        this.jumlah = paramJumlah;
        this.totalHarga = paramTotalHarga;
        this.tanggalJual = paramTanggalJual;
    }
    
    public static ResultSet getDataJual() {
        try {
            String sql = "SELECT pj.id, pj.kode_barang, brg.nama, brg.harga, pj.jumlah, pj.total_harga, pj.tanggal_jual FROM penjualan AS pj INNER JOIN barang AS brg ON pj.kode_barang=brg.kode";
            ResultSet result_data = stmt.executeQuery(sql);
            return result_data;
        } catch(SQLException e) {
            System.out.println("Gagal get data jual: "+e.getMessage());
        }
        return null;
    }
    
    public static void showDataJual(DefaultTableModel model) {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        ResultSet result_data = getDataJual();
        try {
        while(result_data.next()) {
            Object[] object;
            object = new Object[6];
            object[0] = result_data.getString("id");
            object[1] = result_data.getString("kode_barang");
            object[2] = result_data.getString("nama");
            object[3] = result_data.getString("jumlah");
            object[4] = result_data.getString("total_harga");
            object[5] = result_data.getString("tanggal_jual");
            model.addRow(object);
        }
        } catch(SQLException e) {
            System.out.println("Error show data jual: "+e.getMessage());
        }
    }
    
    public static void inputDataJual(Penjualan object) {
        try {
            String sql = "INSERT INTO penjualan(kode_barang, jumlah, total_harga, tanggal_jual) VALUES('%s', '%s', '%s', '%s')";
            sql = String.format(sql, object.kodeBarang, object.jumlah, object.totalHarga, object.tanggalJual);
            stmt.execute(sql);
            JOptionPane.showMessageDialog(null, "Berhasil input data");
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error input data: "+e.getMessage());
        }
    }
    
    public static void deleteDataJual(String idJual) {
        try {
            String sql = "DELETE FROM penjualan WHERE id='%s'";
            sql = String.format(sql, idJual);
            stmt.execute(sql);
            JOptionPane.showMessageDialog(null, "Berhasil hapus data");
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error delete data: "+e.getMessage());
        }
    }
    
    public static void cetakDataJualPdf(String dest) throws MalformedURLException {
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
            Document doc = new Document(pdfDoc, PageSize.A4.rotate());
            ImageData imageData = ImageDataFactory.create("src/images/logo.png");
            Image logoToko = new Image(imageData);
            logoToko.setWidth(75);
            
            // ADD IMAGE
            float[] colWidthsHead = {15,85};
            Table tableHead = new Table(UnitValue.createPercentArray(colWidthsHead)).useAllAvailableWidth();
            tableHead.setHeight(100);
            tableHead.addHeaderCell(new Cell().add(logoToko).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));
            tableHead.addHeaderCell(new Cell().add(new Paragraph("Gudang Barang").setBold().setFontSize(16)).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));
            doc.add(tableHead);
            // END ADD IMAGE
            float[] colWidths = {5,5,10,25,10,25,20};
            Table table = new Table(UnitValue.createPercentArray(colWidths)).useAllAvailableWidth();
            table.addHeaderCell(new Cell().add(new Paragraph("No").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("ID").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Kode").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Nama").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Jumlah").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Bayar").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Tanggal").setBold()));
            
            ResultSet result_data = getDataJual();
            int i = 0;
            try {
                while(result_data.next()) {
                    table.addCell(String.valueOf(i+=1));
                    table.addCell(new Cell().add(new Paragraph(result_data.getString("id"))));
                    table.addCell(new Cell().add(new Paragraph(result_data.getString("kode_barang"))));
                    table.addCell(new Cell().add(new Paragraph(result_data.getString("nama"))));
                    table.addCell(new Cell().add(new Paragraph(result_data.getString("jumlah"))));
                    table.addCell(new Cell().add(new Paragraph(result_data.getString("total_harga"))));
                    table.addCell(new Cell().add(new Paragraph(result_data.getString("tanggal_jual"))));
                }
            } catch(SQLException e){
                System.out.println("Error mengambil data: "+e.getMessage());
            }
            doc.add(table);
            // == DATE FOOTER
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String strDate = formatter.format(date);
            doc.add(new Paragraph("Created at: "+strDate).setFontColor(WebColors.getRGBColor("grey")).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));
            // == END DATE FOOTER
            doc.close();
            JOptionPane.showMessageDialog(null, "Sukses cetak data: "+dest);
        } catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Gagal cetak data");
            System.out.println("Error cetak data: "+ex.getMessage());
        }
    }
}
