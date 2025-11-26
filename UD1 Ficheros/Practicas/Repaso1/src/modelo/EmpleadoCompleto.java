package modelo;

import java.nio.charset.StandardCharsets;

public class EmpleadoCompleto {

    private int id;
    private String nombre;
    private int edad;
    private double salario;
    private String departamento;
    private String localizacion;
    boolean borrado = false;

    public EmpleadoCompleto(int id, String nombre, int edad, double salario, String departamento, String localizacion) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.salario = salario;
        this.departamento = departamento;
        this.localizacion = localizacion;
    }

    public EmpleadoCompleto() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public boolean isBorrado() {
        return borrado;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    /**
     * Calcula el tamaño en bytes que ocuparía la serialización del empleado
     * @return
     */
    public int bytesSerializacionEmpleado (){

        int camposEnterosBytes = Integer.BYTES * 2;     // Id y numpatrocinadores
        int campoDoubleBytes = Double.BYTES;
        int campoBoolean = 1;            // borrado
        int contenidoUtf = 0;

        // NOMBRE

        if(this.nombre != null && !this.nombre.isEmpty()){
            // StandardCharsets.UTF_8 garantiza que no se lanza UnsupportedEncodingException
            contenidoUtf += this.nombre.getBytes(StandardCharsets.UTF_8).length;
        }

        if(this.localizacion != null && !this.localizacion.isEmpty()){
            // StandardCharsets.UTF_8 garantiza que no se lanza UnsupportedEncodingException
            contenidoUtf += this.nombre.getBytes(StandardCharsets.UTF_8).length;
        }

        if(this.departamento != null && !this.departamento.isEmpty()){
            // StandardCharsets.UTF_8 garantiza que no se lanza UnsupportedEncodingException
            contenidoUtf += this.nombre.getBytes(StandardCharsets.UTF_8).length;
        }

        // Si se supera el limite de writeUTF (65535 bytes) -> error de validacion unchecked
        if(contenidoUtf > 0xFFFF){
            throw new IllegalArgumentException("Nombre demasiado largo para writeUTF: " + contenidoUtf + " bytes");
        }


        // Aproximamos writeUTF como 2 bytes de prefijo + longitud en bytes UTF-8
        int nombreTotal = 2 + contenidoUtf;


        return nombreTotal + camposEnterosBytes + campoDoubleBytes + campoBoolean;
    }



    // toString

    @Override
    public String toString() {
        return "EmpleadoCompleto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", salario=" + salario +
                ", departamento='" + departamento + '\'' +
                ", localizacion='" + localizacion + '\'' +
                '}';
    }
}
