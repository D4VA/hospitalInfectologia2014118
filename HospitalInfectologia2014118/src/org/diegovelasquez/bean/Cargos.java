package org.diegovelasquez.bean;

/**
 *
 * @author DAV4
 */
public class Cargos {
    private int codCargo;
    private String nomCargo;

    public Cargos() {
    }

    public Cargos(int codCargo, String nomCargo) {
        this.codCargo = codCargo;
        this.nomCargo = nomCargo;
    }

    public int getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(int codCargo) {
        this.codCargo = codCargo;
    }

    public String getNomCargo() {
        return nomCargo;
    }

    public void setNomCargo(String nomCargo) {
        this.nomCargo = nomCargo;
    }

    @Override
    public String toString() {
        return "" +  codCargo;
    }
    
}
