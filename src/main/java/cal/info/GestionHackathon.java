import java.util.ArrayList;
import java.util.List;

public class GestionHackathon {
    private List<Hackathon> listeHackathons;

    public GestionHackathon() {
        this.listeHackathons = new ArrayList<>();
    }

    public void ajouterHackathon(Hackathon h) {
        listeHackathons.add(h);
    }

    public void modifierHackathon(Hackathon h) {
        for (int i = 0; i < listeHackathons.size(); i++) {
            if (listeHackathons.get(i).obtenirNom().equals(h.obtenirNom())) {
                listeHackathons.set(i, h);
                return;
            }
        }
    }

    public void supprimerHackathon(String nomHackathon) {
        for (int i = 0; i < listeHackathons.size(); i++) {
            if (listeHackathons.get(i).obtenirNom().equals(nomHackathon)) {
                listeHackathons.remove(i);
                break; 
            }
        }
    }

    public void afficherHackathons() {
        for (int i = 0; i < listeHackathons.size(); i++) {
            Hackathon h = listeHackathons.get(i);
            System.out.println(h.obtenirNom() + " - " + h.obtenirLieu());
        }
    }
    


 public void creerEtudiants(List<Etudiant> etudiants) {
    for (int i = 0; i < etudiants.size(); i++) {
        Etudiant e = etudiants.get(i);
        System.out.println("Créé étudiant : " + e.obtenirNom());
    }

  public Equipe formerEquipe(List<Etudiant> etudiants, String nomEquipe) {
    Equipe equipe = new Equipe(nomEquipe);
    for (int i = 0; i < etudiants.size(); i++) {
        equipe.ajouterMembre(etudiants.get(i));
    }
    return equipe;
}
