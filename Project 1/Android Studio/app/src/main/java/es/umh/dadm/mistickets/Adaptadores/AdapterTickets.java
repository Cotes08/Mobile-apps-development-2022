package es.umh.dadm.mistickets.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.umh.dadm.mistickets.Objetos.Tickets;
import es.umh.dadm.mistickets.R;

public class AdapterTickets extends BaseAdapter {

    private Context contexto;
    private ArrayList<Tickets> arrayTickets;

    //Constructor par asignar el Contexto y el origen de datos
    public AdapterTickets(Context context, ArrayList<Tickets> array)
    {
        super();
        contexto=context;   //Asignamos el contexto para poder acceder a la actividad
        arrayTickets=array;   //Establecemos el origen de datos
    }

    @Override
    public int getCount() {
        // devuelve el número de elementos del origen de datos para saber cuantas veces llama a getView
        return arrayTickets.size();
    }

    // Este elemento se llama por cada elemento de datasource
    public View getView(int position, View view, ViewGroup parent)
    {

        // Cargamos el layout
        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.plantila_tickets_list, null);

        // Obtenemos la referencia de los elementos del layout
        ImageView imagenTicket=(ImageView)view.findViewById(R.id.imagenTicket);
        TextView textViewNombreTicket=(TextView)view.findViewById(R.id.nombreTicket);
        TextView textViewDescorta=(TextView)view.findViewById(R.id.descrpcionCorta);

        // Asignamos los valores correspondientes
        imagenTicket.setImageBitmap(arrayTickets.get(position).getImagenTicket());
        textViewNombreTicket.setText(arrayTickets.get(position).getNombreTicket());
        textViewDescorta.setText(arrayTickets.get(position).getDescripcionCorta());

        return view;
    }


    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
