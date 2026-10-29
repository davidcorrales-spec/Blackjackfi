package modelo;

import java.util.ArrayList;
import java.util.List;

// Mano de cartas implementada como lista enlazada
public class Mano {
    private Nodo cabeza;  // primer nodo
    private Nodo cola;    // último nodo
    private int tamano;

    public Mano() {
        cabeza = null;
        cola = null;
        tamano = 0;
    }

    // Agrega una carta al final de la lista
    public void agregar(Carta carta) {
        Nodo nuevo = new Nodo(carta);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            cola = nuevo;
        }
        tamano++;
    }

    // Vacía la mano entre rondas
    public void limpiar() {
        cabeza = null;
        cola = null;
        tamano = 0;
    }

    public int getTamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    // Devuelve las cartas como lista normal (útil para mostrarlas)
    public List<Carta> getCartas() {
        List<Carta> resultado = new ArrayList<>();
        Nodo actual = cabeza;
        while (actual != null) {
            resultado.add(actual.getCarta());
            actual = actual.getSiguiente();
        }
        return resultado;
    }

    // Suma con el As valiendo 1, y lo sube a 11 si no se pasa de 21
    public int getPuntaje() {
        int total = 0;
        boolean hayAs = false;

        Nodo actual = cabeza;
        while (actual != null) {
            Carta c = actual.getCarta();
            total += c.getPuntaje();
            if (c.esAs()) {
                hayAs = true;
            }
            actual = actual.getSiguiente();
        }

        if (hayAs && total + 10 <= 21) {
            total += 10;
        }
        return total;
    }

    public boolean sePaso() {
        return getPuntaje() > 21;
    }

    // Blackjack: 21 con exactamente dos cartas
    public boolean esBlackjack() {
        return tamano == 2 && getPuntaje() == 21;
    }

    // Veinte y medio: dos cartas, una figura y un cuatro
    public boolean esVeinteYMedio() {
        if (tamano != 2) return false;
        Carta a = cabeza.getCarta();
        Carta b = cabeza.getSiguiente().getCarta();
        return (a.esFigura() && b.getValor() == Valor.CUATRO)
            || (b.esFigura() && a.getValor() == Valor.CUATRO);
    }

    // Fuerza de la mano para compararla con otra (mayor = mejor)
    public int getFuerza() {
        if (sePaso())         return -1;   // pasarse de 21 pierde siempre
        if (esBlackjack())    return 100;  // supera cualquier mano
        if (esVeinteYMedio()) return 41;   // equivale a 20.5
        return getPuntaje() * 2;           // 21 vale 42, 20 vale 40, etc.
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Nodo actual = cabeza;
        while (actual != null) {
            sb.append(actual.getCarta());
            if (actual.getSiguiente() != null) {
                sb.append(" -> ");
            }
            actual = actual.getSiguiente();
        }
        return sb.toString();
    }
}
