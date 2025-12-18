// ProdukDAO.java
package com.tokopancing.dao;

import com.tokopancing.database.DatabaseConnection;
import com.tokopancing.model.Produk;
import com.tokopancing.model.Kategori;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukDAO {
    private Connection connection;
    private KategoriDAO kategoriDAO;

    public ProdukDAO() {
        connection = DatabaseConnection.getConnection();
        kategoriDAO = new KategoriDAO();
    }

    public List<Produk> getAllProduk() {
        List<Produk> produkList = new ArrayList<>();
        String query = "SELECT p.*, k.nama_kategori FROM produk p " +
                      "LEFT JOIN kategori k ON p.id_kategori = k.id_kategori " +
                      "ORDER BY p.nama_produk";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Produk produk = new Produk();
                produk.setIdProduk(rs.getString("id_produk"));
                produk.setNamaProduk(rs.getString("nama_produk"));
                produk.setHarga(rs.getDouble("harga"));
                produk.setStok(rs.getInt("stok"));
                produk.setDeskripsi(rs.getString("deskripsi"));
                
                Kategori kategori = new Kategori(
                    rs.getInt("id_kategori"),
                    rs.getString("nama_kategori")
                );
                produk.setKategori(kategori);
                
                produkList.add(produk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produkList;
    }

    public Produk getProdukById(String id) {
        String query = "SELECT p.*, k.nama_kategori FROM produk p " +
                      "LEFT JOIN kategori k ON p.id_kategori = k.id_kategori " +
                      "WHERE p.id_produk = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Produk produk = new Produk();
                produk.setIdProduk(rs.getString("id_produk"));
                produk.setNamaProduk(rs.getString("nama_produk"));
                produk.setHarga(rs.getDouble("harga"));
                produk.setStok(rs.getInt("stok"));
                produk.setDeskripsi(rs.getString("deskripsi"));
                
                Kategori kategori = new Kategori(
                    rs.getInt("id_kategori"),
                    rs.getString("nama_kategori")
                );
                produk.setKategori(kategori);
                
                return produk;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addProduk(Produk produk) {
        String query = "INSERT INTO produk (id_produk, nama_produk, harga, stok, id_kategori, deskripsi) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, produk.getIdProduk());
            pstmt.setString(2, produk.getNamaProduk());
            pstmt.setDouble(3, produk.getHarga());
            pstmt.setInt(4, produk.getStok());
            pstmt.setInt(5, produk.getKategori().getIdKategori());
            pstmt.setString(6, produk.getDeskripsi());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduk(Produk produk) {
        String query = "UPDATE produk SET nama_produk = ?, harga = ?, stok = ?, " +
                      "id_kategori = ?, deskripsi = ? WHERE id_produk = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, produk.getNamaProduk());
            pstmt.setDouble(2, produk.getHarga());
            pstmt.setInt(3, produk.getStok());
            pstmt.setInt(4, produk.getKategori().getIdKategori());
            pstmt.setString(5, produk.getDeskripsi());
            pstmt.setString(6, produk.getIdProduk());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduk(String id) {
        String query = "DELETE FROM produk WHERE id_produk = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStok(String idProduk, int jumlah) {
        String query = "UPDATE produk SET stok = stok - ? WHERE id_produk = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, jumlah);
            pstmt.setString(2, idProduk);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Produk> searchProduk(String keyword) {
        List<Produk> produkList = new ArrayList<>();
        String query = "SELECT p.*, k.nama_kategori FROM produk p " +
                      "LEFT JOIN kategori k ON p.id_kategori = k.id_kategori " +
                      "WHERE p.nama_produk LIKE ? OR p.id_produk LIKE ? " +
                      "ORDER BY p.nama_produk";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Produk produk = new Produk();
                produk.setIdProduk(rs.getString("id_produk"));
                produk.setNamaProduk(rs.getString("nama_produk"));
                produk.setHarga(rs.getDouble("harga"));
                produk.setStok(rs.getInt("stok"));
                produk.setDeskripsi(rs.getString("deskripsi"));
                
                Kategori kategori = new Kategori(
                    rs.getInt("id_kategori"),
                    rs.getString("nama_kategori")
                );
                produk.setKategori(kategori);
                
                produkList.add(produk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produkList;
    }
}