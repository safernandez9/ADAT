package modelo;

import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
@XmlSeeAlso({Telefonos.class, Email.class})
public abstract class Contacto {
    // Abstracta por que no comparte nada, solo es herencia para hacer
    // Email O Telefono, al igual que se hace con Estudiante O Trabajador
    // Por algun motivo no hace falta contructor vacio
    // Por algun motivo no gece falta transient, no escribe contacto
}
