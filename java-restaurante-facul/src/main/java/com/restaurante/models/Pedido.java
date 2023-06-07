package com.restaurante.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Mesa mesa;

    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER)
    private List<Prato> prato;

    private double VlrTot;

    public Pedido(Mesa mesa) {
        this.mesa = mesa;
        this.prato = new ArrayList<>();
        this.VlrTot = 0;
    }

    public Pedido() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public List<Prato> getPrato() {
        return prato;
    }

    public void addPrato(Prato prato) {
        this.prato.add(prato);
    }

    public double getVlrTot() {
        return VlrTot;
    }

    public void setVlrTot(double vlrTot) {
        VlrTot = vlrTot;
    }

}
