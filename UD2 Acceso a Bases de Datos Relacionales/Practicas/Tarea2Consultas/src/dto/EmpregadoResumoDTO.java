package dto;


public class EmpregadoResumoDTO {

    private String nss;
    private String nomeCompleto;
    private String info;

    public EmpregadoResumoDTO(String nss, String nomeCompleto, String info) {
        this.nss = nss;
        this.nomeCompleto = nomeCompleto;
        this.info = info;
    }

    @Override
    public String toString() {
        return nss + " - " + nomeCompleto + " - " + info;
    }
}
