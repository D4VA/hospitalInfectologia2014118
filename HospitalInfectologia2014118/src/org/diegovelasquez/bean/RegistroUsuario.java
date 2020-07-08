package org.diegovelasquez.bean;

import java.util.Date;

/**
 *
 * @author DAV4
 */
public class RegistroUsuario {
    private int codUsuario;
    private String usuarioLogin;
    private String usuarioContrasena;
    private Boolean usuarioEstado;
    private Date usuarioFecha;
    private String usuarioHora;
    private int codTipoUsuario;

    public RegistroUsuario() {
    }

    public RegistroUsuario(int codUsuario, String usuarioLogin, String usuarioContrasena, Boolean usuarioEstado, Date usuarioFecha, String usuarioHora, int codTipoUsuario) {
        this.codUsuario = codUsuario;
        this.usuarioLogin = usuarioLogin;
        this.usuarioContrasena = usuarioContrasena;
        this.usuarioEstado = usuarioEstado;
        this.usuarioFecha = usuarioFecha;
        this.usuarioHora = usuarioHora;
        this.codTipoUsuario = codTipoUsuario;
    }

    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(String usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public String getUsuarioContrasena() {
        return usuarioContrasena;
    }

    public void setUsuarioContrasena(String usuarioContrasena) {
        this.usuarioContrasena = usuarioContrasena;
    }

    public Boolean getUsuarioEstado() {
        return usuarioEstado;
    }

    public void setUsuarioEstado(Boolean usuarioEstado) {
        this.usuarioEstado = usuarioEstado;
    }

    public Date getUsuarioFecha() {
        return usuarioFecha;
    }

    public void setUsuarioFecha(Date usuarioFecha) {
        this.usuarioFecha = usuarioFecha;
    }

    public String getUsuarioHora() {
        return usuarioHora;
    }

    public void setUsuarioHora(String usuarioHora) {
        this.usuarioHora = usuarioHora;
    }

    public int getCodTipoUsuario() {
        return codTipoUsuario;
    }

    public void setCodTipoUsuario(int codTipoUsuario) {
        this.codTipoUsuario = codTipoUsuario;
    }

    @Override
    public String toString() {
        return "" + codUsuario;
    }

    
}