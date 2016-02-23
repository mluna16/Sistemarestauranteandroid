package com.example.pedro.tesisalpha;

/**
 * Created by Pedro on 16/04/2015.
 */
public class pedidos {

    public String nombre;
    public String id;
    public String status;
    public Integer numeromesa;
    public String mesonero;
    private boolean isSelected;
    public pedidos(String identidad,String nom, Integer lim, String img, String des,boolean isSelected){
        id=identidad;
        nombre=nom;
        numeromesa=lim;
        mesonero=img;
        status=des;
        this.isSelected = isSelected;

    }
    public String getId(){
        return id;
    }
    public String getMesonero(){
        return mesonero;
    }
    public String getNombre(){
        return nombre;
    }
    public String getStatus(){
        return status;
    }
    public Integer getNumeromesa(){
        return numeromesa;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}


