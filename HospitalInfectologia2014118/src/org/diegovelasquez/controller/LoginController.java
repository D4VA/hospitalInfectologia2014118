
package org.diegovelasquez.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.diegovelasquez.bean.Login;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.sistema.Principal;


public class LoginController implements Initializable{
    private Principal escenarioPrincipal;
    public ObservableList<Login> ListarLogin;
    
    @FXML private TextField txtUsuario;
    @FXML private PasswordField pswContrasena;
    @FXML private ComboBox cmbTipoUsuario;
    @FXML private Button btnLogin;
    @FXML private Button btnCancelar;

    public void Logear(){
        login();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cmbTipoUsuario.setItems(getTipoUsuarios());
    }
    
    public ObservableList<Login>getTipoUsuarios(){
        ArrayList<Login> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTipoUsuario}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
        
            lista.add(new Login(resultado.getInt("codTipoUsuario"),
                                resultado.getString("descripcion")));
            }
        }catch(SQLException e){
        e.getMessage();
        e.printStackTrace();
        }
        ObservableList<Login> ListarLogin;
            return ListarLogin = FXCollections.observableList(lista);
    }
    
     public Login buscarTipoUsuario(int codTipoUsuario){
         Login resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTipoUsuario(?)}");
            procedimiento.setInt(1, codTipoUsuario);
            ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
                resultado = new Login(registro.getInt("codTipoUsuario"),
                                      registro.getString("descripcion"));
            }
       }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public LoginController(){
        con = Conexion.getInstancia().getConexion();
    }
    
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    private void login(){
        String usuarioLogin = txtUsuario.getText().toString();
        String usuarioContrasena = pswContrasena.getText().toString();
        Integer codTipoUsuario = (((Login) cmbTipoUsuario.getSelectionModel().getSelectedItem()).getCodTipoUsuario());

        String sql = "SELECT  usuarioLogin, usuarioContrasena FROM Usuarios WHERE usuarioLogin = ? and usuarioContrasena = ? and codTipoUsuario = ?";
        
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, usuarioLogin);
            preparedStatement.setString(2, usuarioContrasena);
            preparedStatement.setInt(3, codTipoUsuario);
            resultSet = preparedStatement.executeQuery();
            if (! resultSet.next()){
                 JOptionPane.showMessageDialog(null, "Usuario o/y contraseña incorrectos.");
                 
            }  else{
                JOptionPane.showMessageDialog(null, "Inicio de sesión correcto");
                menuPrincipal();              
                    }   
        } catch (Exception e) {
           e.printStackTrace();
        }       
    }      
    
    
    
    public void cancelar(){
    limpiarBotones();
    }
    
    public void limpiarBotones(){
        txtUsuario.setText("");
        pswContrasena.setText("");
        cmbTipoUsuario.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal EscenarioPrincipal) {
        this.escenarioPrincipal = EscenarioPrincipal;
    }
    
    
    public void logear(){
        escenarioPrincipal.login();
    }
    
    public void menuPrincipal (){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void registroUsuario (){
        escenarioPrincipal.registroUsuario();
    }
    
}
    
