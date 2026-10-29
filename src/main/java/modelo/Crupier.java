package modelo;

public class Crupier {
    private static final int LIMITE = 17;
    private final Mano mano = new Mano();

    // Se ejecuta completo cuando el jugador se planta
    public void jugarTurno(Mazo mazo) {
        mano.agregar(mazo.siguienteCarta());
        mano.agregar(mazo.siguienteCarta());

        while (mano.getPuntaje() < LIMITE) {
            mano.agregar(mazo.siguienteCarta());
        }
    }

    public Mano getMano() { return mano; }
}
