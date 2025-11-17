package cal.info;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import javax.sql.DataSource;
import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.io.IOException;

public class App 
{
   public static void main(String[] args) throws IOException {
    // Récupère le DataSource du pool
    DataSource ds = BaseDeDonneesUtil.getDataSource();

    // Création du DAO et du service à injecter
    GestionHackathonDao dao = new GestionHackathonDao(ds);
    GestionHackathon service = new GestionHackathon(dao);

    HttpServer serveur = HttpServer.create(new InetSocketAddress(8000), 0);
    serveur.createContext("/accueil", new HttpHandler() {
        @Override
        public void handle(HttpExchange echange) throws IOException {
            String response = "Bienvenue sur la page d'accueil !";
            echange.sendResponseHeaders(200, response.length());
            OutputStream os = echange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    });

    serveur.createContext("/etudiants", new ControleurEtudiant(service));
    serveur.createContext("/hackathons", new ControleurHackathon(service));

    serveur.setExecutor(null);
    serveur.start();

    System.out.println("Serveur démarré et en écoute sur le port 8000");

   }
}
