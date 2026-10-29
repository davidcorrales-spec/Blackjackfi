package modelo;

public enum Palo {
    PICAS('P'),
    CORAZONES('C'),
    DIAMANTES('D'),
    TREBOLES('T');

    private final char simbolo;

    Palo(char simbolo) {
        this.simbolo = simbolo;
    }

    public char getSimbolo() {
        return simbolo;
    }

    // Convierte 'P', 'C', 'D' o 'T' en el palo correspondiente
    public static Palo desdeSimbolo(char c) {
        for (Palo p : values()) {
            if (p.simbolo == Character.toUpperCase(c)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Palo inválido: " + c);
    }
}
