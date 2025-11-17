package cal.info;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;

import java.util.HashSet;

public class GestionHackathonDao {
    private final DataSource ds;
    public GestionHackathonDao(DataSource ds) {
        this.ds = ds;
    }

    Map<String, Set<Etudiant>> etudiantsParProg = new HashMap<>();

    public boolean ajouterHackathon(Hackathon hackathon) throws SQLException {
        String insertSQL = "INSERT INTO Hackathon1 (nom, lieu) VALUES (?, ?)";
        try (Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, hackathon.getNom());
            pstmt.setString(2, hackathon.getLieu());
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Hackathon> retrouverHackathons() throws SQLException {
        List<Hackathon> hackathons = new ArrayList<>();
        String selectSQL = "SELECT * FROM Hackathon1";

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                Hackathon h = new Hackathon();
                h.setId(rs.getInt("id"));
                h.setNom(rs.getString("nom"));
                h.setLieu(rs.getString("lieu"));
                hackathons.add(h);
            }
        }
        return hackathons;
    }

    public List<Hackathon> rechercherParNom(String nom) throws SQLException {
        List<Hackathon> resultats = new ArrayList<>();
        String selectSQL = "SELECT * FROM Hackathon1 WHERE LOWER(nom) = LOWER(?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, nom);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Hackathon h = new Hackathon();
                    h.setId(rs.getInt("id"));
                    h.setNom(rs.getString("nom"));
                    h.setLieu(rs.getString("lieu"));
                    resultats.add(h);
                }
            }
        }
        return resultats;
    }

    public boolean modifierHackathon(Hackathon hackathon) throws SQLException {
        String updateSQL = "UPDATE Hackathon1 SET nom=?, lieu=? WHERE id=?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, hackathon.getNom());
            pstmt.setString(2, hackathon.getLieu());
            pstmt.setInt(3, hackathon.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean supprimerHackathon(int id) throws SQLException {
        String deleteSQL = "DELETE FROM Hackathon1 WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    
public boolean ajouterEtudiant(HashSet<Etudiant> etudiants) throws SQLException {
    String insertSQL = "INSERT INTO Etudiant1 (nom, age, programme, matricule) VALUES (?, ?, ?, ?)";
    try (Connection conn = ds.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
        for (Etudiant etudiant : etudiants) {
            pstmt.setString(1, etudiant.getNom());
            pstmt.setInt(2, etudiant.getAge());
            pstmt.setString(3, etudiant.getProgramme());
            pstmt.setObject(4, etudiant.getMatricule());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        return true;
    }
}

public List<Etudiant> retrouverTousEtudiants() throws SQLException {
    List<Etudiant> etudiants = new ArrayList<>();
    String selectSQL = "SELECT * FROM Etudiant1";

    try (Connection conn = ds.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(selectSQL)) {

        while (rs.next()) {
            Etudiant etudiant = new Etudiant();
            etudiant.setNom(rs.getString("nom"));
            etudiant.setAge(rs.getInt("age"));
            etudiant.setProgramme(rs.getString("programme"));
            
            int matricule = rs.getInt("matricule");
            if (!rs.wasNull()) {
                etudiant.setMatricule(matricule);
            }

            etudiants.add(etudiant);
        }
    }

    return etudiants;
}

public List<Hackathon> retrouverTousHackathons() throws SQLException {
    List<Hackathon> hackathons = new ArrayList<>();
    String selectSQL = "SELECT * FROM Hackathon1";

    try (Connection conn = ds.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(selectSQL)) {

        while (rs.next()) {
            Hackathon hackathon = new Hackathon();
            hackathon.setId(rs.getInt("id"));
            hackathon.setNom(rs.getString("nom"));
            hackathon.setLieu(rs.getString("lieu"));
            hackathons.add(hackathon);
        }
    }

    return hackathons;
}

}