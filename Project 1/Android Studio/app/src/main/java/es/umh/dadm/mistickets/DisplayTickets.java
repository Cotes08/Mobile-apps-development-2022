package es.umh.dadm.mistickets;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import es.umh.dadm.mistickets.Adaptadores.AdapterTickets;
import es.umh.dadm.mistickets.Objetos.Tickets;
import es.umh.dadm.mistickets.sqlite.Sqlite;

public class DisplayTickets extends AppCompatActivity {

    private ListView lvTickets;           //Control lista de películas
    private AdapterTickets adapterTickets;  //Adaptador de datos para películas
    private Context context;    //Contexto de la actividad

    //Para la base de datos
    Sqlite usdb = new Sqlite(this, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tickets);
        //Back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Creamos el Boton
        FloatingActionButton btnAddTicket= (FloatingActionButton) findViewById(R.id.floatingActionButtonTicket);

        //setOnClick que hace que el botn nos lleve a la actividad
        btnAddTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llamada con el intent a otra actividad
                Intent intentAddTick = new Intent(DisplayTickets.this, es.umh.dadm.mistickets.AnyadirTicket.class);
                startActivity(intentAddTick);
            }
        });

        //Creamos el arrayTickets y llamamos a la funcion ObtenerTicket(Esta funcion nos devuelve ticktes los tickets creados en la clase tickets)
        ArrayList<Tickets> arrayTickets = usdb.ObtenerTicket();
        context=this; //Asignamos el contexto para tenerlo en toda la clase

        //Para garantizar que no se cuelgue
        if (arrayTickets != null) {
            //Funcion que carga el array de tickets en el adapter para que los muestre de forma correcta
            cargarTickets(arrayTickets);
        }
    }

    private void cargarTickets(ArrayList array)
    {
        //Referenciamos el control
        lvTickets=(ListView)findViewById(R.id.listViewTickets);

        registerForContextMenu(lvTickets);

        ArrayList<Tickets> arrayTickets=array;

        // Creamos el adaptador
        adapterTickets = new AdapterTickets(this, arrayTickets);

        // Enlazamos el adaptador de datos al listview
        lvTickets.setAdapter(adapterTickets);

        lvTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intentAdapTicket = new Intent(DisplayTickets.this, es.umh.dadm.mistickets.AnyadirTicket.class);
                //le pasamos por el bundle la posicion en el array del ticket que ha clickado y empezamos la actividad
                Bundle infoTicket = new Bundle();
                infoTicket.putInt("ticketKey", position);
                intentAdapTicket.putExtras(infoTicket);
                startActivity(intentAdapTicket);
            }
        });

        //Si no hay registros mostramos un aviso
        if (arrayTickets.isEmpty())
        {
            Toast.makeText(getBaseContext(), getString(R.string.ToastTick), Toast.LENGTH_LONG).show();
        }

        lvTickets.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //Con una larga pulsacion sobre el ticket sale un menu donde nos dice si queremos eliminarlo o no
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayTickets.this);


                builder.setMessage("¿Deseas borrar este ticket?");
                builder.setTitle("Borrar Ticket");

                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Si decidimos eleiminarlo cogemos el id del ticket seleccionado y
                        //le decimos a la base de datos que queremos eliminarlo

                        int id = arrayTickets.get(position).getIdTicket();
                        usdb.borrarTickets(id);

                        //Recargamos la actividad para ver lo cambios
                        Intent refresh = new Intent(DisplayTickets.this, DisplayTickets.class);
                        startActivity(refresh);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

    }


}