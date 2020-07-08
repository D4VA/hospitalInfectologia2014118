
package org.diegovelasquez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.diegovelasquez.bean.Especialidades;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.report.GenerarReporte;
import org.diegovelasquez.sistema.Principal;

/**
 *
 * @author DAV4
 */
public class EspecialidadesController implements Initializable{
    
    private enum operaciones {Nuevo, Guardar,Editar,Eliminar,Actualizar,Cancelar,Reportar,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Especialidades>listarEspecialidades;
    
    @FXML private ComboBox cmbCodEspecialidad;
    @FXML private TextField txtNombreEspecialidad;
    @FXML private TableView tblEspecialidades;
    @FXML private TableColumn colCodEspecialidad;
    @FXML private TableColumn colNombreEspecialidad;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodEspecialidad.setItems(getEspecialidades());
    }

    public void cargarDatos(){
        tblEspecialidades.setItems(getEspecialidades());
        colCodEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidades, Integer>("codEspecialidad"));
        colNombreEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidades, String>("nomEspecialidad"));
    }
    
    public ObservableList<Especialidades>getEspecialidades(){
    ArrayList<Especialidades> lista = new ArrayList <Especialidades>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEspecialidades}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Especialidades(resultado.getInt("codEspecialidad"),
                                         resultado.getString("nomEspecialidad")
                                        ));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listarEspecialidades = FXCollections.observableList(lista);
    }
    public void seleccionarElemento(){
        cmbCodEspecialidad.getSelectionModel().select(((Especialidades)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodEspecialidad());
        txtNombreEspecialidad.setText(((Especialidades)tblEspecialidades.getSelectionModel().getSelectedItem()).getNomEspecialidad());
    }
    
        public Especialidades buscarEspecialidades (int codEspecialidad){
        Especialidades resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEspecialidades(?)}");
            procedimiento.setInt(1, codEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Especialidades(registro.getInt("codEspecialidad"),
                                                 registro.getString("nomEspecialidad")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void nuevo (){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
            btnAgregar.setText("Guardar");
            btnEliminar.setText("Cancelar");
            btnEditar.setDisable(true);
            btnReporte.setDisable(true);
            
            tipoDeOperacion = operaciones.Guardar;
            break;
            case Guardar:
                    guardar();
                    desactivarControles();
                    limpiarControles();
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar (){
        Especialidades registro = new Especialidades();
        registro.setNomEspecialidad(txtNombreEspecialidad.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEspecialidades (?)}");
            procedimiento.setString(1, registro.getNomEspecialidad());
            procedimiento.execute();
            listarEspecialidades.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void Editar (){
        switch(tipoDeOperacion){
            case NINGUNO:
            if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
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
                btnReporte.setDisable(false);
                tipoDeOperacion = EspecialidadesController.operaciones.NINGUNO;
                cargarDatos ();
                break;               
        }
    }
    
        public void actualizar (){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarEspecialidades(?,?)}");
            Especialidades registro = (Especialidades)tblEspecialidades.getSelectionModel().getSelectedItem();
            registro.setCodEspecialidad(Integer.parseInt(cmbCodEspecialidad.getSelectionModel().getSelectedItem().toString()));
            registro.setNomEspecialidad(txtNombreEspecialidad.getText());
            
            procedimiento.setInt(1, registro.getCodEspecialidad());
            procedimiento.setString(2, registro.getNomEspecialidad());
            procedimiento.execute();
        }catch(Exception e){
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
                if (tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?","Eliminar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement eliminar = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEspecialidades(?)}");
                            eliminar.setInt(1, ((Especialidades)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodEspecialidad());
                            eliminar.execute();
                            
                            
                            cargarDatos();
                            limpiarControles();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
        }
    }
    
    public void imprimirReporte (){
        if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
            int codEspecialidad = ((Especialidades) tblEspecialidades.getSelectionModel().getSelectedItem()).getCodEspecialidad();
            Map parametros = new HashMap();
            parametros .put("p_codEspecialidad", codEspecialidad);
            GenerarReporte.mostrarReporte("reportBuscarEspecialidades.jasper", "Reporte de Especialidades", parametros);
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
            
 public void desactivarControles(){
        txtNombreEspecialidad.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreEspecialidad.setEditable(true);
    }
    
    public void limpiarControles (){
        txtNombreEspecialidad.setText("");
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
