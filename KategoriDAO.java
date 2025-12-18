// KategoriDAO.java
package com.tokopancing.dao;

import com.tokopancing.database.DatabaseConnection;
import com.tokopancing.model.Kategori;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriDAO {
    private Connection connection;

    public KategoriDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public List<Kategori> getAllKategori() {
        List<Kategori> kategoriList = new ArrayList<>();
        String query = "SELECT * FROM kategori ORDER BY nama_kategori";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Kategori kategori = new Kategori();
                kategori.setIdKategori(rs.getInt("id_kategori"));
                kategori.setNamaKategori(rs.getString("nama_kategori"));
                kategoriList.add(kategori);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kategoriList;
    }

    public Kategori getKategoriById(int id) {
        String query = "SELECT * FROM kategori WHERE id_kategori = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Kategori(
                    rs.getInt("id_kategori"),
                    rs.getString("nama_kategori")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addKategori(Kategori kategori) {
        String query = "INSERT INTO kategori (nama_kategori) VALUES (?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, kategori.getNamaKategori());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateKategori(Kategori kategori) {
        String query = "UPDATE kategori SET nama_kategori = ? WHERE id_kategori = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, kategori.getNamaKategori());
            pstmt.setInt(2, kategori.getIdKategori());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteKategori(int id) {
        String query = "DELETE FROM kategori WHERE id_kategori = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}