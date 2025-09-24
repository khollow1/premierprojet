package cal.info;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ControleurEtudiant implements HttpHandler {
    private List<Etudiant> lesEtudiants = new ArrayList<>();

    public ControleurEtudiant() {
        lesEtudiants.add(new Etudiant("Dominic", 20, 85.5));
        lesEtudiants.add(new Etudiant("Ken", 22, 78.0));
        lesEtudiants.add(new Etudiant("Benga", 19, 91.2));
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
                break;
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void listeEtudiants(HttpExchange exchange) throws IOException {
        String texte = "Liste des étudiants:\n";
        for (int i = 0; i < lesEtudiants.size(); i++) {
            Etudiant e = lesEtudiants.get(i);
            texte += e.toString() + "\n";
        }
        sendResponse(exchange, texte, 200);
    }

    private void rechercheEtudiant(HttpExchange exchange, String query) throws IOException {
        String nomRecherche = query.replace("nom=", "");
        String texte = "Résultats pour " + nomRecherche + ":\n";
        for (int i = 0; i < lesEtudiants.size(); i++) {
            Etudiant e = lesEtudiants.get(i);
            if (e.getNom().equalsIgnoreCase(nomRecherche)) {
                texte += e.toString() + "\n";
            }
        }
        sendResponse(exchange, texte, 200);
    }

    private void ajouterEtudiant(HttpExchange exchange) throws IOException {
        Etudiant nouveau = new Etudiant("NouvelEtudiant", 18, 60.0);
        lesEtudiants.add(nouveau);
        String texte = "Étudiant ajouté: " + nouveau.toString();
        sendResponse(exchange, texte, 201);
    }

    private void modifierEtudiant(HttpExchange exchange) throws IOException {
        if (!lesEtudiants.isEmpty()) {
            Etudiant e = lesEtudiants.get(0);
            e.setNom("EtudiantModifie");
            e.setAge(25);
            e.setNote(99.9);
            String texte = "Étudiant modifié: " + e.toString();
            sendResponse(exchange, texte, 200);
        } else {
            sendResponse(exchange, "Aucun étudiant à modifier", 404);
        }
    }

    private void supprimerEtudiant(HttpExchange exchange) throws IOException {
        if (!lesEtudiants.isEmpty()) {
            Etudiant e = lesEtudiants.remove(0);
            String texte = "Étudiant supprimé: " + e.getNom();
            sendResponse(exchange, texte, 200);
        } else {
            sendResponse(exchange, "Aucun étudiant à supprimer", 404);
        }
    }
}
