package org.diegovelasquez.bean;

/**
 *
 * @author DAV4
 */
public class Estado {
    private boolean usuarioEstado;

    public Estado() {
    }

    public Estado(boolean usuarioEstado) {
        this.usuarioEstado = usuarioEstado;
    }

    public boolean isUsuarioEstado() {
        return usuarioEstado;
    }

    public void setUsuarioEstado(boolean usuarioEstado) {
        this.usuarioEstado = usuarioEstado;
    }

    @Override
    public String toString() {
        return "" + usuarioEstado;
    }
    
    
}
