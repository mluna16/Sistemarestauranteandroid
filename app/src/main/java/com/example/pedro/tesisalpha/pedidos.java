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
    public pedidos(String identidad,String nom, Integer lim, String img, String des){
        id=identidad;
        nombre=nom;
        numeromesa=lim;
        mesonero=img;
        status=des;


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
}


