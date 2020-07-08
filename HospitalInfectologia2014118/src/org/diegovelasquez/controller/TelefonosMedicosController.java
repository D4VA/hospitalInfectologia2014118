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
import org.diegovelasquez.bean.TelefonosMedicos;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.report.GenerarReporte;
import org.diegovelasquez.sistema.Principal;

/**
 *
 * @author DAV4
 */

public class TelefonosMedicosController implements Initializable{
    
    private enum operaciones{Nuevo, Guardar,Editar,Eliminar,Actualizar,Cancelar,Reportar,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <TelefonosMedicos> listarTelefonosMedicos;
    
    @FXML private ComboBox cmbCodTelefonoMedico;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private TextField txtTelefonoTrabajo;
    @FXML private TableView tblTelefonosMedicos;
    @FXML private TableColumn colCodTelefonoMedico;
    @FXML private TableColumn colCodMedico;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colTelefonoTrabajo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportar;
    @FXML private TextField txtCodMedico;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodTelefonoMedico.setItems(getTelefonosMedicos());
    }
    
    public void cargarDatos(){
       tblTelefonosMedicos.setItems(getTelefonosMedicos());
       colCodTelefonoMedico.setCellValueFactory(new PropertyValueFactory<TelefonosMedicos,Integer>("codTelefonoMedico"));
       colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<TelefonosMedicos, String>("telefonoPersonal"));
       colTelefonoTrabajo.setCellValueFactory(new PropertyValueFactory<TelefonosMedicos, String>("telefonoTrabajo"));
       colCodMedico.setCellValueFactory(new PropertyValueFactory<TelefonosMedicos, Integer>("codMedico"));
    }
    
    public ObservableList<TelefonosMedicos>getTelefonosMedicos(){
        ArrayList<TelefonosMedicos> lista = new ArrayList<TelefonosMedicos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonosMedicos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new TelefonosMedicos(resultado.getInt("codTelefonoMedico"),
                                               resultado.getString("telefonoPersonal"),
                                               resultado.getString("telefonoTrabajo"),
                                               resultado.getInt("codMedico")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarTelefonosMedicos = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento (){
        cmbCodTelefonoMedico.getSelectionModel().select(((TelefonosMedicos)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getCodTelefonoMedico());
        txtTelefonoPersonal.setText(((TelefonosMedicos)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getTelefonoPersonal()); 
        txtTelefonoTrabajo.setText(((TelefonosMedicos)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getTelefonoTrabajo());
        txtCodMedico.setText((""+ ((TelefonosMedicos)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getCodMedico()));
    }
    
    public TelefonosMedicos buscarTelefonosMedicos (int codTelefonoMedico){
        TelefonosMedicos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonoMedico(?)}");
            procedimiento.setInt(1, codTelefonoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TelefonosMedicos(registro.getInt("codTelefonoMedico"),
                                                 registro.getString("telefonoPersonal"),
                                                 registro.getString("telefonoTrabajo"),
                                                 registro.getInt("codMedico")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
        public void Agregar (){
        switch (tipoDeOperacion){
            case NINGUNO:
                    activarControles();
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
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
        
        public void Guardar (){
        TelefonosMedicos registro = new TelefonosMedicos();
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
        registro.setCodMedico(Integer.parseInt(txtCodMedico.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonosMedico(?,?,?)}");
            procedimiento.setString(1, registro.getTelefonoPersonal());
            procedimiento.setString(2, registro.getTelefonoTrabajo());
            procedimiento.setInt(3, registro.getCodMedico());
            procedimiento.execute();
            listarTelefonosMedicos.add(registro);
        }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
    }
    
    public void editar (){ 
         switch(tipoDeOperacion){
             case NINGUNO:
                if (tblTelefonosMedicos.getSelectionModel().getSelectedItem() != null ){
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
    
    public void actualizar (){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTelefonosMedicos(?,?,?,?)}");
            TelefonosMedicos registro = (TelefonosMedicos)tblTelefonosMedicos.getSelectionModel().getSelectedItem();
            registro.setCodTelefonoMedico(Integer.parseInt(cmbCodTelefonoMedico.getSelectionModel().getSelectedItem().toString()));
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
            registro.setCodMedico(Integer.parseInt(txtCodMedico.getText()));
            
            procedimiento.setInt(1, registro.getCodTelefonoMedico());
            procedimiento.setString(2, registro.getTelefonoPersonal());
            procedimiento.setString(3, registro.getTelefonoTrabajo());
            procedimiento.setInt(4, registro.getCodMedico());
            procedimiento.execute();
        }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
        
    }
    
    
    public void desactivarControles(){
        txtTelefonoPersonal.setEditable(false);
        txtTelefonoTrabajo.setEditable(false);
        txtCodMedico.setEditable(false);
    }
    
    public void activarControles(){
        txtTelefonoPersonal.setEditable(true);
        txtTelefonoTrabajo.setEditable(true);
        txtCodMedico.setEditable(true);
    }
    
    public void limpiarControles (){
        txtTelefonoPersonal.setText("");
        txtTelefonoTrabajo.setText("");
        txtCodMedico.setText("");
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
                if(tblTelefonosMedicos.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea elminar el registro de responsable turno!!!", "TelefonosMedicos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta== JOptionPane.YES_OPTION)
                    {
                     try{
                         PreparedStatement eliminar = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonosMedicos(?)}");
                         eliminar.setInt(1, ((TelefonosMedicos)tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getCodTelefonoMedico());
                         eliminar.execute();
                         cargarDatos();
                         
                     }catch (Exception ex){
                                ex.printStackTrace();
                        }
                }
                }else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento"); 
                }
                break;
    }
}
    
    
        private void cancelar(){
            btnAgregar.setText("Agregar");
            btnEliminar.setText("Eliminar");
            btnEditar.setDisable(false);
            btnReportar.setDisable(false);
            btnAgregar.setDisable(false);
    }
        
        
        
    public void imprimirReporte (){
        if(tblTelefonosMedicos.getSelectionModel().getSelectedItem() != null){
            int codTelefonoMedico = ((TelefonosMedicos) tblTelefonosMedicos.getSelectionModel().getSelectedItem()).getCodTelefonoMedico();
            Map parametros = new HashMap();
            parametros .put("p_codTelefonoMedico",codTelefonoMedico);
            GenerarReporte.mostrarReporte("reporteBuscarTelefonosMedicos.jasper", "Reporte de Telefonos Medicos", parametros);
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
