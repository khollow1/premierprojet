package cal.info;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.io.IOException;


/**
 * Hello world!
 *
 */
public class App 
{
   public static void main(String[] args) throws IOException {
    // Création du serveur HTTP qui écoutera sur le port 8000
    HttpServer serveur = HttpServer.create(new InetSocketAddress(8000), 0);
    // Route "/accueil"
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
    serveur.createContext("/etudiants", new ControleurEtudiant());
    serveur.createContext("/hackathons", new ControleurHackathon());


    serveur.setExecutor(null);
    serveur.start();

    System.out.println("Serveur démarré et en écoute sur le port 8000");

    
}
}
