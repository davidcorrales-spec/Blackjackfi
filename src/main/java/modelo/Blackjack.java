package modelo;

public class Blackjack {
    private static final int CARTAS_MINIMAS = 20; // para no quedarse sin mazo a mitad de ronda

    private final Mazo mazo = new Mazo();
    private final Crupier crupier = new Crupier();
    private Jugador jugador;
    private Estado estado;
    private Resultado resultado;

    public Blackjack() {
        nuevaPartida();
    }

    // Nueva partida: el jugador vuelve a empezar con 1000 fichas
    public void nuevaPartida() {
        jugador = new Jugador();
        mazo.inicializar();
        crupier.getMano().limpiar();
        resultado = null;
        estado = Estado.APUESTA;
    }

    // Paso 1: apostar y recibir las dos cartas iniciales
    public void apostar(int cantidad) {
        exigirEstado(Estado.APUESTA);
        jugador.apostar(cantidad);

        if (mazo.cartasDisponibles() < CARTAS_MINIMAS) {
            mazo.inicializar();
        }

        jugador.getMano().agregar(mazo.siguienteCarta());
        jugador.getMano().agregar(mazo.siguienteCarta());
        estado = Estado.TURNO_JUGADOR;

        if (jugador.getMano().getPuntaje() == 21) {
            plantarse();
        }
    }

    // Acción: pedir otra carta
    public void pedir() {
        exigirEstado(Estado.TURNO_JUGADOR);
        jugador.getMano().agregar(mazo.siguienteCarta());

        if (jugador.getMano().sePaso()) {
            terminarRonda(Resultado.PIERDE_JUGADOR); // el crupier no juega
        } else if (jugador.getMano().getPuntaje() == 21) {
            plantarse();
        }
    }

    // Acción: plantarse, sigue el turno del crupier
    public void plantarse() {
        exigirEstado(Estado.TURNO_JUGADOR);
        crupier.jugarTurno(mazo);

        Mano manoJugador = jugador.getMano();
        int fuerzaJugador = manoJugador.getFuerza();
        int fuerzaCrupier = crupier.getMano().getFuerza();

        if (fuerzaJugador > fuerzaCrupier) {
            if (manoJugador.esBlackjack()) {
                terminarRonda(Resultado.GANA_BLACKJACK);
            } else if (manoJugador.esVeinteYMedio()) {
                terminarRonda(Resultado.GANA_VEINTE_Y_MEDIO);
            } else {
                terminarRonda(Resultado.GANA_JUGADOR);
            }
        } else if (fuerzaJugador < fuerzaCrupier) {
            terminarRonda(Resultado.PIERDE_JUGADOR);
        } else {
            terminarRonda(Resultado.EMPATE);
        }
    }

    // Prepara la siguiente ronda dentro de la misma partida
    public void nuevaRonda() {
        exigirEstado(Estado.FIN_RONDA);
        jugador.getMano().limpiar();
        crupier.getMano().limpiar();
        resultado = null;
        estado = jugador.puedeApostar() ? Estado.APUESTA : Estado.FIN_PARTIDA;
    }

    private void terminarRonda(Resultado r) {
        resultado = r;
        switch (r) {
            case GANA_BLACKJACK:      jugador.ganarBlackjack(); break;
            case GANA_VEINTE_Y_MEDIO:
            case GANA_JUGADOR:        jugador.ganar();          break;
            case PIERDE_JUGADOR:      jugador.perder();         break;
            case EMPATE:              jugador.empatar();        break;
        }
        estado = Estado.FIN_RONDA;
    }

    private void exigirEstado(Estado esperado) {
        if (estado != esperado) {
            throw new IllegalStateException("Acción no válida en el estado " + estado);
        }
    }

    public int getCartasDisponibles() {
        return mazo.cartasDisponibles();
    }

    public Jugador getJugador()     { return jugador; }
    public Crupier getCrupier()     { return crupier; }
    public Estado getEstado()       { return estado; }
    public Resultado getResultado() { return resultado; }
}
