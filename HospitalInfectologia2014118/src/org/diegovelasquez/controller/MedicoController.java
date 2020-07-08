
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.diegovelasquez.bean.Medico;
import org.diegovelasquez.db.Conexion;
import org.diegovelasquez.report.GenerarReporte;
import org.diegovelasquez.sistema.Principal;

/**
 *
 * @author DAV4
 */

public class MedicoController implements Initializable{
    private enum operaciones {Nuevo,Guardar, Editar, Eliminar, Actualizar, Cancelar, Reportar, NINGUNO};
    public Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Medico> listarMedico;

    @FXML private TextField txtLicenciaMedica;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTurnoMaximo;
    @FXML private TextField txtSexo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReportar;
    @FXML private Button btnReporteGeneral;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private TableView tblMedicos;
    @FXML private TableColumn colCodMedico;
    @FXML private TableColumn colLicenciaMedica;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colHoraEntrada;
    @FXML private TableColumn colHoraSalida;
    @FXML private TableColumn colTurnoMaximo;
    @FXML private TableColumn colSexo;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblMedicos.setItems(getMedicos());
        colCodMedico.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("codMedico"));
        colLicenciaMedica.setCellValueFactory(new PropertyValueFactory<Medico, Integer> ("LicenciaMedica"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Medico, String>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Medico, String>("apellidos"));
        colHoraEntrada.setCellValueFactory(new PropertyValueFactory<Medico, String>("horaEntrada"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory<Medico, String>("HoraSalida"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Medico, String>("sexo"));
        colTurnoMaximo.setCellValueFactory(new PropertyValueFactory<Medico, String>("turnoMaximo"));
    }
    
    public ObservableList<Medico> getMedicos(){
        ArrayList<Medico> lista = new ArrayList<Medico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Medico (resultado.getInt("codMedico"),
                                      resultado.getInt("licenciaMedica"),
                                      resultado.getString("nombre"),
                                      resultado.getString("apellidos"),
                                      resultado.getString("horaEntrada"),
                                      resultado.getString("horaSalida"),
                                      resultado.getString("sexo"),
                                      resultado.getInt("turnoMaximo")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listarMedico = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento (){
        txtLicenciaMedica.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica()));
        txtNombre.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getNombre());
        txtApellidos.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getApellidos());
        txtHoraEntrada.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada());
        txtHoraSalida.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida());
        txtSexo.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getSexo());
        txtTurnoMaximo.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getTurnoMaximo()));
    }
            
    
    public  Medico buscarMedico (int codMedico){
        Medico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedico(?)}");
            procedimiento.setInt(1, codMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medico (registro.getInt("codMedico"),
                                        registro.getInt("licenciaMedica"),
                                        registro.getString("nombre"),
                                        registro.getString("apellidos"),
                                        registro.getString("horaEntrada"),
                                        registro.getString("horaSalida"),
                                        registro.getString("sexo"),
                                        registro.getInt("turnoMaximo")); 
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void editar (){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblMedicos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnReportar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.Actualizar;
                }else{
                    JOptionPane.showConfirmDialog(null,"Debe seleccionar un elemento.");
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
    
    public void actualizar (){
        try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarMedicos(?,?,?,?,?,?,?)}");
        Medico registro = (Medico)tblMedicos.getSelectionModel().getSelectedItem();
        registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
        registro.setNombre(txtNombre.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setHoraEntrada(txtHoraEntrada.getText());
        registro.setHoraSalida(txtHoraSalida.getText());
        registro.setSexo(txtSexo.getText());
        
        procedimiento.setInt(1, registro.getCodMedico());
        procedimiento.setInt(2, registro.getLicenciaMedica());
        procedimiento.setString(3, registro.getNombre());
        procedimiento.setString(4, registro.getApellidos());
        procedimiento.setString(5, registro.getHoraEntrada());
        procedimiento.setString(6, registro.getHoraSalida());
        procedimiento.setString(7, registro.getSexo());
        procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar (){
        switch(tipoDeOperacion){
            case Guardar:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportar.setDisable(false);
                tipoDeOperacion = operaciones.Nuevo;
                break;
            case Actualizar:
                cancelar();
                break;
            default:
                if (tblMedicos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?","Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicos(?)}");
                            procedimiento.setInt(1, ((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodMedico());
                            procedimiento.execute();
                            listarMedico.remove(tblMedicos.getSelectionModel().getSelectedIndex());
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
    
     public void nuevo (){
       switch (tipoDeOperacion){
       // Guardar datos, ciclo.
           case NINGUNO: 
                activarControles ();
               btnAgregar.setText("Nuevo");
               btnEliminar.setText("Cancelar");
               btnEditar.setDisable(true);
               btnReportar.setDisable(true);
               tipoDeOperacion = operaciones.Guardar;
        //       
               break;
           case Guardar:
               guardar();
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
    
    
    public void guardar (){
        Medico registro = new Medico();
        registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
        registro.setNombre(txtNombre.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setHoraEntrada(txtHoraEntrada.getText());
        registro.setHoraSalida(txtHoraSalida.getText());
        registro.setSexo(txtSexo.getText()); 
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicos(?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getLicenciaMedica());
            procedimiento.setString(2, registro.getNombre());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getHoraEntrada());
            procedimiento.setString(5, registro.getHoraSalida());
            procedimiento.setString(6, registro.getSexo());
            procedimiento.execute();
            listarMedico.add(registro);
        }catch (Exception e){
            e.printStackTrace();
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
    
    public void desactivarControles(){
        txtLicenciaMedica.setEditable(false);
        txtNombre.setEditable(false);
        txtApellidos.setEditable(false);
        txtHoraEntrada.setEditable(false);
        txtHoraSalida.setEditable(false);
        txtTurnoMaximo.setEditable(false);
        txtSexo.setEditable(false);   
    }
    
    public void activarControles(){
        txtLicenciaMedica.setEditable(true);
        txtNombre.setEditable(true);
        txtApellidos.setEditable(true);
        txtHoraEntrada.setEditable(true);
        txtHoraSalida.setEditable(true);
        txtTurnoMaximo.setEditable(false);
        txtSexo.setEditable(true);
    }
    
    public void limpiarControles (){
        txtLicenciaMedica.setText("");
        txtNombre.setText("");
        txtApellidos.setText("");
        txtHoraEntrada.setText("");
        txtHoraSalida.setText("");
        txtTurnoMaximo.setText("");
        txtSexo.setText("");
    }
    
    public void imprimirReporte (){
        if(tblMedicos.getSelectionModel().getSelectedItem() != null){
            int codMedico = ((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getCodMedico();
            Map parametros = new HashMap();
            parametros .put("p_codMedico",codMedico);
            GenerarReporte.mostrarReporte("reporteBuscarMedicos.jasper", "Reporte de Medicos", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "¡Debe seleccionar un registro!");
        }
    }
    
    public void imprimirReporte1 (){
        if(tblMedicos.getSelectionModel().getSelectedItem() != null){
            int codMedico = ((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getCodMedico();
            Map parametros = new HashMap();
            parametros .put("p_codMedico",codMedico);
            GenerarReporte.mostrarReporte("reporteMedicoGeneral.jasper", "Reporte General", parametros);
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
    