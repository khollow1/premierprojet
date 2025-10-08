package cal.info;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDeDonneesUtil {
    private static final String URL = "jdbc:h2:./data/hackathonDB1"; 
    private static final String USER = "kenny";
    private static final String PASSWORD = "1234";
    static {
        try {
            // Charger le driver H2
            Class.forName("org.h2.Driver");

            // Initialiser la base de données en créant la table si elle n'existe pas
            initialiserBase();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void initialiserBase() {
        String creationTableSQL = "CREATE TABLE IF NOT EXISTS Hackathon1 ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nom VARCHAR(255) NOT NULL,"
                + "lieu VARCHAR(255) NOT NULL"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(creationTableSQL); // Correction ici

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}