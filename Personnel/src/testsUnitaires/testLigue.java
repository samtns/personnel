package testsUnitaires;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import personnel.*;


class testDeLigue {
    GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
    
    @Test
    void createLigue() throws SauvegardeImpossible {

        // Test pour créer ligue

        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        assertEquals("Fléchettes", ligue.getNom());
    }
    
    // Modif nom de ligue
    
    void setLigue() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        ligue.setNom("Test");
    }
    
    @Test 
    void Supprimer() throws SauvegardeImpossible, Erreurdate {

        // Création d'une ligue et ajouts employés pour test la suppression 

        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe;

        employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        Employe employe1 = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)); 
        
        // Suppression Employé et vérif si il a bien été supprimer

        employe.remove();
        assertFalse(ligue.getEmployes().contains(employe));
        
        // Suppression de la ligue et vérification si il a bien été supprimer dans GestionPersonnel

        ligue.remove();
        assertFalse(gestionPersonnel.getLigues().contains(ligue));
    }
    
    @Test
    void changerEtSupprimerAdmin() throws SauvegardeImpossible, Erreurdate {

        // Création d'une ligue et d'un employé 

        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        
        Employe test;
        test = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        

        assertEquals(gestionPersonnel.getRoot(), ligue.getAdministrateur());
        
        // Modification de l'administrateur de la ligue et vérification

        ligue.setAdministrateur(test);
        assertEquals(test, ligue.getAdministrateur());
        
        // Suppression de l'administrateur et vérification qu'il a bien été supprimer

        test.remove();
        assertFalse(ligue.getEmployes().contains(test));
        
        assertFalse(ligue.getEmployes().contains(test));
        assertEquals(gestionPersonnel.getRoot(), ligue.getAdministrateur());
    }
    
    @Test
    void Employe() throws SauvegardeImpossible, Erreurdate {

        // Création d'une ligue et d'un employé pour tester les gets et sets d'Employe

        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe test = ligue.addEmploye("Bakht", "Momo", "mail", "azerty", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        
        // Vérification des getters

        assertEquals("Momo", test.getPrenom());
        assertEquals("Mail", test.getMail());
        assertEquals(ligue, test.getLigue());
        
        // Modification de l'email de l'employé et vérification

        test.setMail("Nouveau Mail");
        assertEquals("Nouveau Mail", test.getMail());
        
        // Modification du nom de l'employé et vérification

        test.setNom("Nouveau Nom");
        assertEquals("Nouveau Nom", test.getNom());
        
        // Modification du mot de passe de l'employé et vérification avec checkPassword !!

        test.setPassword("Nouveau mot de passe");
        assertTrue(test.checkPassword("Nouveau mot de passe"));
        
        // Modification du prénom de l'employé et vérification

        test.setPrenom("Nouveau Prénom");
        assertEquals("Nouveau Prénom", test.getPrenom());
    }
}


//Test validité des dates.

class testDesDates {

    @Test
    void testDateArriveNonNull() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Football");
        
        Exception exception = assertThrows(Erreurdate.class, () -> {
            ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", null, LocalDate.of(2023, 1, 1));
        });
        assertEquals("La date d'arrivée ne peut pas être null.", exception.getMessage());
    }

    @Test
    void testDepartNonNull() throws SauvegardeImpossible, Erreurdate {
        Ligue ligue = gestionPersonnel.addLigue("Football");
        
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2023, 1, 1), null);
        assertEquals(LocalDate.of(2023, 1, 1), employe.getDateArrive());
        assertNull(employe.getDateDepart());
    }

    @Test
    void testDepartAvantArrive() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Football");
        
        Exception exception = assertThrows(Erreurdate.class, () -> {
            ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2023, 1, 1), LocalDate.of(2022, 12, 31));
        });
        assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception.getMessage());
    }
}
        