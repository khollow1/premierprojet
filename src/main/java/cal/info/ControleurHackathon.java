package cal.info;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ControleurHackathon implements HttpHandler {
    private List<Hackathon> lesHackathons = new ArrayList<>();

    public ControleurHackathon() {
        lesHackathons.add(new Hackathon("HackQC", "Montréal"));
        lesHackathons.add(new Hackathon("HackConcordia", "Montréal"));
        lesHackathons.add(new Hackathon("HackUQam", "Montréal"));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String typeRequete = exchange.getRequestMethod();
        String chemin = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        System.out.println("Requête reçue : " + typeRequete + " " + chemin + (query != null ? "?" + query : ""));

        switch (typeRequete) {
            case "GET":
                if (query != null && query.contains("nom=")) {
                    rechercheHackathons(exchange, query);
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
                supprimerHackathon(exchange);
                break;

            default:
                sendResponse(exchange, "Méthode HTTP non supportée : " + typeRequete, 405);
                break;
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void listeHackathons(HttpExchange exchange) throws IOException {
        String texte = "Liste des hackathons:\n";
        for (int i = 0; i < lesHackathons.size(); i++) {
            Hackathon h = lesHackathons.get(i);
            texte += h.getNom() + " - " + h.getLieu() + "\n";
        }
        sendResponse(exchange, texte, 200);
    }

    private void rechercheHackathons(HttpExchange exchange, String query) throws IOException {
        String nomRecherche = query.replace("nom=", "");
        String texte = "Résultats pour " + nomRecherche + ":\n";
        for (int i = 0; i < lesHackathons.size(); i++) {
            Hackathon h = lesHackathons.get(i);
            if (h.getNom().equalsIgnoreCase(nomRecherche)) {
                texte += h.getNom() + " - " + h.getLieu() + "\n";
            }
        }
        sendResponse(exchange, texte, 200);
    }

    private void ajouterHackathon(HttpExchange exchange) throws IOException {
        Hackathon nouveau = new Hackathon("HackNew", "VilleX");
        lesHackathons.add(nouveau);
        String texte = "Hackathon ajouté: " + nouveau.getNom() + " - " + nouveau.getLieu();
        sendResponse(exchange, texte, 201);
    }

    private void modifierHackathon(HttpExchange exchange) throws IOException {
        if (lesHackathons.size() > 0) {
            Hackathon h = lesHackathons.get(0);
            h.setNom("HackModifie");
            h.setLieu("VilleY");
            sendResponse(exchange, "Hackathon modifié : " + h.getNom() + " - " + h.getLieu(), 200);
        } else {
            sendResponse(exchange, "Aucun hackathon à modifier", 404);
        }
    }

    private void supprimerHackathon(HttpExchange exchange) throws IOException {
        if (lesHackathons.size() > 0) {
            Hackathon h = lesHackathons.remove(0);
            sendResponse(exchange, "Hackathon supprimé : " + h.getNom(), 200);
        } else {
            sendResponse(exchange, "Aucun hackathon à supprimer", 404);
        }
    }
}
