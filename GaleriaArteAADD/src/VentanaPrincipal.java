
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mirenordonezdearce
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    //Abrimos la conexión con la BBDD, llamando al constructor.
    GestionConexion conexion = new GestionConexion();
    
    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
        mostrarTablaArtistas();
        mostrarTablaObras();
        mostrarTablaVentas();
        mostrarTablaClientes();
        editar();
        
        //Rellenamos el jComboBox para filtrar búsquedas
        //Creamos un arrayList para que muestre en el jComboBox los artistas
        ArrayList<String> listaArtistas = conexion.rellenarComboBoxArtistas();
        for (int i=0; i < listaArtistas.size(); i++) {
            jComboBoxFiltrarObras.addItem(listaArtistas.get(i));
            //Para insertar un nuevo artista, o poder modificarlo en la Tab Obras.
            jComboBoxArtistaObras.addItem(listaArtistas.get(i));
        } 
        
        //Rellenamos los jComboBox en la tab Ventas
        ArrayList<String> listaCodObras = conexion.rellenarComboBoxVentas();
        for (int i=0; i < listaCodObras.size(); i++) {
            jComboBoxObraVentas.addItem(listaCodObras.get(i));
        }
        ArrayList<String> listaTituloObras = conexion.rellenarComboBoxVentas1();
        for (int i=0; i < listaTituloObras.size(); i++) {
            jComboBoxTituloVentas.addItem(listaTituloObras.get(i));
        }
        ArrayList<String> listaDniCompradorVentas = conexion.rellenarComboBoxVentas2();
        for (int i=0; i < listaDniCompradorVentas.size(); i++) {
            jComboBoxCompradorVentas.addItem(listaDniCompradorVentas.get(i));
        }
        
        //Para desconectarse de la BBDD cuando se cierre la interfaz de la Ventana Principal
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                conexion.desconectar();
                
                System.exit(0);
            }
        });
    }
    
    //PARA MOSTRAR LAS TABLAS
    private void mostrarTablaArtistas() {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        //Hacemos la consulta
        ResultSet rs = (ResultSet) conexion.mostrarTabla("SELECT codArtista AS 'C. ARTISTA', dni AS 'DNI',"
                + "nombre AS 'NOMBRE', primer_apellido AS 'PRIMER APELLIDO', segundo_apellido AS "
                + "'SEGUNDO APELLIDO', alias AS 'ALIAS', telf as 'TELÉFONO', direccion AS 'DIRECCIÓN' FROM artistas "
                + "ORDER BY 'C. ARTISTA';");
        
        try {
            //Obtenemos los nombres de las columnas
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               modelo.setColumnIdentifiers(etiquetas);
            }
            while (rs.next()) {
                //Obtenemos los datos de las filas
                modelo.addRow(new Object[]{rs.getInt("C. ARTISTA"), rs.getString("DNI"),
                rs.getString("NOMBRE"), rs.getString("PRIMER APELLIDO"), rs.getString("SEGUNDO APELLIDO"),
                rs.getString("ALIAS"), rs.getInt("TELÉFONO"), rs.getString("DIRECCIÓN")});
            }
            //Rellenamos la tabla con los datos.
            jTableArtistas.setModel(modelo);
            rs.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
    }
    
    private void mostrarTablaObras() {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        //Hacemos la consulta
        ResultSet rs = (ResultSet) conexion.mostrarTabla("SELECT obras.codObra AS 'C. OBRA', obras.titulo AS 'TÍTULO', "
                + "obras.publicado_en AS 'AÑO PUBLICACIÓN', obras.estilo AS 'ESTILO', obras.descripcion AS "
                + "'DESCRIPCIÓN', obras.precio AS 'PRECIO', obras.artista AS 'C. ARTISTA', "
                + "artistas.alias AS 'ARTISTA', obras.estado AS 'ESTADO' \n" +
                "FROM obras, artistas\n" +
                "WHERE obras.artista = artistas.codArtista\n" +
                "ORDER BY codObra;");
        
        try {
            //Obtenemos los nombres de las columnas
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               modelo.setColumnIdentifiers(etiquetas);
            }
            while (rs.next()) {
                //Obtenemos los datos de las filas
                modelo.addRow(new Object[]{rs.getInt("C. OBRA"), rs.getString("TÍTULO"),
                rs.getInt("AÑO PUBLICACIÓN"), rs.getString("ESTILO"), rs.getString("DESCRIPCIÓN"),
                rs.getString("PRECIO"), rs.getInt("C. ARTISTA"), rs.getString("ARTISTA"), rs.getString("ESTADO")});
                
            }
            //Rellenamos la tabla con los datos.
            jTableObras.setModel(modelo);
            rs.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
    }
    
    private void mostrarTablaVentas() {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        //Hacemos la consulta
        ResultSet rs = (ResultSet) conexion.mostrarTabla("SELECT ventas.codVenta AS 'C. VENTA', "
                + "ventas.codObra AS 'C. OBRA', obras.titulo AS 'TITULO', "
                + "artistas.alias AS 'ARTISTA', obras.precio AS 'PRECIO', ventas.dni AS 'COMPRADOR', "
                + "DATE_FORMAT(ventas.fecha_compra,'%d/%m/%Y') AS 'FECHA COMPRA'\n" +
                "FROM ventas, obras, artistas\n" +
                "WHERE ventas.codObra = obras.codObra AND artistas.codArtista = obras.artista\n" +
                "ORDER BY ventas.codVenta;");
        
        try {
            //Obtenemos los nombres de las columnas
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               modelo.setColumnIdentifiers(etiquetas);
            }
            while (rs.next()) {
                //Obtenemos los datos de las filas
                modelo.addRow(new Object[]{rs.getInt("C. VENTA"), rs.getInt("C. OBRA"),
                rs.getString("TITULO"), rs.getString("ARTISTA"), rs.getString("PRECIO"),
                rs.getString("COMPRADOR"), rs.getString("FECHA COMPRA")});
            }
            //Rellenamos la tabla con los datos.
            jTableVentas.setModel(modelo);
            rs.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
    }
    
    private void mostrarTablaClientes() {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        //Hacemos la consulta
        ResultSet rs = (ResultSet) conexion.mostrarTabla("SELECT dni as 'DNI', nombre AS 'NOMBRE', "
                + "primer_apellido AS 'PRIMER APELLIDO', segundo_apellido AS 'SEGUNDO APELLIDO', "
                + "telf AS 'TELÉFONO', direccion AS 'DIRECCIÓN' FROM clientes;");
        
        try {
            //Obtenemos los nombres de las columnas
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               modelo.setColumnIdentifiers(etiquetas);
            }
            while (rs.next()) {
                //Obtenemos los datos de las filas
                modelo.addRow(new Object[]{rs.getString("DNI"), rs.getString("NOMBRE"),
                rs.getString("PRIMER APELLIDO"), rs.getString("SEGUNDO APELLIDO"),
                rs.getInt("TELÉFONO"), rs.getString("DIRECCIÓN")});
            }
            //Rellenamos la tabla con los datos.
            jTableClientes.setModel(modelo);
            rs.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
    }
    
    private void editar() {
        
        //Para que aparezcan en los TextFields los datos de la fila seleccionada.
        //Para la tabla Artistas.
        jTableArtistas.addMouseListener(new MouseAdapter()  {
            @Override
		public void mousePressed(MouseEvent e) { 
                    int filaArtistas = jTableArtistas.getSelectedRow();
                    if (filaArtistas == -1){
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila.");
                    } else {
                        String dniArtista = (String)jTableArtistas.getValueAt(filaArtistas, 1);
                        String nombreArtista = (String)jTableArtistas.getValueAt(filaArtistas, 2);
                        String apellido1Artista = (String)jTableArtistas.getValueAt(filaArtistas, 3);
                        String apellido2Artista = (String)jTableArtistas.getValueAt(filaArtistas, 4);
                        String aliasArtista = (String)jTableArtistas.getValueAt(filaArtistas, 5);
                        String telfArtista = (String)jTableArtistas.getValueAt(filaArtistas, 6).toString();
                        String dirArtista = (String)jTableArtistas.getValueAt(filaArtistas, 7);
                        jTextFieldDniArtistas.setText(dniArtista);
                        jTextFieldNombreArtistas.setText(nombreArtista);
                        jTextFieldApellido1Artistas.setText(apellido1Artista);
                        jTextFieldApellido2Artistas.setText(apellido2Artista);
                        jTextFieldAliasArtistas.setText(aliasArtista);
                        jTextFieldTelfArtistas.setText(telfArtista);
                        jTextFieldDirArtistas.setText(dirArtista);
                    }
                }
        });
        //Para la tabla Obras.
        jTableObras.addMouseListener(new MouseAdapter()  {
            @Override
		public void mousePressed(MouseEvent e) { 
                    int filaObras = jTableObras.getSelectedRow();
                    if (filaObras == -1){
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila.");
                    } else {
                        String tituloObra = (String)jTableObras.getValueAt(filaObras, 1);
                        String annoObra = (String)jTableObras.getValueAt(filaObras, 2).toString();
                        String estiloObra = (String)jTableObras.getValueAt(filaObras, 3);
                        String descripcionObra = (String)jTableObras.getValueAt(filaObras, 4);
                        String precioObra = (String)jTableObras.getValueAt(filaObras, 5).toString();
                        String estadoObra = (String)jTableObras.getValueAt(filaObras, 8);
                        //Para que según el estado de la obra, esté de un color u otro.
                        if (estadoObra.equals("Vendido")) {
                            jTextFieldEstadoObras.setForeground(Color.red);
                        }
                        else {
                            jTextFieldEstadoObras.setForeground(Color.green);
                        }
                        jTextFieldTituloObras.setText(tituloObra);
                        jTextFieldAnnoObras.setText(annoObra);
                        jTextFieldEstiloObras.setText(estiloObra);
                        jTextFieldDescripcionObras.setText(descripcionObra);
                        jTextFieldPrecioObras.setText(precioObra);
                        jTextFieldEstadoObras.setText(estadoObra);
                        jComboBoxArtistaObras.setSelectedIndex((int) jTableObras.getValueAt(filaObras, 6) - 1);
                        
                    }
                }
        });
        //Para la tabla Ventas.
        jTableVentas.addMouseListener(new MouseAdapter()  {
            @Override
		public void mousePressed(MouseEvent e) { 
                    int filaVentas = jTableVentas.getSelectedRow();
                    if (filaVentas == -1){
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila.");
                    } else {
                        String artistaVentas = (String)jTableVentas.getValueAt(filaVentas, 3);
                        String precioVentas = (String)jTableVentas.getValueAt(filaVentas, 4).toString();
                        String fechaVentas = (String)jTableVentas.getValueAt(filaVentas, 6).toString();
                        jComboBoxObraVentas.setSelectedIndex((int)jTableVentas.getValueAt(filaVentas, 1) -1);
                        jComboBoxTituloVentas.setSelectedItem(jTableVentas.getValueAt(filaVentas, 2));
                        jTextFieldArtistaVentas.setText(artistaVentas);
                        jTextFieldPrecioVentas.setText(precioVentas);
                        jComboBoxCompradorVentas.setSelectedItem(jTableVentas.getValueAt(filaVentas, 5));
                        jTextFieldFechaVentas.setText(fechaVentas);
                        
                    }
                    
                }
        });
        //Para la tabla Clientes.
        jTableClientes.addMouseListener(new MouseAdapter()  {
            @Override
		public void mousePressed(MouseEvent e) { 
                    int filaClientes = jTableClientes.getSelectedRow();
                    if (filaClientes == -1){
                    JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila.");
                    } else {
                        String dniClientes = (String)jTableClientes.getValueAt(filaClientes, 0);
                        String nombreClientes = (String)jTableClientes.getValueAt(filaClientes, 1);
                        String apellido1Clientes = (String)jTableClientes.getValueAt(filaClientes, 2);
                        String apellido2Clientes = (String)jTableClientes.getValueAt(filaClientes, 3);
                        String telfClientes = (String)jTableClientes.getValueAt(filaClientes, 4).toString();
                        String dirClientes = (String)jTableClientes.getValueAt(filaClientes, 5);
                        jTextFieldDniClientes.setText(dniClientes);
                        jTextFieldNombreClientes.setText(nombreClientes);
                        jTextFieldApellido1Clientes.setText(apellido1Clientes);
                        jTextFieldApellido2Clientes.setText(apellido2Clientes);
                        jTextFieldTelfClientes.setText(telfClientes);
                        jTextFieldDirClientes.setText(dirClientes);
                    }
                }
        });
        
    }
    
    //PARA FILTRAR LAS BÚSQUEDAS
    private void mostrarTablaFiltroArtista() {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        //Hacemos la consulta
        ResultSet rs = (ResultSet) conexion.filtrarArtistas(jComboBoxFiltrarArtistas.getSelectedItem().toString());
        
        try {
            //Obtenemos los nombres de las columnas
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               modelo.setColumnIdentifiers(etiquetas);
            }
            while (rs.next()) {
                //Obtenemos los datos de las filas
                
                modelo.addRow(new Object[]{rs.getInt("C. ARTISTA"), rs.getString("DNI"),
                rs.getString("NOMBRE"), rs.getString("PRIMER APELLIDO"), rs.getString("SEGUNDO APELLIDO"),
                rs.getString("ALIAS"), rs.getInt("TELÉFONO"), rs.getString("DIRECCIÓN")});
                
            }
            //Rellenamos la tabla con los datos.
            jTableArtistas.setModel(modelo);
            rs.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
    }
    
    private void mostrarTablaFiltroObras() {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        //Hacemos la consulta
        ResultSet rs = (ResultSet) conexion.filtrarObras(jComboBoxFiltrarObras.getSelectedItem().toString());
        
        try {
            //Obtenemos los nombres de las columnas
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               modelo.setColumnIdentifiers(etiquetas);
            }
            while (rs.next()) {
                //Obtenemos los datos de las filas
                modelo.addRow(new Object[]{rs.getInt("C. OBRA"), rs.getString("TÍTULO"),
                rs.getInt("AÑO PUBLICACIÓN"), rs.getString("ESTILO"), rs.getString("DESCRIPCIÓN"),
                rs.getString("PRECIO"),rs.getInt("C. ARTISTA"), rs.getString("ARTISTA"), rs.getString("ESTADO")});
            }
            //Rellenamos la tabla con los datos.
            jTableObras.setModel(modelo);
            rs.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
    }
    
    private void mostrarTablaFiltroVentas() {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        //Hacemos la consulta
        ResultSet rs = (ResultSet) conexion.filtrarVentas(jComboBoxFiltrarVentasAnno.getSelectedItem().toString());
        
        try {
            //Obtenemos los nombres de las columnas
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               modelo.setColumnIdentifiers(etiquetas);
            }
            while (rs.next()) {
                //Obtenemos los datos de las filas
                modelo.addRow(new Object[]{rs.getInt("codVenta"), rs.getInt("codObra"),
                rs.getString("obras.titulo"), rs.getString("artistas.alias"), rs.getString("obras.precio"),
                rs.getString("dni"), rs.getDate("fecha_compra")});
            }
            //Rellenamos la tabla con los datos.
            jTableVentas.setModel(modelo);
            rs.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
    }
    
    private void mostrarTablaFiltroClientes() {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        //Hacemos la consulta
        ResultSet rs = (ResultSet) conexion.filtrarClientes(jComboBoxFiltrarClientes.getSelectedItem().toString());
        
        try {
            //Obtenemos los nombres de las columnas
            ResultSetMetaData metaDatos = rs.getMetaData();
            
            int numeroColumnas = metaDatos.getColumnCount();
            // Se crea un array de etiquetas para rellenar
            String[] etiquetas = new String[numeroColumnas];

            // Se obtiene cada una de las etiquetas para cada columna
            for (int i = 0; i < numeroColumnas; i++)
            {
               etiquetas[i] = metaDatos.getColumnLabel(i + 1);
               modelo.setColumnIdentifiers(etiquetas);
            }
            while (rs.next()) {
                //Obtenemos los datos de las filas
                modelo.addRow(new Object[]{rs.getString("dni"), rs.getString("nombre"),
                rs.getString("primer_apellido"), rs.getString("segundo_apellido"),
                rs.getInt("telf"), rs.getString("direccion")});
            }
            //Rellenamos la tabla con los datos.
            jTableClientes.setModel(modelo);
            rs.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
    }
    
    
   


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        tabArtistas = new javax.swing.JPanel();
        jPanelTablaArtistas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableArtistas = new javax.swing.JTable();
        lblBuscarArtistas = new javax.swing.JLabel();
        jComboBoxFiltrarArtistas = new javax.swing.JComboBox<>();
        btnFiltrarArtistas = new javax.swing.JButton();
        btnMostrarTodoArtistas = new javax.swing.JButton();
        jPanelDatosArtistas = new javax.swing.JPanel();
        lblDniArtistas = new javax.swing.JLabel();
        jTextFieldDniArtistas = new javax.swing.JTextField();
        lblNombreArtistas = new javax.swing.JLabel();
        jTextFieldNombreArtistas = new javax.swing.JTextField();
        lblApellido1Artistas = new javax.swing.JLabel();
        jTextFieldApellido1Artistas = new javax.swing.JTextField();
        btnInsertarArtistas = new javax.swing.JButton();
        btnModifArtistas = new javax.swing.JButton();
        btnDeleteArtistas = new javax.swing.JButton();
        lblAnnoApellido2Artistas = new javax.swing.JLabel();
        jTextFieldApellido2Artistas = new javax.swing.JTextField();
        lblAliasArtistas = new javax.swing.JLabel();
        jTextFieldAliasArtistas = new javax.swing.JTextField();
        lblTelfArtistas = new javax.swing.JLabel();
        jTextFieldTelfArtistas = new javax.swing.JTextField();
        lblDirArtistas = new javax.swing.JLabel();
        jTextFieldDirArtistas = new javax.swing.JTextField();
        lblInfoArtistas = new javax.swing.JLabel();
        tabObras = new javax.swing.JPanel();
        jPanelTablaObras = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableObras = new javax.swing.JTable();
        lblBuscarObras = new javax.swing.JLabel();
        jComboBoxFiltrarObras = new javax.swing.JComboBox<>();
        btnFiltrarObras = new javax.swing.JButton();
        btnMostrarTodoObras = new javax.swing.JButton();
        jPanelDatosObras = new javax.swing.JPanel();
        lblTituloObras = new javax.swing.JLabel();
        jTextFieldTituloObras = new javax.swing.JTextField();
        lblAnnoObras = new javax.swing.JLabel();
        jTextFieldAnnoObras = new javax.swing.JTextField();
        lblEstiloObras = new javax.swing.JLabel();
        jTextFieldEstiloObras = new javax.swing.JTextField();
        btnInsertarObras = new javax.swing.JButton();
        btnModifObras = new javax.swing.JButton();
        btnDeleteObras = new javax.swing.JButton();
        lblDescripcionObras = new javax.swing.JLabel();
        jTextFieldDescripcionObras = new javax.swing.JTextField();
        lblPrecioObras = new javax.swing.JLabel();
        jTextFieldPrecioObras = new javax.swing.JTextField();
        lblArtistaObras = new javax.swing.JLabel();
        lblEstadoObras = new javax.swing.JLabel();
        jTextFieldEstadoObras = new javax.swing.JTextField();
        lblInfoObras = new javax.swing.JLabel();
        jComboBoxArtistaObras = new javax.swing.JComboBox<>();
        tabVentas = new javax.swing.JPanel();
        jPanelTablaVentas = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableVentas = new javax.swing.JTable();
        lblBuscarVentas = new javax.swing.JLabel();
        jComboBoxFiltrarVentasAnno = new javax.swing.JComboBox<>();
        btnFiltrarVentas = new javax.swing.JButton();
        btnMostrarTodoVentas = new javax.swing.JButton();
        jPanelDatosVentas = new javax.swing.JPanel();
        lblCodObraVentas = new javax.swing.JLabel();
        lblTituloVentas = new javax.swing.JLabel();
        lblArtistaVentas = new javax.swing.JLabel();
        btnInsertarVentas = new javax.swing.JButton();
        btnModifVentas = new javax.swing.JButton();
        btnDeleteVentas = new javax.swing.JButton();
        lblPrecioVentas = new javax.swing.JLabel();
        jTextFieldPrecioVentas = new javax.swing.JTextField();
        lblDniCompradorVentas = new javax.swing.JLabel();
        lblFechaVentas = new javax.swing.JLabel();
        jTextFieldFechaVentas = new javax.swing.JTextField();
        lblInfoVentas = new javax.swing.JLabel();
        jComboBoxObraVentas = new javax.swing.JComboBox<>();
        jComboBoxTituloVentas = new javax.swing.JComboBox<>();
        jTextFieldArtistaVentas = new javax.swing.JTextField();
        jComboBoxCompradorVentas = new javax.swing.JComboBox<>();
        tabClientes = new javax.swing.JPanel();
        jPanelTablaClientes = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableClientes = new javax.swing.JTable();
        lblBuscarClientes = new javax.swing.JLabel();
        jComboBoxFiltrarClientes = new javax.swing.JComboBox<>();
        btnFiltrarClientes = new javax.swing.JButton();
        btnMostrarTodoClientes = new javax.swing.JButton();
        jPanelDatosClientes = new javax.swing.JPanel();
        lblDniClientes = new javax.swing.JLabel();
        jTextFieldDniClientes = new javax.swing.JTextField();
        lblNombreClientes = new javax.swing.JLabel();
        jTextFieldNombreClientes = new javax.swing.JTextField();
        lblApellido1Clientes = new javax.swing.JLabel();
        jTextFieldApellido1Clientes = new javax.swing.JTextField();
        btnInsertarClientes = new javax.swing.JButton();
        btnModifClientes = new javax.swing.JButton();
        btnDeleteClientes = new javax.swing.JButton();
        lblApellido2Clientes = new javax.swing.JLabel();
        jTextFieldApellido2Clientes = new javax.swing.JTextField();
        lblTelfClientes = new javax.swing.JLabel();
        jTextFieldTelfClientes = new javax.swing.JTextField();
        lblDirClientes = new javax.swing.JLabel();
        jTextFieldDirClientes = new javax.swing.JTextField();
        lblInfoClientes = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(219, 238, 248));

        tabArtistas.setBackground(new java.awt.Color(0, 153, 153));

        jPanelTablaArtistas.setBackground(new java.awt.Color(0, 153, 153));

        jTableArtistas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTableArtistas);

        lblBuscarArtistas.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lblBuscarArtistas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblBuscarArtistas.setText("Seleccione según localidad:");

        jComboBoxFiltrarArtistas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Madrid", "Barcelona", "Sin datos" }));
        jComboBoxFiltrarArtistas.setMinimumSize(new java.awt.Dimension(231, 27));

        btnFiltrarArtistas.setText("Filtrar búsqueda");
        btnFiltrarArtistas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarArtistasActionPerformed(evt);
            }
        });

        btnMostrarTodoArtistas.setText("Mostrar todo");
        btnMostrarTodoArtistas.setMaximumSize(new java.awt.Dimension(146, 29));
        btnMostrarTodoArtistas.setMinimumSize(new java.awt.Dimension(146, 29));
        btnMostrarTodoArtistas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarTodoArtistasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTablaArtistasLayout = new javax.swing.GroupLayout(jPanelTablaArtistas);
        jPanelTablaArtistas.setLayout(jPanelTablaArtistasLayout);
        jPanelTablaArtistasLayout.setHorizontalGroup(
            jPanelTablaArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaArtistasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTablaArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanelTablaArtistasLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblBuscarArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxFiltrarArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104)
                        .addComponent(btnFiltrarArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMostrarTodoArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelTablaArtistasLayout.setVerticalGroup(
            jPanelTablaArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaArtistasLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanelTablaArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscarArtistas)
                    .addComponent(jComboBoxFiltrarArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrarArtistas)
                    .addComponent(btnMostrarTodoArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelDatosArtistas.setBackground(new java.awt.Color(0, 153, 153));
        jPanelDatosArtistas.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), null));
        jPanelDatosArtistas.setBounds(new java.awt.Rectangle(0, 0, 626, 178));
        jPanelDatosArtistas.setMaximumSize(new java.awt.Dimension(626, 178));

        lblDniArtistas.setText("DNI");

        lblNombreArtistas.setText("NOMBRE");

        lblApellido1Artistas.setText("PRIMER APELLIDO");

        btnInsertarArtistas.setText("Insertar");
        btnInsertarArtistas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarArtistasActionPerformed(evt);
            }
        });

        btnModifArtistas.setText("Modificar");
        btnModifArtistas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifArtistasActionPerformed(evt);
            }
        });

        btnDeleteArtistas.setText("Eliminar");
        btnDeleteArtistas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteArtistasActionPerformed(evt);
            }
        });

        lblAnnoApellido2Artistas.setText("SEGUNDO APELLIDO");

        lblAliasArtistas.setText("ALIAS");

        lblTelfArtistas.setText("TELÉFONO");

        lblDirArtistas.setText("DIRECCIÓN");

        lblInfoArtistas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanelDatosArtistasLayout = new javax.swing.GroupLayout(jPanelDatosArtistas);
        jPanelDatosArtistas.setLayout(jPanelDatosArtistasLayout);
        jPanelDatosArtistasLayout.setHorizontalGroup(
            jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosArtistasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDatosArtistasLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDeleteArtistas))
                    .addGroup(jPanelDatosArtistasLayout.createSequentialGroup()
                        .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblInfoArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelDatosArtistasLayout.createSequentialGroup()
                                .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanelDatosArtistasLayout.createSequentialGroup()
                                        .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(lblAnnoApellido2Artistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblApellido1Artistas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblNombreArtistas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblDniArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jTextFieldNombreArtistas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                            .addComponent(jTextFieldDniArtistas, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldApellido1Artistas)
                                            .addComponent(jTextFieldApellido2Artistas)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelDatosArtistasLayout.createSequentialGroup()
                                        .addGap(48, 48, 48)
                                        .addComponent(btnInsertarArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnModifArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(44, 44, 44)
                                .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblDirArtistas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTelfArtistas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblAliasArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldTelfArtistas, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldAliasArtistas, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldDirArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanelDatosArtistasLayout.setVerticalGroup(
            jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosArtistasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfoArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDatosArtistasLayout.createSequentialGroup()
                        .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDniArtistas)
                            .addComponent(jTextFieldDniArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombreArtistas)
                            .addComponent(jTextFieldNombreArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblApellido1Artistas)
                            .addComponent(jTextFieldApellido1Artistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelDatosArtistasLayout.createSequentialGroup()
                        .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAliasArtistas)
                            .addComponent(jTextFieldAliasArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTelfArtistas)
                            .addComponent(jTextFieldTelfArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDirArtistas)
                            .addComponent(jTextFieldDirArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAnnoApellido2Artistas)
                    .addComponent(jTextFieldApellido2Artistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatosArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsertarArtistas)
                    .addComponent(btnModifArtistas)
                    .addComponent(btnDeleteArtistas)))
        );

        javax.swing.GroupLayout tabArtistasLayout = new javax.swing.GroupLayout(tabArtistas);
        tabArtistas.setLayout(tabArtistasLayout);
        tabArtistasLayout.setHorizontalGroup(
            tabArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTablaArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabArtistasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelDatosArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabArtistasLayout.setVerticalGroup(
            tabArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabArtistasLayout.createSequentialGroup()
                .addComponent(jPanelTablaArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDatosArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Artistas", tabArtistas);

        tabObras.setBackground(new java.awt.Color(0, 153, 153));

        jPanelTablaObras.setBackground(new java.awt.Color(0, 153, 153));

        jTableObras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(jTableObras);

        lblBuscarObras.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lblBuscarObras.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblBuscarObras.setText("Seleccione el artista que desee buscar:");

        jComboBoxFiltrarObras.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jComboBoxFiltrarObras.setMinimumSize(new java.awt.Dimension(231, 27));

        btnFiltrarObras.setText("Filtrar búsqueda");
        btnFiltrarObras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarObrasActionPerformed(evt);
            }
        });

        btnMostrarTodoObras.setText("Mostrar todo");
        btnMostrarTodoObras.setMaximumSize(new java.awt.Dimension(146, 29));
        btnMostrarTodoObras.setMinimumSize(new java.awt.Dimension(146, 29));
        btnMostrarTodoObras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarTodoObrasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTablaObrasLayout = new javax.swing.GroupLayout(jPanelTablaObras);
        jPanelTablaObras.setLayout(jPanelTablaObrasLayout);
        jPanelTablaObrasLayout.setHorizontalGroup(
            jPanelTablaObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaObrasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTablaObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanelTablaObrasLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblBuscarObras, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxFiltrarObras, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104)
                        .addComponent(btnFiltrarObras, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMostrarTodoObras, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelTablaObrasLayout.setVerticalGroup(
            jPanelTablaObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaObrasLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanelTablaObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscarObras)
                    .addComponent(jComboBoxFiltrarObras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrarObras)
                    .addComponent(btnMostrarTodoObras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelDatosObras.setBackground(new java.awt.Color(0, 153, 153));
        jPanelDatosObras.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), null));
        jPanelDatosObras.setBounds(new java.awt.Rectangle(0, 0, 626, 178));
        jPanelDatosObras.setMaximumSize(new java.awt.Dimension(626, 178));

        lblTituloObras.setText("TÍTULO");

        lblAnnoObras.setText("AÑO PUBLICACION");

        lblEstiloObras.setText("ESTILO");

        btnInsertarObras.setText("Insertar");
        btnInsertarObras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarObrasActionPerformed(evt);
            }
        });

        btnModifObras.setText("Modificar");
        btnModifObras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifObrasActionPerformed(evt);
            }
        });

        btnDeleteObras.setText("Eliminar");
        btnDeleteObras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteObrasActionPerformed(evt);
            }
        });

        lblDescripcionObras.setText("DESCRIPCIÓN");

        lblPrecioObras.setText("PRECIO");

        lblArtistaObras.setText("ARTISTA");

        lblEstadoObras.setText("ESTADO");

        lblInfoObras.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanelDatosObrasLayout = new javax.swing.GroupLayout(jPanelDatosObras);
        jPanelDatosObras.setLayout(jPanelDatosObrasLayout);
        jPanelDatosObrasLayout.setHorizontalGroup(
            jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosObrasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDatosObrasLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDeleteObras))
                    .addGroup(jPanelDatosObrasLayout.createSequentialGroup()
                        .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblInfoObras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelDatosObrasLayout.createSequentialGroup()
                                .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanelDatosObrasLayout.createSequentialGroup()
                                        .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(lblDescripcionObras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblEstiloObras, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblAnnoObras, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblTituloObras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jTextFieldAnnoObras, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                            .addComponent(jTextFieldTituloObras, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldEstiloObras)
                                            .addComponent(jTextFieldDescripcionObras)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelDatosObrasLayout.createSequentialGroup()
                                        .addGap(48, 48, 48)
                                        .addComponent(btnInsertarObras, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnModifObras, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(44, 44, 44)
                                .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblEstadoObras, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblArtistaObras, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblPrecioObras, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldPrecioObras, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                    .addComponent(jTextFieldEstadoObras, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                    .addComponent(jComboBoxArtistaObras, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanelDatosObrasLayout.setVerticalGroup(
            jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosObrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfoObras, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDatosObrasLayout.createSequentialGroup()
                        .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTituloObras)
                            .addComponent(jTextFieldTituloObras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAnnoObras)
                            .addComponent(jTextFieldAnnoObras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEstiloObras)
                            .addComponent(jTextFieldEstiloObras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelDatosObrasLayout.createSequentialGroup()
                        .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPrecioObras)
                            .addComponent(jTextFieldPrecioObras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblArtistaObras)
                            .addComponent(jComboBoxArtistaObras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEstadoObras)
                            .addComponent(jTextFieldEstadoObras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDescripcionObras)
                    .addComponent(jTextFieldDescripcionObras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsertarObras)
                    .addComponent(btnModifObras)
                    .addComponent(btnDeleteObras)))
        );

        javax.swing.GroupLayout tabObrasLayout = new javax.swing.GroupLayout(tabObras);
        tabObras.setLayout(tabObrasLayout);
        tabObrasLayout.setHorizontalGroup(
            tabObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTablaObras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabObrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelDatosObras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabObrasLayout.setVerticalGroup(
            tabObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabObrasLayout.createSequentialGroup()
                .addComponent(jPanelTablaObras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDatosObras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Obras", tabObras);

        tabVentas.setBackground(new java.awt.Color(0, 153, 153));

        jPanelTablaVentas.setBackground(new java.awt.Color(0, 153, 153));

        jTableVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(jTableVentas);

        lblBuscarVentas.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lblBuscarVentas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblBuscarVentas.setText("Seleccione las ventas que desee buscar:");

        jComboBoxFiltrarVentasAnno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Año", "2018", "2019", "2020", "2021" }));
        jComboBoxFiltrarVentasAnno.setMinimumSize(new java.awt.Dimension(231, 27));

        btnFiltrarVentas.setText("Filtrar búsqueda");
        btnFiltrarVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarVentasActionPerformed(evt);
            }
        });

        btnMostrarTodoVentas.setText("Mostrar todo");
        btnMostrarTodoVentas.setMaximumSize(new java.awt.Dimension(146, 29));
        btnMostrarTodoVentas.setMinimumSize(new java.awt.Dimension(146, 29));
        btnMostrarTodoVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarTodoVentasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTablaVentasLayout = new javax.swing.GroupLayout(jPanelTablaVentas);
        jPanelTablaVentas.setLayout(jPanelTablaVentasLayout);
        jPanelTablaVentasLayout.setHorizontalGroup(
            jPanelTablaVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTablaVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanelTablaVentasLayout.createSequentialGroup()
                        .addComponent(lblBuscarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxFiltrarVentasAnno, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 246, Short.MAX_VALUE)
                        .addComponent(btnFiltrarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMostrarTodoVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelTablaVentasLayout.setVerticalGroup(
            jPanelTablaVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaVentasLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanelTablaVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFiltrarVentas)
                    .addComponent(btnMostrarTodoVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxFiltrarVentasAnno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBuscarVentas))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanelDatosVentas.setBackground(new java.awt.Color(0, 153, 153));
        jPanelDatosVentas.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), null));
        jPanelDatosVentas.setBounds(new java.awt.Rectangle(0, 0, 626, 178));
        jPanelDatosVentas.setMaximumSize(new java.awt.Dimension(626, 178));

        lblCodObraVentas.setText("CÓDIGO OBRA");

        lblTituloVentas.setText("TÍTULO");

        lblArtistaVentas.setText("ARTISTA");

        btnInsertarVentas.setText("Insertar");
        btnInsertarVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarVentasActionPerformed(evt);
            }
        });

        btnModifVentas.setText("Modificar");
        btnModifVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifVentasActionPerformed(evt);
            }
        });

        btnDeleteVentas.setText("Eliminar");
        btnDeleteVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteVentasActionPerformed(evt);
            }
        });

        lblPrecioVentas.setText("PRECIO");

        lblDniCompradorVentas.setText("DNI COMPRADOR");

        lblFechaVentas.setText("FECHA COMPRA");

        lblInfoVentas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfoVentas.setText(" ");

        javax.swing.GroupLayout jPanelDatosVentasLayout = new javax.swing.GroupLayout(jPanelDatosVentas);
        jPanelDatosVentas.setLayout(jPanelDatosVentasLayout);
        jPanelDatosVentasLayout.setHorizontalGroup(
            jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblInfoVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                                .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblArtistaVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTituloVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblCodObraVentas, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxObraVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxTituloVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldArtistaVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(38, 38, 38)
                                .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblFechaVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblDniCompradorVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblPrecioVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldPrecioVentas)
                                    .addComponent(jTextFieldFechaVentas)
                                    .addComponent(jComboBoxCompradorVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                    .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(btnInsertarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModifVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDeleteVentas)
                        .addGap(96, 96, 96))))
        );
        jPanelDatosVentasLayout.setVerticalGroup(
            jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfoVentas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCodObraVentas)
                            .addComponent(jComboBoxObraVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxTituloVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTituloVentas))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldArtistaVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblArtistaVentas)))
                    .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPrecioVentas)
                            .addComponent(jTextFieldPrecioVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDniCompradorVentas)
                            .addComponent(jComboBoxCompradorVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFechaVentas)
                            .addComponent(jTextFieldFechaVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(36, 36, 36)
                .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsertarVentas)
                    .addComponent(btnModifVentas)
                    .addComponent(btnDeleteVentas)))
        );

        javax.swing.GroupLayout tabVentasLayout = new javax.swing.GroupLayout(tabVentas);
        tabVentas.setLayout(tabVentasLayout);
        tabVentasLayout.setHorizontalGroup(
            tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTablaVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelDatosVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabVentasLayout.setVerticalGroup(
            tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabVentasLayout.createSequentialGroup()
                .addComponent(jPanelTablaVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDatosVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ventas", tabVentas);

        tabClientes.setBackground(new java.awt.Color(0, 153, 153));

        jPanelTablaClientes.setBackground(new java.awt.Color(0, 153, 153));

        jTableClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(jTableClientes);

        lblBuscarClientes.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lblBuscarClientes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblBuscarClientes.setText("Seleccione según localidad:");

        jComboBoxFiltrarClientes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Barcelona", "Madrid", " " }));
        jComboBoxFiltrarClientes.setMinimumSize(new java.awt.Dimension(231, 27));

        btnFiltrarClientes.setText("Filtrar búsqueda");
        btnFiltrarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarClientesActionPerformed(evt);
            }
        });

        btnMostrarTodoClientes.setText("Mostrar todo");
        btnMostrarTodoClientes.setMaximumSize(new java.awt.Dimension(146, 29));
        btnMostrarTodoClientes.setMinimumSize(new java.awt.Dimension(146, 29));

        javax.swing.GroupLayout jPanelTablaClientesLayout = new javax.swing.GroupLayout(jPanelTablaClientes);
        jPanelTablaClientes.setLayout(jPanelTablaClientesLayout);
        jPanelTablaClientesLayout.setHorizontalGroup(
            jPanelTablaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTablaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanelTablaClientesLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblBuscarClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxFiltrarClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104)
                        .addComponent(btnFiltrarClientes, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMostrarTodoClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelTablaClientesLayout.setVerticalGroup(
            jPanelTablaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaClientesLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanelTablaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscarClientes)
                    .addComponent(jComboBoxFiltrarClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrarClientes)
                    .addComponent(btnMostrarTodoClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelDatosClientes.setBackground(new java.awt.Color(0, 153, 153));
        jPanelDatosClientes.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), null));
        jPanelDatosClientes.setBounds(new java.awt.Rectangle(0, 0, 626, 178));
        jPanelDatosClientes.setMaximumSize(new java.awt.Dimension(626, 178));

        lblDniClientes.setText("DNI");

        lblNombreClientes.setText("NOMBRE");

        lblApellido1Clientes.setText("PRIMER APELLIDO");

        btnInsertarClientes.setText("Insertar");
        btnInsertarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarClientesActionPerformed(evt);
            }
        });

        btnModifClientes.setText("Modificar");
        btnModifClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifClientesActionPerformed(evt);
            }
        });

        btnDeleteClientes.setText("Eliminar");
        btnDeleteClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteClientesActionPerformed(evt);
            }
        });

        lblApellido2Clientes.setText("SEGUNDO APELLIDO");

        lblTelfClientes.setText("TELÉFONO");

        lblDirClientes.setText("DIRECCIÓN");

        lblInfoClientes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanelDatosClientesLayout = new javax.swing.GroupLayout(jPanelDatosClientes);
        jPanelDatosClientes.setLayout(jPanelDatosClientesLayout);
        jPanelDatosClientesLayout.setHorizontalGroup(
            jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInfoClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelDatosClientesLayout.createSequentialGroup()
                        .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelDatosClientesLayout.createSequentialGroup()
                                .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblApellido2Clientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblApellido1Clientes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblNombreClientes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblDniClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldNombreClientes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                    .addComponent(jTextFieldDniClientes, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldApellido1Clientes)
                                    .addComponent(jTextFieldApellido2Clientes)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelDatosClientesLayout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(btnInsertarClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnModifClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(44, 44, 44)
                        .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblDirClientes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTelfClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextFieldDirClientes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .addComponent(jTextFieldTelfClientes, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 146, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDatosClientesLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnDeleteClientes))
        );
        jPanelDatosClientesLayout.setVerticalGroup(
            jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfoClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDatosClientesLayout.createSequentialGroup()
                        .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDniClientes)
                            .addComponent(jTextFieldDniClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombreClientes)
                            .addComponent(jTextFieldNombreClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblApellido1Clientes)
                            .addComponent(jTextFieldApellido1Clientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelDatosClientesLayout.createSequentialGroup()
                        .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTelfClientes)
                            .addComponent(jTextFieldTelfClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDirClientes)
                            .addComponent(jTextFieldDirClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApellido2Clientes)
                    .addComponent(jTextFieldApellido2Clientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsertarClientes)
                    .addComponent(btnModifClientes)
                    .addComponent(btnDeleteClientes)))
        );

        javax.swing.GroupLayout tabClientesLayout = new javax.swing.GroupLayout(tabClientes);
        tabClientes.setLayout(tabClientesLayout);
        tabClientesLayout.setHorizontalGroup(
            tabClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTablaClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelDatosClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabClientesLayout.setVerticalGroup(
            tabClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabClientesLayout.createSequentialGroup()
                .addComponent(jPanelTablaClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDatosClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Clientes", tabClientes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Artistas");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarArtistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarArtistasActionPerformed
        //Filtra la búsqueda por artista
        try {
            mostrarTablaFiltroArtista();
        } catch(Exception ex) {
            lblInfoArtistas.setText("Ha sucedido un error al filtrar.");
        }
    }//GEN-LAST:event_btnFiltrarArtistasActionPerformed

    private void btnFiltrarObrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarObrasActionPerformed
        //Filtra la búsqueda por obras
        try {
            mostrarTablaFiltroObras();
        } catch(Exception ex) {
            lblInfoObras.setText("Ha sucedido un error al filtrar.");
        }
    }//GEN-LAST:event_btnFiltrarObrasActionPerformed

    private void btnFiltrarVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarVentasActionPerformed
        //Filtra la búsqueda por ventas
        try {
            mostrarTablaFiltroVentas();
        } catch(Exception ex) {
            lblInfoVentas.setText("Ha sucedido un error al filtrar.");
        }
    }//GEN-LAST:event_btnFiltrarVentasActionPerformed

    private void btnFiltrarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarClientesActionPerformed
        //Filtra la búsqueda por clientes
        try {
            mostrarTablaFiltroClientes();
        } catch(Exception ex) {
            lblInfoClientes.setText("Ha sucedido un error al filtrar.");
        }
    }//GEN-LAST:event_btnFiltrarClientesActionPerformed

    private void btnInsertarArtistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertarArtistasActionPerformed
        //Inserta un nuevo artista
        try {
            conexion.insertarArtista(jTextFieldDniArtistas.getText(),jTextFieldNombreArtistas.getText(), 
                    jTextFieldApellido1Artistas.getText(), jTextFieldApellido2Artistas.getText(), 
                    jTextFieldAliasArtistas.getText(), jTextFieldTelfArtistas.getText(), jTextFieldDirArtistas.getText());
            lblInfoArtistas.setText("Artista insertado correctamente.");
            mostrarTablaArtistas();
        } catch (Exception ex) {
            lblInfoArtistas.setText(ex.toString());
        }
    }//GEN-LAST:event_btnInsertarArtistasActionPerformed

    private void btnModifArtistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifArtistasActionPerformed
        //Modifica un artista existente
        try {
            int filaArtistas = jTableArtistas.getSelectedRow();
            conexion.modifArtistas(jTableArtistas.getValueAt(filaArtistas, 0).toString(),jTextFieldDniArtistas.getText(),
                    jTextFieldNombreArtistas.getText(), jTextFieldApellido1Artistas.getText(), jTextFieldApellido2Artistas.getText(), 
                    jTextFieldAliasArtistas.getText(), jTextFieldTelfArtistas.getText(), jTextFieldDirArtistas.getText());
            //Volvemos a mostrar la tabla para que aparezcan la modificación.
            mostrarTablaArtistas();
            lblInfoArtistas.setText("Artista modificado correctamente.");
        } catch (Exception ex) {
            lblInfoArtistas.setText("Error al modificar el álbum.");
        }
    }//GEN-LAST:event_btnModifArtistasActionPerformed

    private void btnDeleteArtistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteArtistasActionPerformed
        //Elimina un artista existente
        try {
            int filaArtistas = jTableArtistas.getSelectedRow();
            conexion.deleteArtistas(jTableArtistas.getValueAt(filaArtistas, 0).toString());
            //Volvemos a mostrar la tabla para que aparezcan la modificación.
            mostrarTablaArtistas();
            lblInfoArtistas.setText("Artista eliminado correctamente.");
            //Limpiamos los campos
            jTextFieldDniArtistas.setText("");
            jTextFieldNombreArtistas.setText("");
            jTextFieldApellido1Artistas.setText("");
            jTextFieldApellido2Artistas.setText("");
            jTextFieldAliasArtistas.setText("");
            jTextFieldTelfArtistas.setText("");
            jTextFieldDirArtistas.setText("");
        } catch (Exception ex) {
            lblInfoArtistas.setText("Error al eliminar el álbum.");
        }
    }//GEN-LAST:event_btnDeleteArtistasActionPerformed

    private void btnMostrarTodoArtistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodoArtistasActionPerformed
        //Muestra todos los artistas de nuevo
        mostrarTablaArtistas();
        jComboBoxFiltrarArtistas.setSelectedIndex(0);
    }//GEN-LAST:event_btnMostrarTodoArtistasActionPerformed

    private void btnMostrarTodoObrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodoObrasActionPerformed
        //Muestra todos las obras de nuevo
        mostrarTablaObras();
        jComboBoxFiltrarObras.setSelectedIndex(0);
    }//GEN-LAST:event_btnMostrarTodoObrasActionPerformed

    private void btnMostrarTodoVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodoVentasActionPerformed
        //Muestra todas las ventas
        mostrarTablaVentas();
        jComboBoxFiltrarVentasAnno.setSelectedIndex(0);
    }//GEN-LAST:event_btnMostrarTodoVentasActionPerformed

    private void btnInsertarObrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertarObrasActionPerformed
        //Inserta una obra nueva
        try {
            conexion.insertarObra(jTextFieldTituloObras.getText(),jTextFieldAnnoObras.getText(), 
                    jTextFieldEstiloObras.getText(), jTextFieldDescripcionObras.getText(), 
                    jTextFieldPrecioObras.getText(), jComboBoxArtistaObras.getSelectedItem().toString(), 
                    jTextFieldEstadoObras.getText());
            lblInfoObras.setText("Obra insertado correctamente.");
            mostrarTablaObras();
        } catch (Exception ex) {
            lblInfoObras.setText(ex.toString());
        }
    }//GEN-LAST:event_btnInsertarObrasActionPerformed

    private void btnModifObrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifObrasActionPerformed
        //Modifica una obra
        try {
            int filaObras = jTableObras.getSelectedRow();
            conexion.modifObras(jTableObras.getValueAt(filaObras, 0).toString(),jTextFieldTituloObras.getText(),
                    jTextFieldAnnoObras.getText(), jTextFieldEstiloObras.getText(), jTextFieldDescripcionObras.getText(), 
                    jTextFieldPrecioObras.getText(), jComboBoxArtistaObras.getSelectedItem().toString(), 
                    jTextFieldEstadoObras.getText());
            //Volvemos a mostrar la tabla para que aparezcan la modificación.
            mostrarTablaObras();
            lblInfoObras.setText("Obra modificado correctamente.");
        } catch (Exception ex) {
            lblInfoObras.setText("Error al modificar la obra.");
        }
    }//GEN-LAST:event_btnModifObrasActionPerformed

    private void btnDeleteObrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteObrasActionPerformed
        //Borra una obra
        try {
            int filaObras = jTableObras.getSelectedRow();
            conexion.deleteObras(jTableObras.getValueAt(filaObras, 0).toString());
            //Volvemos a mostrar la tabla para que aparezcan la modificación.
            mostrarTablaObras();
            lblInfoObras.setText("Obra eliminada correctamente.");
            //Limpiamos los campos
            jTextFieldTituloObras.setText("");
            jTextFieldAnnoObras.setText("");
            jTextFieldEstiloObras.setText("");
            jTextFieldDescripcionObras.setText("");
            jTextFieldPrecioObras.setText("");
            jTextFieldEstadoObras.setText("");
            jComboBoxArtistaObras.setSelectedIndex(0);
        } catch (Exception ex) {
            lblInfoObras.setText("Error al eliminar la obra.");
        }
    }//GEN-LAST:event_btnDeleteObrasActionPerformed

    private void btnInsertarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertarClientesActionPerformed
        //Inserta un cliente nuevo
        try {
            conexion.insertarCliente(jTextFieldDniClientes.getText(),jTextFieldNombreClientes.getText(), 
                    jTextFieldApellido1Clientes.getText(), jTextFieldApellido2Clientes.getText(), 
                    jTextFieldTelfClientes.getText(), jTextFieldDirClientes.getText());
            lblInfoClientes.setText("Cliente insertado correctamente.");
            mostrarTablaClientes();
        } catch (Exception ex) {
            lblInfoClientes.setText(ex.toString());
        }
    }//GEN-LAST:event_btnInsertarClientesActionPerformed

    private void btnModifClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifClientesActionPerformed
        //Modifica un cliente existente
        try {
            int filaClientes = jTableClientes.getSelectedRow();
            conexion.modifCliente(jTableClientes.getValueAt(filaClientes, 0).toString(),jTextFieldDniClientes.getText(),
                    jTextFieldNombreClientes.getText(), jTextFieldApellido1Clientes.getText(), 
                    jTextFieldApellido2Clientes.getText(), jTextFieldTelfClientes.getText(), 
                    jTextFieldDirClientes.getText());
            //Volvemos a mostrar la tabla para que aparezcan la modificación.
            mostrarTablaClientes();
            lblInfoClientes.setText("Cliente modificado correctamente.");
        } catch (Exception ex) {
            lblInfoClientes.setText("Error al modificar la obra.");
        }
        
    }//GEN-LAST:event_btnModifClientesActionPerformed

    private void btnDeleteClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteClientesActionPerformed
        //Elimina un cliente existente
        try {
            conexion.deleteClientes(jTextFieldDniClientes.getText());
            //Volvemos a mostrar la tabla para que aparezcan la modificación.
            mostrarTablaClientes();
            lblInfoClientes.setText("Clientes eliminada correctamente.");
            //Limpiamos los campos
            jTextFieldDniClientes.setText("");
            jTextFieldNombreClientes.setText("");
            jTextFieldApellido1Clientes.setText("");
            jTextFieldApellido2Clientes.setText("");
            jTextFieldTelfClientes.setText("");
            jTextFieldDirClientes.setText("");
        } catch (Exception ex) {
            lblInfoClientes.setText("Error al eliminar el cliente.");
        }
    }//GEN-LAST:event_btnDeleteClientesActionPerformed

    private void btnInsertarVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertarVentasActionPerformed
        //Inserta una venta nueva
        //Transacción: si inserto una venta, la obra pasa a tener de estado: Vendido en lugar de Disponible.
        try {
            conexion.insertarVenta(jComboBoxObraVentas.getSelectedItem().toString(), 
                    jComboBoxCompradorVentas.getSelectedItem().toString(), jTextFieldFechaVentas.getText());
            lblInfoVentas.setText("Venta insertada correctamente.");
            mostrarTablaVentas();
            mostrarTablaObras();
        } catch (Exception ex) {
            lblInfoVentas.setText(ex.toString());
        }
    }//GEN-LAST:event_btnInsertarVentasActionPerformed

    private void btnDeleteVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteVentasActionPerformed
        //Elimina una venta existente
        //Transacción: si elimino una venta, la obra pasa a tener de estado: Disponible en lugar de Vendido.
        try {
            int filaVentas = jTableVentas.getSelectedRow();
            conexion.deleteVentas(jTableObras.getValueAt(filaVentas, 0).toString(), jComboBoxObraVentas.getSelectedItem().toString());
            lblInfoVentas.setText("Venta eliminada correctamente.");
            mostrarTablaVentas();
            mostrarTablaObras();
        } catch (Exception ex) {
            lblInfoVentas.setText(ex.toString());
        }
    }//GEN-LAST:event_btnDeleteVentasActionPerformed

    private void btnModifVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifVentasActionPerformed
        //Modifica una venta existente
        try {
            int filaVentas = jTableVentas.getSelectedRow();
            conexion.modifVentas(jTableObras.getValueAt(filaVentas, 0).toString(),
                    jComboBoxObraVentas.getSelectedItem().toString(), jComboBoxCompradorVentas.getSelectedItem().toString(),
                    jTextFieldFechaVentas.getText());
            lblInfoVentas.setText("Venta modificada correctamente.");
            mostrarTablaVentas();
        } catch (Exception ex) {
            lblInfoVentas.setText(ex.toString());
        }
    }//GEN-LAST:event_btnModifVentasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeleteArtistas;
    private javax.swing.JButton btnDeleteClientes;
    private javax.swing.JButton btnDeleteObras;
    private javax.swing.JButton btnDeleteVentas;
    private javax.swing.JButton btnFiltrarArtistas;
    private javax.swing.JButton btnFiltrarClientes;
    private javax.swing.JButton btnFiltrarObras;
    private javax.swing.JButton btnFiltrarVentas;
    private javax.swing.JButton btnInsertarArtistas;
    private javax.swing.JButton btnInsertarClientes;
    private javax.swing.JButton btnInsertarObras;
    private javax.swing.JButton btnInsertarVentas;
    private javax.swing.JButton btnModifArtistas;
    private javax.swing.JButton btnModifClientes;
    private javax.swing.JButton btnModifObras;
    private javax.swing.JButton btnModifVentas;
    private javax.swing.JButton btnMostrarTodoArtistas;
    private javax.swing.JButton btnMostrarTodoClientes;
    private javax.swing.JButton btnMostrarTodoObras;
    private javax.swing.JButton btnMostrarTodoVentas;
    private javax.swing.JComboBox<String> jComboBoxArtistaObras;
    private javax.swing.JComboBox<String> jComboBoxCompradorVentas;
    private javax.swing.JComboBox<String> jComboBoxFiltrarArtistas;
    private javax.swing.JComboBox<String> jComboBoxFiltrarClientes;
    private javax.swing.JComboBox<String> jComboBoxFiltrarObras;
    private javax.swing.JComboBox<String> jComboBoxFiltrarVentasAnno;
    private javax.swing.JComboBox<String> jComboBoxObraVentas;
    private javax.swing.JComboBox<String> jComboBoxTituloVentas;
    private javax.swing.JPanel jPanelDatosArtistas;
    private javax.swing.JPanel jPanelDatosClientes;
    private javax.swing.JPanel jPanelDatosObras;
    private javax.swing.JPanel jPanelDatosVentas;
    private javax.swing.JPanel jPanelTablaArtistas;
    private javax.swing.JPanel jPanelTablaClientes;
    private javax.swing.JPanel jPanelTablaObras;
    private javax.swing.JPanel jPanelTablaVentas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableArtistas;
    private javax.swing.JTable jTableClientes;
    private javax.swing.JTable jTableObras;
    private javax.swing.JTable jTableVentas;
    private javax.swing.JTextField jTextFieldAliasArtistas;
    private javax.swing.JTextField jTextFieldAnnoObras;
    private javax.swing.JTextField jTextFieldApellido1Artistas;
    private javax.swing.JTextField jTextFieldApellido1Clientes;
    private javax.swing.JTextField jTextFieldApellido2Artistas;
    private javax.swing.JTextField jTextFieldApellido2Clientes;
    private javax.swing.JTextField jTextFieldArtistaVentas;
    private javax.swing.JTextField jTextFieldDescripcionObras;
    private javax.swing.JTextField jTextFieldDirArtistas;
    private javax.swing.JTextField jTextFieldDirClientes;
    private javax.swing.JTextField jTextFieldDniArtistas;
    private javax.swing.JTextField jTextFieldDniClientes;
    private javax.swing.JTextField jTextFieldEstadoObras;
    private javax.swing.JTextField jTextFieldEstiloObras;
    private javax.swing.JTextField jTextFieldFechaVentas;
    private javax.swing.JTextField jTextFieldNombreArtistas;
    private javax.swing.JTextField jTextFieldNombreClientes;
    private javax.swing.JTextField jTextFieldPrecioObras;
    private javax.swing.JTextField jTextFieldPrecioVentas;
    private javax.swing.JTextField jTextFieldTelfArtistas;
    private javax.swing.JTextField jTextFieldTelfClientes;
    private javax.swing.JTextField jTextFieldTituloObras;
    private javax.swing.JLabel lblAliasArtistas;
    private javax.swing.JLabel lblAnnoApellido2Artistas;
    private javax.swing.JLabel lblAnnoObras;
    private javax.swing.JLabel lblApellido1Artistas;
    private javax.swing.JLabel lblApellido1Clientes;
    private javax.swing.JLabel lblApellido2Clientes;
    private javax.swing.JLabel lblArtistaObras;
    private javax.swing.JLabel lblArtistaVentas;
    private javax.swing.JLabel lblBuscarArtistas;
    private javax.swing.JLabel lblBuscarClientes;
    private javax.swing.JLabel lblBuscarObras;
    private javax.swing.JLabel lblBuscarVentas;
    private javax.swing.JLabel lblCodObraVentas;
    private javax.swing.JLabel lblDescripcionObras;
    private javax.swing.JLabel lblDirArtistas;
    private javax.swing.JLabel lblDirClientes;
    private javax.swing.JLabel lblDniArtistas;
    private javax.swing.JLabel lblDniClientes;
    private javax.swing.JLabel lblDniCompradorVentas;
    private javax.swing.JLabel lblEstadoObras;
    private javax.swing.JLabel lblEstiloObras;
    private javax.swing.JLabel lblFechaVentas;
    private javax.swing.JLabel lblInfoArtistas;
    private javax.swing.JLabel lblInfoClientes;
    private javax.swing.JLabel lblInfoObras;
    private javax.swing.JLabel lblInfoVentas;
    private javax.swing.JLabel lblNombreArtistas;
    private javax.swing.JLabel lblNombreClientes;
    private javax.swing.JLabel lblPrecioObras;
    private javax.swing.JLabel lblPrecioVentas;
    private javax.swing.JLabel lblTelfArtistas;
    private javax.swing.JLabel lblTelfClientes;
    private javax.swing.JLabel lblTituloObras;
    private javax.swing.JLabel lblTituloVentas;
    private javax.swing.JPanel tabArtistas;
    private javax.swing.JPanel tabClientes;
    private javax.swing.JPanel tabObras;
    private javax.swing.JPanel tabVentas;
    // End of variables declaration//GEN-END:variables
}
