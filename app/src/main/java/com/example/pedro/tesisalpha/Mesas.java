package com.example.pedro.tesisalpha;

/**
 * Created by Pedro on 16/04/2015.
 */
public class Mesas {
    public String id;
    public String idorder;
    public String nombre;
    public String descripcion;
    public Double precio;
    public String status;
    public Mesas(String idord, String identidad, String nom, Double pre, String stat){
        id=identidad;
        nombre=nom;
        idorder=idord;
      //  descripcion=des;
        precio=pre;
        status=stat;
    }
    public String getId(){
        return id;
    }
    public String getIdorder(){
        return idorder;
    }
    public String getStatus(){
        return status;
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

}


