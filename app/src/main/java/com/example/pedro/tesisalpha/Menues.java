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
    public Menues(String identidad, String img, String nom, String des, Double pre, Integer lim,boolean isSelected,int cant){
        id=identidad;
        imagen=img;
        nombre=nom;
        descripcion=des;
        precio=pre;
        limite=lim;
        this.cantidad=cant;
        this.isSelected = isSelected;
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

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public void setCantidad(int can) {
        this.cantidad = can;
    }

}


