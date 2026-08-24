package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:cantina.db";

    public Connection conectar() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }

        return conn;
    }

    public void inicializarBaseDeDatos() {

        String sqlPartida = """
                CREATE TABLE IF NOT EXISTS Partida (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    jugador TEXT NOT NULL,
                    dia INTEGER NOT NULL,
                    dinero REAL NOT NULL,
                    hambre INTEGER NOT NULL,
                    infracciones INTEGER NOT NULL,
                    fechaCreacion TEXT NOT NULL
                );
                """;

        String sqlProducto = """
                CREATE TABLE IF NOT EXISTS Producto (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    precioCompra REAL NOT NULL,
                    precioVenta REAL NOT NULL,
                    categoria TEXT NOT NULL
                );
                """;

        String sqlStock = """
                CREATE TABLE IF NOT EXISTS Stock (
                    idPartida INTEGER NOT NULL,
                    idProducto INTEGER NOT NULL,
                    cantidad INTEGER NOT NULL,
                    PRIMARY KEY (idPartida, idProducto),
                    FOREIGN KEY (idPartida) REFERENCES Partida(id),
                    FOREIGN KEY (idProducto) REFERENCES Producto(id)
                );
                """;

        String sqlVenta = """
                CREATE TABLE IF NOT EXISTS Venta (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    idProducto INTEGER NOT NULL,
                    cantidad INTEGER NOT NULL,
                    importeTotal REAL NOT NULL,
                    fecha TEXT NOT NULL,
                    idPartida INTEGER NOT NULL,
                    FOREIGN KEY (idProducto) REFERENCES Producto(id),
                    FOREIGN KEY (idPartida) REFERENCES Partida(id)
                );
                """;

        String sqlEstadistica = """
                CREATE TABLE IF NOT EXISTS Estadistica (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    idPartida INTEGER NOT NULL UNIQUE,
                    clientesAtendidos INTEGER NOT NULL,
                    productosVendidos INTEGER NOT NULL,
                    ingresosObtenidos REAL NOT NULL,
                    mejorGanancia REAL NOT NULL,
                    diasSobrevividos INTEGER NOT NULL,
                    FOREIGN KEY (idPartida) REFERENCES Partida(id)
                );
                """;

        String sqlConfiguracion = """
                CREATE TABLE IF NOT EXISTS Configuracion (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    idPartida INTEGER NOT NULL UNIQUE,
                    volumenMusica REAL NOT NULL,
                    FOREIGN KEY (idPartida) REFERENCES Partida(id)
                );
                """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlPartida);
            stmt.execute(sqlProducto);
            stmt.execute(sqlStock);
            stmt.execute(sqlVenta);
            stmt.execute(sqlEstadistica);
            stmt.execute(sqlConfiguracion);

            System.out.println("Base de datos SQLite inicializada correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
