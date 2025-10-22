package clases;

public class Puntuacion {

    private int anio;
    private float puntos;

    public Puntuacion(int anio, float puntos) {
        this.anio = anio;
        this.puntos = puntos;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public float getPuntos() {
        return puntos;
    }

    public void setPuntos(float puntos) {
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.anio);
        sb.append("=");
        sb.append(this.puntos);
        return sb.toString();
    }
}
