package modelo;

import java.util.Random;

public class Mazo {
    public static final int TOTAL_CARTAS = 52;

    private final Carta[] cartas = new Carta[TOTAL_CARTAS];
    private int siguiente; // posición de la próxima carta disponible
    private final Random random = new Random();

    // Deja el mazo completo, mezclado y listo para repartir
    public Mazo() {
        inicializar();
    }

    public void inicializar() {
        crearCartas();
        barajar();
    }

    // Crea las 52 combinaciones de valor y palo, sin repeticiones
    private void crearCartas() {
        int indice = 0;
        for (Palo palo : Palo.values()) {
            for (Valor valor : Valor.values()) {
                cartas[indice] = new Carta(valor, palo);
                indice++;
            }
        }
        siguiente = 0;
    }

    // Cambia aleatoriamente el orden de las cartas (Fisher-Yates)
    public void barajar() {
        for (int i = TOTAL_CARTAS - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Carta temporal = cartas[i];
            cartas[i] = cartas[j];
            cartas[j] = temporal;
        }
        siguiente = 0;
    }

    // Devuelve la siguiente carta y avanza el índice
    public Carta siguienteCarta() {
        if (siguiente >= TOTAL_CARTAS) {
            throw new IllegalStateException("No quedan cartas en el mazo");
        }
        Carta carta = cartas[siguiente];
        siguiente++;
        return carta;
    }

    // Cantidad de cartas que aún no se han repartido
    public int cartasDisponibles() {
        return TOTAL_CARTAS - siguiente;
    }
}
