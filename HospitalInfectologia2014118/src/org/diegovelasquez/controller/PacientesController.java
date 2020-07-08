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
import org.diegovelasquez.bean.Paciente;
import org.diegovelasquez.sistema.Principal;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.report.GenerarReporte;

public class PacientesController implements Initializable{
    
    private enum operaciones {Agregar,Guardar, Editar, Eliminar, Actualizar, Cancelar, Reportar, NINGUNO};
    public Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Paciente> listarPaciente;
    
    @FXML private ComboBox cmbcodPaciente;
    @FXML private TextField txtdpi;
    @FXML private TextField txtapellidos;
    @FXML private TextField txtnombres;
    @FXML private TextField txtfechaDeNacimiento;
    @FXML private TextField txtedad;
    @FXML private TextField txtdireccion;
    @FXML private TextField txtocupacion;
    @FXML private TextField txtsexo;
    @FXML private TableColumn colcodPaciente;
    @FXML private TableColumn coldpi;
    @FXML private TableColumn colapellidos;
    @FXML private TableColumn colnombres;
    @FXML private TableColumn colfechaDeNacimiento;
    @FXML private TableColumn coledad;
    @FXML private TableColumn coldireccion;
    @FXML private TableColumn colocupacion;
    @FXML private TableColumn colsexo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportar;
    @FXML private TableView tblPacientes;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbcodPaciente.setItems(getPacientes());
    
    }  
    public void cargarDatos(){
        tblPacientes.setItems(getPacientes()); 
        colcodPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codPaciente"));
        coldpi.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("dpi"));
        colapellidos.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellidos"));
        colnombres.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombre"));    
        colfechaDeNacimiento.setCellValueFactory(new PropertyValueFactory <Paciente, String>("fechaNacimiento"));
        coledad.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("edad"));
        coldireccion.setCellValueFactory(new PropertyValueFactory <Paciente, String>("direccion"));
        colocupacion.setCellValueFactory(new PropertyValueFactory <Paciente, String>("ocupacion"));
        coledad.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("edad"));
        colsexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo"));
        
                }    
    public ObservableList<Paciente> getPacientes(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Paciente(resultado.getInt("codPaciente"),
                                    resultado.getString("dpi"),
                                    resultado.getString("apellidos"),
                                    resultado.getString("nombre"),
                                    resultado.getString("fechaNacimiento"),
                                    resultado.getInt("edad"),
                                    resultado.getString("direccion"),
                                    resultado.getString("ocupacion"),
                                    resultado.getString("sexo")
                     
                
                ));    
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarPaciente = FXCollections.observableList(lista);
    }
     public void seleccionarElemento(){
         cmbcodPaciente.getSelectionModel().select(buscarPaciente(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodPaciente()));
         txtdpi.setText(((Paciente) tblPacientes.getSelectionModel().getSelectedItem()).getDpi());
         txtnombres.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombre());
         txtapellidos.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellidos());
         txtfechaDeNacimiento.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
         txtdireccion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccion());
         txtocupacion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getOcupacion());
         txtedad.setText(("" + ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getEdad()));
         txtsexo.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
    }
     public Paciente buscarPaciente(int codPaciente){
            Paciente resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPacientes(?)}");
                procedimiento.setInt(1, (codPaciente));
                ResultSet registro =  procedimiento.executeQuery();
                while(registro.next()){
                                resultado = new Paciente(registro.getInt("codPaciente")
                                                       ,registro.getString("dpi")
                                                       ,registro.getString("apellidos")
                                                       ,registro.getString("nombre")
                                                       ,registro.getString("fechaNacimiento")
                                                       ,registro.getInt("edad")
                                                       ,registro.getString("direccion")
                                                       ,registro.getString("ocupacion")
                                                       ,registro.getString("sexo"));
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
        Paciente registro = new Paciente();
        registro.setDpi((txtdpi.getText()));
        registro.setApellidos(txtapellidos.getText());     
        registro.setNombre(txtnombres.getText());
        registro.setFechaNacimiento(txtfechaDeNacimiento.getText());
        registro.setDireccion(txtdireccion.getText());
        registro.setOcupacion(txtocupacion.getText());
        registro.setSexo(txtsexo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_AgregarPacientes (?,?,?,?,?,?,?)}"); 
        procedimiento.setString(1, registro.getDpi());
        procedimiento.setString(2, registro.getApellidos());
        procedimiento.setString(3, registro.getNombre());
        procedimiento.setString(4, registro.getFechaNacimiento());
        procedimiento.setString(5, registro.getDireccion());
        procedimiento.setString(6, registro.getOcupacion());
        procedimiento.setString(7, registro.getSexo());
        procedimiento.execute();
        listarPaciente.add(registro);
        }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
    }
        public void editar (){
         
         switch(tipoDeOperacion){
             case NINGUNO:
                if (tblPacientes.getSelectionModel().getSelectedItem() != null ){
                    activarControles();
                 btnEditar.setText("Actualizar");
                 btnEliminar.setText("Cancelar");
                 tipoDeOperacion = operaciones.Actualizar;
                 btnAgregar.setDisable(true);
                 btnReportar.setDisable(true);
                 break;
             }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un registro para editar");
                break;
               }
                case Actualizar:
                actualizar();
                btnEditar.setText("Editar");
                btnReportar.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnReportar.setDisable(false);
                cargarDatos();
                break;    
         }
        
     }  
        
     public void actualizar(){
         try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarPacientes(?,?,?,?,?,?,?,?)}");
             Paciente registro = (Paciente) tblPacientes.getSelectionModel().getSelectedItem();
             registro.setCodPaciente(Integer.parseInt(cmbcodPaciente.getSelectionModel().getSelectedItem().toString()));
             registro.setDpi((txtdpi.getText()));
             registro.setApellidos(txtapellidos.getText());
             registro.setNombre(txtnombres.getText());
             registro.setFechaNacimiento(txtfechaDeNacimiento.getText());
             registro.setDireccion(txtdireccion.getText());
             registro.setOcupacion(txtocupacion.getText());
             registro.setSexo(txtsexo.getText());
             
             procedimiento.setInt(1, registro.getCodPaciente());    
             procedimiento.setString(2, registro.getDpi());
             procedimiento.setString(3, registro.getApellidos());
             procedimiento.setString(4, registro.getNombre());
             procedimiento.setString(5, registro.getFechaNacimiento());
             procedimiento.setString(6, registro.getDireccion());
             procedimiento.setString(7, registro.getOcupacion());
             procedimiento.setString(8, registro.getSexo());
             procedimiento.execute();
             
         }catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
        }
         
     
        
    }  
        public void desactivarControles (){
        txtdpi.setEditable(false);
        txtnombres.setEditable(false);
        txtapellidos.setEditable(false);
        txtfechaDeNacimiento.setEditable(false);
        txtdireccion.setEditable(false);
        txtocupacion.setEditable(false);
        txtsexo.setEditable(false);
    }
    
    public void activarControles(){
        txtdpi.setEditable(true);
        txtnombres.setEditable(true);
        txtapellidos.setEditable(true);
        txtfechaDeNacimiento.setEditable(true);
        txtdireccion.setEditable(true);
        txtocupacion.setEditable(true);
        txtsexo.setEditable(true);
}
    public void limpiarControles(){
        txtdpi.setText("");
        txtnombres.setText("");
        txtapellidos.setText("");
        txtfechaDeNacimiento.setText("");
        txtedad.setText("");
        txtdireccion.setText("");
        txtocupacion.setText("");
        txtsexo.setText("");
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
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                    
                    int respuesta = JOptionPane.showConfirmDialog(null, "Desea elminar el registro de pacientes!!!", "Pacientes", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta== JOptionPane.YES_OPTION)
                    {
                     try{
                         PreparedStatement eliminar = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarPacientes(?)}");
                         eliminar.setInt(1, ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodPaciente());
                         eliminar.execute();
                         cargarDatos();
                         
                     }catch (SQLException ex){
                                ex.printStackTrace();
                        }
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

       public void imprimirReporte (){
        if(tblPacientes.getSelectionModel().getSelectedItem() != null){
            int codPaciente = ((Paciente) tblPacientes.getSelectionModel().getSelectedItem()).getCodPaciente();
            Map parametros = new HashMap();
            parametros .put("p_codPaciente", codPaciente);
            GenerarReporte.mostrarReporte("reporteBuscarPaciente.jasper", "Reporte de Paciente", parametros);
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