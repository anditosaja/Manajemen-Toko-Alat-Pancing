// ProdukManagementForm.java
package com.tokopancing.gui;

import com.tokopancing.dao.KategoriDAO;
import com.tokopancing.dao.ProdukDAO;
import com.tokopancing.model.Kategori;
import com.tokopancing.model.Produk;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

public class ProdukManagementForm extends JFrame {
    private ProdukDAO produkDAO;
    private KategoriDAO kategoriDAO;
    private JTable produkTable;
    private DefaultTableModel produkTableModel;
    private JComboBox<Kategori> cbKategori;
    
    private JTextField tfIdProduk, tfNama, tfHarga, tfStok, tfDeskripsi;
    private JButton btnTambah, btnUpdate, btnHapus, btnClear;
    
    private DecimalFormat df = new DecimalFormat("#,###.00");

    public ProdukManagementForm() {
        produkDAO = new ProdukDAO();
        kategoriDAO = new KategoriDAO();
        
        initComponents();
        loadProduk();
        loadKategori();
    }

    private void initComponents() {
        setTitle("Manajemen Produk");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Produk"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID Produk:"), gbc);
        gbc.gridx = 1;
        tfIdProduk = new JTextField(20);
        formPanel.add(tfIdProduk, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nama Produk:"), gbc);
        gbc.gridx = 1;
        tfNama = new JTextField(20);
        formPanel.add(tfNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Kategori:"), gbc);
        gbc.gridx = 1;
        cbKategori = new JComboBox<>();
        formPanel.add(cbKategori, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Harga:"), gbc);
        gbc.gridx = 1;
        tfHarga = new JTextField(20);
        formPanel.add(tfHarga, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Stok:"), gbc);
        gbc.gridx = 1;
        tfStok = new JTextField(20);
        formPanel.add(tfStok, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridx = 1;
        tfDeskripsi = new JTextField(20);
        formPanel.add(tfDeskripsi, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnClear = new JButton("Clear");
        
        btnTambah.addActionListener(e -> tambahProduk());
        btnUpdate.addActionListener(e -> updateProduk());
        btnHapus.addActionListener(e -> hapusProduk());
        btnClear.addActionListener(e -> clearForm());
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnClear);

        // Table Panel
        String[] columns = {"ID Produk", "Nama", "Kategori", "Harga", "Stok", "Deskripsi"};
        produkTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        produkTable = new JTable(produkTableModel);
        produkTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                pilihProdukDariTable();
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(produkTable);

        // Layout
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(formPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadProduk() {
        produkTableModel.setRowCount(0);
        List<Produk> produkList = produkDAO.getAllProduk();
        
        for (Produk produk : produkList) {
            Object[] row = {
                produk.getIdProduk(),
                produk.getNamaProduk(),
                produk.getKategori().getNamaKategori(),
                "Rp " + df.format(produk.getHarga()),
                produk.getStok(),
                produk.getDeskripsi()
            };
            produkTableModel.addRow(row);
        }
    }

    private void loadKategori() {
        cbKategori.removeAllItems();
        List<Kategori> kategoriList = kategoriDAO.getAllKategori();
        for (Kategori kategori : kategoriList) {
            cbKategori.addItem(kategori);
        }
    }

    private void pilihProdukDariTable() {
        int selectedRow = produkTable.getSelectedRow();
        if (selectedRow >= 0) {
            tfIdProduk.setText(produkTableModel.getValueAt(selectedRow, 0).toString());
            tfNama.setText(produkTableModel.getValueAt(selectedRow, 1).toString());
            
            // Set kategori
            String kategoriNama = produkTableModel.getValueAt(selectedRow, 2).toString();
            for (int i = 0; i < cbKategori.getItemCount(); i++) {
                if (cbKategori.getItemAt(i).getNamaKategori().equals(kategoriNama)) {
                    cbKategori.setSelectedIndex(i);
                    break;
                }
            }
            
            // Hapus "Rp " dari harga
            String hargaStr = produkTableModel.getValueAt(selectedRow, 3).toString();
            tfHarga.setText(hargaStr.replace("Rp ", "").replace(",", ""));
            
            tfStok.setText(produkTableModel.getValueAt(selectedRow, 4).toString());
            tfDeskripsi.setText(produkTableModel.getValueAt(selectedRow, 5).toString());
        }
    }

    private void tambahProduk() {
        if (!validasiForm()) return;
        
        Produk produk = new Produk();
        produk.setIdProduk(tfIdProduk.getText().trim());
        produk.setNamaProduk(tfNama.getText().trim());
        produk.setHarga(Double.parseDouble(tfHarga.getText().trim()));
        produk.setStok(Integer.parseInt(tfStok.getText().trim()));
        produk.setKategori((Kategori) cbKategori.getSelectedItem());
        produk.setDeskripsi(tfDeskripsi.getText().trim());
        
        try {
            produkDAO.addProduk(produk);
            JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan!", "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadProduk();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduk() {
        if (!validasiForm()) return;
        
        Produk produk = produkDAO.getProdukById(tfIdProduk.getText().trim());
        if (produk == null) {
            JOptionPane.showMessageDialog(this, "Produk tidak ditemukan!", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        produk.setNamaProduk(tfNama.getText().trim());
        produk.setHarga(Double.parseDouble(tfHarga.getText().trim()));
        produk.setStok(Integer.parseInt(tfStok.getText().trim()));
        produk.setKategori((Kategori) cbKategori.getSelectedItem());
        produk.setDeskripsi(tfDeskripsi.getText().trim());
        
        try {
            produkDAO.updateProduk(produk);
            JOptionPane.showMessageDialog(this, "Produk berhasil diupdate!", "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadProduk();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusProduk() {
        String idProduk = tfIdProduk.getText().trim();
        if (idProduk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih produk terlebih dahulu!", "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Hapus produk " + idProduk + "?", 
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                produkDAO.deleteProduk(idProduk);
                JOptionPane.showMessageDialog(this, "Produk berhasil dihapus!", "Sukses", 
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadProduk();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validasiForm() {
        if (tfIdProduk.getText().trim().isEmpty() ||
            tfNama.getText().trim().isEmpty() ||
            tfHarga.getText().trim().isEmpty() ||
            tfStok.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Validasi", 
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            double harga = Double.parseDouble(tfHarga.getText().trim());
            if (harga <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus angka positif!", "Validasi", 
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            int stok = Integer.parseInt(tfStok.getText().trim());
            if (stok < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stok harus angka positif!", "Validasi", 
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void clearForm() {
        tfIdProduk.setText("");
        tfNama.setText("");
        tfHarga.setText("");
        tfStok.setText("");
        tfDeskripsi.setText("");
        if (cbKategori.getItemCount() > 0) {
            cbKategori.setSelectedIndex(0);
        }
        produkTable.clearSelection();
    }
}