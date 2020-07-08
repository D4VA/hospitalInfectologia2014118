
package org.diegovelasquez.bean;

/**
 *
 * @author Dievelas
 */
public class MedicoEspecialidad {
    private int codMedicoEspecialidad;
    private int codMedico;
    private int codHorario;
    private int codEspecialidad;

    public MedicoEspecialidad() {
    }

    public MedicoEspecialidad(int codMedicoEspecialidad, int codMedico, int codHorario, int codEspecialidad) {
        this.codMedicoEspecialidad = codMedicoEspecialidad;
        this.codMedico = codMedico;
        this.codHorario = codHorario;
        this.codEspecialidad = codEspecialidad;
    }

    public int getCodMedicoEspecialidad() {
        return codMedicoEspecialidad;
    }

    public void setCodMedicoEspecialidad(int codMedicoEspecialidad) {
        this.codMedicoEspecialidad = codMedicoEspecialidad;
    }

    public int getCodMedico() {
        return codMedico;
    }

    public void setCodMedico(int codMedico) {
        this.codMedico = codMedico;
    }

    public int getCodHorario() {
        return codHorario;
    }

    public void setCodHorario(int codHorario) {
        this.codHorario = codHorario;
    }

    public int getCodEspecialidad() {
        return codEspecialidad;
    }

    public void setCodEspecialidad(int codEspecialidad) {
        this.codEspecialidad = codEspecialidad;
    }

    @Override
    public String toString() {
        return "" +  codMedicoEspecialidad ;
    }
    
    
}
