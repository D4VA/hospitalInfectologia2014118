
package org.diegovelasquez.sistema;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.diegovelasquez.controller.AreasController;
import org.diegovelasquez.controller.CargosController;
import org.diegovelasquez.controller.ContactoUrgenciaController;
import org.diegovelasquez.controller.EspecialidadesController;
import org.diegovelasquez.controller.HorariosController;
import org.diegovelasquez.controller.LoginController;
import org.diegovelasquez.controller.MedicoController;
import org.diegovelasquez.controller.MedicoEspecialidadController;
import org.diegovelasquez.controller.MenuPrincipalController;
import org.diegovelasquez.controller.PacientesController;
import org.diegovelasquez.controller.ProgramadorController;
import org.diegovelasquez.controller.RegistroUsuarioController;
import org.diegovelasquez.controller.ResponsableTurnoController;
import org.diegovelasquez.controller.TelefonosMedicosController;
import org.diegovelasquez.controller.TurnoController;
import org.diegovelasquez.db.Conexion;

/**
 *
 * @author DAV4
 */


public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/diegovelasquez/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_IMAGE = "/org/diegovelasquez/images/";
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        Connection conn = Conexion.getInstancia().getConexion();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("Select * from Especialidades");
        
        while (rs.next()){
                System.out.print(rs.getInt(1));
        }
        
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Hospital de Infectologia");
        login();
        escenarioPrincipal.show();
        
    }

    public void registroUsuario(){
        try{
            RegistroUsuarioController registroUsuario = (RegistroUsuarioController)cambiarEscena("VentanaRegistroUsuarioView.fxml",699,473);
            registroUsuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void login(){
        try{
            LoginController login = (LoginController)cambiarEscena("VentanaLoginView.fxml",534,362);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuPrincipal (){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("menuPrincipalView.fxml",553,255);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void medicos (){
        try{
            MedicoController medicos = (MedicoController)cambiarEscena("VentanaMedicoView.fxml",775,484);
            medicos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void programador (){
        try{
            ProgramadorController programador = (ProgramadorController)cambiarEscena("VentanaProgramadorView.fxml",600,374);
            programador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void pacientes (){
        try{
            PacientesController pacientes = (PacientesController)cambiarEscena("VentanaPacientesView.fxml",640,400);
            pacientes.setEscenarioPrincipal(this);
        }catch(Exception e){
                e.printStackTrace();
        }
    }
    
    public void telefonosMedicos(){
        try{
            TelefonosMedicosController telefonosMedicos = (TelefonosMedicosController)cambiarEscena("VentanaTelefonosMedicosView.fxml",639,399);
            telefonosMedicos.setEscenarioPrincipal(this);
        }catch(Exception e){
                e.printStackTrace();
        }
    }
    
    public void contactoUrgencia (){
        try{
            ContactoUrgenciaController contactoUrgencia = (ContactoUrgenciaController)cambiarEscena("VentanaContactoUrgenciaView.fxml",637,398);
            contactoUrgencia.setEscenarioPrincipal(this);
        }catch(Exception e){
                e.printStackTrace();
        }
    }
    
    public void horarios(){
        try{
            HorariosController horarios = (HorariosController)cambiarEscena("VentanaHorariosView.fxml",654,409);
            horarios.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void areas (){
        try{
            AreasController areas = (AreasController)cambiarEscena("VentanaAreasView.fxml",561,350);
            areas.setEscenarioPrincipal(this);
        }catch(Exception e){
                e.printStackTrace();
        }
    }
    
    public void especialidades (){
        try{
            EspecialidadesController especialidades = (EspecialidadesController)cambiarEscena("VentanaEspecialidadesView.fxml",502,315);
            especialidades.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargos (){
        try{
            CargosController cargos = (CargosController)cambiarEscena("VentanaCargosView.fxml",564,350);
            cargos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void responsableTurno(){
        try{
            ResponsableTurnoController responsableTurno = (ResponsableTurnoController)cambiarEscena("VentanaResponsableTurnoView.fxml",762,476);
            responsableTurno.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void medicoEspecialidad(){
        try{
            MedicoEspecialidadController medicoEspecialidad = (MedicoEspecialidadController)cambiarEscena("VentanaMedicoEspecialidadView.fxml",631,393);
            medicoEspecialidad.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }     
    }
    
    public void turno () {
        try{
            TurnoController turno = (TurnoController)cambiarEscena("VentanaTurnoView.fxml",630,393);
            turno.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.setResizable(false);
        escenarioPrincipal.sizeToScene();
        escenarioPrincipal.getIcons().add(new Image(Principal.class.getResourceAsStream(PAQUETE_IMAGE+"Icono.png")));
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
