package cal.info;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;

public class ControleurEtudiant implements HttpHandler {

    private final GestionHackathon service;
    private final Gson gson = new Gson();

    public ControleurEtudiant(GestionHackathon service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case "POST":
                    ajouterEtudiants(exchange);
                    break;
                default:
                    exchange.sendResponseHeaders(405, -1); 
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            envoyerReponseTexte(exchange, "Erreur serveur : " + e.getMessage(), 500);
        }
    }

    private void ajouterEtudiants(HttpExchange exchange) throws IOException {
        try (InputStream requestBody = exchange.getRequestBody();
             InputStreamReader reader = new InputStreamReader(requestBody, StandardCharsets.UTF_8)) {

            Type setType = new TypeToken<HashSet<Etudiant>>() {}.getType();
            HashSet<Etudiant> etudiants;
            try {
                etudiants = gson.fromJson(reader, setType);
            } catch (Exception ex) {
                envoyerReponseTexte(exchange, "JSON invalide", 400);
                return;
            }

            if (etudiants == null || etudiants.isEmpty()) {
                envoyerReponseTexte(exchange, "Aucun étudiant fourni", 400);
                return;
            }

            try {
            
                service.ajouterEtudiant(new ArrayList<>(etudiants));
                envoyerReponseTexte(exchange, "Les étudiants ont été ajoutés (" + etudiants.size() + ")", 201);
            } catch (Exception ex) {
                ex.printStackTrace();
                envoyerReponseTexte(exchange, "Erreur serveur lors de l'ajout des étudiants", 500);
            }
        }
    }

    private void envoyerReponseTexte(HttpExchange exchange, String texte, int code) throws IOException {
        byte[] bytes = texte.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}