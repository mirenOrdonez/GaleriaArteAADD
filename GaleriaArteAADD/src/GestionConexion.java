
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mirenordonezdearce
 */
public class GestionConexion {
    Connection conexion = null;
    
    public GestionConexion() {
        //Creamos constructor para que al ejecutar, conecte con la BBDD.
        try {
            String urll = "jdbc:mysql://localhost:3306/galeria_arte?serverTimezone=UTC";
            String user = "root";
            String password = "root1234";
            
            conexion = DriverManager.getConnection(urll, user, password);
            
            if (conexion != null) {
               System.out.println("Conectado correctamente a la BBDD galeria_arte."); 
            }
            else {
                System.out.println("Error al conectar a la BBDD.");
            }
            
        } catch (Exception ex) {
            System.out.println("Error en la conexión." + ex.toString());
        }
    }
    
    public void desconectar() {
        try {
            //Cerramos la conexión.
            conexion.close();
            System.out.println("Desconectado correctamente de la BBDD galeria_arte.");
        } catch (Exception ex) {
            System.out.println("Error al desconectar de la BBDD." + ex.toString());
        }
    }
    
    //--------------------------------------------------
    
    //PARA MOSTRAR LAS TABLAS
    public ResultSet mostrarTabla(String consulta) {
        //Guardamos en el ResultSet 'datos' el resultado de la ejecución de la consulta,
        //y lo devolvemos.
        Statement sta;
        ResultSet datos = null;
        try {
            sta = conexion.createStatement();
            datos =  sta.executeQuery(consulta);
        } catch(Exception ex) {
            System.out.println(ex.toString());
        } 
        //No cerramos ni el Statement ni el ResultSet porque debe devolver valores y requiere
        //que siga abierto, pero desde el método en el que lo llamamos, lo cerramos. 
        return datos;
    }
}
