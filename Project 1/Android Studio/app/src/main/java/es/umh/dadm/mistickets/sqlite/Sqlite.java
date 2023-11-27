package es.umh.dadm.mistickets.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import es.umh.dadm.mistickets.Objetos.Tickets;

public class Sqlite extends SQLiteOpenHelper {

    Cursor cursor;
    SQLiteDatabase db;
    String[] campos = new String[]{"_id","NombreTicket", "idCategoria", "PrecioTicket", "FechaTicket", "DescripcionCorta", "DescripcionLarga", "ImagenTicket"};

    //Sentencia SQL para crear la tabla de Peliculas
    private static final String sqlCreate = "CREATE TABLE Tickets (_id integer primary key autoincrement, NombreTicket text not null, idCategoria integer not null, PrecioTicket integer, FechaTicket text, " +
            "DescripcionCorta text, DescripcionLarga text, ImagenTicket blob)";

    //Nombre de la BD
    private static final String nombreBD = "dbTickets";

    public Sqlite(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,nombreBD ,factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Tickets");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }


    /*
    * OBTENER TICKETS
    * */
    /* Método para obtener todos los tickets de la bd */
    public ArrayList<Tickets> ObtenerTicket()
    {
        //Abrimos la base de datos 'bdpeliculas' en modo escritura
        db = getWritableDatabase();

        cursor = db.query("Tickets", campos, "", null, null, null, null);

        ArrayList<Tickets> arrayTickets = new ArrayList<>();
        Tickets tickets;

        if (cursor.moveToFirst())
        {
            do{
                tickets = obtenerValores();

                arrayTickets.add(tickets);

            }while(cursor.moveToNext());
        }

        return arrayTickets;
    }

    /*
     * Método para obtener valores del cursor y devolver un objeto Pelicula
     * */
    private Tickets obtenerValores(){

        Tickets tickets = new Tickets();
        tickets.setIdTicket(cursor.getInt(0));
        tickets.setNombreTicket(cursor.getString(1));
        tickets.setidCategoria(cursor.getInt(2));
        tickets.setPrecioTicket(cursor.getInt(3));
        tickets.setFechaTicket(cursor.getString(4));
        tickets.setDescripcionCorta(cursor.getString(5));
        tickets.setDescripcionLarga(cursor.getString(6));
        tickets.setImagenTicket(getImage(cursor.getBlob(7)));

        return tickets;
    }

    //Para convertir una byte array en un bitmap
    public static Bitmap getImage (byte[] data)
    {
        return BitmapFactory.decodeByteArray(data,0, data.length);
    }



    //Metodo que crea los tickets
    public void crearTicket(Tickets tickets)
    {
        ContentValues nuevoRegistro = asignarValores(tickets);

        db = getWritableDatabase();
        db.insert("Tickets", null, nuevoRegistro);
    }

    /*
     * Método para asignar valores al registro a actualizar o a insertar
     * */
    private ContentValues asignarValores(Tickets tickets){
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("NombreTicket", tickets.getNombreTicket());
        nuevoRegistro.put("idCategoria", tickets.getidCategoria());
        nuevoRegistro.put("PrecioTicket", tickets.getPrecioTicket());
        nuevoRegistro.put("FechaTicket", tickets.getFechaTicket());
        nuevoRegistro.put("DescripcionCorta", tickets.getDescripcionCorta());
        nuevoRegistro.put("DescripcionLarga", tickets.getDescripcionLarga());
        nuevoRegistro.put("ImagenTicket", getBytes(tickets.getImagenTicket()));
        return nuevoRegistro;
    }
    //Metodo para pasar de bitmap a byte array
    public static byte[] getBytes(Bitmap bm)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }


    /*
    * Metodo para actualizar los tickets
    * */
    public void actualizarTickets(Tickets tickets)
    {
        db = getWritableDatabase();

        ContentValues updateRegistro = actualizarValores(tickets);

        int aux = tickets.getIdTicket();
        String idTicket = String.valueOf(aux);
        db.update("Tickets", updateRegistro, "_id ="+idTicket, null);

    }

    /*
    * Metodo para actualizar los valores de un ticket
    * */
    private ContentValues actualizarValores(Tickets tickets)
    {


        ContentValues updateRegistro = new ContentValues();
        updateRegistro.put("NombreTicket", tickets.getNombreTicket());
        updateRegistro.put("idCategoria", tickets.getidCategoria());
        updateRegistro.put("PrecioTicket", tickets.getPrecioTicket());
        updateRegistro.put("FechaTicket", tickets.getFechaTicket());
        updateRegistro.put("DescripcionCorta", tickets.getDescripcionCorta());
        updateRegistro.put("DescripcionLarga", tickets.getDescripcionLarga());
        updateRegistro.put("ImagenTicket", getBytes(tickets.getImagenTicket()));

        return updateRegistro;
    }



    //Metodo para borrar tickets
    public void borrarTickets(int id)
    {
        db = getWritableDatabase();
        db.execSQL("DELETE FROM Tickets WHERE _id= "+id+"");
    }


    //Metodo para borrar las categorias y actualizar los tickets
    public void borrarCategorias(int idCategoria)
    {
        //Le sumamos 1 a la categoria porque el array empieza en 0 pero el spinner
        //de los tickets empeiza en 1
        idCategoria++;

        //Abrimos la base de datos 'bdpeliculas' en modo escritura
        db = getWritableDatabase();

        //Cogemos todos los tickets
        ArrayList<Tickets> arrayTickets;
        arrayTickets=ObtenerTicket();
        int idTicket;
        //Recorremos todos los tickets
        for (int i = 0 ; i< arrayTickets.size(); i++)
        {
            //Obtenemos el id de cada ticket
            idTicket = arrayTickets.get(i).getIdTicket();

            //si la categoria del ticket que estamos buscando es la misma que la
            //que estamos borrando redirigimos los tickets a la categoria por defecto
            if(arrayTickets.get(i).getidCategoria() == idCategoria)
            {
                db.execSQL("UPDATE Tickets SET idCategoria="+0+" WHERE _id="+idTicket);
            }
            else if(arrayTickets.get(i).getidCategoria() > idCategoria)
            {
                //si el idCategoria del ticket es mayor tenemos que restarle 1 su id
                //porque al haber eliminado 1 todos los que esten por arriban deben eroganizarse
                //ya que en el array se elimina un elemento
                db.execSQL("UPDATE Tickets SET idCategoria="+((arrayTickets.get(i).getidCategoria()-1))+" WHERE _id="+idTicket);
            }
        }
    }





















}
