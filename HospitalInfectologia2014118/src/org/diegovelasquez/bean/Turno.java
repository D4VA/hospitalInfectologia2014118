package org.diegovelasquez.bean;

import java.util.Date;

/**
 *
 * @author Dievelas
 */
public class Turno {
    
    private int codTurno;
    private Date fechaTurno;
    private Date fechaCita;
    private int valorCita;
    private int codMedicoEspecialidad;
    private int codResponsableTurno;
    private int codPaciente;

    public Turno() {
    }

    public Turno(int codTurno, Date fechaTurno, Date fechaCita, int valorCita, int codMedicoEspecialidad, int codResponsableTurno, int codPaciente) {
        this.codTurno = codTurno;
        this.fechaTurno = fechaTurno;
        this.fechaCita = fechaCita;
        this.valorCita = valorCita;
        this.codMedicoEspecialidad = codMedicoEspecialidad;
        this.codResponsableTurno = codResponsableTurno;
        this.codPaciente = codPaciente;
    }

    public int getCodTurno() {
        return codTurno;
    }

    public void setCodTurno(int codTurno) {
        this.codTurno = codTurno;
    }

    public Date getFechaTurno() {
        return fechaTurno;
    }

    public void setFechaTurno(Date fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public int getValorCita() {
        return valorCita;
    }

    public void setValorCita(int valorCita) {
        this.valorCita = valorCita;
    }

    public int getCodMedicoEspecialidad() {
        return codMedicoEspecialidad;
    }

    public void setCodMedicoEspecialidad(int codMedicoEspecialidad) {
        this.codMedicoEspecialidad = codMedicoEspecialidad;
    }

    public int getCodResponsableTurno() {
        return codResponsableTurno;
    }

    public void setCodResponsableTurno(int codResponsableTurno) {
        this.codResponsableTurno = codResponsableTurno;
    }

    public int getCodPaciente() {
        return codPaciente;
    }

    public void setCodPaciente(int codPaciente) {
        this.codPaciente = codPaciente;
    }

    @Override
    public String toString() {
        return "" + codTurno;
    }


    
    
}
