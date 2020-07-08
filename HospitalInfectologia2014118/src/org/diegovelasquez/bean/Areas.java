package org.diegovelasquez.bean;

/**
 *
 * @author DAV4
 */
public class Areas {
    private int codArea;
    private String nomArea;

    public Areas() {
    }

    public Areas(int codArea, String nomArea) {
        this.codArea = codArea;
        this.nomArea = nomArea;
    }

    public int getCodArea() {
        return codArea;
    }

    public void setCodArea(int codArea) {
        this.codArea = codArea;
    }

    public String getNomArea() {
        return nomArea;
    }

    public void setNomArea(String nomArea) {
        this.nomArea = nomArea;
    }

    @Override
    public String toString() {
        return "" + codArea ;
    }

    
    
}
