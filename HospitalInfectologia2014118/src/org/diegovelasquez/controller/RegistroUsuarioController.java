package org.diegovelasquez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.diegovelasquez.bean.Estado;
import org.diegovelasquez.bean.RegistroUsuario;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.sistema.Principal;


public class RegistroUsuarioController implements Initializable{
    
    
    private enum operaciones {Agregar, Guardar,Editar,Eliminar,Actualizar,Cancelar,Reporte,NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <RegistroUsuario> listarRegistroUsuario;
    private ObservableList<Estado> ListarEstado;
    
    @FXML private ComboBox cmbCodUsuario;
    @FXML private TextField txtUsuarioLogin;
    @FXML private TextField txtContrasena;
    @FXML private TextField txtHorario;
    @FXML private GridPane grpFecha;
    private DatePicker dtpFecha;
    @FXML private TableView tblRegistroUsuario;
    @FXML private TableColumn colUsuario;
    @FXML private TableColumn colContrasena;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colCodUsuario;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnCancelar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ListarEstado = FXCollections.observableArrayList();
        cargarDatos ();
        
        
        dtpFecha = new DatePicker(Locale.ENGLISH);
        dtpFecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpFecha.getCalendarView().todayButtonTextProperty().set("Today");
        grpFecha.add(dtpFecha, 0, 0);
        
        
    }
    
    
    public void cargarDatos(){
        tblRegistroUsuario.setItems(getRegistroUsuario());
        colUsuario.setCellValueFactory(new PropertyValueFactory<RegistroUsuario, String>("usuarioLogin"));
        colCodUsuario.setCellValueFactory(new PropertyValueFactory <RegistroUsuario,Integer>("codTipoUsuario"));
        colContrasena.setCellValueFactory(new PropertyValueFactory<RegistroUsuario,String>("usuarioContrasena"));
        colEstado.setCellValueFactory(new PropertyValueFactory<RegistroUsuario, Boolean>("usuarioEstado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<RegistroUsuario, Date>("usuarioFecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<RegistroUsuario, String>("usuarioHora"));
    }
    
   public ObservableList<RegistroUsuario> getRegistroUsuario(){
        ArrayList<RegistroUsuario> lista = new ArrayList<RegistroUsuario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarUsuarios()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new RegistroUsuario (resultado.getInt("codUsuario"),
                                               resultado.getString("usuarioLogin"),
                                               resultado.getString("usuarioContrasena"),
                                               resultado.getBoolean("usuarioEstado"),
                                               resultado.getDate("usuarioFecha"),
                                               resultado.getString("usuarioHora"),
                                               resultado.getInt("codTipoUsuario")
                ));    
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarRegistroUsuario = FXCollections.observableList(lista);
    }
   
    public ObservableList<Estado> getEstado(){
        ArrayList<Estado> lista = new ArrayList<Estado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEstado()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Estado(resultado.getBoolean("usuarioEstado")
                ));    
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ListarEstado = FXCollections.observableList(lista);
    }
   
    
    public void seleccionarElemento(){
        cmbCodUsuario.getSelectionModel().select(buscarRegistroUsuario(((RegistroUsuario)tblRegistroUsuario.getSelectionModel().getSelectedItem()).getCodTipoUsuario()));
        
        dtpFecha.selectedDateProperty().set(((RegistroUsuario)tblRegistroUsuario.getSelectionModel().getSelectedItem()).getUsuarioFecha());
        txtUsuarioLogin.setText(((RegistroUsuario)tblRegistroUsuario.getSelectionModel().getSelectedItem()).getUsuarioLogin());
        txtContrasena.setText(((RegistroUsuario)tblRegistroUsuario.getSelectionModel().getSelectedItem()).getUsuarioContrasena());
        txtHorario.setText(((RegistroUsuario)tblRegistroUsuario.getSelectionModel().getSelectedItem()).getUsuarioHora());
    }
    
    public  RegistroUsuario buscarRegistroUsuario (int codUsuario){
        RegistroUsuario resultado = null;
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarUsuarios(?)}");
        procedimiento.setInt(1, codUsuario);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
                resultado = new RegistroUsuario(registro.getInt("codUsuario"),
                                                registro.getString("usuarioLogin"),
                                                registro.getString("usuarioContrasena"),
                                                registro.getBoolean("usuarioEstado"),
                                                registro.getDate("usuarioFecha"),
                                                registro.getString("usuarioHora"),
                                                registro.getInt("codTipoUsuario")
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
               btnEliminar.setDisable(true);
               btnEditar.setDisable(true);
               ListarEstado.add(new Estado(false));
               ListarEstado.add(new Estado (true));
               cmbCodUsuario.setItems(getRegistroUsuario());
               tipoDeOperacion = operaciones.Guardar;
               break;
           case Guardar:
               Guardar();
               desactivarControles();
               limpiarControles();
               btnAgregar.setText("Guardar");
               btnEliminar.setDisable(false);
               btnEditar.setDisable(false);
               tipoDeOperacion = operaciones.NINGUNO;
               cargarDatos();
               break;
       }
    } 
    
public void Guardar (){
        RegistroUsuario registro = new RegistroUsuario();
        registro.setUsuarioLogin(txtUsuarioLogin.getText());
        registro.setUsuarioContrasena(txtContrasena.getText());
        registro.setUsuarioEstado(Boolean.logicalAnd(true, false));
        registro.setUsuarioFecha((java.sql.Date) dtpFecha.getSelectedDate());
        registro.setUsuarioHora(txtHorario.getText());
        
      try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_AgregarUsuarios(?,?,?,?,?,?)}"); 
        procedimiento.setString(1, registro.getUsuarioLogin());
        procedimiento.setString(2, registro.getUsuarioContrasena());
        procedimiento.setBoolean(3, registro.getUsuarioEstado());
        procedimiento.setDate(4, new java.sql.Date(registro.getUsuarioFecha().getTime()));
        procedimiento.setString(5, registro.getUsuarioHora());
        procedimiento.setInt(6, registro.getCodUsuario());
        procedimiento.execute();
        listarRegistroUsuario.add(registro);
        }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
        
     }

     public void Eliminar (){
    switch(tipoDeOperacion){
       default:
                if(tblRegistroUsuario.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea elminar el registro de Usuario!!!", "Usuarios", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta== JOptionPane.YES_OPTION)
                    {
                     try{
                         PreparedStatement eliminar = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarUsuarios(?)}");
                         eliminar.setInt(1, ((RegistroUsuario)tblRegistroUsuario.getSelectionModel().getSelectedItem()).getCodUsuario());
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
     
     
     public void cancelar (){
       btnAgregar.setText("Agregar");
       btnAgregar.setDisable(false);
       btnEditar.setText("Editar");
       btnEditar.setDisable(false);
       btnEliminar.setDisable(false);
       
     } 
     
     public void desactivarControles (){
        txtUsuarioLogin.setEditable(false);
        txtContrasena.setEditable(false);
        dtpFecha.setDisable(true);
        txtHorario.setEditable(false);
    }
    
    public void activarControles(){
        txtUsuarioLogin.setEditable(true);
        txtContrasena.setEditable(true);
        dtpFecha.setDisable(false);
        txtHorario.setEditable(true);
} 
    
    public void limpiarControles(){
        txtUsuarioLogin.setText("");
        txtContrasena.setText("");
        txtHorario.setText("");
    }


    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
}
