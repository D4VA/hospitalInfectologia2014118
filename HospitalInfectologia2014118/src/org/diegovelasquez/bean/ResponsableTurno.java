package org.diegovelasquez.bean;

/**
 *
 * @author Dievelas
 */
public class ResponsableTurno {
    private int codResponsableTurno;
    private String nomResponsable;
    private String apellidosResponsable;
    private String telefonoPersonal;
    private int codArea;
    private int codCargo;

    public ResponsableTurno() {
    }

    public ResponsableTurno(int codResponsableTurno, String nomResponsable, String apellidosResponsable, String telefonoPersonal, int codArea, int codCargo) {
        this.codResponsableTurno = codResponsableTurno;
        this.nomResponsable = nomResponsable;
        this.apellidosResponsable = apellidosResponsable;
        this.telefonoPersonal = telefonoPersonal;
        this.codArea = codArea;
        this.codCargo = codCargo;
    }

    public int getCodResponsableTurno() {
        return codResponsableTurno;
    }

    public void setCodResponsableTurno(int codResponsableTurno) {
        this.codResponsableTurno = codResponsableTurno;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public String getApellidosResponsable() {
        return apellidosResponsable;
    }

    public void setApellidosResponsable(String apellidosResponsable) {
        this.apellidosResponsable = apellidosResponsable;
    }

    public String getTelefonoPersonal() {
        return telefonoPersonal;
    }

    public void setTelefonoPersonal(String telefonoPersonal) {
        this.telefonoPersonal = telefonoPersonal;
    }

    public int getCodArea() {
        return codArea;
    }

    public void setCodArea(int codArea) {
        this.codArea = codArea;
    }

    public int getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(int codCargo) {
        this.codCargo = codCargo;
    }

    @Override
    public String toString() {
        return "" + codResponsableTurno;
    }

    

    
}
