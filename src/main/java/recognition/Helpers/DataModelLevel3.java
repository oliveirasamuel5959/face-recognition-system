package recognition.Helpers;

public class DataModelLevel3 {
    private int ano;
    private String documento;
    private int numero;
    private String ementa;
    private String status;

    public DataModelLevel3(int ano, String documento, int numero, String ementa, String status) {
        this.ano = ano;
        this.documento = documento;
        this.numero = numero;
        this.ementa = ementa;
        this.status = status;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getEmenta() {
        return ementa;
    }

    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

