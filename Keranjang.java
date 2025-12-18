// Keranjang.java
package com.tokopancing.model;

import java.util.ArrayList;
import java.util.List;

public class Keranjang {
    private List<ItemKeranjang> items;
    private double totalHarga;

    public Keranjang() {
        this.items = new ArrayList<>();
        this.totalHarga = 0;
    }

    public void tambahItem(Produk produk, int jumlah) {
        for (ItemKeranjang item : items) {
            if (item.getProduk().getIdProduk().equals(produk.getIdProduk())) {
                item.tambahJumlah(jumlah);
                hitungTotalHarga();
                return;
            }
        }
        items.add(new ItemKeranjang(produk, jumlah));
        hitungTotalHarga();
    }

    public void hapusItem(String idProduk) {
        items.removeIf(item -> item.getProduk().getIdProduk().equals(idProduk));
        hitungTotalHarga();
    }

    public void updateJumlah(String idProduk, int jumlahBaru) {
        for (ItemKeranjang item : items) {
            if (item.getProduk().getIdProduk().equals(idProduk)) {
                item.setJumlah(jumlahBaru);
                hitungTotalHarga();
                return;
            }
        }
    }

    private void hitungTotalHarga() {
        totalHarga = 0;
        for (ItemKeranjang item : items) {
            totalHarga += item.getSubtotal();
        }
    }

    public void kosongkanKeranjang() {
        items.clear();
        totalHarga = 0;
    }

    public List<ItemKeranjang> getItems() {
        return items;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public int getJumlahItem() {
        return items.size();
    }
}