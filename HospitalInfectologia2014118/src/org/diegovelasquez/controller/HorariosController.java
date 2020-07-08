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
import org.diegovelasquez.bean.Horarios;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.report.GenerarReporte;
import org.diegovelasquez.sistema.Principal;


public class HorariosController implements Initializable{
    private enum operaciones {Agregar,Guardar, Editar, Eliminar, Actualizar, Cancelar, Reportar, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Horarios> listarHorario;
    
    @FXML private ComboBox cmbCodHorario;
    @FXML private GridPane grpHorarioInicio;
    @FXML private GridPane grpHorarioSalida;
    private DatePicker dtpHorarioInicio;
    private DatePicker dtpHorarioSalida;
    @FXML private TextField txtLunes;
    @FXML private TextField txtMartes;
    @FXML private TextField txtMiercoles;
    @FXML private TextField txtJueves;
    @FXML private TextField txtViernes;
    @FXML private TableView tblHorarios;
    @FXML private TableColumn colCodHorario;
    @FXML private TableColumn colHorarioInicio;
    @FXML private TableColumn colHorarioSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();  
        cmbCodHorario.setItems(getHorarios());

        dtpHorarioInicio = new DatePicker(Locale.ENGLISH);
        dtpHorarioInicio.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        dtpHorarioInicio.getCalendarView().todayButtonTextProperty().set("Today");
        dtpHorarioInicio.getCalendarView().setShowWeeks(false);
        grpHorarioInicio.add(dtpHorarioInicio, 0,0);

        dtpHorarioSalida = new DatePicker(Locale.ENGLISH);
        dtpHorarioSalida.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpHorarioSalida.getCalendarView().todayButtonTextProperty().set("Today");
        dtpHorarioSalida.getCalendarView().setShowWeeks(false);
        grpHorarioSalida.add(dtpHorarioSalida, 0,0);
    }
    public void cargarDatos(){
        tblHorarios.setItems(getHorarios()); 
        colCodHorario.setCellValueFactory(new PropertyValueFactory<Horarios,Integer>("codHorario"));
        colHorarioInicio.setCellValueFactory(new PropertyValueFactory <Horarios, Date>("horarioInicio"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory <Horarios, Date>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("miercoles"));    
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("viernes"));
        }
    public ObservableList<Horarios> getHorarios(){
        ArrayList<Horarios> lista = new ArrayList<Horarios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarHorarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Horarios(resultado.getInt("codHorario")
                                    ,resultado.getDate("horarioInicio")
                                    ,resultado.getDate("horarioSalida")
                                    ,resultado.getInt("lunes")
                                    ,resultado.getInt("martes")
                                    ,resultado.getInt("miercoles")
                                    ,resultado.getInt("jueves")
                                    ,resultado.getInt("viernes")
                
                ));    
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarHorario = FXCollections.observableList(lista);
    }
    public void seleccionarElemento(){
         cmbCodHorario.getSelectionModel().select(buscarHorario(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodHorario()));
         dtpHorarioInicio.selectedDateProperty().set(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioInicio());
         dtpHorarioSalida.selectedDateProperty().set(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
         txtLunes.setText(("" + ((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getLunes()));
         txtMartes.setText(("" + ((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getMartes()));
         txtMiercoles.setText(("" + ((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getMiercoles()));
         txtJueves.setText(("" + ((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getJueves()));
         txtViernes.setText((("" + ((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getViernes())));
    }
    public Horarios buscarHorario(int codHorario){
            Horarios resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarHorarios(?)}");
                procedimiento.setInt(1, (codHorario));
                ResultSet registro =  procedimiento.executeQuery();
                while(registro.next()){
                                resultado = new Horarios(registro.getInt("codHorario")
                                                       ,registro.getDate("horarioInicio")
                                                       ,registro.getDate("horarioSalida")
                                                       ,registro.getInt("lunes")
                                                       ,registro.getInt("martes")
                                                       ,registro.getInt("miercoles")
                                                       ,registro.getInt("jueves")
                                                       ,registro.getInt("viernes"));
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
               btnReporte.setDisable(true);
               tipoDeOperacion = operaciones.Guardar;
               break;
           case Guardar:
               Guardar();
               desactivarControles();
               limpiarControles();
               btnAgregar.setText("Agregar");
               btnEliminar.setText("Eliminar");
               btnEditar.setDisable(false);
               btnReporte.setDisable(false);
               tipoDeOperacion = operaciones.NINGUNO;
               cargarDatos();
               break;
       } 
    }   
    public void Guardar (){
        Horarios registro = new Horarios();
        registro.setHorarioInicio(dtpHorarioInicio.getSelectedDate());
        registro.setHorarioSalida(dtpHorarioSalida.getSelectedDate());
        registro.setLunes(Integer.parseInt(txtLunes.getText()));
        registro.setMartes(Integer.parseInt(txtMartes.getText()));
        registro.setMiercoles(Integer.parseInt(txtMiercoles.getText()));
        registro.setJueves(Integer.parseInt(txtJueves.getText()));
        registro.setViernes(Integer.parseInt(txtViernes.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_AgregarHorarios (?,?,?,?,?,?,?)}"); 
        procedimiento.setDate(1, new java.sql.Date(registro.getHorarioInicio().getTime()));
        procedimiento.setDate(2, new java.sql.Date(registro.getHorarioSalida().getTime()));
        procedimiento.setInt(3, registro.getLunes());
        procedimiento.setInt(4, registro.getMartes());
        procedimiento.setInt(5, registro.getMiercoles());
        procedimiento.setInt(6, registro.getJueves());
        procedimiento.setInt(7, registro.getViernes());
        procedimiento.execute();
        listarHorario.add(registro);
        }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
        
     }
    public void Editar (){
         
         switch(tipoDeOperacion){
             case NINGUNO:
                if (tblHorarios.getSelectionModel().getSelectedItem() != null ){
                    activarControles();
                 btnEditar.setText("Actualizar");
                 btnEliminar.setText("Cancelar");
                 btnAgregar.setDisable(true);
                 btnReporte.setDisable(true);
                 tipoDeOperacion = operaciones.Actualizar;
                 break;
                 
             }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un registro para editar");
                break;
               }
                case Actualizar:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;    
         }
        
     }  
    public void actualizar(){
         try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarHorarios(?,?,?,?,?,?,?,?)}");
             Horarios registro = (Horarios) tblHorarios.getSelectionModel().getSelectedItem();
             registro.setCodHorario(Integer.parseInt(cmbCodHorario.getSelectionModel().getSelectedItem().toString()));
             registro.setHorarioInicio( dtpHorarioInicio.getSelectedDate());
             registro.setHorarioSalida(dtpHorarioSalida.getSelectedDate());
             registro.setLunes(Integer.parseInt(txtLunes.getText()));
             registro.setMartes(Integer.parseInt(txtMartes.getText()));
             registro.setMiercoles(Integer.parseInt(txtMiercoles.getText()));
             registro.setJueves(Integer.parseInt(txtJueves.getText()));
             registro.setViernes(Integer.parseInt(txtViernes.getText()));
             
             procedimiento.setInt(1, registro.getCodHorario());    
             procedimiento.setDate(2, new java.sql.Date (registro.getHorarioInicio().getTime()));
             procedimiento.setDate(3, new java.sql.Date (registro.getHorarioSalida().getTime()));
             procedimiento.setInt(4, registro.getLunes());
             procedimiento.setInt(5, registro.getMartes());
             procedimiento.setInt(6, registro.getMiercoles());
             procedimiento.setInt(7, registro.getJueves());
             procedimiento.setInt(8, registro.getViernes());
             procedimiento.execute();
             
         }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
      }
    public void Eliminar (){
    switch(tipoDeOperacion){
        case Guardar:
            cancelar();
            break;
        case Actualizar:
            cancelar();
            break;
            default:
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea elminar el registro de horarios!!!", "horarios", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta== JOptionPane.YES_OPTION)
                    {
                     try{
                         PreparedStatement eliminar = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarHorarios(?)}");
                         eliminar.setInt(1, ((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodHorario());
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
       btnEditar.setText("Editar");
       btnEditar.setDisable(false);
       btnReporte.setDisable(false);
       btnAgregar.setDisable(false);
   }
    
    
    
    
public void desactivarControles (){
        dtpHorarioInicio.setDisable(true);
        dtpHorarioSalida.setDisable(true);
        txtLunes.setEditable(false);
        txtMartes.setEditable(false);
        txtMiercoles.setEditable(false);
        txtJueves.setEditable(false);
        txtViernes.setEditable(false);
    }
    
    public void activarControles(){
        dtpHorarioInicio.setDisable(false);
        dtpHorarioSalida.setDisable(false);
        txtLunes.setEditable(true);
        txtMartes.setEditable(true);
        txtMiercoles.setEditable(true);
        txtJueves.setEditable(true);
        txtViernes.setEditable(true);

}
    public void limpiarControles(){
        txtLunes.setText("");
        txtMartes.setText("");
        txtMiercoles.setText("");
        txtJueves.setText("");
        txtViernes.setText("");
    }
    
    public void imprimirReporte (){
        if(tblHorarios.getSelectionModel().getSelectedItem() != null){
            int codHorario = ((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getCodHorario();
            Map parametros = new HashMap();
            parametros .put("p_codHorario", codHorario);
            GenerarReporte.mostrarReporte("reporteBuscarHorarios.jasper", "Reporte de Horarios", parametros);
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
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
