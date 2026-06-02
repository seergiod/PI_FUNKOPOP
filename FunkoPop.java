public class FunkoPop {
    public int id;
    
    public int getId() {
        return id;
    }

    private String nombre;
    private String franquicia;
    private double precio;
    private int stock;

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

    public String getNombre() {
        return nombre;
    }

    public String getFranquicia() {
        return franquicia;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }
}