
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
import org.diegovelasquez.bean.Cargos;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.report.GenerarReporte;
import org.diegovelasquez.sistema.Principal;

/**
 *
 * @author Dievelas
 */
public class CargosController implements Initializable{
    
private enum operaciones {Nuevo, Guardar,Editar,Eliminar,Actualizar,Cancelar,Reportar,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Cargos>listarCargos;
    
    @FXML private ComboBox cmbCodCargo;
    @FXML private TextField txtNombreCargo;
    @FXML private TableView tblCargos;
    @FXML private TableColumn colCodCargo;
    @FXML private TableColumn colNombreArea;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodCargo.setItems(getCargos());
    }

    public void cargarDatos(){
        tblCargos.setItems(getCargos());
        colCodCargo.setCellValueFactory(new PropertyValueFactory<Cargos, Integer>("codCargo"));
        colNombreArea.setCellValueFactory(new PropertyValueFactory<Cargos, String>("nomCargo"));
    }
    
    public ObservableList<Cargos>getCargos(){
    ArrayList<Cargos> lista = new ArrayList <Cargos>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargos}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Cargos(resultado.getInt("codCargo"),
                                         resultado.getString("nomCargo")
                                        ));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listarCargos = FXCollections.observableList(lista);
    }
    public void seleccionarElemento(){
        cmbCodCargo.getSelectionModel().select(((Cargos)tblCargos.getSelectionModel().getSelectedItem()).getCodCargo());
        txtNombreCargo.setText(((Cargos)tblCargos.getSelectionModel().getSelectedItem()).getNomCargo());
    }
    
        public Cargos buscarAreas (int codCargo){
        Cargos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargo(?)}");
            procedimiento.setInt(1, codCargo);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cargos(registro.getInt("codCargos"),
                                                 registro.getString("nomCargo")
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
            btnEditar.setText("Guardar");
            btnEliminar.setText("Cancelar");
            btnAgregar.setDisable(true);
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
        Cargos registro = new Cargos();
        registro.setNomCargo(txtNombreCargo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargos(?)}");
            procedimiento.setString(1, registro.getNomCargo());
            procedimiento.execute();
            listarCargos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void Editar (){
        switch(tipoDeOperacion){
            case NINGUNO:
            if(tblCargos.getSelectionModel().getSelectedItem() != null){
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
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos ();
                break;               
        }
    }
    
        public void actualizar (){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarCargos(?,?)}");
            Cargos registro = (Cargos)tblCargos.getSelectionModel().getSelectedItem();
            registro.setCodCargo(Integer.parseInt(cmbCodCargo.getSelectionModel().getSelectedItem().toString()));
            registro.setNomCargo(txtNombreCargo.getText());
            
            procedimiento.setInt(1, registro.getCodCargo());
            procedimiento.setString(2, registro.getNomCargo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
        
    public void Eliminar (){
        switch(tipoDeOperacion){
            case Guardar:
                desactivarControles();
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case Actualizar:
                cancelar();
                break;
            default:
                if (tblCargos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?","Eliminar Cargos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement eliminar = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCargos(?)}");
                            eliminar.setInt(1, ((Cargos)tblCargos.getSelectionModel().getSelectedItem()).getCodCargo());
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
            
            
    public void imprimirReporte (){
        if(tblCargos.getSelectionModel().getSelectedItem() != null){
            int codCargo = ((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getCodCargo();
            Map parametros = new HashMap();
            parametros .put("p_codCargo",codCargo);
            GenerarReporte.mostrarReporte("reporteBuscarCargos.jasper", "Reporte de Cargos", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "¡Debe seleccionar un registro!");
        }
    }
            
 public void desactivarControles(){
        txtNombreCargo.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreCargo.setEditable(true);
    }
    
    public void limpiarControles (){
        txtNombreCargo.setText("");
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
