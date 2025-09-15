import java.util.ArrayList;
import java.util.List;
import cal.info;
public class Etudiant {
    private String nomEtudiant;
    private int ageEtudiant;
    private double noteEtudiant;
    private List<Hackathon> preferences;

    public Etudiant(String nomEtudiant, int ageEtudiant, double noteEtudiant) {
        this.nomEtudiant = nomEtudiant;
        this.ageEtudiant = ageEtudiant;
        this.noteEtudiant = noteEtudiant;
        this.preferences = new ArrayList<>();
    }

    public String obtenirNom() {
        return nomEtudiant;
    }
    public int obtenirAge() {
        return ageEtudiant;
    }
    public double obtenirNote() {
        return noteEtudiant;
    }
    public void ajouterPreferenceHackathon(Hackathon hackathon) {
        preferences.add(hackathon);
    }
    public afficherPreferences() {
        System.out.println("Préférences de " + nomEtudiant + ":");
        for (Hackathon hackathon : preferences) {
            System.out.println("- " + hackathon.obtenirNomHackathon());
        }
    }
    

    