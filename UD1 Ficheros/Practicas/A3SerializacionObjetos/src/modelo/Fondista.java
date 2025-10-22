package modelo;

public class Fondista extends Corredor{

    private static final long serialVersionUID = 1L; //pOR QUE SE METE AQUI TB SI SE SUPONE QUE HEREDA? No se pero funciona internamente y debe llamarse asi.
    //Tiene que ver con las cabeceras
    float distanciaMax;

    public Fondista() {

    }

    public float getDistanciaMax() {
        return distanciaMax;
    }

    public void setDistanciaMax(float distanciaMax) {
        this.distanciaMax = distanciaMax;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  |  DISTANCIA MAX:  ");
        sb.append(String.format("%.3f km", this.distanciaMax));
        return sb.toString();
    }

}
