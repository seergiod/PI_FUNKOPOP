public class FunkoPop {
    public int id;
    
    public int getId() {
        return id;
    }

    public String nombre;
    public String franquicia;
    public double precio;
    public int stock;

    public FunkoPop(String nombre, String franquicia, double precio, int stock) {
        this.nombre = nombre;
        this.franquicia = franquicia;
        this.precio = precio;
        this.stock = stock;
    }

    public FunkoPop(int id, String nombre, String franquicia, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.franquicia = franquicia;
        this.precio = precio;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Funko: " + nombre + " (" + franquicia + ") | Precio: " + precio + "€ | Stock: " + stock;
    }
}