// Transaksi.java
package com.tokopancing.model;

import java.util.Date;
import java.util.List;

public class Transaksi {
    private int idTransaksi;
    private Date tanggalTransaksi;
    private double totalHarga;
    private String status;
    private List<ItemKeranjang> items;

    public Transaksi() {}

    public Transaksi(int idTransaksi, Date tanggalTransaksi, double totalHarga, String status) {
        this.idTransaksi = idTransaksi;
        this.tanggalTransaksi = tanggalTransaksi;
        this.totalHarga = totalHarga;
        this.status = status;
    }

    // Getter dan Setter
    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Date getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(Date tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemKeranjang> getItems() {
        return items;
    }

    public void setItems(List<ItemKeranjang> items) {
        this.items = items;
    }
}