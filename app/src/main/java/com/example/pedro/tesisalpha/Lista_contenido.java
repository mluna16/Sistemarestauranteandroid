package com.example.pedro.tesisalpha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.pedro.tesisalpha.Activity_Principal.*;


public class Lista_contenido {

	/** 
	 * Donde se guardan las entradas de la lista.
	 */
	public static ArrayList<Lista_entrada> ENTRADAS_LISTA = new ArrayList<Lista_entrada>();
	
	/** 
	 * Donde se asigna el identificador a cada entrada de la lista
	 */
	public static Map<String, Lista_entrada> ENTRADAS_LISTA_HASHMAP = new HashMap<String, Lista_entrada>();


	static {


/*
		aniadirEntrada(new Lista_entrada("0", "BUHO", "B�ho es el nombre com�n de aves de la familia Strigidae, del orden de las estrigiformes o aves rapaces nocturnas. Habitualmente designa especies que, a diferencia de las lechuzas, tienen plumas alzadas que parecen orejas."));
		aniadirEntrada(new Lista_entrada("1", "COLIBR�", "Los troquilinos (Trochilinae) son una subfamilia de aves apodiformes de la familia Trochilidae, conocidas vulgarmente como colibr�es, quindes, tucusitos, picaflores, chupamirtos, chuparrosas, huichichiquis (idioma nahuatl), mainumby (idioma guaran�) o guanumby. Conjuntamente con las ermitas, que pertenecen a la subfamilia Phaethornithinae, conforman la familia Trochilidae que, en la sistem�tica de Charles Sibley, se clasifica en un orden propio: Trochiliformes, independiente de los vencejos del orden Apodiformes. La subfamilia Trochilinae incluye m�s de 100 g�neros que comprenden un total de 330 a 340 especies."));
		aniadirEntrada(new Lista_entrada("2", "CUERVO", "El cuervo com�n (Corvus corax) es una especie de ave paseriforme de la familia de los c�rvidos (Corvidae). Presente en todo el hemisferio septentrional, es la especie de c�rvido con la mayor superficie de distribuci�n. Con el cuervo de pico grueso, es el mayor de los c�rvidos y probablemente la paseriforme m�s pesada; en su madurez, el cuervo com�n mide entre 52 y 69 cent�metros de longitud y su peso var�a de 0,69 a 1,7 kilogramos. Los cuervos comunes viven generalmente de 10 a 15 a�os pero algunos individuos han vivido 40 a�os. Los juveniles pueden desplazarse en grupos pero las parejas ya formadas permanecen juntas toda su vida, cada pareja defendiendo un territorio. Existen 8 subespecies conocidas que se diferencian muy poco aparentemente, aunque estudios recientes hayan demostrado diferencias gen�ticas significativas entre las poblaciones de distintas regiones."));
		aniadirEntrada(new Lista_entrada("3", "FLAMENCO", "Los fenicopteriformes (Phoenicopteriformes), los cuales reciben el nombre vulgar de flamencos, son un orden de aves neognatas, con un �nico g�nero viviente: Phoenicopterus. Son aves que se distribuyen tanto por el hemisferio occidental como por el hemisferio oriental: existen cuatro especies en Am�rica y dos en el Viejo Mundo. Tienen cr�neo desmognato holorrino, con 16 a 20 v�rtebras cervicales y pies anisod�ctilos."));
		aniadirEntrada(new Lista_entrada("4", "KIWI", "Los kiwis (Apterix, gr. 'sin alas') son un g�nero de aves paleognatas compuesto por cinco especies end�micas de Nueva Zelanda.1 2 Son aves no voladoras peque�as, aproximadamente del tama�o de una gallina. Antes de la llegada de los humanos alrededor del a�o 1300, en Nueva Zelanda los �nicos mam�feros que hab�a eran murci�lagos, y los nichos ecol�gicos que en otras partes del mundo eran ocupados por animales tan diversos como caballos, lobos y ratones fueron utilizados en Nueva Zelanda por p�jaros (y en menor proporci�n por ciertas especies de reptiles). La denominaci�n kiwi es maor�, idioma del pueblo hom�nimo de linaje malayopolinesio que coloniz� Nueva Zelanda antes de la llegada de los europeos."));
		aniadirEntrada(new Lista_entrada("5", "LORO", "Las Psit�cidas (Psittacidae) son una familia de aves psitaciformes llamadas com�nmente loros o papagayos, e incluye a los guacamayos, las cotorras, los periquitos, los agapornis y formas afines."));
		aniadirEntrada(new Lista_entrada("6", "PAVO", "Pavo es un g�nero de aves galliformes de la familia Phasianidae, que incluye dos especies, el pavo real com�n (Pavo cristatus) y el pavo real cuelliverde (Pavo muticus)."));
*/

        for (int i = 1; i< 5  ; i++) {
            aniadirEntrada(new Lista_entrada(Integer.toString(i), "Mesa "+i, "pedido numero "+i));
        }
	}

	
	/** A�ade una entrada a la lista
	 * @param entrada Elemento que a�adimos a la lista
	 */
	private static void aniadirEntrada(Lista_entrada entrada) {
		ENTRADAS_LISTA.add(entrada);
		ENTRADAS_LISTA_HASHMAP.put(entrada.id, entrada);
	}

	/**
	 * Representa una entrada del contenido de la lista
	 */
	public static class Lista_entrada {
		public String id;
		public String mesa;
		public String pedido;
		  
		public Lista_entrada(String id, String textoEncima, String textoDebajo) {
			this.id = id;
		    this.mesa = textoEncima;
		    this.pedido = textoDebajo;
		}
	}
	
}
