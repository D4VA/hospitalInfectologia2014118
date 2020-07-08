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
import org.diegovelasquez.bean.ContactoUrgencia;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.report.GenerarReporte;
import org.diegovelasquez.sistema.Principal;

/**
 *
 * @author DAV4
 */
public class ContactoUrgenciaController implements Initializable{

    private enum operaciones {Agregar,Guardar,Editar,Eliminar,Actualizar,Cancelar,Reporte,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <ContactoUrgencia> listarContactoUrgencia;
    
    @FXML private ComboBox cmbCodContactoUrgencia;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNumeroContacto;
    @FXML private TextField txtCodPaciente;
    @FXML private TableView tblContactoUrgencia;
    @FXML private TableColumn colCodContactoUrgencia;
    @FXML private TableColumn colCodPaciente;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colNumeroContacto;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportar;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos ();
        cmbCodContactoUrgencia.setItems(getContactoUrgencia());
    }

    public void cargarDatos(){
        tblContactoUrgencia.setItems(getContactoUrgencia());
        colCodContactoUrgencia.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codContactoUrgencia"));
        colNombres.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia,String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("apellidos"));
        colNumeroContacto.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia,String>("numContacto"));
        colCodPaciente.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia,Integer>("codPaciente"));
    }
    
    public ObservableList<ContactoUrgencia>getContactoUrgencia(){
        ArrayList<ContactoUrgencia> lista = new ArrayList<ContactoUrgencia>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarContactoUrgencia()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new ContactoUrgencia(resultado.getInt("codContactoUrgencia"),
                                               resultado.getString("nombres"),
                                               resultado.getString("apellidos"),
                                               resultado.getString("numContacto"),
                                               resultado.getInt("codPaciente")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarContactoUrgencia = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        cmbCodContactoUrgencia.getSelectionModel().select(buscarContactoUrgencia(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodContactoUrgencia()));
        txtNombres.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNombres());
        txtApellidos.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getApellidos());
        txtNumeroContacto.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNumContacto());
        txtCodPaciente.setText(("" +((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodPaciente()));
    }
    
    public ContactoUrgencia buscarContactoUrgencia (int codContactoUrgencia){
        ContactoUrgencia resultado = null;
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarContactoUrgencia(?)}");
        procedimiento.setInt(1, codContactoUrgencia);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
                resultado = new ContactoUrgencia(registro.getInt("codContactoUrgencia"),
                                                 registro.getString("nombres"),
                                                 registro.getString("apellidos"),
                                                 registro.getString("numContacto"),
                                                 registro.getInt("codPaciente")        
                );
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
        ContactoUrgencia registro = new ContactoUrgencia();
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setCodPaciente(Integer.parseInt(txtCodPaciente.getText()));
        registro.setNumContacto(txtNumeroContacto.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarContactoUrgencia(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombres());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNumContacto());
            procedimiento.setInt(4, registro.getCodPaciente());
            procedimiento.execute();
            listarContactoUrgencia.add(registro);
        }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
    }

        public void editar (){
        switch(tipoDeOperacion){
            case NINGUNO:
                    activarControles();
                if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnReportar.setDisable(true);
                    
                    tipoDeOperacion = operaciones.Actualizar;
                    break;
                }else{
                    JOptionPane.showConfirmDialog(null,"Debe seleccionar un registro para editar.");
                }
                break;
            case Actualizar:
                actualizar();
                btnEditar.setText("Editar");
                btnReportar.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnReportar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos ();
                break;
        }
    }
            
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarContactoUrgencia(?,?,?,?,?)}");
            ContactoUrgencia registro = (ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem();
            registro.setCodContactoUrgencia(Integer.parseInt(cmbCodContactoUrgencia.getSelectionModel().getSelectedItem().toString()));
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setNumContacto(txtNumeroContacto.getText());
            registro.setCodPaciente(Integer.parseInt(txtCodPaciente.getText()));
            
            procedimiento.setInt(1, registro.getCodContactoUrgencia());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNumContacto());
            procedimiento.setInt(5, registro.getCodPaciente());
            procedimiento.execute();
            
        }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
    }
    

        
    public void desactivarControles(){
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtNumeroContacto.setEditable(false);
        txtCodPaciente.setEditable(false);
    }
    
    public void activarControles(){
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtNumeroContacto.setEditable(true);
        txtCodPaciente.setEditable(true);
    }
    
    public void limpiarControles (){
        txtNombres.setText("");
        txtApellidos.setText("");
        txtNumeroContacto.setText("");
        txtCodPaciente.setText("");
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
                if (tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?","Eliminar el Contacto de Urgencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement eliminar = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarContactoUrgencia(?)}");
                            eliminar.setInt(1, ((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodContactoUrgencia());
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
            btnAgregar.setText("Agregar");
            btnEliminar.setText("Eliminar");
            btnEditar.setText("Editar");
            btnEditar.setDisable(false);
            btnReportar.setDisable(false);
            btnAgregar.setDisable(false);
    }
    
    public void imprimirReporte (){
        if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
            int codContactoUrgencia = ((ContactoUrgencia) tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodContactoUrgencia();
            Map parametros = new HashMap();
            parametros .put("p_codContactoUrgencia", codContactoUrgencia);
            GenerarReporte.mostrarReporte("reporteBuscarContactoUrgencia.jasper", "Reporte de Contacto Urgencia", parametros);
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
