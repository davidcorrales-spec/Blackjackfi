package controlador;

import java.net.URL;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import modelo.Blackjack;
import modelo.Carta;
import modelo.Estado;
import modelo.Mano;
import modelo.Mazo;
import modelo.Palo;

public class MesaController {

    // Medidas de las cartas y de la zona donde se dibujan (según el FXML)
    private static final double ANCHO_CARTA = 88;
    private static final double ALTO_CARTA = 114;
    private static final double ANCHO_ZONA = 490;
    private static final double SEPARACION = 9;

    // Componentes del FXML (los nombres deben coincidir con los fx:id)
    @FXML private HBox cartasCrupier;
    @FXML private HBox cartasJugador;
    @FXML private TextField txtApuesta;
    @FXML private TextField txtSaldo;
    @FXML private Label lblMensaje;
    @FXML private Label lblMazo;
    @FXML private Button btnPedir;
    @FXML private Button btnPlantarse;
    @FXML private Button btnPrincipal;   // "Hacer apuesta" / "Siguiente ronda" / "Nueva partida"

    private final Blackjack juego = new Blackjack();

    // JavaFX lo llama automáticamente cuando ya se inyectaron los @FXML
    @FXML
    private void initialize() {
        actualizarVista();
    }

    // ---------------------------------------------------------------
    // Eventos
    // ---------------------------------------------------------------

    // Un solo botón cambia de función según el estado de la partida
    @FXML
    private void onAccionPrincipal() {
        switch (juego.getEstado()) {
            case APUESTA:
                if (!intentarApostar()) {
                    return;              // hubo un error: se muestra el aviso y no se redibuja
                }
                break;
            case FIN_RONDA:
                juego.nuevaRonda();
                break;
            case FIN_PARTIDA:
                juego.nuevaPartida();
                txtApuesta.clear();
                break;
            default:
                return;
        }
        actualizarVista();
    }

    @FXML
    private void onPedir() {
        juego.pedir();
        actualizarVista();
    }

    @FXML
    private void onPlantarse() {
        juego.plantarse();
        actualizarVista();
    }

    @FXML
    private void onComoJugar() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Cómo jugar");
        alerta.setHeaderText("Acércate a 21 sin pasarte");
        alerta.setContentText(
              "• Las cartas del 2 al 10 valen su número; J, Q y K valen 10.\n"
            + "• El As vale 1 u 11, lo que más te convenga.\n"
            + "• Apuesta mínima: " + modelo.Jugador.APUESTA_MINIMA + " fichas.\n"
            + "• Blackjack (As + carta de 10 con dos cartas): paga 3 a 2.\n"
            + "• Veinte y medio (figura + 4 con las dos primeras cartas): "
            + "vence a todo menos a un 21 o un Blackjack.\n"
            + "• El crupier pide carta hasta llegar a 17 o más.");
        alerta.showAndWait();
    }

    // Valida el texto escrito y apuesta. Devuelve false si hubo algún error.
    private boolean intentarApostar() {
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtApuesta.getText().trim());
        } catch (NumberFormatException e) {
            lblMensaje.setText("Escribe una apuesta válida (número entero).");
            return false;
        }

        try {
            juego.apostar(cantidad);
        } catch (IllegalArgumentException e) {
            lblMensaje.setText(e.getMessage()); // mínimo 10 o saldo insuficiente
            return false;
        }
        return true;
    }

    // ---------------------------------------------------------------
    // Dibujo: lee el modelo y refresca toda la pantalla
    // ---------------------------------------------------------------

    private void actualizarVista() {
        Estado estado = juego.getEstado();

        // Botones según el estado
        btnPedir.setDisable(estado != Estado.TURNO_JUGADOR);
        btnPlantarse.setDisable(estado != Estado.TURNO_JUGADOR);
        btnPrincipal.setDisable(estado == Estado.TURNO_JUGADOR);
        txtApuesta.setDisable(estado != Estado.APUESTA);

        switch (estado) {
            case FIN_RONDA:   btnPrincipal.setText("Siguiente ronda"); break;
            case FIN_PARTIDA: btnPrincipal.setText("Nueva partida");   break;
            default:          btnPrincipal.setText("Hacer apuesta");   break;
        }

        // Saldo y mazo
        txtSaldo.setText(String.valueOf(juego.getJugador().getSaldo()));
        lblMazo.setText(juego.getCartasDisponibles() + " / " + Mazo.TOTAL_CARTAS);

        // Cartas
        dibujarMano(cartasJugador, juego.getJugador().getMano());
        dibujarMano(cartasCrupier, juego.getCrupier().getMano());

        // Mensaje
        lblMensaje.setText(textoMensaje(estado));
    }

    private String textoMensaje(Estado estado) {
        switch (estado) {
            case APUESTA:
                return "Haz tu apuesta";
            case TURNO_JUGADOR:
                return "Tu turno: pide carta o plántate";
            case FIN_PARTIDA:
                return "Te quedaste sin fichas.\nEmpieza una nueva partida.";
            default: // FIN_RONDA
                switch (juego.getResultado()) {
                    case GANA_BLACKJACK:      return "¡BLACKJACK! ¡Ganaste!";
                    case GANA_VEINTE_Y_MEDIO: return "¡VEINTE Y MEDIO! ¡Ganaste!";
                    case GANA_JUGADOR:        return "¡GANASTE!";
                    case PIERDE_JUGADOR:      return "PERDISTE";
                    default:                  return "EMPATE";
                }
        }
    }

    private void dibujarMano(HBox contenedor, Mano mano) {
        contenedor.getChildren().clear();

        List<Carta> cartas = mano.getCartas();
        int n = cartas.size();

        // Si no caben todas en la zona, se solapan un poco (separación negativa)
        double separacion = SEPARACION;
        if (n > 1 && n * ANCHO_CARTA + (n - 1) * SEPARACION > ANCHO_ZONA) {
            separacion = (ANCHO_ZONA - n * ANCHO_CARTA) / (n - 1);
        }
        contenedor.setSpacing(separacion);

        for (Carta c : cartas) {
            contenedor.getChildren().add(crearVistaCarta(c));
        }
    }

    // Usa la imagen /cartas/<codigo>.png (por ejemplo /cartas/10C.png).
    // Si el archivo no existe, dibuja la carta como texto.
    private Node crearVistaCarta(Carta c) {
        URL url = getClass().getResource("/cartas/" + c + ".png");

        if (url != null) {
            ImageView vista = new ImageView(new Image(url.toExternalForm()));
            vista.setFitWidth(ANCHO_CARTA);
            vista.setFitHeight(ALTO_CARTA);
            vista.setPreserveRatio(true);
            return vista;
        }

        boolean roja = c.getPalo() == Palo.CORAZONES || c.getPalo() == Palo.DIAMANTES;
        Label texto = new Label(c.toString());
        texto.setFont(Font.font("Arial Black", 24));
        texto.setTextFill(roja ? Color.RED : Color.BLACK);

        StackPane carta = new StackPane(texto);
        carta.setMinSize(ANCHO_CARTA, ALTO_CARTA);
        carta.setPrefSize(ANCHO_CARTA, ALTO_CARTA);
        carta.setMaxSize(ANCHO_CARTA, ALTO_CARTA);
        carta.setStyle("-fx-background-color: white;"
                     + "-fx-border-color: black;"
                     + "-fx-border-radius: 6;"
                     + "-fx-background-radius: 6;");
        return carta;
    }
}
