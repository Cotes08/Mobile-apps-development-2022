package es.umh.dadm.mistickets;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import es.umh.dadm.mistickets.Objetos.Categorias;

public class AnyadirCategoria extends AppCompatActivity {

    private static final int CAPTURAR_IMAGEN = 1;
    private static final int SELECCIONAR_IMAGEN_GALERIA = 3;

    private EditText ed1;
    private EditText ed2;
    private EditText ed3;
    private EditText ed4;
    private ImageView iv1;

    //Variable position
    private int position;

    //Array de las categorias
    static ArrayList<Categorias> arrayCategorias = new ArrayList<Categorias>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_categoria);
        //Back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Vaciamos el array para que no se llene de categorias repetidas
        arrayCategorias.clear();

        //Una vez vaciado el array lo cargamos con los datos del fichero
        cargarCategorias();

        //Boton para seleccionar la imagen
        ImageButton imageButtonCategoria = (ImageButton) findViewById(R.id.ImagenCategoria);
        //Boton para confirmar los datos
        Button btnCategoria = findViewById(R.id.buttonAddCategoria);
        //Boton para acceder a la camara
        Button  btnFoto = findViewById(R.id.TomarFotoCategoria);

        //Referenciamos los edit text
        ed1 = findViewById(R.id.TextCategoria);
        ed2 = findViewById(R.id.TextCorCategoria);
        ed3 = findViewById(R.id.TextLargCategoria);
        ed4 = findViewById(R.id.textDetalles);
        iv1 = findViewById(R.id.ImagenCategoria);


        //Cogemos la informacion de si ha hecho click a algun ticket
        Bundle infoCategoria = getIntent().getExtras();

        if (infoCategoria != null)
        {
            position = infoCategoria.getInt("categoriatKey");
            ed1.setText(arrayCategorias.get(position).getNombreCategoria());
            ed2.setText(arrayCategorias.get(position).getDescripcionCorta());
            ed3.setText(arrayCategorias.get(position).getDescripcionLarga());
            ed4.setText(arrayCategorias.get(position).getDetallesCategoria());
            iv1.setImageBitmap(arrayCategorias.get(position).getImagenCategoria());

        }

        //Listener del boton
        btnCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Transformamos lo que hay en los  edittext al tipo que queramos
                String nomCat = ed1.getText().toString();
                String descCorta = ed2.getText().toString();
                String descLarga = ed3.getText().toString();
                String ExtraDet = ed4.getText().toString();
                Bitmap imagenCategoria = ((BitmapDrawable) iv1.getDrawable()).getBitmap();

                /*
                    Si el usuario no ha pulsado un ticket se crea
                    En el caso de que lo haya pulsado se actualiza
                    en ambos casos se llama a la funcion guardar texto que lo guardara en un fichero
                 */
                if (infoCategoria == null)
                {
                    //Lo añadimos al array de categorias
                    CrearCategoria(nomCat, descCorta, descLarga, ExtraDet, imagenCategoria);
                    guardarCategorias();
                    //Una vez guardemos las categorias guardamos la imagen tambein
                    try {
                        SaveImage(imagenCategoria, nomCat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ActualizarCategoria(nomCat, descCorta, descLarga, ExtraDet, position, imagenCategoria);
                    guardarCategorias();
                    //Una vez guardemos las categorias actualizamos la imagen
                    try {
                        SaveImage(imagenCategoria, nomCat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //Pasamos de la actividad actual a la de display
                Intent intentInfoCat = new Intent(AnyadirCategoria.this, DisplayCategorias.class);
                startActivity(intentInfoCat);
            }
        });


        /*
         * IMAGENES
         * */

        //Listener de la imagen
        imageButtonCategoria.setOnClickListener(new View.OnClickListener() {
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

    //Funcion encargada de colocar las imagenes en el imageviwq
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
                    Toast.makeText(this, "Se ha producido un error al cargar la imagen", Toast.LENGTH_LONG).show();
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



    /*
    * Metodos para creacion y actualizacion de los arrays
    * */

    //Funcion para crear Categorias, donde se le pasan los datos, se le añaden los datos al objeto y se añaden al array
    public static void CrearCategoria (String nombre, String descripcionC, String descripcionL, String detalles, Bitmap Imagen)
    {
        Categorias categoria = new Categorias();
        categoria.setNombreCategoria(nombre);
        categoria.setDescripcionCorta(descripcionC);
        categoria.setDescripcionLarga(descripcionL);
        categoria.setDetallesCategoria(detalles);
        categoria.setImagenCategoria(Imagen);
        arrayCategorias.add(categoria);
    }

    public static void ActualizarCategoria (String nombre, String descripcionC, String descripcionL, String detalles, int position, Bitmap Imagen)
    {
        arrayCategorias.get(position).setNombreCategoria(nombre);
        arrayCategorias.get(position).setDescripcionCorta(descripcionC);
        arrayCategorias.get(position).setDescripcionLarga(descripcionL);
        arrayCategorias.get(position).setDetallesCategoria(detalles);
        arrayCategorias.get(position).setImagenCategoria(Imagen);
    }


    /*
    * Metodos para gestion de ficheros
    * */

    // Etiqueta para los Logs
    private static final String TAG = "Info";

    // Nombre del fichero privado
    private static final String FICHERO = "categorias.txt";
    public void guardarCategorias()
    {
        if (!puedoEscribirMemoriaExterna())
        {
            Toast.makeText(getApplicationContext(),R.string.ficheroNoDispinible, Toast.LENGTH_SHORT).show();
            return;
        }

        File raiz = getExternalFilesDir(null);
        File fichero = new File(raiz, FICHERO);
        Log.d(TAG, "guardarCategorias: la raiz es "+raiz.getAbsolutePath());

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

        //Cogemos lo que hay en los arrays y lo escribimos
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
                cat.setImagenCategoria(cargarImagen(line));
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

    private boolean puedoEscribirMemoriaExterna(){
        String state = Environment.getExternalStorageState();
        return (state.equals(Environment.MEDIA_MOUNTED)? true: false);
    }

    //Metodo para guardar la imagen en un fichero
    private void SaveImage(Bitmap bitmap, String nombre) throws IOException {
        File raiz = getExternalFilesDir(null);
        File file = new File(raiz, nombre+".jpg");
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();
    }

    //Metodo par cargar la imgaden de un fichero
    private Bitmap cargarImagen(String nombre) throws IOException {
        File raiz = getExternalFilesDir(null);
        File file = new File(raiz, nombre+".jpg");
        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));

        return b;
    }


}