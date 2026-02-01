package dto;

public class DepartamentoEmpleadosDTO {
    private String nome;
    private int fixos;
    private int temporais;

    public DepartamentoEmpleadosDTO(String nome, int fixos, int temporais) {
        this.nome = nome;
        this.fixos = fixos;
        this.temporais = temporais;
    }

    @Override
    public String toString() {
        return nome + " | Fixos: " + fixos + " | Temporais: " + temporais;
    }
}
