
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

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
        ResultSet rs = (ResultSet) conexion.mostrarTabla("SELECT * FROM artistas;");
        
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
                modelo.addRow(new Object[]{rs.getInt("codArtista"), rs.getString("dni"),
                rs.getString("nombre"), rs.getString("primer_apellido"), rs.getString("segundo_apellido"),
                rs.getString("alias"), rs.getInt("telf"), rs.getString("direccion")});
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
        ResultSet rs = (ResultSet) conexion.mostrarTabla("SELECT obras.codObra, obras.titulo, "
                + "obras.publicado_en, obras.estilo, obras.descripcion, obras.precio, "
                + "artistas.alias, obras.estado \n" +
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
                modelo.addRow(new Object[]{rs.getInt("codObra"), rs.getString("titulo"),
                rs.getInt("publicado_en"), rs.getString("estilo"), rs.getString("descripcion"),
                rs.getString("precio"), rs.getString("artistas.alias"), rs.getString("estado")});
                
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
        ResultSet rs = (ResultSet) conexion.mostrarTabla("SELECT ventas.codVenta, ventas.codObra, obras.titulo, "
                + "artistas.alias, obras.precio, ventas.dni, ventas.fecha_compra\n" +
                "FROM ventas, obras, artistas\n" +
                "WHERE ventas.codObra = obras.codObra AND artistas.codArtista = obras.artista\n" +
                "ORDER BY ventas.codVenta;;");
        
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
    
    private void mostrarTablaClientes() {
        //Ponemos el model a la tabla
        DefaultTableModel modelo = new DefaultTableModel();
        //Hacemos la consulta
        ResultSet rs = (ResultSet) conexion.mostrarTabla("SELECT * FROM clientes;");
        
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
                        String artistaObra = (String)jTableArtistas.getValueAt(filaObras - 1, 5);
                        String estadoObra = (String)jTableObras.getValueAt(filaObras, 7);
                        jTextFieldTituloObras.setText(tituloObra);
                        jTextFieldAnnoObras.setText(annoObra);
                        jTextFieldEstiloObras.setText(estiloObra);
                        jTextFieldDescripcionObras.setText(descripcionObra);
                        jTextFieldPrecioObras.setText(precioObra);
                        jTextFieldArtistaObras.setText(artistaObra);
                        jTextFieldEstadoObras.setText(estadoObra);
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
                        String codObra = (String)jTableVentas.getValueAt(filaVentas, 1).toString();
                        String tituloVentas = (String)jTableVentas.getValueAt(filaVentas, 2);
                        String artistaVentas = (String)jTableVentas.getValueAt(filaVentas, 3);
                        String precioVentas = (String)jTableVentas.getValueAt(filaVentas, 4).toString();
                        String compradorVentas = (String)jTableVentas.getValueAt(filaVentas, 5);
                        String fechaVentas = (String)jTableVentas.getValueAt(filaVentas, 6).toString();
                        jTextFieldCodObraVentas.setText(codObra);
                        jTextFieldTituloVentas.setText(tituloVentas);
                        jTextFieldArtistaVentas.setText(artistaVentas);
                        jTextFieldPrecioVentas.setText(precioVentas);
                        jTextFieldCompradorVentas.setText(compradorVentas);
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
        jTextFieldArtistaObras = new javax.swing.JTextField();
        lblEstadoObras = new javax.swing.JLabel();
        jTextFieldEstadoObras = new javax.swing.JTextField();
        lblInfoObras = new javax.swing.JLabel();
        tabVentas = new javax.swing.JPanel();
        jPanelTablaVentas = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableVentas = new javax.swing.JTable();
        lblBuscarVentas = new javax.swing.JLabel();
        jComboBoxFiltrarVentas = new javax.swing.JComboBox<>();
        btnFiltrarVentas = new javax.swing.JButton();
        btnMostrarTodoVentas = new javax.swing.JButton();
        jPanelDatosVentas = new javax.swing.JPanel();
        lblCodObraVentas = new javax.swing.JLabel();
        jTextFieldCodObraVentas = new javax.swing.JTextField();
        lblTituloVentas = new javax.swing.JLabel();
        jTextFieldTituloVentas = new javax.swing.JTextField();
        lblArtistaVentas = new javax.swing.JLabel();
        jTextFieldArtistaVentas = new javax.swing.JTextField();
        btnInsertarVentas = new javax.swing.JButton();
        btnModifVentas = new javax.swing.JButton();
        btnDeleteVentas = new javax.swing.JButton();
        lblPrecioVentas = new javax.swing.JLabel();
        jTextFieldPrecioVentas = new javax.swing.JTextField();
        lblDniCompradorVentas = new javax.swing.JLabel();
        jTextFieldCompradorVentas = new javax.swing.JTextField();
        lblFechaVentas = new javax.swing.JLabel();
        jTextFieldFechaVentas = new javax.swing.JTextField();
        lblInfoVentas = new javax.swing.JLabel();
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
        lblBuscarArtistas.setText("Seleccione el álbum que desee buscar:");

        jComboBoxFiltrarArtistas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jComboBoxFiltrarArtistas.setMinimumSize(new java.awt.Dimension(231, 27));

        btnFiltrarArtistas.setText("Filtrar búsqueda");

        btnMostrarTodoArtistas.setText("Mostrar todo");
        btnMostrarTodoArtistas.setMaximumSize(new java.awt.Dimension(146, 29));
        btnMostrarTodoArtistas.setMinimumSize(new java.awt.Dimension(146, 29));

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

        btnModifArtistas.setText("Modificar");

        btnDeleteArtistas.setText("Eliminar");

        lblAnnoApellido2Artistas.setText("SEGUNDO APELLIDO");

        lblAliasArtistas.setText("ALIAS");

        lblTelfArtistas.setText("TELÉFONO");

        lblDirArtistas.setText("DIRECCIÓN");

        lblInfoArtistas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfoArtistas.setText("LABEL INFO");

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
                .addComponent(lblInfoArtistas)
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
        lblBuscarObras.setText("Seleccione el álbum que desee buscar:");

        jComboBoxFiltrarObras.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jComboBoxFiltrarObras.setMinimumSize(new java.awt.Dimension(231, 27));

        btnFiltrarObras.setText("Filtrar búsqueda");

        btnMostrarTodoObras.setText("Mostrar todo");
        btnMostrarTodoObras.setMaximumSize(new java.awt.Dimension(146, 29));
        btnMostrarTodoObras.setMinimumSize(new java.awt.Dimension(146, 29));

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
                        .addComponent(btnFiltrarObras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        btnModifObras.setText("Modificar");

        btnDeleteObras.setText("Eliminar");

        lblDescripcionObras.setText("DESCRIPCIÓN");

        lblPrecioObras.setText("PRECIO");

        lblArtistaObras.setText("ARTISTA");

        lblEstadoObras.setText("ESTADO");

        lblInfoObras.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfoObras.setText("LABEL INFO");

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
                                .addGroup(jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldArtistaObras, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldPrecioObras, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldEstadoObras, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanelDatosObrasLayout.setVerticalGroup(
            jPanelDatosObrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosObrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfoObras)
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
                            .addComponent(jTextFieldArtistaObras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        lblBuscarVentas.setText("Seleccione el álbum que desee buscar:");

        jComboBoxFiltrarVentas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jComboBoxFiltrarVentas.setMinimumSize(new java.awt.Dimension(231, 27));

        btnFiltrarVentas.setText("Filtrar búsqueda");

        btnMostrarTodoVentas.setText("Mostrar todo");
        btnMostrarTodoVentas.setMaximumSize(new java.awt.Dimension(146, 29));
        btnMostrarTodoVentas.setMinimumSize(new java.awt.Dimension(146, 29));

        javax.swing.GroupLayout jPanelTablaVentasLayout = new javax.swing.GroupLayout(jPanelTablaVentas);
        jPanelTablaVentas.setLayout(jPanelTablaVentasLayout);
        jPanelTablaVentasLayout.setHorizontalGroup(
            jPanelTablaVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTablaVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanelTablaVentasLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblBuscarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxFiltrarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104)
                        .addComponent(btnFiltrarVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMostrarTodoVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelTablaVentasLayout.setVerticalGroup(
            jPanelTablaVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablaVentasLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanelTablaVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscarVentas)
                    .addComponent(jComboBoxFiltrarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrarVentas)
                    .addComponent(btnMostrarTodoVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelDatosVentas.setBackground(new java.awt.Color(0, 153, 153));
        jPanelDatosVentas.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), null));
        jPanelDatosVentas.setBounds(new java.awt.Rectangle(0, 0, 626, 178));
        jPanelDatosVentas.setMaximumSize(new java.awt.Dimension(626, 178));

        lblCodObraVentas.setText("CÓDIGO OBRA");

        lblTituloVentas.setText("TÍTULO");

        lblArtistaVentas.setText("ARTISTA");

        btnInsertarVentas.setText("Insertar");

        btnModifVentas.setText("Modificar");

        btnDeleteVentas.setText("Eliminar");

        lblPrecioVentas.setText("PRECIO");

        lblDniCompradorVentas.setText("DNI COMPRADOR");

        lblFechaVentas.setText("FECHA COMPRA");

        lblInfoVentas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfoVentas.setText("LABEL INFO");

        javax.swing.GroupLayout jPanelDatosVentasLayout = new javax.swing.GroupLayout(jPanelDatosVentas);
        jPanelDatosVentas.setLayout(jPanelDatosVentasLayout);
        jPanelDatosVentasLayout.setHorizontalGroup(
            jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDeleteVentas))
                    .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblInfoVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                                .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(lblArtistaVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblTituloVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblCodObraVentas, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jTextFieldTituloVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                            .addComponent(jTextFieldCodObraVentas, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldArtistaVentas)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelDatosVentasLayout.createSequentialGroup()
                                        .addGap(48, 48, 48)
                                        .addComponent(btnInsertarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnModifVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(44, 44, 44)
                                .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblFechaVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblDniCompradorVentas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblPrecioVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldCompradorVentas, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldPrecioVentas, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldFechaVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 146, Short.MAX_VALUE)))
                        .addContainerGap())))
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
                            .addComponent(jTextFieldCodObraVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTituloVentas)
                            .addComponent(jTextFieldTituloVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblArtistaVentas)
                            .addComponent(jTextFieldArtistaVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelDatosVentasLayout.createSequentialGroup()
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPrecioVentas)
                            .addComponent(jTextFieldPrecioVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDniCompradorVentas)
                            .addComponent(jTextFieldCompradorVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDatosVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFechaVentas)
                            .addComponent(jTextFieldFechaVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(38, 38, 38)
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
        lblBuscarClientes.setText("Seleccione el álbum que desee buscar:");

        jComboBoxFiltrarClientes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jComboBoxFiltrarClientes.setMinimumSize(new java.awt.Dimension(231, 27));

        btnFiltrarClientes.setText("Filtrar búsqueda");

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
                        .addComponent(btnFiltrarClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        btnModifClientes.setText("Modificar");

        btnDeleteClientes.setText("Eliminar");

        lblApellido2Clientes.setText("SEGUNDO APELLIDO");

        lblTelfClientes.setText("TELÉFONO");

        lblDirClientes.setText("DIRECCIÓN");

        lblInfoClientes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfoClientes.setText("LABEL INFO");

        javax.swing.GroupLayout jPanelDatosClientesLayout = new javax.swing.GroupLayout(jPanelDatosClientes);
        jPanelDatosClientes.setLayout(jPanelDatosClientesLayout);
        jPanelDatosClientesLayout.setHorizontalGroup(
            jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDatosClientesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDeleteClientes))
                    .addGroup(jPanelDatosClientesLayout.createSequentialGroup()
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
                        .addContainerGap())))
        );
        jPanelDatosClientesLayout.setVerticalGroup(
            jPanelDatosClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDatosClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfoClientes)
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
    private javax.swing.JComboBox<String> jComboBoxFiltrarArtistas;
    private javax.swing.JComboBox<String> jComboBoxFiltrarClientes;
    private javax.swing.JComboBox<String> jComboBoxFiltrarObras;
    private javax.swing.JComboBox<String> jComboBoxFiltrarVentas;
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
    private javax.swing.JTextField jTextFieldArtistaObras;
    private javax.swing.JTextField jTextFieldArtistaVentas;
    private javax.swing.JTextField jTextFieldCodObraVentas;
    private javax.swing.JTextField jTextFieldCompradorVentas;
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
    private javax.swing.JTextField jTextFieldTituloVentas;
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
