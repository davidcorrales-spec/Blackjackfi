package modelo;

public enum Estado {
    APUESTA,          // esperando que el jugador apueste
    TURNO_JUGADOR,    // el jugador puede pedir o plantarse
    FIN_RONDA,        // la ronda terminó, se ve el resultado
    FIN_PARTIDA       // el jugador no tiene saldo para apostar el mínimo
}
