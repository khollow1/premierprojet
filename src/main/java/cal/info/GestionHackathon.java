package cal.info;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GestionHackathon {
    private GestionHackathonDao dao;

    Map<String, Set<Etudiant>> etudiantsParProg = new HashMap<>();

    public GestionHackathon(GestionHackathonDao dao) {
        this.dao = dao;
    }
        // ========== Méthodes Hackathon (logique métier) ==========
    
    public boolean ajouterHackathon(Hackathon hackathon) throws SQLException {
        return dao.ajouterHackathon(hackathon);
    }

    public List<Hackathon> retrouverHackathons() throws SQLException {
        return dao.retrouverTousHackathons();
    }

    public List<Hackathon> rechercherParNom(String nom) throws SQLException {
        return dao.rechercherParNom(nom);
    }

    public boolean modifierHackathon(Hackathon hackathon) throws SQLException {
        return dao.modifierHackathon(hackathon);
    }

    public boolean supprimerHackathon(int id) throws SQLException {
        return dao.supprimerHackathon(id);
    }

    // ========== Méthodes Etudiant (logique métier + map) ==========
    
    /**
     * Ajoute une liste d'étudiants en mémoire (map par programme)
     * Évite les doublons grâce au HashSet
     */
    public void ajouterEtudiant(List<Etudiant> etudiants) {
        if (etudiants == null || etudiants.isEmpty()) return;

        for (int i = 0; i < etudiants.size(); i++) {
            Etudiant etudiant = etudiants.get(i);
            if (etudiant == null) continue;

            String rawProg = etudiant.getProgramme();
            String normalized;
            if (rawProg == null) {
                normalized = "default";
            } else {
                normalized = rawProg.trim();
                if (normalized.isEmpty()) {
                    normalized = "default";
                } else {
                    normalized = normalized.toLowerCase();
                }
            }

            etudiantsParProg.putIfAbsent(normalized, new HashSet<>());
            Set<Etudiant> set = etudiantsParProg.get(normalized);
            set.add(etudiant);
        }
    }

    /**
     * Persiste les étudiants en mémoire vers la base de données
     * @return nombre d'étudiants ajoutés
     */
    public int persisterEtudiants() throws SQLException {
        int total = 0;

        for (Set<Etudiant> setEtudiants : etudiantsParProg.values()) {
            HashSet<Etudiant> etudiantsBatch = new HashSet<>(setEtudiants);
            try {
                if (dao.ajouterEtudiant(etudiantsBatch)) {
                    total += etudiantsBatch.size();
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Unique index or primary key violation")) {
                    System.err.println("Certains étudiants existent déjà dans la base.");
                } else {
                    throw e;
                }
            }
        }

        return total;
    }

    /**
     * Charge les étudiants depuis la base vers la map en mémoire
     */
    public void chargerEtudiantsDepuisBase() throws SQLException {
        etudiantsParProg.clear();
        List<Etudiant> tous = dao.retrouverTousEtudiants();
        ajouterEtudiant(tous);
    }

    /**
     * Obtient les étudiants d'un programme spécifique
     */
    public Set<Etudiant> getEtudiantsProgramme(String programme) {
        if (programme == null) programme = "default";
        String normalized = programme.trim().toLowerCase();
        Set<Etudiant> set = etudiantsParProg.get(normalized);
        return set == null ? new HashSet<>() : new HashSet<>(set); 
    }

    /**
     * Retourne la map complète (pour affichage/debug)
     */
    public Map<String, Set<Etudiant>> getEtudiantsParProg() {
        return etudiantsParProg;
    }

    /**
     * Compte le nombre total d'étudiants en mémoire
     */
    public int compterEtudiants() {
        int total = 0;
        for (Set<Etudiant> set : etudiantsParProg.values()) {
            total += set.size();
        }
        return total;
    }
}