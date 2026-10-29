package modelo;

public class Nodo {
    private final Carta carta;
    private Nodo siguiente;

    public Nodo(Carta carta) {
        this.carta = carta;
        this.siguiente = null;
    }

    public Carta getCarta() {
        return carta;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}
