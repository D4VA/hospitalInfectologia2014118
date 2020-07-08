
package org.diegovelasquez.bean;


/**
 *
 * @author DAV4
 */
public class Login {
    private int codTipoUsuario;
    private String descripcion;

    public Login() {
    }

    public Login(int codTipoUsuario, String descripcion) {
        this.codTipoUsuario = codTipoUsuario;
        this.descripcion = descripcion;
    }

    public int getCodTipoUsuario() {
        return codTipoUsuario;
    }

    public void setCodTipoUsuario(int codTipoUsuario) {
        this.codTipoUsuario = codTipoUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "" + descripcion;
    }

    
}
