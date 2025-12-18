// KategoriManagementForm.java
package com.tokopancing.gui;

import com.tokopancing.dao.KategoriDAO;
import com.tokopancing.model.Kategori;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class KategoriManagementForm extends JFrame {
    private KategoriDAO kategoriDAO;
    private JTable kategoriTable;
    private DefaultTableModel kategoriTableModel;
    
    private JTextField tfNamaKategori;
    private JButton btnTambah, btnUpdate, btnHapus, btnClear;

    public KategoriManagementForm() {
        kategoriDAO = new KategoriDAO();
        
        initComponents();
        loadKategori();
    }

    private void initComponents() {
        setTitle("Manajemen Kategori");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Kategori"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nama Kategori:"), gbc);
        gbc.gridx = 1;
        tfNamaKategori = new JTextField(20);
        formPanel.add(tfNamaKategori, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnClear = new JButton("Clear");
        
        btnTambah.addActionListener(e -> tambahKategori());
        btnUpdate.addActionListener(e -> updateKategori());
        btnHapus.addActionListener(e -> hapusKategori());
        btnClear.addActionListener(e -> clearForm());
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnClear);

        // Table Panel
        String[] columns = {"ID", "Nama Kategori"};
        kategoriTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        kategoriTable = new JTable(kategoriTableModel);
        kategoriTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                pilihKategoriDariTable();
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(kategoriTable);

        // Layout
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(formPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadKategori() {
        kategoriTableModel.setRowCount(0);
        List<Kategori> kategoriList = kategoriDAO.getAllKategori();
        
        for (Kategori kategori : kategoriList) {
            Object[] row = {
                kategori.getIdKategori(),
                kategori.getNamaKategori()
            };
            kategoriTableModel.addRow(row);
        }
    }

    private void pilihKategoriDariTable() {
        int selectedRow = kategoriTable.getSelectedRow();
        if (selectedRow >= 0) {
            tfNamaKategori.setText(kategoriTableModel.getValueAt(selectedRow, 1).toString());
        }
    }

    private void tambahKategori() {
        String namaKategori = tfNamaKategori.getText().trim();
        if (namaKategori.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kategori harus diisi!", "Validasi", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Kategori kategori = new Kategori(0, namaKategori);
        try {
            kategoriDAO.addKategori(kategori);
            JOptionPane.showMessageDialog(this, "Kategori berhasil ditambahkan!", "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadKategori();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateKategori() {
        int selectedRow = kategoriTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih kategori terlebih dahulu!", "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) kategoriTableModel.getValueAt(selectedRow, 0);
        String namaKategori = tfNamaKategori.getText().trim();
        
        if (namaKategori.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kategori harus diisi!", "Validasi", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Kategori kategori = new Kategori(id, namaKategori);
        try {
            kategoriDAO.updateKategori(kategori);
            JOptionPane.showMessageDialog(this, "Kategori berhasil diupdate!", "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadKategori();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusKategori() {
        int selectedRow = kategoriTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih kategori terlebih dahulu!", "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) kategoriTableModel.getValueAt(selectedRow, 0);
        String namaKategori = kategoriTableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Hapus kategori '" + namaKategori + "'?\n" +
                "Produk dengan kategori ini akan kehilangan kategori.", 
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                kategoriDAO.deleteKategori(id);
                JOptionPane.showMessageDialog(this, "Kategori berhasil dihapus!", "Sukses", 
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadKategori();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        tfNamaKategori.setText("");
        kategoriTable.clearSelection();
    }
}