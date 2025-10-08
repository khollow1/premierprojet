package cal.info;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ControleurEtudiant implements HttpHandler {
    private List<Etudiant> lesEtudiants;
    private final String FICHIER = "etudiants.json";
    private final Gson gson = new Gson();

    public ControleurEtudiant() {
        lesEtudiants = chargerEtudiants();
        if (lesEtudiants == null) {
            lesEtudiants = new ArrayList<>();
            // étudiants par défaut
            lesEtudiants.add(new Etudiant("dom", 20, 85.5));
            lesEtudiants.add(new Etudiant("ken", 22, 78.0));
            lesEtudiants.add(new Etudiant("ben", 19, 91.2));
            sauvegarderEtudiants();
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String typeRequete = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();

        switch (typeRequete) {
            case "GET":
                if (query != null && query.contains("nom=")) {
                    rechercheEtudiant(exchange, query);
                } else {
                    listeEtudiants(exchange);
                }
                break;
            case "POST":
                ajouterEtudiant(exchange);
                break;
            case "PUT":
                modifierEtudiant(exchange);
                break;
            case "DELETE":
                supprimerEtudiant(exchange);
                break;
            default:
                sendResponse(exchange, "Méthode HTTP non supportée : " + typeRequete, 405);
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void listeEtudiants(HttpExchange exchange) throws IOException {
        String texte = gson.toJson(lesEtudiants);
        sendResponse(exchange, texte, 200);
    }

    private void rechercheEtudiant(HttpExchange exchange, String query) throws IOException {
        String nomRecherche = query.replace("nom=", "");
        List<Etudiant> resultat = new ArrayList<>();
        for (Etudiant e : lesEtudiants) {
            if (e.getNom().equalsIgnoreCase(nomRecherche)) {
                resultat.add(e);
            }
        }
        String texte = gson.toJson(resultat);
        sendResponse(exchange, texte, 200);
    }

    private void ajouterEtudiant(HttpExchange exchange) throws IOException {
        Etudiant nouveau = new Etudiant("NouvelEtudiant", 18, 60.0);
        lesEtudiants.add(nouveau);
        sauvegarderEtudiants();
        sendResponse(exchange, gson.toJson(nouveau), 201);
    }

    private void modifierEtudiant(HttpExchange exchange) throws IOException {
        if (!lesEtudiants.isEmpty()) {
            Etudiant e = lesEtudiants.get(0);
            e.setNom("EtudiantModifie");
            e.setAge(25);
            e.setNote(99.9);
            sauvegarderEtudiants();
            sendResponse(exchange, gson.toJson(e), 200);
        } else {
            sendResponse(exchange, "Aucun étudiant à modifier", 404);
        }
    }

    private void supprimerEtudiant(HttpExchange exchange) throws IOException {
        if (!lesEtudiants.isEmpty()) {
            Etudiant e = lesEtudiants.remove(0);
            sauvegarderEtudiants();
            sendResponse(exchange, gson.toJson(e), 200);
        } else {
            sendResponse(exchange, "Aucun étudiant à supprimer", 404);
        }
    }

    // ================== GSON ==================

    private void sauvegarderEtudiants() {
        try (Writer writer = new FileWriter(FICHIER)) {
            gson.toJson(lesEtudiants, writer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private List<Etudiant> chargerEtudiants() {
        try (Reader reader = new FileReader(FICHIER)) {
            return gson.fromJson(reader, new TypeToken<List<Etudiant>>() {}.getType());
        } catch (IOException ex) {
            return null; 
        }
    }
}
