package dto;

public class DepartamentoDirectorDTO {
    private int numDepartamento;
    private String nomeDepartamento;
    private String nomeDirector;
    private String apelidosDirector;

    public DepartamentoDirectorDTO(int numDepartamento, String nomeDepartamento,
                                   String nomeDirector, String apelidosDirector) {
        this.numDepartamento = numDepartamento;
        this.nomeDepartamento = nomeDepartamento;
        this.nomeDirector = nomeDirector;
        this.apelidosDirector = apelidosDirector;
    }

    @Override
    public String toString() {
        return numDepartamento + " - " + nomeDepartamento +
                " | Director: " + nomeDirector + " " + apelidosDirector;
    }
}
