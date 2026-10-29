package modelo;

public class Carta {
    private final Valor valor;
    private final Palo palo;

    public Carta(Valor valor, Palo palo) {
        this.valor = valor;
        this.palo = palo;
    }

    // Constructor que recibe el texto, por ejemplo "2P" o "10C"
    public Carta(String codigo) {
        if (codigo == null || codigo.length() < 2 || codigo.length() > 3) {
            throw new IllegalArgumentException("Código de carta inválido: " + codigo);
        }
        String textoValor = codigo.substring(0, codigo.length() - 1);
        char textoPalo = codigo.charAt(codigo.length() - 1);

        this.valor = Valor.desdeSimbolo(textoValor);
        this.palo = Palo.desdeSimbolo(textoPalo);
    }

    public Valor getValor() {
        return valor;
    }

    public Palo getPalo() {
        return palo;
    }

    public int getPuntaje() {
        return valor.getPuntaje();
    }

    public boolean esAs() {
        return valor.esAs();
    }

    public boolean esFigura() {
        return valor.esFigura();
    }

    @Override
    public String toString() {
        return valor.getSimbolo() + palo.getSimbolo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carta)) return false;
        Carta otra = (Carta) o;
        return valor == otra.valor && palo == otra.palo;
    }

    @Override
    public int hashCode() {
        return 31 * valor.hashCode() + palo.hashCode();
    }
}
