public class Cliente {
    public int id;
    public String nombre;
    public String email;

    public Cliente(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public Cliente(int id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Nombre: " + nombre + " | Email: " + email;
    }
}