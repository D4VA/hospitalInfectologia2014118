
package org.diegovelasquez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.swing.JOptionPane;
import org.diegovelasquez.bean.ResponsableTurno;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.report.GenerarReporte;
import org.diegovelasquez.sistema.Principal;


public class ResponsableTurnoController implements Initializable{
        private enum operaciones {Agregar,Guardar, Editar, Eliminar, Actualizar, Cancelar, Reportar, NINGUNO};
        public Principal escenarioPrincipal;
        private operaciones tipoDeOperacion = operaciones.NINGUNO;
        private ObservableList <ResponsableTurno> listarResponsableTurno;
        
        @FXML private ComboBox cmbcodResponsableTurno;
        @FXML private TextField txtnomResponsableTurno;
        @FXML private TextField txtapellidosResponsableTurno;
        @FXML private TextField txttelefonoPersonal;
        @FXML private TextField txtcodArea;
        @FXML private TextField txtcodCargo;
        @FXML private TableView tblResponsableTurno;
        @FXML private TableColumn colcodResponsableTurno;
        @FXML private TableColumn colnombre;
        @FXML private TableColumn colapellidos;
        @FXML private TableColumn coltelefonoPersonal;
        @FXML private TableColumn colcodArea;
        @FXML private TableColumn colcodCargo;
        @FXML private Button btnAgregar;
        @FXML private Button btnEditar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReportar;
     
        @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();  
      cmbcodResponsableTurno.setItems(getResponsableTurno());
    }
    public void cargarDatos(){
        tblResponsableTurno.setItems(getResponsableTurno()); 
        colcodResponsableTurno.setCellValueFactory(new PropertyValueFactory <ResponsableTurno, Integer>("codResponsableTurno"));
        colnombre.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("nomResponsable"));    
        colapellidos.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("apellidosResponsable"));
        coltelefonoPersonal.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("telefonoPersonal"));
        colcodArea.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codArea"));
        colcodCargo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codCargo"));
        
                }
    public ObservableList<ResponsableTurno> getResponsableTurno(){
        ArrayList<ResponsableTurno> lista = new ArrayList<ResponsableTurno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarResponsableTurno()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new ResponsableTurno(resultado.getInt("codResponsableTurno")
                                    ,resultado.getString("nomResponsable")
                                    ,resultado.getString("apellidosResponsable")
                                    ,resultado.getString("telefonoPersonal")
                                    ,resultado.getInt("codArea")
                                    ,resultado.getInt("codCargo")
                      ));    
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarResponsableTurno = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
         cmbcodResponsableTurno.getSelectionModel().select(buscarResponsableTurno(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodResponsableTurno()));
         txtnomResponsableTurno.setText(((ResponsableTurno) tblResponsableTurno.getSelectionModel().getSelectedItem()).getNomResponsable());
         txtapellidosResponsableTurno.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getApellidosResponsable());
         txttelefonoPersonal.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
         txtcodArea.setText(("" + ((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodArea()));
         txtcodCargo.setText(("" + ((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodCargo()));
    }
    public ResponsableTurno buscarResponsableTurno(int codResponsableTurno){
            ResponsableTurno resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarResponsableTurno(?)}");
                procedimiento.setInt(1, (codResponsableTurno));
                ResultSet registro =  procedimiento.executeQuery();
                while(registro.next()){
                                resultado = new ResponsableTurno(registro.getInt("codResponsableTurno"),
                                                                 registro.getString("nomResponsable"),
                                                                 registro.getString("apellidosResponsable"),
                                                                 registro.getString("telefonoPersonal"),
                                                                 registro.getInt("codArea"),
                                                                 registro.getInt("codCargo"));
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
        ResponsableTurno registro = new ResponsableTurno();
        registro.setNomResponsable(txtnomResponsableTurno.getText());
        registro.setApellidosResponsable(txtapellidosResponsableTurno.getText());
        registro.setTelefonoPersonal(txttelefonoPersonal.getText());
        registro.setCodArea(Integer.parseInt(txtcodArea.getText()));
        registro.setCodCargo(Integer.parseInt(txtcodCargo.getText()));
       try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_AgregarResponsableTurno (?,?,?,?,?)}"); 
        procedimiento.setString(1, registro.getNomResponsable());
        procedimiento.setString(2, registro.getApellidosResponsable());
        procedimiento.setString(3, registro.getTelefonoPersonal());
        procedimiento.setInt(4, registro.getCodArea());
        procedimiento.setInt(5, registro.getCodCargo());
        procedimiento.execute();
        listarResponsableTurno.add(registro);
        }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
        
     }
    
    public void editar (){ 
         switch(tipoDeOperacion){
             case NINGUNO:  
                if (tblResponsableTurno.getSelectionModel().getSelectedItem() != null ){
                    activarControles();
                 btnEditar.setText("Actualizar");
                 btnEliminar.setText("Cancelar");
                 btnAgregar.setDisable(true);
                 btnReportar.setDisable(true);
                 
                 tipoDeOperacion = operaciones.Actualizar;
                 break;
                 
             }else{
                JOptionPane.showConfirmDialog(null, "Debe seleccionar un registro para editar");
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
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarResponsableTurno(?,?,?,?,?,?)}");
             ResponsableTurno registro = (ResponsableTurno) tblResponsableTurno.getSelectionModel().getSelectedItem();
             registro.setCodResponsableTurno(Integer.parseInt(cmbcodResponsableTurno.getSelectionModel().getSelectedItem().toString()));
             registro.setNomResponsable(txtnomResponsableTurno.getText());
             registro.setApellidosResponsable(txtapellidosResponsableTurno.getText());
             registro.setTelefonoPersonal(txttelefonoPersonal.getText());
             registro.setCodArea(Integer.parseInt(txtcodArea.getText()));
             registro.setCodCargo(Integer.parseInt(txtcodCargo.getText()));
             
             procedimiento.setInt(1, registro.getCodResponsableTurno());    
             procedimiento.setString(2, registro.getNomResponsable());
             procedimiento.setString(3, registro.getApellidosResponsable());
             procedimiento.setString(4, registro.getTelefonoPersonal());
             procedimiento.setInt(5, registro.getCodArea());
             procedimiento.setInt(6, registro.getCodCargo());
             procedimiento.execute();
             
         }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
         
     }
    
    
    public void imprimirReporte (){
        if(tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
            int codResponsableTurno = ((ResponsableTurno) tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodResponsableTurno();
            Map parametros = new HashMap();
            parametros .put("p_codResponsableTurno", codResponsableTurno);
            GenerarReporte.mostrarReporte("reporteBuscarResponsableTurno.jasper", "Reporte de Responsable del Turno", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "¡Debe seleccionar un registro!");
        }
    }
    
    
public void desactivarControles (){
        txtnomResponsableTurno.setEditable(false);
        txtapellidosResponsableTurno.setEditable(false);
        txttelefonoPersonal.setEditable(false);
        txtcodArea.setEditable(false);
        txtcodCargo.setEditable(false);
     }
    
    public void activarControles(){
        txtnomResponsableTurno.setEditable(true);
        txtapellidosResponsableTurno.setEditable(true);
        txttelefonoPersonal.setEditable(true);
        txtcodArea.setEditable(true);
        txtcodCargo.setEditable(true);
    }
    public void limpiarControles(){
        txtnomResponsableTurno.setText("");
        txtapellidosResponsableTurno.setText("");
        txttelefonoPersonal.setText("");
        txtcodArea.setText("");
        txtcodCargo.setText("");
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
                if(tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea elminar el registro de responsable turno!!!", "ResponsableTurno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION)
                    {
                     try{
                         PreparedStatement eliminar = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarResponsableTurno(?)}");
                         eliminar.setInt(1, ((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodResponsableTurno());
                         eliminar.execute();
                         
                         cargarDatos();
                         limpiarControles();
                     }catch (SQLException ex){
                                ex.printStackTrace();
                        }
                }else{
                                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
            break;  
                }
              tipoDeOperacion = operaciones.NINGUNO;  
    }
}
   private void cancelar (){
       btnAgregar.setText("Agregar");
       btnEliminar.setText("Eliminar");
       btnEditar.setText("Editar");
       btnEditar.setDisable(false);
       btnReportar.setDisable(false);
       btnAgregar.setDisable(false);
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
