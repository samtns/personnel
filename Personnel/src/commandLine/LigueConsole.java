package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.util.ArrayList;

import commandLineMenus.List;
import commandLineMenus.Menu;
import commandLineMenus.Option;

import personnel.*;

public class LigueConsole 
{
	private GestionPersonnel gestionPersonnel;
	private EmployeConsole employeConsole;

	public LigueConsole(GestionPersonnel gestionPersonnel, EmployeConsole employeConsole)
	{
		this.gestionPersonnel = gestionPersonnel;
		this.employeConsole = employeConsole;
	}

	Menu menuLigues()
	{
		Menu menu = new Menu("Gérer les ligues", "l");
		menu.add(afficherLigues());
		menu.add(ajouterLigue());
		menu.add(selectionnerLigue());
		menu.addBack("q");
		return menu;
	}

	private Option afficherLigues()
	{
		return new Option("Afficher les ligues", "l", () -> {System.out.println(gestionPersonnel.getLigues());});
	}

	private Option afficher(final Ligue ligue)
	{
		return new Option("Afficher la ligue", "l", 
				() -> 
				{
					System.out.println(ligue);
					System.out.println("administrée par " + ligue.getAdministrateur());
				}
		);
	}
	private Option afficherEmployes(final Ligue ligue)
	{
		return new Option("Afficher les employes", "l", () -> {System.out.println(ligue.getEmployes());});
	}

	private Option ajouterLigue()
	{
		return new Option("Ajouter une ligue", "a", () -> 
		{
			try
			{
				gestionPersonnel.addLigue(getString("nom : "));
			}
			catch(SauvegardeImpossible exception)
			{
				System.err.println("Impossible de sauvegarder cette ligue");
			}
		});
	}
	
	private Menu editerLigue(Ligue ligue)
	{
		Menu menu = new Menu("Editer " + ligue.getNom());
		menu.add(afficher(ligue));
		menu.add(gererEmployes(ligue));
		//menu.add(changerAdministrateur(ligue));
		menu.add(changerNom(ligue));
		menu.add(supprimer(ligue));
		menu.addBack("q");
		return menu;
	}

	private Option changerNom(final Ligue ligue)
	{
		return new Option("Renommer", "r", 
				() -> {ligue.setNom(getString("Nouveau nom : "));});
	}

	private List<Ligue> selectionnerLigue()
	{
		return new List<Ligue>("Sélectionner une ligue", "e", 
				() -> new ArrayList<>(gestionPersonnel.getLigues()),
				(element) -> editerLigue(element)
				);
	}
	
	private Option ajouterEmploye(final Ligue ligue)
	{
		return new Option("ajouter un employé", "a",)
				() -> 
				{
					String nom = getString("Nom : ");
					String Prenom = getString("Prenom : ");
					String mail = getString("Mail : ");
					String password = getString("Mot de passe : ");
					String dateArrive = getString("La date d'arrivé est : (AAAA-MM-JJ) : ");
					String dateDepart = getString("La date de départ est : (AAAA-MM-JJ) : ");
					try{
						LocalDate dateArrive = LocalDate.parse(dateArrive);
						LocalDate dateDepart = LocalDate.parse(dateDepart);

						if (dateDepart.isBefore(dateArrive)){
							throw new Erreurdate();
						}

						ligue.addEmploye(nom, prenom, mail, password, dateArrive, dateDepart);
						System.out.println("Employé ajouté");

					} catch (DateTimeParseException e) {
						
						System.out.println("Erreur : Format de date invalide. Utilisez le format AAAA-MM-JJ");

					} catch (Erreurdate e) {
						System.out.println(e.getMessage());
					}

				}
		 
	}

	private Menu GererEmployeBis(Ligue ligue){
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "m");
		menu.add(modifierEmploye(ligue));
		menu.add(supprimerEmploye(ligue));
		menu.add(changerAdministrateur(ligue));
		menu.addBack("q");
		return menu;

	}

		private List<Employe> GererEmployeBis(final Ligue ligue)
	{
		return new List<Employe>("Gérer un employé", "x",
				() -> new ArrayList<>(ligue.getEmployes()),
				(element) -> GererEmployeBis(element)
				);
	}

	
	
	private Menu gererEmployes(Ligue ligue)
	{
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
		menu.add(afficherEmployes(ligue));
		menu.add(ajouterEmploye(ligue));
		menu.addBack("q");
		return menu;
	}

	private List<Employe> changerAdministrateur(final Ligue ligue)
	{
		return null;
	}		
	
	private Option supprimer(Ligue ligue)
	{
		return new Option("Supprimer", "d", () -> {ligue.remove();});
	}
	
}