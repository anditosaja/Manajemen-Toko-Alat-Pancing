// ItemKeranjang.java
package com.tokopancing.model;

public class ItemKeranjang {
    private Produk produk;
    private int jumlah;

    public ItemKeranjang(Produk produk, int jumlah) {
        this.produk = produk;
        this.jumlah = jumlah;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public double getSubtotal() {
        return produk.getHarga() * jumlah;
    }

    public void tambahJumlah(int jumlah) {
        this.jumlah += jumlah;
    }
}