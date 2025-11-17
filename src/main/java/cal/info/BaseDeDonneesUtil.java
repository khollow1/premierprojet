package cal.info;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDeDonneesUtil {
    private static final String URL = "jdbc:h2:./data/hackathonDB1";
    private static final String USER = "kenny";
    private static final String PASSWORD = "1234";

    
    private static HikariDataSource poolDeConnexions;

    static {
        try {
            Class.forName("org.h2.Driver");
            
            
            poolDeConnexions = creerPool();
            
            
            initialiserBase();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Driver H2 introuvable", e);
        }
    }

    private static HikariDataSource creerPool() {
        HikariConfig config = new HikariConfig();
        
        
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        
       
        config.setMaximumPoolSize(10);          
        config.setMinimumIdle(2);               
        config.setConnectionTimeout(30000);    
        config.setIdleTimeout(600000);          
        config.setMaxLifetime(1800000);         
      
        config.setAutoCommit(true);
        config.setConnectionTestQuery("SELECT 1");
        
      
        config.setPoolName("HackathonPool");
        
        return new HikariDataSource(config);
    }

    private static void initialiserBase() {
        String creationHackathon = "CREATE TABLE IF NOT EXISTS Hackathon1 ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nom VARCHAR(255) NOT NULL,"
                + "lieu VARCHAR(255) NOT NULL"
                + ");";

        String creationEtudiant = "CREATE TABLE IF NOT EXISTS Etudiant1 ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nom VARCHAR(255) NOT NULL,"
                + "age INT,"
                + "note DOUBLE,"
                + "matricule INT UNIQUE"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(creationHackathon);
            stmt.execute(creationEtudiant);
            
            System.out.println("Tables créées/vérifiées avec succès");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'initialisation de la base", e);
        }
    }

   
    public static Connection getConnection() throws SQLException {
        return poolDeConnexions.getConnection();
    }

    
    public static DataSource getDataSource() {
        return poolDeConnexions;
    }

    
    public static void fermerPool() {
        if (poolDeConnexions != null && !poolDeConnexions.isClosed()) {
            poolDeConnexions.close();
            System.out.println("Pool de connexions fermé");
        }
    }
}