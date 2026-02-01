package dto;

public class DepartamentoDTO {
    private int numDepartamento;
    private String nomeDepartamento;

    public DepartamentoDTO(int numDepartamento, String nomeDepartamento) {
        this.numDepartamento = numDepartamento;
        this.nomeDepartamento = nomeDepartamento;
    }

    @Override
    public String toString() {
        return numDepartamento + " - " + nomeDepartamento;
    }
}
