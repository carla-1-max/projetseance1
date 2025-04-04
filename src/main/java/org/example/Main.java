package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        CompagnieService service = new CompagnieService();
    }
}

abstract class Personne {
    protected String id;
    protected String nom;
    protected String prenom;

    public Personne(String id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String obtenirInfos() {
        return id + " " + nom + " " + prenom;
    }

    public abstract String obtenirRole();

    public String getId() {
        return id;
    }
}

abstract class Employe extends Personne {
    public Employe(String id, String nom, String prenom) {
        super(id, nom, prenom);
    }
}

class Pilote extends Employe {
    public Pilote(String id, String nom, String prenom) {
        super(id, nom, prenom);
    }

    public String obtenirRole() {
        return "Pilote";
    }
}

class PersonnelCabine extends Employe {
    public PersonnelCabine(String id, String nom, String prenom) {
        super(id, nom, prenom);
    }

    public String obtenirRole() {
        return "Personnel Cabine";
    }
}

class Passager extends Personne {
    private List<Reservation> reservations = new ArrayList<>();

    public Passager(String id, String nom, String prenom) {
        super(id, nom, prenom);
    }

    public String obtenirRole() {
        return "Passager";
    }

    public void reserverVol(Vol vol, String numeroReservation) {
        Reservation reservation = new Reservation(numeroReservation, this, vol);
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
    private Pilote pilote;
    private List<PersonnelCabine> equipeCabine = new ArrayList<>();
    private Avion avion;
    private List<Reservation> reservations = new ArrayList<>();

    public Vol(String numero) {
        this.numero = numero;
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
}

class Avion {
    private String id;
    private Set<String> horairesOccupes = new HashSet<>();

    public Avion(String id) {
        this.id = id;
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

    public Reservation(String numero, Passager passager, Vol vol) {
        this.numero = numero;
        this.passager = passager;
        this.vol = vol;
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

class CompagnieService {
    private List<Vol> vols = new ArrayList<>();
    private List<Avion> avions = new ArrayList<>();
    private List<Personne> personnes = new ArrayList<>();

    public void planifierVol(String numero) {
        vols.add(new Vol(numero));
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
}
