package br.unipar.alunoonline.model;

import java.util.ArrayList;
import java.util.List;

public class Aluno {

    private Integer id;
    private String nome;
    private String ra;

    private List<Nota> notas = new ArrayList<>();

    public Aluno() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public Double mediaDasNotas() {
        if (notas == null || notas.isEmpty()) {
            return 0.0;
        }

        return notas.stream()
                .mapToDouble(Nota::getNota)
                .average()
                .orElse(0.0);
    }
}
