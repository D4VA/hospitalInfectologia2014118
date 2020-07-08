
package org.diegovelasquez.bean;

/**
 *
 * @author Dievelas
 */
public class Especialidades {
    private int codEspecialidad;
    private String nomEspecialidad;

    public Especialidades() {
    }

    public Especialidades(int codEspecialidad, String nomEspecialidad) {
        this.codEspecialidad = codEspecialidad;
        this.nomEspecialidad = nomEspecialidad;
    }

    public int getCodEspecialidad() {
        return codEspecialidad;
    }

    public void setCodEspecialidad(int codEspecialidad) {
        this.codEspecialidad = codEspecialidad;
    }

    public String getNomEspecialidad() {
        return nomEspecialidad;
    }

    public void setNomEspecialidad(String nomEspecialidad) {
        this.nomEspecialidad = nomEspecialidad;
    }

    @Override
    public String toString() {
        return "" + codEspecialidad ;
    }
    
    
    
}
