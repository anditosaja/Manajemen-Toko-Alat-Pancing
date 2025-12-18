// MainForm.java
package com.tokopancing.gui;

import com.tokopancing.dao.KategoriDAO;
import com.tokopancing.dao.ProdukDAO;
import com.tokopancing.dao.TransaksiDAO;
import com.tokopancing.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class MainForm extends JFrame {
    private ProdukDAO produkDAO;
    private KategoriDAO kategoriDAO;
    private TransaksiDAO transaksiDAO;
    private Keranjang keranjang;
    
    private JTable produkTable;
    private DefaultTableModel produkTableModel;
    private JTable keranjangTable;
    private DefaultTableModel keranjangTableModel;
    
    private JTextField tfSearch;
    private JLabel lblTotalHarga;
    
    private DecimalFormat df = new DecimalFormat("#,###.00");

    public MainForm() {
        produkDAO = new ProdukDAO();
        kategoriDAO = new KategoriDAO();
        transaksiDAO = new TransaksiDAO();
        keranjang = new Keranjang();
        
        initComponents();
        loadProduk();
    }

    private void initComponents() {
        setTitle("Toko Alat Pancing - Sistem Penjualan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem menuKeluar = new JMenuItem("Keluar");
        menuKeluar.addActionListener(e -> System.exit(0));
        menuFile.add(menuKeluar);
        
        JMenu menuMaster = new JMenu("Master Data");
        JMenuItem menuProduk = new JMenuItem("Kelola Produk");
        menuProduk.addActionListener(e -> new ProdukManagementForm().setVisible(true));
        JMenuItem menuKategori = new JMenuItem("Kelola Kategori");
        menuKategori.addActionListener(e -> new KategoriManagementForm().setVisible(true));
        menuMaster.add(menuProduk);
        menuMaster.add(menuKategori);
        
        menuBar.add(menuFile);
        menuBar.add(menuMaster);
        setJMenuBar(menuBar);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari Produk:"));
        tfSearch = new JTextField(20);
        searchPanel.add(tfSearch);
        JButton btnSearch = new JButton("Cari");
        btnSearch.addActionListener(e -> searchProduk());
        searchPanel.add(btnSearch);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadProduk());
        searchPanel.add(btnRefresh);

        // Produk Table
        String[] produkColumns = {"ID", "Nama Produk", "Kategori", "Harga", "Stok", "Deskripsi"};
        produkTableModel = new DefaultTableModel(produkColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        produkTable = new JTable(produkTableModel);
        JScrollPane produkScrollPane = new JScrollPane(produkTable);

        // Keranjang Panel
        JPanel keranjangPanel = new JPanel(new BorderLayout());
        keranjangPanel.setBorder(BorderFactory.createTitledBorder("Keranjang Belanja"));
        
        String[] keranjangColumns = {"Produk", "Harga", "Jumlah", "Subtotal"};
        keranjangTableModel = new DefaultTableModel(keranjangColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        keranjangTable = new JTable(keranjangTableModel);
        JScrollPane keranjangScrollPane = new JScrollPane(keranjangTable);
        
        // Total Panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(new JLabel("Total Harga:"));
        lblTotalHarga = new JLabel("Rp 0");
        lblTotalHarga.setFont(new Font("Arial", Font.BOLD, 14));
        totalPanel.add(lblTotalHarga);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnTambahKeranjang = new JButton("Tambah ke Keranjang");
        btnTambahKeranjang.addActionListener(e -> tambahKeKeranjang());
        
        JButton btnHapusKeranjang = new JButton("Hapus dari Keranjang");
        btnHapusKeranjang.addActionListener(e -> hapusDariKeranjang());
        
        JButton btnCheckout = new JButton("Checkout");
        btnCheckout.addActionListener(e -> checkout());
        
        JButton btnKosongkan = new JButton("Kosongkan Keranjang");
        btnKosongkan.addActionListener(e -> kosongkanKeranjang());
        
        buttonPanel.add(btnTambahKeranjang);
        buttonPanel.add(btnHapusKeranjang);
        buttonPanel.add(btnCheckout);
        buttonPanel.add(btnKosongkan);
        
        keranjangPanel.add(keranjangScrollPane, BorderLayout.CENTER);
        keranjangPanel.add(totalPanel, BorderLayout.NORTH);
        keranjangPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, produkScrollPane, keranjangPanel);
        splitPane.setResizeWeight(0.6);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

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

    private void searchProduk() {
        String keyword = tfSearch.getText().trim();
        if (!keyword.isEmpty()) {
            produkTableModel.setRowCount(0);
            List<Produk> produkList = produkDAO.searchProduk(keyword);
            
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
        } else {
            loadProduk();
        }
    }

    private void tambahKeKeranjang() {
        int selectedRow = produkTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih produk terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idProduk = (String) produkTableModel.getValueAt(selectedRow, 0);
        Produk produk = produkDAO.getProdukById(idProduk);
        
        String input = JOptionPane.showInputDialog(this, "Masukkan jumlah untuk " + produk.getNamaProduk() + ":");
        if (input == null) return;
        
        try {
            int jumlah = Integer.parseInt(input);
            if (jumlah <= 0) {
                throw new NumberFormatException();
            }
            
            if (jumlah > produk.getStok()) {
                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Stok tersedia: " + produk.getStok(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            keranjang.tambahItem(produk, jumlah);
            updateKeranjangTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusDariKeranjang() {
        int selectedRow = keranjangTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih item keranjang terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String namaProduk = (String) keranjangTableModel.getValueAt(selectedRow, 0);
        keranjang.hapusItem(getProdukIdFromName(namaProduk));
        updateKeranjangTable();
    }

    private String getProdukIdFromName(String namaProduk) {
        for (ItemKeranjang item : keranjang.getItems()) {
            if (item.getProduk().getNamaProduk().equals(namaProduk)) {
                return item.getProduk().getIdProduk();
            }
        }
        return "";
    }

    private void kosongkanKeranjang() {
        int confirm = JOptionPane.showConfirmDialog(this, "Kosongkan keranjang?", "Konfirmasi", 
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            keranjang.kosongkanKeranjang();
            updateKeranjangTable();
        }
    }

    private void updateKeranjangTable() {
        keranjangTableModel.setRowCount(0);
        
        for (ItemKeranjang item : keranjang.getItems()) {
            Object[] row = {
                item.getProduk().getNamaProduk(),
                "Rp " + df.format(item.getProduk().getHarga()),
                item.getJumlah(),
                "Rp " + df.format(item.getSubtotal())
            };
            keranjangTableModel.addRow(row);
        }
        
        lblTotalHarga.setText("Rp " + df.format(keranjang.getTotalHarga()));
    }

    private void checkout() {
        if (keranjang.getJumlahItem() == 0) {
            JOptionPane.showMessageDialog(this, "Keranjang kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Total pembelian: Rp " + df.format(keranjang.getTotalHarga()) + "\nLanjutkan checkout?", 
                "Konfirmasi Checkout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Kurangi stok di database
                for (ItemKeranjang item : keranjang.getItems()) {
                    produkDAO.updateStok(item.getProduk().getIdProduk(), item.getJumlah());
                }
                
                // Simpan transaksi
                int idTransaksi = transaksiDAO.createTransaksi(keranjang.getTotalHarga());
                transaksiDAO.createDetailTransaksi(idTransaksi, keranjang.getItems());
                
                JOptionPane.showMessageDialog(this, 
                        "Transaksi berhasil!\nID Transaksi: " + idTransaksi + 
                        "\nTotal: Rp " + df.format(keranjang.getTotalHarga()), 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                keranjang.kosongkanKeranjang();
                updateKeranjangTable();
                loadProduk();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error saat checkout: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainForm().setVisible(true);
        });
    }
}