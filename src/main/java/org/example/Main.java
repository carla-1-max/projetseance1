package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        CompagnieService service = new CompagnieService();

        // Exemples aéroports
        Aeroport aeroport1 = new Aeroport("CDG", "Paris Charles de Gaulle", "Paris Nord");
        Aeroport aeroport2 = new Aeroport("Orly", "Aéroport Orly", "Paris Sud");

        service.ajouterAeroport(aeroport1);
        service.ajouterAeroport(aeroport2);

        Vol vol1 = new Vol("AF123", aeroport1, aeroport2, "2024-03-15 10:00", "2024-03-15 12:00", "Enregistrement");
        service.planifierVol(vol1);

        Avion avion1 = new Avion("A320", "Airbus A320", 150, "F-ABCD");
        service.ajouterAvion(avion1);

        Pilote pilote1 = new Pilote("P001", "Jean", "Dupont", "1 Rue du Pilote", "jean.dupont@example.com", "E001", "2020-01-01", "PL12345", 1000);
        PersonnelCabine cabine1 = new PersonnelCabine("C001", "Marie", "Durand", "2 Rue de l'Hôtesse", "marie.durand@example.com", "E002", "2021-02-02", "Hôtesse principale");
        service.ajouterPersonne(pilote1);
        service.ajouterPersonne(cabine1);

        vol1.affecterVol(pilote1, Arrays.asList(cabine1));
        vol1.affecterAvion(avion1);

        Passager passager1 = new Passager("PA001", "Luc", "Martin", "12 Rue de la Paix", "luc.martin@example.com", "PA123456");
        service.ajouterPersonne(passager1);

        passager1.reserverVol(vol1, "R001", "2024-03-10", "Confirmée");

        service.afficherDestinationsPopulaires();
        service.genererRapportStatistiques(); // ➤ Appel de la méthode bonus
    }
}

abstract class Personne {
    protected String id;
    protected String nom;
    protected String prenom;
    protected String adresse;
    protected String contact;

    public Personne(String id, String nom, String prenom, String adresse, String contact) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.contact = contact;
    }

    public String obtenirInfos() {
        return id + " " + nom + " " + prenom + " " + adresse + " " + contact;
    }

    public abstract String obtenirRole();

    public String getId() {
        return id;
    }
}

abstract class Employe extends Personne {
    protected String numeroEmploye;
    protected String dateEmbauche;

    public Employe(String id, String nom, String prenom, String adresse, String contact, String numeroEmploye, String dateEmbauche) {
        super(id, nom, prenom, adresse, contact);
        this.numeroEmploye = numeroEmploye;
        this.dateEmbauche = dateEmbauche;
    }
}

class Pilote extends Employe {
    private String licence;
    private int heuresDeVol;

    public Pilote(String id, String nom, String prenom, String adresse, String contact, String numeroEmploye, String dateEmbauche, String licence, int heuresDeVol) {
        super(id, nom, prenom, adresse, contact, numeroEmploye, dateEmbauche);
        this.licence = licence;
        this.heuresDeVol = heuresDeVol;
    }

    public String obtenirRole() {
        return "Pilote";
    }
}

class PersonnelCabine extends Employe {
    private String qualification;

    public PersonnelCabine(String id, String nom, String prenom, String adresse, String contact, String numeroEmploye, String dateEmbauche, String qualification) {
        super(id, nom, prenom, adresse, contact, numeroEmploye, dateEmbauche);
        this.qualification = qualification;
    }

    public String obtenirRole() {
        return "Personnel Cabine";
    }
}

class Passager extends Personne {
    private List<Reservation> reservations = new ArrayList<>();
    private String passeport;

    public Passager(String id, String nom, String prenom, String adresse, String contact, String passeport) {
        super(id, nom, prenom, adresse, contact);
        this.passeport = passeport;
    }

    public String obtenirRole() {
        return "Passager";
    }

    public void reserverVol(Vol vol, String numeroReservation, String dateReservation, String statut) {
        Reservation reservation = new Reservation(numeroReservation, this, vol, dateReservation, statut);
        reservations.add(reservation);
        vol.getReservations().add(reservation);
    }

    public void annulerReservation(String numeroReservation) {
        reservations.removeIf(r -> r.getNumero().equals(numeroReservation));
    }

    public List<Reservation> obtenirReservations() {
        return reservations;
    }
}

class Vol {
    private String numero;
    private Aeroport origine;
    private Aeroport destination;
    private String dateHeureDepart;
    private String dateHeureArrivee;
    private String etat;
    private Pilote pilote;
    private List<PersonnelCabine> equipeCabine = new ArrayList<>();
    private Avion avion;
    private List<Reservation> reservations = new ArrayList<>();

    public Vol(String numero, Aeroport origine, Aeroport destination, String dateHeureDepart, String dateHeureArrivee, String etat) {
        this.numero = numero;
        this.origine = origine;
        this.destination = destination;
        this.dateHeureDepart = dateHeureDepart;
        this.dateHeureArrivee = dateHeureArrivee;
        this.etat = etat;
    }

    public void affecterVol(Pilote pilote, List<PersonnelCabine> equipe) {
        this.pilote = pilote;
        this.equipeCabine = equipe;
    }

    public void affecterAvion(Avion avion) {
        this.avion = avion;
    }

    public String getNumero() {
        return numero;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void annulerVol() {
        reservations.clear();
    }

    public String obtenirVol() {
        return numero;
    }

    public Aeroport getDestination() {
        return destination;
    }
}

class Avion {
    private String id;
    private String modele;
    private int capacite;
    private String immatriculation;
    private Set<String> horairesOccupes = new HashSet<>();

    public Avion(String id, String modele, int capacite, String immatriculation) {
        this.id = id;
        this.modele = modele;
        this.capacite = capacite;
        this.immatriculation = immatriculation;
    }

    public boolean verifierDisponibilite(String horaire) {
        return !horairesOccupes.contains(horaire);
    }

    public void affecterVol(String horaire) {
        horairesOccupes.add(horaire);
    }
}

class Reservation {
    private String numero;
    private Passager passager;
    private Vol vol;
    private String dateReservation;
    private String statut;

    public Reservation(String numero, Passager passager, Vol vol, String dateReservation, String statut) {
        this.numero = numero;
        this.passager = passager;
        this.vol = vol;
        this.dateReservation = dateReservation;
        this.statut = statut;
    }

    public String getNumero() {
        return numero;
    }

    public Passager getPassager() {
        return passager;
    }

    public Vol getVol() {
        return vol;
    }
}

class Aeroport {
    private String nom;
    private String ville;
    private String description;

    public Aeroport(String nom, String ville, String description) {
        this.nom = nom;
        this.ville = ville;
        this.description = description;
    }

    public String getVille() {
        return ville;
    }
}

class CompagnieService {
    private List<Vol> vols = new ArrayList<>();
    private List<Avion> avions = new ArrayList<>();
    private List<Personne> personnes = new ArrayList<>();
    private List<Aeroport> aeroports = new ArrayList<>();

    public void planifierVol(Vol vol) {
        vols.add(vol);
    }

    public void annulerVol(String numero) {
        vols.removeIf(v -> v.getNumero().equals(numero));
    }

    public Vol obtenirVol(String numero) {
        return vols.stream().filter(v -> v.getNumero().equals(numero)).findFirst().orElse(null);
    }

    public void ajouterAvion(Avion avion) {
        avions.add(avion);
    }

    public void ajouterPersonne(Personne p) {
        personnes.add(p);
    }

    public Personne chercherPersonne(String id) {
        return personnes.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public void supprimerPersonne(String id) {
        personnes.removeIf(p -> p.getId().equals(id));
    }

    public void ajouterAeroport(Aeroport aeroport) {
        aeroports.add(aeroport);
    }

    public void afficherDestinationsPopulaires() {
        Map<String, Integer> destinations = new HashMap<>();
        for (Vol vol : vols) {
            String ville = vol.getDestination().getVille();
            destinations.put(ville, destinations.getOrDefault(ville, 0) + 1);
        }

        List<Map.Entry<String, Integer>> listeDestinations = new ArrayList<>(destinations.entrySet());
        listeDestinations.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        System.out.println("Destinations populaires :");
        for (Map.Entry<String, Integer> destination : listeDestinations) {
            System.out.println(destination.getKey() + " : " + destination.getValue() + " vols");
        }
    }

    public void genererRapportStatistiques() {
        int totalVols = vols.size();
        int totalPassagers = 0;
        double prixParReservation = 150.0;
        double revenus = 0;

        for (Vol vol : vols) {
            int nbReservations = vol.getReservations().size();
            totalPassagers += nbReservations;
            revenus += nbReservations * prixParReservation;
        }

        System.out.println("\n--- Rapport Statistique ---");
        System.out.println("Nombre total de vols planifiés : " + totalVols);
        System.out.println("Nombre total de passagers transportés : " + totalPassagers);
        System.out.println("Revenus estimés générés : " + revenus + " €");
    }
}
