package com.example.pedro.tesisalpha;

/**
 * Created by Pedro on 16/04/2015.
 */
public class Menues {
    public String id;
    public String nombre;
    public String descripcion;
    public Integer limite;
    public Integer cantidad;
    public Double precio;
    public String imagen;
    private boolean isSelected;
    private boolean isSelected1;
    public Menues(String identidad, String img, String nom, String des, Double pre, Integer lim,boolean isSelected,int cant,boolean isSelected1){
        id=identidad;
        imagen=img;
        nombre=nom;
        descripcion=des;
        precio=pre;
        limite=lim;
        this.cantidad=cant;
        this.isSelected = isSelected;
        this.isSelected1 = isSelected1;
    }
    public String getId(){
        return id;
    }
    public String getImagen(){
        return imagen;
    }
    public String getNombre(){
        return nombre;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public Double getPrecio(){
        return precio;
    }
    public Integer getLimite(){
        return limite;
    }
    public Integer getCantidad(){
        return cantidad;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public boolean isSelected1() {
        return isSelected1;
    }
    public void setSelected1(boolean isSelected) {
        this.isSelected1 = isSelected1;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public void setCantidad(int can) {
        this.cantidad = can;
    }
    public void setLimite(int lim) {
        this.limite = lim;
    }
}


