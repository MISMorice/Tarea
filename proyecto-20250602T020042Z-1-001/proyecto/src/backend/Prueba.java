package backend;

import java.util.List;

public class Prueba {
    private String nombre_prueba;
    private int tiempo_total;
    private List<Pregunta> preguntas;

    public String getNombre_prueba() {
        return nombre_prueba;
    }

    public int getTiempo_total() {
        return tiempo_total;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }
}
