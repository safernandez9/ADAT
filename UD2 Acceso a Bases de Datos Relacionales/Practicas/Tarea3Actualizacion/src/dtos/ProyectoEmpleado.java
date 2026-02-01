package dtos;

import modelo.Empregado;
import modelo.Proxecto;

import java.util.List;

public class ProyectoEmpleado {

    Proxecto proxecto;
    List<Empregado> empregadosProxecto;

    public ProyectoEmpleado(Proxecto p, List<Empregado> emps) {
        this.proxecto = p;
        this.empregadosProxecto = emps;
    }

    public Proxecto getP() {
        return proxecto;
    }

    public void setP(Proxecto p) {
        this.proxecto = p;
    }

    public List<Empregado> getEmpregadosProxecto() {
        return empregadosProxecto;
    }

    public void setEmpregadosProxecto(List <Empregado> e) {
        this.empregadosProxecto = e;
    }
}
