package es.umh.dadm.mistickets;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import es.umh.dadm.mistickets.Adaptadores.AdapterCategorias;
import es.umh.dadm.mistickets.Objetos.Categorias;
import es.umh.dadm.mistickets.sqlite.Sqlite;

public class DisplayCategorias extends AppCompatActivity {

    private GridView gvCategorias;     //Control lista de Categorias
    private AdapterCategorias adapterCategorias;  //Adaptador de datos para Categorias
    private Context context;    //Contexto de la actividad

    //Para la base de datos
    Sqlite usdb = new Sqlite(this, null, 1);

    //Array de las categorias
    ArrayList<Categorias> arrayCategorias = new ArrayList<Categorias>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_categorias);
        //Back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Creamos el Boton
        FloatingActionButton btnAddCateg= (FloatingActionButton) findViewById(R.id.floatingActionButtonTicket);

        //setOnClick que hace que el boton nos lleve a la actividad
        btnAddCateg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llamada con el intent a otra actividad
                Intent intentAddCateg = new Intent(DisplayCategorias.this, es.umh.dadm.mistickets.AnyadirCategoria.class);
                startActivity(intentAddCateg);
            }
        });

        //Cargamos en el array con lo que hay en el fichero prueba.txt
        cargarCategorias();
        context=this; //Asignamos el contexto para tenerlo en toda la clase

        //Para garantizar que no se cuelgue
        if (arrayCategorias != null) {
            //Funcion que carga el array de tickets en el adapter para que los muestre de forma correcta
            cargarCategorias(arrayCategorias);
        }
    }

    private void cargarCategorias(ArrayList<Categorias> arrayCategorias)
    {
        //Referenciamos el control
        gvCategorias=(GridView)findViewById(R.id.gridViewCategorias);


        // Creamos el adaptador
        adapterCategorias = new AdapterCategorias(this, arrayCategorias);

        // Enlazamos el adaptador de datos al listview
        gvCategorias.setAdapter(adapterCategorias);

        gvCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intentAdapTicket = new Intent(DisplayCategorias.this, es.umh.dadm.mistickets.AnyadirCategoria.class);
                //le pasamos por el bundle la posicion en el array de la categoria que ha clickado y empezamos la actividad
                Bundle infoCategoria = new Bundle();
                infoCategoria.putInt("categoriatKey", position);
                intentAdapTicket.putExtras(infoCategoria);
                startActivity(intentAdapTicket);
            }
        });

        //Si no hay registros mostramos un aviso
        if (arrayCategorias.isEmpty())
        {
            Toast.makeText(getBaseContext(), getString(R.string.ToastCat), Toast.LENGTH_LONG).show();
        }

        gvCategorias.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //Con una larga pulsacion sobre la categoria sale un menu donde nos dice si queremos eliminarlo o no
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayCategorias.this);

                builder.setMessage(R.string.preguntaBorrarCat);
                builder.setTitle(R.string.borrarCat);

                builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Cogemos el nombre de la categoria seleccionada
                        String nombre = arrayCategorias.get(position).getNombreCategoria();

                        //borramos dicho elemento del array de esta actividad
                        arrayCategorias.remove(position);

                        //Con el nombre de la categoria y su ruta eliminamos la imagen
                        //de esta forma cuando se elimine la categoria tambien la imagen
                        //para que luego no se quede la imagen residual
                        File raiz = getExternalFilesDir(null);
                        File fichero = new File(raiz, nombre+".jpg");
                        File fdelete = new File(String.valueOf(fichero));
                        fdelete.delete();

                        //Una vez este borrada la categoria tenemos que actualizar los tickets que
                        //tenian esa categoria en la base de datos
                        usdb.borrarCategorias(position);
                        //Despues de todos los cambios guardamos las categorias
                        guardarCategorias();
                        Intent refresh = new Intent(DisplayCategorias.this, DisplayCategorias.class);
                        startActivity(refresh);
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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


    /*
     * Metodos para gestion de ficheros
     * */

    // Etiqueta para los Logs
    private static final String TAG = "Info";

    // Nombre del fichero privado
    private static final String FICHERO = "categorias.txt";

    //Para cargar en el array lo que hay en el fichero
    public void cargarCategorias(){

        String line;

        if (!puedoLeerMemoriaExterna()){
            Toast.makeText(this, R.string.ficheroNoDispinible, Toast.LENGTH_LONG).show();
            return;
        }
        //Obtenemos
        File raiz = getExternalFilesDir(null);
        File fichero = new File(raiz, FICHERO);

        // Abrir el fichero para lectura
        FileReader fr = null;
        try {
            fr = new FileReader(fichero);
        } catch (FileNotFoundException e) {
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
                //Llamamos a la funcion cargar imagen y le pasamos el nombre de la categoria
                cat.setImagenCategoria(cargarImagen(line));
                arrayCategorias.add(cat);
            }
        } catch(IOException e){
            Log.e(TAG, "cargarCategorias: IOException");
        }
    }

    //Metodo para guardar las categorias en el fichero
    public void guardarCategorias()
    {
        if (!puedoEscribirMemoriaExterna())
        {
            Toast.makeText(getApplicationContext(),R.string.ficheroNoDispinible, Toast.LENGTH_SHORT).show();
            return;
        }

        File raiz = getExternalFilesDir(null);
        File fichero = new File(raiz, FICHERO);

        if (!fichero.exists()){
            try {
                fichero.createNewFile();
            } catch (IOException e1){
                Log.e(TAG, "guardarCategorias: no se puede crear el fichero");
                return;
            } catch (SecurityException e2){
                Log.e(TAG, "guardarCategorias: no se puede crear el fichero");
                return;
            }
        }

        // Abrir el fichero para escritura
        BufferedWriter buf;
        try {
            buf = new BufferedWriter(new FileWriter(fichero.getAbsolutePath()));
        } catch (IOException e) {
            Log.e(TAG, "guardarCategorias: FileNotFound");
            return;
        }

        //Cogemos lo que hay en los arrays y lo escribimos en el fichero
        for(int i = 0; i<arrayCategorias.size(); i++)
        {
            try {
                buf.write(arrayCategorias.get(i).getNombreCategoria());
                buf.write("\n");
                buf.write(arrayCategorias.get(i).getDescripcionCorta());
                buf.write("\n");
                buf.write(arrayCategorias.get(i).getDescripcionLarga());
                buf.write("\n");
                buf.write(arrayCategorias.get(i).getDetallesCategoria());
                buf.write("\n");
                buf.flush();
            }catch(IOException e)
            {
                Log.e(TAG, "guardar texto : IOException");
            }

        }

    }

    private boolean puedoEscribirMemoriaExterna(){
        String state = Environment.getExternalStorageState();
        return (state.equals(Environment.MEDIA_MOUNTED)? true: false);
    }

    private boolean puedoLeerMemoriaExterna(){
        String state = Environment.getExternalStorageState();
        return (state.equals(Environment.MEDIA_MOUNTED)
                || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)? true: false);
    }


    //Funcion para cargar la imagen de memoria a el arry
    private Bitmap cargarImagen(String nombre) throws IOException {
        File raiz = getExternalFilesDir(null);
        File file = new File(raiz, nombre+".jpg");
        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));

        return b;
    }


}