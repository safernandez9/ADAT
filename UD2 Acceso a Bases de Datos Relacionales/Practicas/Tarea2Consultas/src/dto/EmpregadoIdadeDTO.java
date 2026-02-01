package dto;

public class EmpregadoIdadeDTO {
    private String nss;
    private String nome;
    private int idade;

    public EmpregadoIdadeDTO(String nss, String nome, int idade) {
        this.nss = nss;
        this.nome = nome;
        this.idade = idade;
    }

    @Override
    public String toString() {
        return nss + " - " + nome + " | Idade: " + idade;
    }
}
