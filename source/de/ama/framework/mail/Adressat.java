package de.ama.framework.mail;

import de.ama.db.PersistentMarker;

public class Adressat implements PersistentMarker {
    public static final int EMAIL = 0;
    public static final int FAX   = 1;
    public static final int SMS   = 2;

    private String adresse;
    private String name;
    private int    typ;


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Adressat)) return false;

        final Adressat adressat = (Adressat) o;

        if (typ != adressat.typ) return false;
        if (adresse != null ? !adresse.equals(adressat.adresse) : adressat.adresse != null) return false;
        if (name != null ? !name.equals(adressat.name) : adressat.name != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (adresse != null ? adresse.hashCode() : 0);
        result = 29 * result + (name != null ? name.hashCode() : 0);
        result = 29 * result + typ;
        return result;
    }

    public Adressat(){
        setAdresse("");
        setName   ("");
        setTyp    (EMAIL);
    }

    public Adressat(String adresse, String name, int typ) {
        setAdresse(adresse);
        setName   (name);
        setTyp    (typ);
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTyp() {
        return typ;
    }

    public void setTyp(int typ) {
        this.typ = typ;
    }

}
