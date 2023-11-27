package es.umh.dadm.mistickets;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import es.umh.dadm.mistickets.Objetos.Categorias;
import es.umh.dadm.mistickets.Objetos.Tickets;
import es.umh.dadm.mistickets.sqlite.Sqlite;


public class AnyadirTicket extends AppCompatActivity {

    //Variables necesarias para la gestion de las fotos y camaras
    private static final int CAPTURAR_IMAGEN = 1;
    private static final int SELECCIONAR_IMAGEN_GALERIA = 3;
    private static final String CAT_DEFECTO = "Categoria por defecto";

    //Declaramos las variables de los contenedores de info fuera
    private EditText ed1;
    private Spinner spn2;
    private EditText ed3;
    private EditText ed4;
    private EditText ed5;
    private EditText ed6;
    private ImageView iv1;

    //Variable position del ticket en cuestion
    private int position;

    //PARA LA FECHA
    //Para coger dia, mes y hora.
    Calendar calendar =Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    //El listener para que salga para seleccionar la decha
    DatePickerDialog.OnDateSetListener setListener;

    //Para que todos tengan el array
    ArrayList<Tickets> arrayTickets;
    ArrayList<Categorias> arrayCategorias = new ArrayList<Categorias>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_ticket);

        //Back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Boton para confirmar los datos
        Button btnTicket = findViewById(R.id.buttonAddTicket);

        //Boton para acceder a la camara
        Button btnFoto = findViewById(R.id.TomarFotoTicket);

        //Llamamos al metodo para que lea las categorias y las cargue en el array
        cargarCategorias();

        //Para la base de datos
        Sqlite usdb = new Sqlite(this, null, 1);

        //Referenciamos los EditText, spiners, imagenes.....
        ed1 = findViewById(R.id.TextTicket);
        spn2 = findViewById(R.id.CategTicket);
        ed3 = findViewById(R.id.PrecioTicket);
        ed4 = findViewById(R.id.FechaTicket);
        ed5 = findViewById(R.id.TicketDescCorta);
        ed6 = findViewById(R.id.TicketDescLarga);
        iv1 = findViewById(R.id.ImagenTicket);

       //Cremos uin arraylist de strings
       ArrayList item = new ArrayList<String>();

       //En la posición 0 ponemos la categoria por defecto
       item.add(CAT_DEFECTO);

       //Para que siempre este por defecto elegida la categoria por defecto
       spn2.setSelection(0);

       //Lo llenamos con los nombres de las categorias
       for (int i=0; i<arrayCategorias.size(); i++)
       {
           item.add(arrayCategorias.get(i).getNombreCategoria());
       }


        //Creamos el array adapter que tendra las categorias
        ArrayAdapter <String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, item);
        //Mandamos el adapatdor al spinner
        spn2.setAdapter(adapter);

        //Cogemos la informacion de si ha hecho click a algun ticket
        Bundle infoTicket = getIntent().getExtras();


        //Si ha hecho click a algun tiquet ponemos la informacion para modificarla
        if (infoTicket != null)
        {
            //Cogemos la posicion del ticket que ha clickado y por ende la posicion en el array
            //del ticket que queremos actualizar
            position = infoTicket.getInt("ticketKey");
            arrayTickets = usdb.ObtenerTicket();
            ed1.setText(arrayTickets.get(position).getNombreTicket());
            spn2.setSelection(arrayTickets.get(position).getidCategoria());
            ed3.setText(String.valueOf(arrayTickets.get(position).getPrecioTicket()));
            ed4.setText(arrayTickets.get(position).getFechaTicket());
            ed5.setText(arrayTickets.get(position).getDescripcionCorta());
            ed6.setText(arrayTickets.get(position).getDescripcionLarga());
            iv1.setImageBitmap(arrayTickets.get(position).getImagenTicket());
        }

        //Listener del boton de añadir
        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int precioTicket;

                //Los transformamos en el formato que queramos
                String nomTicket = ed1.getText().toString();
                int idCategoria = spn2.getSelectedItemPosition();
                String aux = ed3.getText().toString();
                //Try catch para que no de error al hacer el parseint por si esta null el campo
               try {
                   precioTicket = Integer.parseInt(aux);
               }catch (Exception e)
               {
                  precioTicket=0;
               }
                String fechaTicket = ed4.getText().toString();
                String descCorta = ed5.getText().toString();
                String descLarga = ed6.getText().toString();
                Bitmap imagenTicket = ((BitmapDrawable) iv1.getDrawable()).getBitmap();

               //Para diferenciar si es crear ticket o actualizar
                if (infoTicket == null)
                {
                    Tickets ticketsCrear = new Tickets();
                    ticketsCrear.setNombreTicket(nomTicket);
                    ticketsCrear.setidCategoria(idCategoria);
                    ticketsCrear.setPrecioTicket(precioTicket);
                    ticketsCrear.setFechaTicket(fechaTicket);
                    ticketsCrear.setDescripcionCorta(descCorta);
                    ticketsCrear.setDescripcionLarga(descLarga);
                    ticketsCrear.setImagenTicket(imagenTicket);
                    usdb.crearTicket(ticketsCrear);
                }
                else
                {
                    Tickets ticketsActualizar = new Tickets();
                    ticketsActualizar.setIdTicket(arrayTickets.get(position).getIdTicket());
                    ticketsActualizar.setNombreTicket(nomTicket);
                    ticketsActualizar.setidCategoria(idCategoria);
                    ticketsActualizar.setPrecioTicket(precioTicket);
                    ticketsActualizar.setFechaTicket(fechaTicket);
                    ticketsActualizar.setDescripcionCorta(descCorta);
                    ticketsActualizar.setDescripcionLarga(descLarga);
                    ticketsActualizar.setImagenTicket(imagenTicket);
                    usdb.actualizarTickets(ticketsActualizar);

                }

                Intent intentInfoTicket = new Intent(AnyadirTicket.this, DisplayTickets.class);
                startActivity(intentInfoTicket);

            }
        });


        /*
        * PARA LA FECHA
        * */

        //Referenciamos el Boton
        Button btnFecha = findViewById(R.id.buttonAddFecha);

        //Hacemos que al pulsar ese boton se nos habra una ventana para poner la fecha
        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AnyadirTicket.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener,year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        //Cuando la ventana esta abierta se ejecutara esto
        //lo que hace es seleccionar los parametros, pasarlos a string y ponerlos en el edit text
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                ed4.setText(date);
            }
        };


        /*
        * IMAGENES
        * */

        //Boton para seleccionar la imagen
        ImageButton imageButtonTicket = (ImageButton) findViewById(R.id.ImagenTicket);

        //Listener de la imagen
        imageButtonTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentimg= new Intent();
                intentimg.setType("image/*");
                intentimg.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentimg, "Seleccionar imagen"), SELECCIONAR_IMAGEN_GALERIA);
            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentbtn = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentbtn,CAPTURAR_IMAGEN);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECCIONAR_IMAGEN_GALERIA){
            if (resultCode == RESULT_OK) {

                Uri uri = data.getData();
                try {
                    iv1.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv1.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
                } catch (IOException e) {
                    Toast.makeText(this, R.string.ErrorImagen, Toast.LENGTH_LONG).show();
                }

            } else if (resultCode == RESULT_CANCELED) {
                //Toast.makeText(this, "No OK", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == CAPTURAR_IMAGEN) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                iv1.setScaleType(ImageView.ScaleType.FIT_XY);
                iv1.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                //Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
            }
        }
    }



    // Etiqueta para los Logs
    private static final String TAG = "Info";

    // Nombre del fichero privado
    private static final String FICHERO = "categorias.txt";
    //Metodo para cargar las categorias del fichero
    private void cargarCategorias(){

        String line;

        if (!puedoLeerMemoriaExterna()){
            Toast.makeText(this, R.string.ficheroNoDispinible, Toast.LENGTH_LONG).show();
            return;
        }
        File raiz = getExternalFilesDir(null);
        File fichero = new File(raiz, FICHERO);
        Log.d(TAG, "cargarCategorias: la raiz es "+raiz.getAbsolutePath());

        // Abrir el fichero para lectura
        FileReader fr = null;
        try {
            fr = new FileReader(fichero);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "cargarCategorias: FileNotFound");
            return;
        }
        try{
            BufferedReader bufferedReader = new BufferedReader(fr);
            while((line= bufferedReader.readLine())!= null)
            {
                Categorias cat = new Categorias();
                cat.setNombreCategoria(line);
                cat.setDescripcionCorta(bufferedReader.readLine());
                cat.setDescripcionLarga(bufferedReader.readLine());
                cat.setDetallesCategoria(bufferedReader.readLine());
                arrayCategorias.add(cat);
            }
        } catch(IOException e){
            Log.e(TAG, "cargarCategorias: IOException");
        }
    }

    private boolean puedoLeerMemoriaExterna(){
        String state = Environment.getExternalStorageState();
        return (state.equals(Environment.MEDIA_MOUNTED)
                || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)? true: false);
    }



}