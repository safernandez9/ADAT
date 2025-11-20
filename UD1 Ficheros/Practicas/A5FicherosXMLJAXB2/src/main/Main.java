package main;

import modelo.*;
import persistencia.XMLJAXBUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {


        // GUARDO TODO COMO STRInnG? SI NO ES ASI CUANDO LO COnVIERTO???

//        Registro registro = new Registro();
//        registro.setVersion("1.0");
//        registro.setFechaCreacion(LocalDate.of(2025, 11, 17));
//
//        List<String> categorias = new ArrayList<>(List.of("Estudios", "Trabajo", "Deporte"));
//        registro.setCategorias(categorias);
//
//        // Persona 1: Trabajador con teléfonos
//        Trabajador t1 = new Trabajador();
//        t1.setNombre("Carlos Alba");
//        t1.setFechaNacimiento(LocalDate.of(1999, 3, 12));
//
//        List<Telefono> telefonos = new ArrayList<>(List.of(
//                new Telefono("movil", "111111111"),
//                new Telefono("fijo", "222222222")));
//
//        Telefonos t = new Telefonos();
//        t.setTelefonos(telefonos);
//        t1.setContacto(t);
//
//        t1.setEmpresa(new Empresa("LaColmena", "Director"));
//        t1.setSalario(8000.0);
//
//        // Persona 2: Estudiante con email
//        Estudiante es1 = new Estudiante();
//        es1.setNombre("Angel Rodríguez");
//        es1.setFechaNacimiento(LocalDate.of(2001, 7, 21));
//        es1.setContacto(new Email("angel.rodriguez@example.com"));
//        es1.setUniversidad("Santiago");
//        es1.setCarrera("Informática");
//
//        // Persona 3: Trabajador con email
//        Trabajador t2 = new Trabajador();
//        t2.setNombre("Manuel Sánchez");
//        t2.setFechaNacimiento(LocalDate.of(1979, 11, 4));
//        t2.setContacto(new Email("Manuel Sánchez@example.com"));
//        t2.setEmpresa(new Empresa("ElTornillo", "Gerente"));
//        t2.setSalario(2000.0);
//
//        registro.getListaPersonas().add(t1);
//        registro.getListaPersonas().add(es1);
//        registro.getListaPersonas().add(t2);


        Registro registro = XMLJAXBUtils.unmarshall(Registro.class, "ArchivoXML/Registro.xml");
        XMLJAXBUtils.marshall(registro, "ArchivoXML/Registro.xml");
    }
}
