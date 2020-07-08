
package org.diegovelasquez.bean;

/**
 *
 * @author DAV4
 */
public class ContactoUrgencia {
    private int codContactoUrgencia;
    private String nombres;
    private String apellidos;
    private String numContacto;
    private int codPaciente;

    public ContactoUrgencia() {
    }

    public ContactoUrgencia(int codContactoUrgencia, String nombres, String apellidos, String numContacto, int codPaciente) {
        this.codContactoUrgencia = codContactoUrgencia;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.numContacto = numContacto;
        this.codPaciente = codPaciente;
    }

    public int getCodContactoUrgencia() {
        return codContactoUrgencia;
    }

    public void setCodContactoUrgencia(int codContactoUrgencia) {
        this.codContactoUrgencia = codContactoUrgencia;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumContacto() {
        return numContacto;
    }

    public void setNumContacto(String numContacto) {
        this.numContacto = numContacto;
    }

    public int getCodPaciente() {
        return codPaciente;
    }

    public void setCodPaciente(int codPaciente) {
        this.codPaciente = codPaciente;
    }

    @Override
    public String toString() {
        return " " +  codContactoUrgencia;
    }


    
}
