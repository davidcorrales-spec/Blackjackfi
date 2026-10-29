package modelo;

public enum Valor {
    AS("A", 1),
    DOS("2", 2), TRES("3", 3), CUATRO("4", 4), CINCO("5", 5), SEIS("6", 6),
    SIETE("7", 7), OCHO("8", 8), NUEVE("9", 9), DIEZ("10", 10),
    J("J", 10), Q("Q", 10), K("K", 10);

    private final String simbolo;
    private final int puntaje;

    Valor(String simbolo, int puntaje) {
        this.simbolo = simbolo;
        this.puntaje = puntaje;
    }

    public String getSimbolo() {
        return simbolo;
    }

    // Puntaje base: el As devuelve 1
    public int getPuntaje() {
        return puntaje;
    }

    // Puntaje alternativo: el As puede valer 11, el resto no cambia
    public int getPuntajeAlto() {
        return this == AS ? 11 : puntaje;
    }

    public boolean esAs() {
        return this == AS;
    }

    public boolean esFigura() {
        return this == J || this == Q || this == K;
    }

    // Convierte "A", "2", ..., "K" en el valor correspondiente
    public static Valor desdeSimbolo(String s) {
        for (Valor v : values()) {
            if (v.simbolo.equalsIgnoreCase(s)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Valor inválido: " + s);
    }
}
