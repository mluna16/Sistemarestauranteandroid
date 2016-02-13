package com.example.pedro.tesisalpha;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class Fragment_Detalle extends Fragment {
	/**
	 * El argumento que representa el ID del elemento selecionado en la lista y que a este fragmento le llega para cargar el contenido apropiado
	 */
	public static final String ARG_ID_ENTRADA_SELECIONADA = "item_id";

	/**
	 * El contenido que cargaremos en este fragmento
	 */
	private Lista_contenido.Lista_entrada mItem;

	
	/**
	 * Es obligatorio un contructor vac�o para instanciar el fragmento
	 */
	public Fragment_Detalle() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ID_ENTRADA_SELECIONADA)) {
			//Cargamos el contenido de la entrada con cierto ID seleccionado en la lista. Se recomiendo usar un Loader para cargar el contenido
			mItem = Lista_contenido.ENTRADAS_LISTA_HASHMAP.get(getArguments().getString(ARG_ID_ENTRADA_SELECIONADA));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_fragment_detalle, container, false);

		//Mostramos el contenido al usuario
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.textView_superior)).setText(mItem.mesa);
			((TextView) rootView.findViewById(R.id.textView_inferior)).setText(mItem.pedido);
			//((ImageView) rootView.findViewById(R.id.imageView_imagen)).setImageResource(mItem.idImagen);
		}

		return rootView;
	}
	
}
