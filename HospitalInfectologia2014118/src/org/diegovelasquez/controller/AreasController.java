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
import org.diegovelasquez.bean.Areas;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.report.GenerarReporte;
import org.diegovelasquez.sistema.Principal;

/**
 *
 * @author DAV4
 */
public class AreasController implements Initializable{
    
    private enum operaciones {Nuevo, Guardar,Editar,Eliminar,Actualizar,Cancelar,Reportar,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Areas>listarAreas;
    
    @FXML private ComboBox cmbCodArea;
    @FXML private TextField txtNombreArea;
    @FXML private TableView tblAreas;
    @FXML private TableColumn colCodArea;
    @FXML private TableColumn colNombreArea;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodArea.setItems(getAreas());
    }

    public void cargarDatos(){
        tblAreas.setItems(getAreas());
        colCodArea.setCellValueFactory(new PropertyValueFactory<Areas, Integer>("codArea"));
        colNombreArea.setCellValueFactory(new PropertyValueFactory<Areas, String>("nomArea"));
    }
    
    public ObservableList<Areas>getAreas(){
    ArrayList<Areas> lista = new ArrayList <Areas>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarAreas}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new Areas(resultado.getInt("codArea"),
                                         resultado.getString("nomArea")
                                        ));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listarAreas = FXCollections.observableList(lista);
    }
    public void seleccionarElemento(){
        cmbCodArea.getSelectionModel().select(((Areas)tblAreas.getSelectionModel().getSelectedItem()).getCodArea());
        txtNombreArea.setText(((Areas)tblAreas.getSelectionModel().getSelectedItem()).getNomArea());
    }
    
        public Areas buscarAreas (int codArea){
        Areas resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarAreas(?)}");
            procedimiento.setInt(1, codArea);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Areas(registro.getInt("codArea"),
                                                 registro.getString("nomArea")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
        
    public void Agregar (){
       switch (tipoDeOperacion){
       // Guardar datos, ciclo.
           case NINGUNO: 
                activarControles ();
               btnAgregar.setText("Nuevo");
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
        Areas registro = new Areas();
        registro.setNomArea(txtNombreArea.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarAreas(?)}");
            procedimiento.setString(1, registro.getNomArea());
            procedimiento.execute();
            listarAreas.add(registro);
        
            }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
    }
    
    public void Editar (){
        switch(tipoDeOperacion){
            case NINGUNO:
            if(tblAreas.getSelectionModel().getSelectedItem() != null){
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
    // Diego Andres Velásquez Avila (DAV4)
        public void actualizar (){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarAreas(?,?)}");
            Areas registro = (Areas)tblAreas.getSelectionModel().getSelectedItem();
            registro.setCodArea(Integer.parseInt(cmbCodArea.getSelectionModel().getSelectedItem().toString()));
            registro.setNomArea(txtNombreArea.getText());
            
            procedimiento.setInt(1, registro.getCodArea());
            procedimiento.setString(2, registro.getNomArea());
            procedimiento.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
        
    }
    
        
            
 public void desactivarControles(){
        txtNombreArea.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreArea.setEditable(true);
    }
    
    public void limpiarControles (){
        txtNombreArea.setText("");
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
                if (tblAreas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?","Area", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement eliminar = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarAreas(?)}");
                            eliminar.setInt(1, ((Areas)tblAreas.getSelectionModel().getSelectedItem()).getCodArea());
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
    
    private void cancelar(){
        btnAgregar.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnEditar.setText("Editar");
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);
        btnAgregar.setDisable(false);
    }
            
    public void imprimirReporte (){
        if(tblAreas.getSelectionModel().getSelectedItem() != null){
            int codArea = ((Areas) tblAreas.getSelectionModel().getSelectedItem()).getCodArea();
            Map parametros = new HashMap();
            parametros .put("p_codArea", codArea);
            GenerarReporte.mostrarReporte("reporteBuscarAreas.jasper", "Reporte de Area", parametros);
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
