import java.sql.*;
import java.util.Scanner;

public class Tienda {

   private static final String URL = System.getenv("DB_URL") != null 
        ? System.getenv("DB_URL") 
        : "jdbc:mysql://TU_IP_PUBLICA_DE_AWS:3306/tienda_funkos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        
    private static final String USER = System.getenv("DB_USER") != null 
        ? System.getenv("DB_USER") 
        : "root";
        
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null 
        ? System.getenv("DB_PASSWORD") 
        : "toor";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("\n--- GESTIÓN TIENDA FUNKOS (SOLO BASE DE DATOS) ---");
            System.out.println("1. Listar Funkos (SELECT)");
            System.out.println("2. Insertar Funko (INSERT)");
            System.out.println("3. Listar Clientes (SELECT)");
            System.out.println("4. Registrar Cliente (INSERT)");
            System.out.println("5. Ver Historial Pedidos (SELECT)");
            System.out.println("6. Crear un Pedido (INSERT)");
            System.out.println("7. Eliminar un Cliente (DELETE)");
            System.out.println("8. Eliminar un Funko Pop (DELETE) *NUEVO*");
            System.out.println("9. Salir del programa");
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
                    System.out.print("ID del Funko a borrar: ");
                    int idFunkoBorrar = Integer.parseInt(scanner.nextLine());
                    eliminarFunkopop(idFunkoBorrar);
                    break;
                case 9:
                    System.out.println("¡Programa cerrado con éxito!");
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 9);

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
            System.out.println("Ese Funko ya existe en la base de datos.");
            return;
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
            System.out.println("Un cliente con ese email ya está registrado.");
            return;
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
        if (existePedido(p.idCliente, p.idFunko, p.cantidad, p.fecha)) {
            System.out.println("Este pedido exacto ya está registrado.");
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
            if (filas > 0) System.out.println("Cliente eliminado correctamente.");
            else System.out.println("No se encontró ningún cliente con ese ID.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        }
    }

    private static void eliminarFunkopop(int id) {
        String sql = "DELETE FROM funkos WHERE id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Funko Pop eliminado correctamente.");
            else System.out.println("No se encontró ningún Funko Pop con ese ID.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar Funko: " + e.getMessage());
        }
    }
}