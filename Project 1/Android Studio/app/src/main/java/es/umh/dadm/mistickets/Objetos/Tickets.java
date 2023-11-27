package es.umh.dadm.mistickets.Objetos;

import android.graphics.Bitmap;

public class Tickets{

    private String NombreTicket;
    private int idCategoria;
    private int PrecioTicket;
    private String FechaTicket;
    private String DescripcionCorta;
    private String DescripcionLarga;
    private Bitmap ImagenTicket;
    private int idTicket;



    //GETTERS Y SETTERS

    public String getNombreTicket() {
        return NombreTicket;
    }

    public void setNombreTicket(String nombreTicket) {
        NombreTicket = nombreTicket;
    }

    public int getidCategoria() { return idCategoria;}

    public void setidCategoria(int idTipoTicket) { this.idCategoria = idTipoTicket; }

    public int getPrecioTicket() {
        return PrecioTicket;
    }

    public void setPrecioTicket(int precioTicket) {
        PrecioTicket = precioTicket;
    }

    public String getFechaTicket() {
        return FechaTicket;
    }

    public void setFechaTicket(String fechaTicket) {
        FechaTicket = fechaTicket;
    }

    public String getDescripcionCorta() {
        return DescripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) { DescripcionCorta = descripcionCorta; }

    public String getDescripcionLarga() {
        return DescripcionLarga;
    }

    public void setDescripcionLarga(String descripcionLarga) { DescripcionLarga = descripcionLarga; }

    public Bitmap getImagenTicket() {
        return ImagenTicket;
    }

    public void setImagenTicket(Bitmap imagenTicket) {
        ImagenTicket = imagenTicket;
    }

    public void setIdTicket(int idTicket) { this.idTicket = idTicket; }

    public int getIdTicket() { return idTicket; }


}
