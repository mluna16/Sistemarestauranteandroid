package com.example.pedro.tesisalpha;

/**
 * Created by Pedro on 22/03/2015.
 */
public class Titular
{
    public String titulo;
    public String subtitulo;
    public Boolean ocupado;

    public Titular(String tit, String sub, Boolean bool){
        titulo = tit;
        subtitulo = sub;
        ocupado = bool;
    }

    public String getTitulo(){
        return titulo;
    }

    public String getSubtitulo(){
        return subtitulo;
    }
}
