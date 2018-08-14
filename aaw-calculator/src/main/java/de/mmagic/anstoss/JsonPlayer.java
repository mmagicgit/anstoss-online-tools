package de.mmagic.anstoss;

class JsonPlayer {

    private String name;
    private String[] schnelligkeit;
    private String[] zweikampf;
    private String[] kopfball;
    private String[] schusskraft;
    private String[] schussgenauigkeit;
    private String[] technik;
    private String[] spielintelligenz;

    String[] getSchnelligkeit() {
        return schnelligkeit;
    }

    void setSchnelligkeit(String[] schnelligkeit) {
        this.schnelligkeit = schnelligkeit;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String[] getZweikampf() {
        return zweikampf;
    }

    void setZweikampf(String[] zweikampf) {
        this.zweikampf = zweikampf;
    }

    String[] getKopfball() {
        return kopfball;
    }

    void setKopfball(String[] kopfball) {
        this.kopfball = kopfball;
    }

    String[] getSchusskraft() {
        return schusskraft;
    }

    void setSchusskraft(String[] schusskraft) {
        this.schusskraft = schusskraft;
    }

    String[] getSchussgenauigkeit() {
        return schussgenauigkeit;
    }

    void setSchussgenauigkeit(String[] schussgenauigkeit) {
        this.schussgenauigkeit = schussgenauigkeit;
    }

    String[] getTechnik() {
        return technik;
    }

    void setTechnik(String[] technik) {
        this.technik = technik;
    }

    String[] getSpielintelligenz() {
        return spielintelligenz;
    }

    void setSpielintelligenz(String[] spielintelligenz) {
        this.spielintelligenz = spielintelligenz;
    }

}
