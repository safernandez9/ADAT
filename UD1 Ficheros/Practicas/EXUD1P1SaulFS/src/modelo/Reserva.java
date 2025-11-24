package modelo;

import java.time.LocalDate;

public class Reserva {
private String codigo;
private Usuario usuario;
private LocalDate fechaInicio;
private LocalDate fechaFin;

public Reserva(String codigo, Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin) {
    this.codigo = codigo;
    this.usuario = usuario;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
}

public Reserva(String codigo, Usuario usuario, LocalDate fechaInicio) {
    this.codigo = codigo;
    this.usuario = usuario;
    this.fechaInicio = fechaInicio;
    fechaFin = null;
}

    public String getcodigo() {
        return codigo;
    }

    public void setcodigo(String codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
