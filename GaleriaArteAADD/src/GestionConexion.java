
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
            
        } catch (SQLException ex) {
            System.out.println("Error en la conexión." + ex.toString());
        }
    }
    
    public void desconectar() {
        try {
            //Cerramos la conexión.
            conexion.close();
            System.out.println("Desconectado correctamente de la BBDD galeria_arte.");
        } catch (SQLException ex) {
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
        } catch(SQLException ex) {
            System.out.println(ex.toString());
        } 
        //No cerramos ni el Statement ni el ResultSet porque debe devolver valores y requiere
        //que siga abierto, pero desde el método en el que lo llamamos, lo cerramos. 
        return datos;
    }
    
    //--------------------------------------------------
    
    //Para llenar el comboBox en la Tab Artistas, con los artistas.
    public ArrayList<String> rellenarComboBoxArtistas() {
        ArrayList<String> listaArtistas = new ArrayList<String>();
        Statement sta;
        try {
            //Creamos el statement y le pasamos la query.
            sta = conexion.createStatement();
            String query = "SELECT codArtista, alias FROM artistas ORDER BY codArtista;";
            ResultSet rs = sta.executeQuery(query);
            
            //Almacenamos en el ArrayList listaArtistas lo obtenido en el ResultSet
            while (rs.next()) {
                listaArtistas.add(rs.getInt("codArtista")+ "- " + rs.getString("alias"));
            }
            
            rs.close();
            sta.close();
        } catch(SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        
        return listaArtistas;
    }

    ArrayList<String> rellenarComboBoxObras() {
        ArrayList<String> listaObras = new ArrayList<String>();
        Statement sta;
        try {
            //Creamos el statement y le pasamos la query.
            sta = conexion.createStatement();
            String query = "SELECT alias FROM artistas;";
            ResultSet rs = sta.executeQuery(query);
            
            //Almacenamos en el ArrayList listaArtistas lo obtenido en el ResultSet
            while (rs.next()) {
                listaObras.add(rs.getString("alias"));
            }
            
            rs.close();
            sta.close();
        } catch(SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        
        return listaObras;
    }

    ArrayList<String> rellenarComboBoxClientes() {
        ArrayList<String> listaClientes = new ArrayList<String>();
        Statement sta;
        try {
            //Creamos el statement y le pasamos la query.
            sta = conexion.createStatement();
            String query = "SELECT nombre FROM clientes ORDER BY nombre;";
            ResultSet rs = sta.executeQuery(query);
            
            //Almacenamos en el ArrayList listaArtistas lo obtenido en el ResultSet
            while (rs.next()) {
                listaClientes.add(rs.getString("nombre"));
            }
            
            rs.close();
            sta.close();
        } catch(SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        
        return listaClientes;
    }
    
    ArrayList<String> rellenarComboBoxVentas() {
        ArrayList<String> listaCodObrasVentas = new ArrayList<String>();
        Statement sta;
        try {
            //Creamos el statement y le pasamos la query.
            sta = conexion.createStatement();
            String query = "SELECT codObra FROM obras ORDER BY codObra;";
            ResultSet rs = sta.executeQuery(query);
            
            //Almacenamos en el ArrayList listaArtistas lo obtenido en el ResultSet
            while (rs.next()) {
                listaCodObrasVentas.add(rs.getString("codObra"));
            }
            
            rs.close();
            sta.close();
        } catch(SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        
        return listaCodObrasVentas;
    }

    ArrayList<String> rellenarComboBoxVentas1() {
        ArrayList<String> listaTituloVentas = new ArrayList<String>();
        Statement sta;
        try {
            //Creamos el statement y le pasamos la query.
            sta = conexion.createStatement();
            String query = "SELECT titulo FROM obras ORDER BY codObra;";
            ResultSet rs = sta.executeQuery(query);
            
            //Almacenamos en el ArrayList listaArtistas lo obtenido en el ResultSet
            while (rs.next()) {
                listaTituloVentas.add(rs.getString("titulo"));
            }
            
            rs.close();
            sta.close();
        } catch(SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        
        return listaTituloVentas;
    }
    
    ArrayList<String> rellenarComboBoxVentas2() {
        ArrayList<String> listaDniCompradorVentas = new ArrayList<String>();
        Statement sta;
        try {
            //Creamos el statement y le pasamos la query.
            sta = conexion.createStatement();
            String query = "SELECT dni FROM clientes ORDER BY dni;";
            ResultSet rs = sta.executeQuery(query);
            
            //Almacenamos en el ArrayList listaArtistas lo obtenido en el ResultSet
            while (rs.next()) {
                listaDniCompradorVentas.add(rs.getString("dni"));
            }
            
            rs.close();
            sta.close();
        } catch(SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        
        return listaDniCompradorVentas;
    }
    
    //--------------------------------------------------
    //Para los filtros
    
    public ResultSet filtrarArtistas(String ciudad) {
       if (ciudad.equals("Sin datos")) {
           ciudad = "NULL";
       }
        
        String query = "SELECT codArtista AS 'C. ARTISTA', dni AS 'DNI',"
                + "nombre AS 'NOMBRE', primer_apellido AS 'PRIMER APELLIDO', segundo_apellido AS "
                + "'SEGUNDO APELLIDO', alias AS 'ALIAS', telf as 'TELÉFONO', direccion AS 'DIRECCIÓN' FROM artistas "
                + "WHERE direccion LIKE '%' ? '%' ORDER BY 'C. ARTISTA';";
        ResultSet datos = null;
        try {
            //Ejecutamos la consulta con el parámetro que nos pasa el usuario.
            PreparedStatement psta = conexion.prepareStatement(query);
            psta.setString(1, ciudad);
            datos = psta.executeQuery();
        
        } catch(SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        return datos;
    }
    
    public ResultSet filtrarObras(String alias) {
        //El usuario selecciona el artista
        //Hay que separar el codigo del alias
        int aux = 0;
        String codArtista = "";
        while (alias.charAt(aux) != '-') {
            codArtista = codArtista + alias.charAt(aux);
            aux++;
        }
        
        String query = "SELECT obras.codObra AS 'C. OBRA', "
                            + "obras.titulo AS 'TÍTULO', "
                            + "obras.publicado_en AS 'AÑO PUBLICACIÓN', "
                            + "obras.estilo AS 'ESTILO', "
                            + "obras.descripcion AS 'DESCRIPCIÓN', "
                            + "obras.precio AS 'PRECIO', "
                            + "obras.artista AS 'C. ARTISTA', "
                            + "artistas.alias AS 'ARTISTA', "
                            + "obras.estado AS 'ESTADO' " 
                        +"FROM obras, artistas "
                        +"WHERE obras.artista = artistas.codArtista AND obras.artista = ? ;";
        ResultSet datos = null;
        try {
            //Ejecutamos la consulta con el parámetro que nos pasa el usuario.
            PreparedStatement psta = conexion.prepareStatement(query);
            psta.setString(1, codArtista);
            datos = psta.executeQuery();
        
        } catch(SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        return datos;
    }
    
    public ResultSet filtrarVentas(String anno) {
        String fechaInicial;
        String fechaFinal;
        
        if (anno.equals("Año")) {
            //Para que no filtre por año.
            fechaInicial = "2000-01-01";
            fechaFinal = "2030-01-01";
        } else {
            fechaInicial = "'" + anno + "-01-01'";
            fechaFinal = "'" + anno + "-12-31'";
        }
        
        String query = "SELECT ventas.codVenta, ventas.codObra, obras.titulo, "
            + "artistas.alias, obras.precio, ventas.dni, ventas.fecha_compra\n" +
            "FROM ventas, obras, artistas\n" +
            "WHERE ventas.codObra = obras.codObra AND artistas.codArtista = obras.artista\n"
            + "AND ventas.fecha_compra BETWEEN " + fechaInicial + " AND " + fechaFinal + 
            " ORDER BY ventas.codVenta;";
        
        
        //El usuario selecciona el año y el precio
        
        ResultSet datos = null;
        try {
            //Ejecutamos la consulta con el parámetro que nos pasa el usuario.
            Statement sta = conexion.createStatement();
            datos = sta.executeQuery(query);
        
        } catch(SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        return datos;
    }
    
    public ResultSet filtrarClientes(String ciudad) {
        
        //El usuario selecciona el nombre
        String query = "SELECT * FROM clientes WHERE direccion LIKE '%' ? '%' ";
        ResultSet datos = null;
        try {
            //Ejecutamos la consulta con el parámetro que nos pasa el usuario.
            PreparedStatement psta = conexion.prepareStatement(query);
            psta.setString(1, ciudad);
            datos = psta.executeQuery();
        
        } catch(SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
        return datos;
    }
    
    //--------------------------------------------------
    //PARA LOS ARTISTAS
    
    public void insertarArtista(String dni, String nombre, String apellido1, String apellido2, 
            String alias, String telf, String dir) {
        Statement sta;
        
        try {
            //Insertamos los datos y ejecutamos.
            sta = conexion.createStatement();
            sta.executeUpdate("INSERT INTO artistas VALUES (0, '" + dni + "', '" + nombre + "', '" + apellido1 + "', '" +
                    apellido2 + "', '" + alias + "', '" + telf + "', '" + dir + "')");
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error al insertar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
    }
    
    public void modifArtistas (String codArtista, String dni, String nombre, String apellido1, String apellido2, 
            String alias, String telf, String dir) {
        Statement sta;
        
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("UPDATE artistas SET dni = '" + dni + "', nombre = '" + nombre + "', primer_apellido = '"
                    + apellido1 + "', segundo_apellido = '" + apellido2 + "', alias = '" + alias + "', telf = '" + 
                    telf + "', direccion = '" + dir + "' WHERE codArtista = " + codArtista + ";");
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error al modificar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
    }
    
    public void deleteArtistas (String codArtista) {
        Statement sta;
        try {
            //Ejecutamos el borrado del registro.
            sta = conexion.createStatement();
            sta.executeUpdate("DELETE FROM artistas WHERE codArtista = " + codArtista + ";");
            sta.close();
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
    }
    
    //--------------------------------------------------
    //PARA LAS OBRAS
    
    public void insertarObra(String titulo, String anno, String estilo, String descripcion, String precio, 
            String artista, String estado) {
        int aux = 0;
        String codArtista = "";
        while (artista.charAt(aux) != '-') {
            codArtista = codArtista + artista.charAt(aux);
            aux++;
        }
        
        Statement sta;
        
        try {
            //Insertamos los datos y ejecutamos.
            sta = conexion.createStatement();
            sta.executeUpdate("INSERT INTO obras VALUES (0, '" + titulo + "', '" + anno + "', '" + estilo + "', '" +
                    descripcion + "', '" + precio + "', '" + codArtista + "', '" + estado + "')");
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error al insertar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
    }
    
    public void modifObras (String codObra, String titulo, String anno, String estilo, String descripcion, String precio, 
            String artista, String estado) {
        
        int aux = 0;
        String codArtista = "";
        while (artista.charAt(aux) != '-') {
            codArtista = codArtista + artista.charAt(aux);
            aux++;
        }
        
        Statement sta;
        
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("UPDATE obras SET titulo = '" + titulo + "', publicado_en = '" + anno + "', "
                    + "estilo = '" + estilo + "', descripcion = '" + descripcion + "', precio = '" + precio + "', "
                    + "artista = '" + codArtista + "', estado = '" + estado + "' WHERE codObra = " + codObra + ";");
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error al modificar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
    }
    
    public void deleteObras (String codObra) {
        Statement sta;
        try {
            //Ejecutamos el borrado del registro.
            sta = conexion.createStatement();
            sta.executeUpdate("DELETE FROM obras WHERE codObra = " + codObra + ";");
            sta.close();
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
    }
    
    //--------------------------------------------------
    //PARA LOS CLIENTES
    
    public void insertarCliente (String dni, String nombre, String apellido1, String apellido2, 
            String telf, String dir) {
        Statement sta;
        
        try {
            //Insertamos los datos y ejecutamos.
            sta = conexion.createStatement();
            sta.executeUpdate("INSERT INTO clientes VALUES ('" + dni + "', '" + nombre + "', '" + apellido1 + "', '" +
                    apellido2 + "', '" + telf + "', '" + dir + "')");
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error al insertar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
    }
    
    public void modifCliente (String dniAntiguo, String dniNuevo, String nombre, String apellido1, String apellido2, 
            String telf, String dir) {
        
        Statement sta;
        
        try {
            sta = conexion.createStatement();
            sta.executeUpdate("UPDATE clientes SET dni = '" + dniNuevo + "', nombre = '" + nombre + "', "
                    + "primer_apellido = '" + apellido1 + "', segundo_apellido = '" + apellido2 + "',"
                            + " telf = '" + telf + "', direccion = '" + dir +  "' WHERE dni = '" + dniAntiguo + "';");
            sta.close();
        } catch (SQLException ex) {
            System.out.println("Error al modificar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
        }
    }
    
    public void deleteClientes (String dni) {
        Statement sta;
        try {
            //Ejecutamos el borrado del registro.
            sta = conexion.createStatement();
            sta.executeUpdate("DELETE FROM clientes WHERE dni = '" + dni + "';");
            sta.close();
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
    }
    
    //--------------------------------------------------
    //PARA LAS VENTAS
    
    public void insertarVenta (String codObra, String dni, String fechaCompra) {
        //Cambiamos el orden de la fecha para que se inserte bien en la BBDD
        String fecha_compra;
        String dia = fechaCompra.substring(0,2);
        String mes = fechaCompra.substring(3,5);
        String anno = fechaCompra.substring(6,10);
        fecha_compra = anno + "-"+ mes + "-" + dia;
        
        Statement sta;
        
        try {
            //Quitamos el commit automático
            conexion.setAutoCommit(false);
            //Insertamos los datos y ejecutamos.
            sta = conexion.createStatement();
            //Insertamos los datos de la nueva venta
            sta.executeUpdate("INSERT INTO ventas VALUES (0, '" + codObra + "', '" + dni + "', '" + fecha_compra + "')");
            
            //Modificamos el estado de la obra que se ha vendido, a Vendido
            sta.executeUpdate("UPDATE obras SET estado = 'Vendido' WHERE codObra = '" + codObra + "';");
            
            sta.close();
            
            conexion.commit();
        } catch (SQLException ex) {
            System.out.println("Error al insertar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException exl) {
                    System.out.println(exl.toString());
                }
            }
        }
    }
    
    
    public void deleteVentas (String codVenta, String codObra) {
        Statement sta;
        try {
            //Quitamos el commit automático
            conexion.setAutoCommit(false);
            
            sta = conexion.createStatement();
            //Ejecutamos el borrado del registro.
            sta.executeUpdate("DELETE FROM ventas WHERE codVenta = '" + codVenta + "';");
            //Modificamos el estado de la obra
            sta.executeUpdate("UPDATE obras SET estado = 'Disponible' WHERE codObra = '" + codObra + "';");
            
            sta.close();
            conexion.commit();
        } catch (SQLException ex) {
            System.out.println("Error al borrar los datos a la BBDD." + "\n" + "ERROR: " + ex.toString());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException exl) {
                    System.out.println(exl.toString());
                }
            }
        }
    }
    
    public void modifVentas (String codVenta, String codObra, String dni, String fechaCompra) {
        //Cambiamos el orden de la fecha para que se inserte bien en la BBDD
        String fecha_compra;
        String dia = fechaCompra.substring(0,2);
        String mes = fechaCompra.substring(3,5);
        String anno = fechaCompra.substring(6,10);
        fecha_compra = anno + "-"+ mes + "-" + dia;
        Statement sta;
        try {
            sta = conexion.createStatement();
            //Ejecutamos el borrado del registro.
            sta.executeUpdate("UPDATE ventas SET codObra = '" + codObra + "', dni = '" + dni + "', "
                    + "fecha_compra = '" + fecha_compra + "' WHERE codVenta = '" + codVenta + "';");
            
            sta.close();
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.toString());
        }
    }
}


