package es.umh.dadm.mistickets.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.umh.dadm.mistickets.Objetos.Categorias;
import es.umh.dadm.mistickets.R;

public class AdapterCategorias extends BaseAdapter {

    private Context contexto;
    private ArrayList<Categorias> arrayCategorias;


    public AdapterCategorias (Context context, ArrayList<Categorias>  array)
    {
        super();
        contexto=context; //Asignamos el contexto para poder acceder a la actividad
        arrayCategorias= array; //Establecemos el origen de datos

    }

    @Override
    public int getCount() {
        // devuelve el número de elementos del origen de datos para saber cuantas veces llama a getView
        return arrayCategorias.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.plantilla_categorias, null);

        //Cogemos la referencia de el texto de la categoria y de la imagen
        TextView textViewCAT = (TextView) view.findViewById(R.id.textViewCat);
        ImageView imagenCat = (ImageView) view.findViewById(R.id.imageCat);

        //Las rellenamos con sus respectivos datos
        textViewCAT.setText(arrayCategorias.get(position).getNombreCategoria());
        imagenCat.setImageBitmap(arrayCategorias.get(position).getImagenCategoria());

        return view;
    }
}
