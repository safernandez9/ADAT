package modelo;

public class Velocista extends Corredor{

    private static final long serialVersionUID = 1L; //pOR QUE SE METE AQUI TB SI SE SUPONE QUE HEREDA?
    float velocidadMedia;

    public Velocista(){

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  |  VELOCIDAD MEDIA:  ");
        sb.append(String.format("%.2f km/h",this.velocidadMedia));
        return sb.toString();
    }
}
