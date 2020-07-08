
package org.diegovelasquez.db;


import java.sql.DriverManager; 
import java.sql.ResultSet; // respuestas
import java.sql.Connection; 
import java.sql.SQLException; //detecta error
import java.sql.Statement;
import com.mysql.jdbc.Driver;


public class Conexion {
    private Connection conexion;  
      private static Conexion instancia;
     
      private String driver;
      private String url;
      private String usuario;
      private String password;
      private String dbname;
 
      public Conexion() { 
           String dbname = "DBHospitalInfectologia2014118";
 String url = "jdbc:mysql://localhost:3306/"+dbname+"?useSSL=false&zeroDateTimeBehavior=convertToNull";
 String driver = "com.mysql.jdbc.Driver";
 String usuario = "root";
 String password = "admin";
 
    try{
        Class.forName("com.mysql.jdbc.Driver").newInstance();  
        
        
        conexion=DriverManager.getConnection(url, usuario, password);        
       
    }catch(ClassNotFoundException e){ 
            e.printStackTrace();
           e.getMessage();
    }catch(InstantiationException e){ 
            e.printStackTrace();
            e.getMessage();
            
    }catch(IllegalAccessException e){ 
            e.printStackTrace();
            e.getMessage();
    }catch(SQLException e){ 
            e.printStackTrace();
            e.getMessage();
            } 
    }  
    
    public static Conexion getInstancia(){ 
        if(instancia == null){ 
            instancia = new Conexion();
        } 
        return instancia;
    } 


    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
       
}