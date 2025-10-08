package cal.info;

public class Hackathon {
    private int id;
    private String nom;
    private String lieu;

   
    public Hackathon() {
        this.nom = "";
        this.lieu = "";
    }

    public Hackathon(String nom, String lieu) {
        this.nom = nom;
        this.lieu = lieu;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
}
