import java.sql.Date;

public class Pedido {
    private int id;
    private int idCliente;
    private int idFunko;
    private int cantidad;
    private Date fecha;

    public Pedido(int idCliente, int idFunko, int cantidad, Date fecha) {
        this.idCliente = idCliente;
        this.idFunko = idFunko;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public Pedido(int id, int idCliente, int idFunko, int cantidad, Date fecha) {
        this.id = id;
        this.idCliente = idCliente;
        this.idFunko = idFunko;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Pedido ID: " + id + " | Cliente ID: " + idCliente + " | Funko ID: " + idFunko + " | Cantidad: " + cantidad + " | Fecha: " + fecha;
    }

    public int getId() {
        return id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdFunko() {
        return idFunko;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Date getFecha() {
        return fecha;
    }
}