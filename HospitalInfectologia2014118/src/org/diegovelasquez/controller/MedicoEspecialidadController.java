
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
import org.diegovelasquez.bean.MedicoEspecialidad;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.report.GenerarReporte;
import org.diegovelasquez.sistema.Principal;


public class MedicoEspecialidadController implements Initializable{
    private enum operaciones {Agregar,Guardar, Editar, Eliminar, Actualizar, Cancelar, Reportar, NINGUNO};
    public Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <MedicoEspecialidad> listarMedicoEspecialidad;
    
    @FXML private ComboBox cmbcodMedicoEspecialidad;
    @FXML private TextField txtcodMedico;
    @FXML private TextField txtcodHorario;
    @FXML private TextField txtcodEspecialidad;
    @FXML private TableView tblMedicoEspecialidad;
    @FXML private TableColumn colcodMedicoEspecialidad;
    @FXML private TableColumn colcodMedico;
    @FXML private TableColumn colcodHorario;
    @FXML private TableColumn colcodEspecialidad;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();  
           cmbcodMedicoEspecialidad.setItems(getMedicoEspecialidad());
    
    }
    public void cargarDatos(){
        tblMedicoEspecialidad.setItems(getMedicoEspecialidad()); 
        colcodMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad, Integer>("codMedicoEspecialidad"));
        colcodMedico.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codMedico"));    
        colcodHorario.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codHorario"));
        colcodEspecialidad.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codEspecialidad"));
      }
    public ObservableList<MedicoEspecialidad> getMedicoEspecialidad(){
        ArrayList<MedicoEspecialidad> lista = new ArrayList<MedicoEspecialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicoEspecialidad}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new MedicoEspecialidad(resultado.getInt("codMedicoEspecialidad")
                                    ,resultado.getInt("codMedico")
                                    ,resultado.getInt("codHorario")
                                    ,resultado.getInt("codEspecialidad")
                                   ));    
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarMedicoEspecialidad = FXCollections.observableList(lista);
    }
    public void seleccionarElemento(){
         cmbcodMedicoEspecialidad.getSelectionModel().select(buscarMedicoEspecialidad(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodMedicoEspecialidad()));
         txtcodMedico.setText(("" + ((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodMedico()));
         txtcodHorario.setText(("" + ((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodHorario()));
         txtcodEspecialidad.setText(("" + ((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodEspecialidad()));
       }
    public MedicoEspecialidad buscarMedicoEspecialidad(int codMedicoEspecialidad){
            MedicoEspecialidad resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicoEspecialidad(?)}");
                procedimiento.setInt(1, (codMedicoEspecialidad));
                ResultSet registro =  procedimiento.executeQuery();
                while(registro.next()){
                                resultado = new MedicoEspecialidad (registro.getInt("codMedicoEspecialidad")
                                                       ,registro.getInt("codMedico")
                                                       ,registro.getInt("codHorario")
                                                       ,registro.getInt("codEspecialidad"));
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
        MedicoEspecialidad registro = new MedicoEspecialidad();
        registro.setCodMedico(Integer.parseInt(txtcodMedico.getText()));
        registro.setCodHorario(Integer.parseInt(txtcodHorario.getText()));
        registro.setCodEspecialidad(Integer.parseInt(txtcodMedico.getText()));
       try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_AgregarMedicoEspecialidad(?,?,?)}"); 
        procedimiento.setInt(1, registro.getCodMedico());
        procedimiento.setInt(2, registro.getCodHorario());
        procedimiento.setInt(3, registro.getCodEspecialidad());
        procedimiento.execute();
        listarMedicoEspecialidad.add(registro);
        }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
        
     }
    public void editar (){
        switch(tipoDeOperacion){
            case NINGUNO:
            if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
                activarControles();
            btnEditar.setText("Actualizar");
            btnEliminar.setText("Cancelar");
            btnAgregar.setDisable(true);
            btnReporte.setDisable(true);
            
            tipoDeOperacion = operaciones.Actualizar;
            }else{
                JOptionPane.showConfirmDialog(null,"Debe seleccionar un elemento.");
            }
            break;
            case Actualizar:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos ();
                break;  

        }
    }
    
    public void actualizar(){
         try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarMedicoEspecialidad(?,?,?,?)}");
             MedicoEspecialidad registro = (MedicoEspecialidad) tblMedicoEspecialidad.getSelectionModel().getSelectedItem();
             registro.setCodMedicoEspecialidad(Integer.parseInt(cmbcodMedicoEspecialidad.getSelectionModel().getSelectedItem().toString()));
             registro.setCodMedico(Integer.parseInt(txtcodMedico.getText()));
             registro.setCodHorario(Integer.parseInt(txtcodHorario.getText()));
             registro.setCodEspecialidad(Integer.parseInt(txtcodEspecialidad.getText()));
             
             procedimiento.setInt(1, registro.getCodMedicoEspecialidad());    
             procedimiento.setInt(2, registro.getCodMedico());
             procedimiento.setInt(3, registro.getCodHorario());
             procedimiento.setInt(4, registro.getCodEspecialidad());
             procedimiento.execute();
             
         }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
      }
    
    
    
public void desactivarControles (){
        txtcodMedico.setEditable(false);
        txtcodHorario.setEditable(false);
        txtcodEspecialidad.setEditable(false);
        
     }
    
    public void activarControles(){
       txtcodMedico.setEditable(true);
       txtcodHorario.setEditable(true);
        txtcodEspecialidad.setEditable(true);
        
    }
    public void limpiarControles(){
        txtcodMedico.setText("");
        txtcodHorario.setText("");
        txtcodEspecialidad.setText("");
        
    }
    public void eliminar(){
        switch(tipoDeOperacion){
            case Guardar:
               cancelar();
                break;
            case Actualizar:
                cancelar();
                break;
                default:
                    if (tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
                        
                        int respuesta = JOptionPane.showConfirmDialog(null, "Desea elimianr el registro de medico especialidad!!!", "MedicoEspecialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta== JOptionPane.YES_OPTION)
                        {
                            
                            try{
                                PreparedStatement eliminar = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicoEspecialidad(?)}");
                                eliminar.setInt(1, ((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodMedicoEspecialidad());
                                eliminar.execute();
                                
                                cargarDatos();
                                limpiarControles();
                            }catch (SQLException ex){
                                ex.printStackTrace();
                        }
                        
                    }
                break;
        
        
        
        }
        tipoDeOperacion = operaciones.NINGUNO;
 }
    }
    
    public void imprimirReporte (){
        if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
            int codMedicoEspecialidad = ((MedicoEspecialidad) tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodEspecialidad();
            Map parametros = new HashMap();
            parametros .put("p_codMedicoEspecialidad", codMedicoEspecialidad);
            GenerarReporte.mostrarReporte("reporteBuscarMedicoEspecialidad.jasper", "Reporte de Medico Especialidad", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "¡Debe seleccionar un registro!");
        }
    }
    
    private void cancelar(){
            btnAgregar.setText("Agregar");
            btnEliminar.setText("Eliminar");
            btnEditar.setText("Editar");
            btnEditar.setDisable(false);
            btnReporte.setDisable(false);
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
