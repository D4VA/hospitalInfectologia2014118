package org.diegovelasquez.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.diegovelasquez.report.GenerarReporte;
import org.diegovelasquez.sistema.Principal;

/**
 *
 * @author DAV4
 */
public class MenuPrincipalController implements Initializable {
    
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void medicos(){
        escenarioPrincipal.medicos();
    }
    public void programador(){
        escenarioPrincipal.programador();
    }
    public void pacientes (){
        escenarioPrincipal.pacientes();
    }
    public void telefonosMedicos (){
        escenarioPrincipal.telefonosMedicos();
    }
    public void contactoUrgencia (){
        escenarioPrincipal.contactoUrgencia();
    }
    public void horarios(){
        escenarioPrincipal.horarios();
    }
    public void cargos(){
        escenarioPrincipal.cargos();
    }
    public void areas(){
        escenarioPrincipal.areas();
    }
    public void especialidades (){
        escenarioPrincipal.especialidades();
    }
    public void responsableTurno (){
        escenarioPrincipal.responsableTurno();
    }
    public void medicoEspecialidad (){
        escenarioPrincipal.medicoEspecialidad();
    }
    public void turno(){
        escenarioPrincipal.turno();
    }
    
    public void login(){
        escenarioPrincipal.login();
    }

        public void ImprimirReporte (){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("reporteListarPaciente.jasper", "Reporte de Pacientes", parametros);
    }
    
    public void ImprimirReporte1 (){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("reporteListarMedicos.jasper", "Reporte de Medicos", parametros);
    }
    
        public void ImprimirReporte2 (){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("reporteListarTurno.jasper", "Reporte de Turnos", parametros);
    }
    
        public void ImprimirReporte3 (){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("reporteListarTelefonosMedicos.jasper", "Reporte de TelefonosMedicos", parametros);
    }
    
        public void ImprimirReporte4 (){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("reporteListarContactoUrgencia.jasper", "Reporte de Contacto Urgencia", parametros);
    }
        
        public void ImprimirReporte5 (){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("reporteListarAreas.jasper", "Reporte de Areas", parametros);
    }
    
        public void ImprimirReporte6 (){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("reporteListarHorarios.jasper", "Reporte de Horarios", parametros);
    }    
    
        public void ImprimirReporte7 (){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("reporteListarEspecialidades.jasper", "Reporte de Especialidades", parametros);
    }
        
        public void ImprimirReporte8 (){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("reporteListarCargos.jasper", "Reporte de Cargos", parametros);
    }
    
        public void ImprimirReporte9 (){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("reporteListarResponsableTurno.jasper", "Reporte de ResponsableTurno", parametros);
    }
        
        public void ImprimirReporte10 (){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("reporteListarMedicoEspecialidad.jasper", "Reporte de Especialidad", parametros);
    }
      
        
        public void CloseApp(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }
    
}

