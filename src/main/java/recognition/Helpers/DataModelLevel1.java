package recognition.Helpers;

// database source
// https://dados.mma.gov.br/dataset/especies-ameacadas/resource/1f13b062-f3f6-4198-a4c5-3581548bebec

public class DataModelLevel1 {
    private String process_num;
    private String nome_projeto;
    private String valor;
    private String inst_executor;
    private String uf;

    public DataModelLevel1(String process_num, String nome_projeto, String valor, String inst_executor, String uf) {
        this.process_num = process_num;
        this.nome_projeto = nome_projeto;
        this.valor = valor;
        this.inst_executor = inst_executor;
        this.uf = uf;
    }

    public String getProcess_num() {
        return process_num;
    }

    public void setProcess_num(String process_num) {
        this.process_num = process_num;
    }

    public String getNome_projeto() {
        return nome_projeto;
    }

    public void setNome_projeto(String nome_projeto) {
        this.nome_projeto = nome_projeto;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getInst_executor() {
        return inst_executor;
    }

    public void setInst_executor(String inst_executor) {
        this.inst_executor = inst_executor;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
