package cal.info;
import java.io.Serializable;        

public class Etudiant implements Serializable {
    private String nom;
    private int age;
    private double note;

    public Etudiant() {
        this.nom = "";
        this.age = 0;
        this.note = 0.0;
    }

    public Etudiant(String nom, int age, double note) {
        this.nom = nom;
        this.age = age;
        this.note = note;
    }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getNote() { return note; }
    public void setNote(double note) { this.note = note; }

    @Override
    public String toString() {
        return nom + " (" + age + " ans) - Note: " + note;
    }
}
