package modelo;

public class Jugador {
    public static final int SALDO_INICIAL = 1000;
    public static final int APUESTA_MINIMA = 10;

    private int saldo = SALDO_INICIAL;
    private int apuesta = 0;
    private final Mano mano = new Mano();

    public void apostar(int cantidad) {
        if (cantidad < APUESTA_MINIMA) {
            throw new IllegalArgumentException("La apuesta mínima es " + APUESTA_MINIMA);
        }
        if (cantidad > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        saldo -= cantidad;   // la apuesta sale del saldo al apostar
        apuesta = cantidad;
    }

    public void ganar() {        // recupera la apuesta y gana otro tanto igual
        saldo += apuesta * 2;
        apuesta = 0;
    }

    public void ganarBlackjack() {   // recupera la apuesta y gana 1.5 veces su valor
        saldo += apuesta + (apuesta * 3) / 2;
        apuesta = 0;
    }

    public void empatar() {      // recupera la apuesta
        saldo += apuesta;
        apuesta = 0;
    }

    public void perder() {       // la apuesta ya se descontó
        apuesta = 0;
    }

    public boolean puedeApostar() {
        return saldo >= APUESTA_MINIMA;
    }

    public int getSaldo()   { return saldo; }
    public int getApuesta() { return apuesta; }
    public Mano getMano()   { return mano; }
}
