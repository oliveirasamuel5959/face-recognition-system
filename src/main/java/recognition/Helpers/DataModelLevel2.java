package recognition.Helpers;

public class DataModelLevel2 {
    private String fauna_flora;
    private String grupo;
    private String familia;
    private String especie;
    private String nome_comum;
    private String categoria_ameaca;
    private String bioma;

    public DataModelLevel2(String fauna_flora, String grupo, String familia, String especie, String nome_comum, String categoria_ameaca, String bioma) {
        this.fauna_flora = fauna_flora;
        this.grupo = grupo;
        this.familia = familia;
        this.especie = especie;
        this.nome_comum = nome_comum;
        this.categoria_ameaca = categoria_ameaca;
        this.bioma = bioma;
    }

    public String getFauna_flora() {
        return fauna_flora;
    }

    public void setFauna_flora(String fauna_flora) {
        this.fauna_flora = fauna_flora;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getNome_comum() {
        return nome_comum;
    }

    public void setNome_comum(String nome_comum) {
        this.nome_comum = nome_comum;
    }

    public String getCategoria_ameaca() {
        return categoria_ameaca;
    }

    public void setCategoria_ameaca(String categoria_ameaca) {
        this.categoria_ameaca = categoria_ameaca;
    }

    public String getBioma() {
        return bioma;
    }

    public void setBioma(String bioma) {
        this.bioma = bioma;
    }
}
