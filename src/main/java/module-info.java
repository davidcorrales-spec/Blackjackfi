// Renombrar a module-info.java SOLO si el proyecto usa módulos
module blackjack {
    requires javafx.controls;
    requires javafx.fxml;

    opens controlador to javafx.fxml;
    exports app;
    exports modelo;
}
