package backend;

import java.util.List;

public class Pregunta {
    private int id;
    private String pregunta;
    private String tipo;
    private String nivel_bloom;
    private List<String> alternativas; 
    private String respuesta_correcta;
    private int tiempo_estimado;

    public int getId() { return id; }
    public String getPregunta() { return pregunta; }
    public String getTipo() { return tipo; }
    public String getNivel_bloom() { return nivel_bloom; }
    public List<String> getAlternativas() { return alternativas; }
    public String getRespuesta_correcta() { return respuesta_correcta; }
    public int getTiempo_estimado() { return tiempo_estimado; }
}
