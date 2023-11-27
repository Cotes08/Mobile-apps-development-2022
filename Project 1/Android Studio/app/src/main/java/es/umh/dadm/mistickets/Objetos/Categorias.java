package es.umh.dadm.mistickets.Objetos;
import android.graphics.Bitmap;

import java.io.Serializable;

public class Categorias implements Serializable {

    //Definiciones
    private String NombreCategoria;
    private String DescripcionCorta;
    private String DescripcionLarga;
    private String DetallesCategoria;
    private Bitmap ImagenCategoria;
    private String categoriacustom;

    //Getters y setters
    public Bitmap getImagenCategoria() {
        return ImagenCategoria;
    }

    public void setImagenCategoria(Bitmap imagenCategoria) {
        ImagenCategoria = imagenCategoria;
    }

    public String getNombreCategoria() {
        return NombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        NombreCategoria = nombreCategoria;
    }

    public String getDescripcionCorta() {
        return DescripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) { DescripcionCorta = descripcionCorta; }

    public String getDescripcionLarga() {
        return DescripcionLarga;
    }

    public void setDescripcionLarga(String descripcionLarga) { DescripcionLarga = descripcionLarga; }

    public String getDetallesCategoria() {
        return DetallesCategoria;
    }

    public void setDetallesCategoria(String detallesCategoria) { DetallesCategoria = detallesCategoria;}



    //Metodo para poner bien el nombre de las categorias en el spinner
    @Override
    public String toString()
    {
        categoriacustom= NombreCategoria;
        return categoriacustom;
    }

}
