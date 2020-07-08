package org.diegovelasquez.bean;

import java.util.Date;

/**
 *
 * @author DAV4
 */
public class Horarios {
    private int codHorario;
    private Date horarioInicio;
    private Date horarioSalida;
    private int lunes;
    private int martes;
    private int miercoles;
    private int jueves;
    private int viernes;

    public Horarios() {
    }

    public Horarios(int codHorario, Date horarioInicio, Date horarioSalida, int lunes, int martes, int miercoles, int jueves, int viernes) {
        this.codHorario = codHorario;
        this.horarioInicio = horarioInicio;
        this.horarioSalida = horarioSalida;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
    }

    public int getCodHorario() {
        return codHorario;
    }

    public void setCodHorario(int codHorario) {
        this.codHorario = codHorario;
    }

    public Date getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(Date horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public Date getHorarioSalida() {
        return horarioSalida;
    }

    public void setHorarioSalida(Date horarioSalida) {
        this.horarioSalida = horarioSalida;
    }

    public int getLunes() {
        return lunes;
    }

    public void setLunes(int lunes) {
        this.lunes = lunes;
    }

    public int getMartes() {
        return martes;
    }

    public void setMartes(int martes) {
        this.martes = martes;
    }

    public int getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(int miercoles) {
        this.miercoles = miercoles;
    }

    public int getJueves() {
        return jueves;
    }

    public void setJueves(int jueves) {
        this.jueves = jueves;
    }

    public int getViernes() {
        return viernes;
    }

    public void setViernes(int viernes) {
        this.viernes = viernes;
    }

    @Override
    public String toString() {
        return "" + codHorario;
    }
    
    
    
}
