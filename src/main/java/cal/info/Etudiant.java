package cal.info;
import java.io.Serializable;        

public class Etudiant implements Serializable {
    private String nom;
    private int age;
    private String programme;
    private Integer matricule;

    public Etudiant() {
        this.nom = "";
        this.age = 0;
        this.programme = "";
        this.matricule = 0;
        
    }

    public Etudiant(String nom, int age, String programme, Integer matricule) {
        this.nom = nom;
        this.age = age;
        this.programme = programme;
        this.matricule = matricule;
    }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getProgramme() {
        return programme;
    }
    public int getMatricule() {
        return matricule;
    }   
    public void setProgramme(String programme) {
        this.programme = programme;
    }
    public void setMatricule(Integer matricule) {
        this.matricule = matricule;
    }
    @Override
    public String toString() {
        return nom + " (" + age + " ans) - Programme: " + programme;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Etudiant etudiant = (Etudiant) obj;
        return matricule.equals(etudiant.matricule);
    }
    @Override
    public int hashCode() { 
        return 1 * matricule.hashCode();
    
    }
}
