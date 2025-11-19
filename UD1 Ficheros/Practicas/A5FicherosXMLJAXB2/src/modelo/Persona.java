package modelo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import utilidades.LocalDateAdapter;

import java.time.LocalDate;

// ABSTRACTA POR QUE NO VOY A CREAR OBJETOS PERSONA
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({Trabajador.class, Estudiante.class})
public abstract class Persona {

    @XmlElement(name = "Nombre")
    private String nombre;

    @XmlElement(name = "FechaNacimiento")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaNacimiento;



}
