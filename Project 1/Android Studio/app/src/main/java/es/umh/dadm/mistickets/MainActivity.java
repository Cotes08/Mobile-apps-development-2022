package es.umh.dadm.mistickets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creamos el boton que nos lleva a los ticket
        Button btnTicket = (Button) findViewById(R.id.btnTickets);

        //La funcion setOnClick que nos enviara a la nueva actividad
        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llamada con el intent a otra actividad
                Intent intentTicket = new Intent(MainActivity.this, DisplayTickets.class);

                startActivity(intentTicket);
            }
        });

        //Creamos el boton que nos lleva a las categorias
        Button btnCategoria = (Button) findViewById(R.id.btnCategoria);

        //La funcion setOnClick que nos enviara a la nueva actividad
        btnCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llamada con el intent a otra actividad
                Intent intentCategoria = new Intent(MainActivity.this, DisplayCategorias.class);

                startActivity(intentCategoria);
            }
        });
    }
}