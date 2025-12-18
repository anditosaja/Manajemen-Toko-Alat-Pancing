// TransaksiDAO.java
package com.tokopancing.dao;

import com.tokopancing.database.DatabaseConnection;
import com.tokopancing.model.Transaksi;
import com.tokopancing.model.ItemKeranjang;
import java.sql.*;
import java.util.List;

public class TransaksiDAO {
    private Connection connection;

    public TransaksiDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public int createTransaksi(double totalHarga) {
        String query = "INSERT INTO transaksi (total_harga, status_transaksi) VALUES (?, 'selesai')";
        int generatedId = -1;
        
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDouble(1, totalHarga);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public void createDetailTransaksi(int idTransaksi, List<ItemKeranjang> items) {
        String query = "INSERT INTO detail_transaksi (id_transaksi, id_produk, jumlah, subtotal) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (ItemKeranjang item : items) {
                pstmt.setInt(1, idTransaksi);
                pstmt.setString(2, item.getProduk().getIdProduk());
                pstmt.setInt(3, item.getJumlah());
                pstmt.setDouble(4, item.getSubtotal());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}