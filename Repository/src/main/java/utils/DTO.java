package utils;

public class DTO {
    private String litere;
    private String cuvant;
    private Integer idJoc;
    private String idJucator;

    public DTO(String litere, String cuvant, Integer idJoc, String idJucator) {
        this.litere = litere;
        this.cuvant = cuvant;
        this.idJoc = idJoc;
        this.idJucator = idJucator;
    }

    public String getLitere() {
        return litere;
    }

    public void setLitere(String litere) {
        this.litere = litere;
    }

    public String getCuvant() {
        return cuvant;
    }

    public void setCuvant(String cuvant) {
        this.cuvant = cuvant;
    }

    public Integer getIdJoc() {
        return idJoc;
    }

    public void setIdJoc(Integer idJoc) {
        this.idJoc = idJoc;
    }

    public String getIdJucator() {
        return idJucator;
    }

    public void setIdJucator(String idJucator) {
        this.idJucator = idJucator;
    }
}
