package cal.info;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.List;

public class GestionHackathon {


    public boolean ajouterHackathon(Hackathon hackathon) throws SQLException {
        String insertSQL = "INSERT INTO Hackathon1 (nom, lieu) VALUES (?, ?)";
        try (Connection conn = BaseDeDonneesUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, hackathon.getNom());
            pstmt.setString(2, hackathon.getLieu());
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Hackathon> retrouverHackathons() throws SQLException {
        List<Hackathon> hackathons = new ArrayList<>();
        String selectSQL = "SELECT * FROM Hackathon1";

        try (Connection conn = BaseDeDonneesUtil.getConnection();
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
        try (Connection conn = BaseDeDonneesUtil.getConnection();
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
        try (Connection conn = BaseDeDonneesUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, hackathon.getNom());
            pstmt.setString(2, hackathon.getLieu());
            pstmt.setInt(3, hackathon.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean supprimerHackathon(int id) throws SQLException {
        String deleteSQL = "DELETE FROM Hackathon1 WHERE id = ?";
        try (Connection conn = BaseDeDonneesUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}
