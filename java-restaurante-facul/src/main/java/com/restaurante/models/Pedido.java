package com.restaurante.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Mesa mesa;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Prato_Pedido", joinColumns = @JoinColumn(name = "pedido_id"), inverseJoinColumns = @JoinColumn(name = "prato_id"))
    private List<Prato> pratos;

    private double VlrTot;
    private boolean finalizado;

    public Pedido(Mesa mesa, List<Prato> pratos, double VlrTot) {
        this.mesa = mesa;
        this.pratos = pratos;
        this.VlrTot = VlrTot;
        this.finalizado = false;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
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
        return pratos;
    }

    public void addPrato(Prato prato) {
        this.pratos.add(prato);
    }

    public double getVlrTot() {
        return VlrTot;
    }

    public void setVlrTot(double vlrTot) {
        VlrTot = vlrTot;
    }

}
