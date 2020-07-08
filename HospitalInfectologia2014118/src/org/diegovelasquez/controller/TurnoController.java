package org.diegovelasquez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.diegovelasquez.bean.Turno;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.report.GenerarReporte;
import org.diegovelasquez.sistema.Principal;


public class TurnoController implements Initializable{
    private enum operaciones {Agregar,Guardar, Editar, Eliminar, Actualizar, Cancelar, Reportar, NINGUNO};
    public Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Turno> listarTurno;
   
    @FXML private ComboBox cmbcodTurno;
    @FXML private TextField txtvalorCita;
    @FXML private TextField txtcodMedicoEspecialidad;
    @FXML private TextField txtcodResponsableTurno;
    @FXML private TextField txtcodPaciente;
    @FXML private GridPane grpfechaTurno;
    @FXML private GridPane grpfechaCita;
    private DatePicker dtpfechaTurno;
    private DatePicker dtpfechaCita;
    @FXML private TableView tblTurno;
    @FXML private TableColumn colcodTurno;
    @FXML private TableColumn colfechaTurno;
    @FXML private TableColumn colfechaCita;
    @FXML private TableColumn colvalorCita;
    @FXML private TableColumn colcodMedicoEspecialidad;        
    @FXML private TableColumn colcodResponsableTurno;        
    @FXML private TableColumn colcodPaciente;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportar;
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      cargarDatos();  
      cmbcodTurno.setItems(getTurnos());
      
      dtpfechaTurno = new DatePicker(Locale.ENGLISH);
      dtpfechaTurno.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      dtpfechaTurno.getCalendarView().todayButtonTextProperty().set("Today");
      dtpfechaTurno.getCalendarView().setShowWeeks(false);
      grpfechaTurno.add(dtpfechaTurno, 0,0);
      
      dtpfechaCita = new DatePicker(Locale.ENGLISH);
      dtpfechaCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      dtpfechaCita.getCalendarView().todayButtonTextProperty().set("Today");
      dtpfechaCita.getCalendarView().setShowWeeks(false);
      grpfechaCita.add(dtpfechaCita, 0,0);
    } 
    public void cargarDatos(){
        tblTurno.setItems(getTurnos()); 
        colcodTurno.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codTurno"));
        colfechaTurno.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaTurno"));
        colfechaCita.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaCita"));    
        colvalorCita.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("valorCita"));
        colcodMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory <Turno, Integer>("codMedicoEspecialidad"));
        colcodResponsableTurno.setCellValueFactory(new PropertyValueFactory <Turno, Integer>("codResponsableTurno"));
        colcodPaciente.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codPaciente"));
        
                }
    public ObservableList<Turno> getTurnos(){
        ArrayList<Turno> lista = new ArrayList<Turno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTurno}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Turno(resultado.getInt("codTurno"),
                                    resultado.getDate("fechaTurno"),
                                    resultado.getDate("fechaCita"),
                                    resultado.getInt("valorCita"),
                                    resultado.getInt("codMedicoEspecialidad"),
                                    resultado.getInt("codResponsableTurno"),
                                    resultado.getInt("codPaciente")
                     
                
                ));    
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarTurno = FXCollections.observableList(lista);
    }
    public void seleccionarElemento(){
         cmbcodTurno.getSelectionModel().select(buscarTurno(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodTurno()));
         dtpfechaTurno.selectedDateProperty().set(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getFechaTurno());
         dtpfechaCita.selectedDateProperty().set(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getFechaCita());
         txtvalorCita.setText(("" + ((Turno) tblTurno.getSelectionModel().getSelectedItem()).getValorCita()));
         txtcodMedicoEspecialidad.setText(("" + ((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodMedicoEspecialidad()));
         txtcodResponsableTurno.setText(("" + ((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodResponsableTurno()));
         txtcodPaciente.setText(("" + ((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodPaciente()));
    }
    public Turno buscarTurno(int codTurno){
            Turno resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTurno(?)}");
                procedimiento.setInt(1, (codTurno));
                ResultSet registro =  procedimiento.executeQuery();
                while(registro.next()){
                                resultado = new Turno(registro.getInt("codTurno")
                                                       ,registro.getDate("fechaTurno")
                                                       ,registro.getDate("fechaCita")
                                                       ,registro.getInt("valorCita")
                                                       ,registro.getInt("codMedicoEspecialidad")
                                                       ,registro.getInt("codResponsableTurno")
                                                       ,registro.getInt("codPaciente"));
            }
                
            }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
            }
                return resultado;
    }
    public void Agregar (){
       switch (tipoDeOperacion){
           case NINGUNO: 
                   activarControles ();
               btnAgregar.setText("Guardar");
               btnEliminar.setText("Cancelar");
               btnEditar.setDisable(true);
               btnReportar.setDisable(true);
               tipoDeOperacion = operaciones.Guardar;
               break;
           case Guardar:
               Guardar();
               desactivarControles();
               limpiarControles();
               btnAgregar.setText("Agregar");
               btnEliminar.setText("Eliminar");
               btnEditar.setDisable(false);
               btnReportar.setDisable(false);
               tipoDeOperacion = operaciones.NINGUNO;
               cargarDatos();
               break;
       } 
    }   
    public void Guardar (){
        Turno registro = new Turno();
        registro.setFechaTurno(dtpfechaTurno.getSelectedDate());
        registro.setFechaCita(dtpfechaCita.getSelectedDate());
        registro.setValorCita(Integer.parseInt(txtvalorCita.getText()));
        registro.setCodMedicoEspecialidad(Integer.parseInt(txtcodMedicoEspecialidad.getText()));
        registro.setCodResponsableTurno(Integer.parseInt(txtcodResponsableTurno.getText()));
        registro.setCodPaciente(Integer.parseInt(txtcodPaciente.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_AgregarTurno (?,?,?,?,?,?)}"); 
        procedimiento.setDate(1, new java.sql.Date(registro.getFechaTurno().getTime()));
        procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
        procedimiento.setInt(3, registro.getValorCita());
        procedimiento.setInt(4, registro.getCodMedicoEspecialidad());
        procedimiento.setInt(5, registro.getCodResponsableTurno());
        procedimiento.setInt(6, registro.getCodPaciente());
        procedimiento.execute();
        listarTurno.add(registro);
        }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
        
     }
     public void editar (){
         
         switch(tipoDeOperacion){
             case NINGUNO:
                if (tblTurno.getSelectionModel().getSelectedItem() != null ){
                    activarControles();
                 btnEditar.setText("Actualizar");
                 btnEliminar.setText("Cancelar");
                 btnAgregar.setDisable(true);
                 btnReportar.setDisable(true);
                 tipoDeOperacion = operaciones.Actualizar;
                 break;
                 
             }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un registro para editar");
                break;
               }
                case Actualizar:
                actualizar();
                btnEditar.setText("Editar");
                btnReportar.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnReportar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;    
         }
        
     }  
    public void actualizar(){
         try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTurno(?,?,?,?,?,?,?)}");
             Turno registro = (Turno) tblTurno.getSelectionModel().getSelectedItem();
             registro.setCodTurno(Integer.parseInt(cmbcodTurno.getSelectionModel().getSelectedItem().toString()));
             registro.setFechaTurno(dtpfechaTurno.getSelectedDate());
             registro.setFechaCita(dtpfechaCita.getSelectedDate());
             registro.setValorCita(Integer.parseInt(txtvalorCita.getText()));
             registro.setCodMedicoEspecialidad(Integer.parseInt(txtcodMedicoEspecialidad.getText()));
             registro.setCodResponsableTurno(Integer.parseInt(txtcodResponsableTurno.getText()));
             registro.setCodPaciente(Integer.parseInt(txtcodPaciente.getText()));
             
             
             procedimiento.setInt(1, registro.getCodTurno());    
             procedimiento.setDate(2, new java.sql.Date (registro.getFechaTurno().getTime()));
             procedimiento.setDate(3, new java.sql.Date (registro.getFechaCita().getTime()));
             procedimiento.setInt(4, registro.getValorCita());
             procedimiento.setInt(5, registro.getCodMedicoEspecialidad());
             procedimiento.setInt(6, registro.getCodResponsableTurno());
             procedimiento.setInt(7, registro.getCodPaciente());
             procedimiento.execute();
             
         }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
         
     }
    
    
    
    
    
public void desactivarControles (){
        txtvalorCita.setEditable(false);
        txtcodMedicoEspecialidad.setEditable(false);
        txtcodResponsableTurno.setEditable(false);
        dtpfechaTurno.setDisable(true);
        dtpfechaCita.setDisable(true);
        txtcodPaciente.setEditable(false);
    }
    
    public void activarControles(){
        txtvalorCita.setEditable(true);
        txtcodMedicoEspecialidad.setEditable(true);
        txtcodResponsableTurno.setEditable(true);
        dtpfechaTurno.setDisable(false);
        dtpfechaCita.setDisable(false);
        txtcodPaciente.setEditable(true);
}
    public void limpiarControles(){
        txtvalorCita.setText("");
        txtcodMedicoEspecialidad.setText("");
        txtcodResponsableTurno.setText("");
        txtcodPaciente.setText("");
    }
    public void eliminar (){
    switch(tipoDeOperacion){
        case Guardar:
            cancelar();
            break;
        case Actualizar:
            cancelar();
            break;
            default:
                if(tblTurno.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea elminar el registro de turno!!!", "Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta== JOptionPane.YES_OPTION)
                    {
                     try{
                         PreparedStatement eliminar = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTurno(?)}");
                         eliminar.setInt(1, ((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodTurno());
                         eliminar.execute();
                         cargarDatos();
                         limpiarControles();
                     }catch (SQLException ex){
                                ex.printStackTrace();
                        }
                    }
                }else{
                  JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");   
                }
                break; 
    }
}
   private void cancelar (){
       btnAgregar.setText("Agregar");
       btnEliminar.setText("Eliminar");
       btnEditar.setDisable(false);
       btnReportar.setDisable(false);
       btnAgregar.setDisable(false);
   } 
    
   public void imprimirReporte (){
        if(tblTurno.getSelectionModel().getSelectedItem() != null){
            int codTurno = ((Turno) tblTurno.getSelectionModel().getSelectedItem()).getCodTurno();
            Map parametros = new HashMap();
            parametros .put("p_codTurno",codTurno);
            GenerarReporte.mostrarReporte("reporteBuscarTurno.jasper", "Reporte de Medicos", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "¡Debe seleccionar un registro!");
        }
    }
   
    
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal (){
        escenarioPrincipal.menuPrincipal();
    }

}