package cal.info;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ControleurHackathon implements HttpHandler {

    private final GestionHackathon gestionHackathon = new GestionHackathon();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String methode = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();

        try {
            switch (methode) {
                case "GET":
                    if (query != null && query.contains("nom=")) {
                        String nom = extraireParametre(query, "nom");
                        rechercheHackathons(exchange, nom);
                    } else {
                        listeHackathons(exchange);
                    }
                    break;
                case "POST":
                    ajouterHackathon(exchange);
                    break;
                case "PUT":
                    modifierHackathon(exchange);
                    break;
                case "DELETE":
                    supprimerHackathon(exchange, query);
                    break;
                default:
                    exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            envoyerReponseTexte(exchange, "Erreur serveur : " + e.getMessage(), 500);
        }
    }

    private void ajouterHackathon(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            Hackathon nouveau = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), Hackathon.class);

            if (gestionHackathon.ajouterHackathon(nouveau)) {
                envoyerReponseJSON(exchange, nouveau, 201);
            } else {
                envoyerReponseTexte(exchange, "Hackathon existe déjà", 409);
            }
        } catch (Exception e) {
            e.printStackTrace();
            envoyerReponseTexte(exchange, "Erreur dans le JSON", 400);
        }
    }

    private void listeHackathons(HttpExchange exchange) throws IOException {
        try {
            List<Hackathon> hackathons = gestionHackathon.retrouverHackathons();
            envoyerReponseJSON(exchange, hackathons, 200);
        } catch (Exception e) {
            envoyerReponseTexte(exchange, "Erreur lors de la récupération", 500);
        }
    }

    private void rechercheHackathons(HttpExchange exchange, String nom) throws IOException {
        try {
            List<Hackathon> resultats = gestionHackathon.rechercherParNom(nom);
            envoyerReponseJSON(exchange, resultats, 200);
        } catch (Exception e) {
            envoyerReponseTexte(exchange, "Erreur lors de la recherche", 500);
        }
    }

    private void modifierHackathon(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            Hackathon modifie = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), Hackathon.class);
            if (gestionHackathon.modifierHackathon(modifie)) {
                envoyerReponseJSON(exchange, modifie, 200);
            } else {
                envoyerReponseTexte(exchange, "Hackathon non trouvé", 404);
            }
        } catch (Exception e) {
            envoyerReponseTexte(exchange, "Erreur dans le JSON", 400);
        }
    }

    private void supprimerHackathon(HttpExchange exchange, String query) throws IOException {
        if (query != null && query.contains("id=")) {
            try {
                int id = Integer.parseInt(extraireParametre(query, "id"));
                if (gestionHackathon.supprimerHackathon(id)) {
                    envoyerReponseTexte(exchange, "Hackathon supprimé", 200);
                } else {
                    envoyerReponseTexte(exchange, "Hackathon non trouvé", 404);
                }
            } catch (NumberFormatException e) {
                envoyerReponseTexte(exchange, "ID invalide", 400);
            } catch (Exception e) {
                envoyerReponseTexte(exchange, "Erreur lors de la suppression", 500);
            }
        } else {
            envoyerReponseTexte(exchange, "ID manquant", 400);
        }
    }

    private String extraireParametre(String query, String cle) {
        for (String param : query.split("&")) {
            String[] parts = param.split("=");
            if (parts.length == 2 && parts[0].equals(cle)) {
                return parts[1];
            }
        }
        return null;
    }

    private void envoyerReponseJSON(HttpExchange exchange, Object obj, int code) throws IOException {
        String json = gson.toJson(obj);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
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
