import java.sql.Date;

public class Pedido {
    public int id;
    public int idCliente;
    public int idFunko;
    public int cantidad;
    public Date fecha;

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
}