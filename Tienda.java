import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Tienda {

    // Datos de configuración de MySQL (Pon los tuyos si tienen contraseña)
    private static final String URL = "jdbc:mysql://localhost:3306/tienda_funkos?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "toor";

    // Rutas de los ficheros TXT
    private static final String FICHERO_CLIENTES = "datos/clientes.txt";
    private static final String FICHERO_FUNKOS = "datos/funkos.txt";
    private static final String FICHERO_PEDIDOS = "datos/pedidos.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Al arrancar, leemos los TXT e insertamos en MySQL si no existen
        cargarDatosDesdeFicheros();

        int opcion = 0;
        do {
            System.out.println("\n--- GESTIÓN TIENDA FUNKOS (TODO EN MAIN) ---");
            System.out.println("1. Listar Funkos (SELECT)");
            System.out.println("2. Insertar Funko (INSERT)");
            System.out.println("3. Listar Clientes (SELECT)");
            System.out.println("4. Registrar Cliente (INSERT)");
            System.out.println("5. Ver Historial Pedidos (SELECT)");
            System.out.println("6. Crear un Pedido (INSERT)");
            System.out.println("7. Eliminar un Cliente (DELETE)");
            System.out.println("8. Salir y Guardar en TXT");
            System.out.print("Selecciona una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Mete un número válido, anda.");
                continue;
            }

            switch (opcion) {
                case 1:
                    listarFunkos();
                    break;
                case 2:
                    System.out.print("Nombre: "); String nom = scanner.nextLine();
                    System.out.print("Franquicia: "); String fran = scanner.nextLine();
                    System.out.print("Precio: "); double pre = Double.parseDouble(scanner.nextLine());
                    System.out.print("Stock: "); int st = Integer.parseInt(scanner.nextLine());
                    insertarFunko(new FunkoPop(nom, fran, pre, st));
                    break;
                case 3:
                    listarClientes();
                    break;
                case 4:
                    System.out.print("Nombre: "); String nomCli = scanner.nextLine();
                    System.out.print("Email: "); String emailCli = scanner.nextLine();
                    insertarCliente(new Cliente(nomCli, emailCli));
                    break;
                case 5:
                    listarPedidos();
                    break;
                case 6:
                    listarClientes();
                    System.out.print("ID Cliente: "); int idC = Integer.parseInt(scanner.nextLine());
                    listarFunkos();
                    System.out.print("ID Funko: "); int idF = Integer.parseInt(scanner.nextLine());
                    System.out.print("Cantidad: "); int cant = Integer.parseInt(scanner.nextLine());
                    Date fechaActual = new Date(System.currentTimeMillis());
                    insertarPedido(new Pedido(idC, idF, cant, fechaActual));
                    break;
                case 7:
                    System.out.print("ID del cliente a borrar: ");
                    int idBorrar = Integer.parseInt(scanner.nextLine());
                    eliminarCliente(idBorrar);
                    break;
                case 8:
                    System.out.println("Guardando cambios en los archivos TXT antes de salir...");
                    guardarDatosAFicheros();
                    System.out.println("¡Programa cerrado con éxito!");
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 8);

        scanner.close();
    }

    // ==========================================
    // MÉTODO DE CONEXIÓN A LA BASE DE DATOS
    // ==========================================
    private static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ==========================================
    // MÉTODOS DE LA BASE DE DATOS (JDBC)
    // ==========================================

    private static void listarFunkos() {
        String sql = "SELECT * FROM funkos";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                FunkoPop f = new FunkoPop(rs.getInt("id"), rs.getString("nombre"), rs.getString("franquicia"), rs.getDouble("precio"), rs.getInt("stock"));
                System.out.println(f);
            }
        } catch (SQLException e) {
            System.out.println("Error al leer funkos de la BD: " + e.getMessage());
        }
    }

    private static void insertarFunko(FunkoPop f) {
        if (existeFunko(f.nombre, f.franquicia)) {
            return; // Silencioso para la carga inicial
        }
        String sql = "INSERT INTO funkos (nombre, franquicia, precio, stock) VALUES (?, ?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.nombre);
            ps.setString(2, f.franquicia);
            ps.setDouble(3, f.precio);
            ps.setInt(4, f.stock);
            ps.executeUpdate();
            System.out.println("¡Funko guardado!: " + f.nombre);
        } catch (SQLException e) {
            System.out.println("Error al insertar funko: " + e.getMessage());
        }
    }

    private static boolean existeFunko(String nombre, String franquicia) {
        String sql = "SELECT COUNT(*) FROM funkos WHERE nombre = ? AND franquicia = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, franquicia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private static void listarClientes() {
        String sql = "SELECT * FROM clientes";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"));
                System.out.println(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al leer clientes: " + e.getMessage());
        }
    }

    private static void insertarCliente(Cliente c) {
        if (existeEmail(c.email)) {
            return; // Silencioso para la carga inicial
        }
        String sql = "INSERT INTO clientes (nombre, email) VALUES (?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.nombre);
            ps.setString(2, c.email);
            ps.executeUpdate();
            System.out.println("¡Cliente registrado!: " + c.nombre);
        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
        }
    }

    private static boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE email = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private static void listarPedidos() {
        String sql = "SELECT * FROM pedidos";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido p = new Pedido(rs.getInt("id"), rs.getInt("id_cliente"), rs.getInt("id_funko"), rs.getInt("cantidad"), rs.getDate("fecha"));
                System.out.println(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pedidos: " + e.getMessage());
        }
    }

    private static void insertarPedido(Pedido p) {
        // CORRECCIÓN CLAVE: Si ya existe este pedido idéntico en la BD, no lo duplicamos
        if (existePedido(p.idCliente, p.idFunko, p.cantidad, p.fecha)) {
            return; 
        }
        String sql = "INSERT INTO pedidos (id_cliente, id_funko, cantidad, fecha) VALUES (?, ?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.idCliente);
            ps.setInt(2, p.idFunko);
            ps.setInt(3, p.cantidad);
            ps.setDate(4, p.fecha);
            ps.executeUpdate();
            System.out.println("¡Pedido registrado correctamente!");
        } catch (SQLException e) {
            System.out.println("Error al insertar pedido: " + e.getMessage());
        }
    }

    // NUEVO MÉTODO COMPROBACIÓN: Evita que los pedidos se dupliquen al leer el TXT
    private static boolean existePedido(int idCliente, int idFunko, int cantidad, Date fecha) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE id_cliente = ? AND id_funko = ? AND cantidad = ? AND fecha = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.setInt(2, idFunko);
            ps.setInt(3, cantidad);
            ps.setDate(4, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private static void eliminarCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Cliente eliminado.");
            else System.out.println("No se encontró ese ID.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS DE PERSISTENCIA (FICHEROS TXT)
    // ==========================================

    private static void cargarDatosDesdeFicheros() {
        System.out.println("[Sincronizando Ficheros -> Base de Datos...]");

        // Cargar Clientes
        try (BufferedReader br = new BufferedReader(new FileReader(FICHERO_CLIENTES))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");
                insertarCliente(new Cliente(datos[0], datos[1]));
            }
        } catch (IOException e) { System.out.println("No se encontró clientes.txt, se creará al salir."); }

        // Cargar Funkos
        try (BufferedReader br = new BufferedReader(new FileReader(FICHERO_FUNKOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");
                insertarFunko(new FunkoPop(datos[0], datos[1], Double.parseDouble(datos[2]), Integer.parseInt(datos[3])));
            }
        } catch (IOException e) { System.out.println("No se encontró funkos.txt, se creará al salir."); }

        // Cargar Pedidos
        try (BufferedReader br = new BufferedReader(new FileReader(FICHERO_PEDIDOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(";");
                // Leemos los datos del TXT (idCliente, idFunko, cantidad, fecha)
                insertarPedido(new Pedido(Integer.parseInt(datos[0]), Integer.parseInt(datos[1]), Integer.parseInt(datos[2]), Date.valueOf(datos[3])));
            }
        } catch (IOException e) { System.out.println("No se encontró pedidos.txt, se creará al salir."); }
    }

    private static void guardarDatosAFicheros() {
        File carpeta = new File("datos");
        if (!carpeta.exists()) carpeta.mkdir();

        // Volcar Clientes
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHERO_CLIENTES))) {
            String sql = "SELECT * FROM clientes";
            try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pw.println(rs.getString("nombre") + ";" + rs.getString("email"));
                }
            }
        } catch (Exception e) { System.out.println("Error guardando clientes.txt"); }

        // Volcar Funkos
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHERO_FUNKOS))) {
            String sql = "SELECT * FROM funkos";
            try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pw.println(rs.getString("nombre") + ";" + rs.getString("franquicia") + ";" + rs.getDouble("precio") + ";" + rs.getInt("stock"));
                }
            }
        } catch (Exception e) { System.out.println("Error guardando funkos.txt"); }

        // Volcar Pedidos
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHERO_PEDIDOS))) {
            String sql = "SELECT * FROM pedidos";
            try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Guardamos los datos de relación en el archivo
                    pw.println(rs.getInt("id_cliente") + ";" + rs.getInt("id_funko") + ";" + rs.getInt("cantidad") + ";" + rs.getDate("fecha"));
                }
            }
        } catch (Exception e) { System.out.println("Error guardando pedidos.txt"); }
    }
}