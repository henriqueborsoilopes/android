package br.unipar.alunoonline.model;

import java.util.ArrayList;
import java.util.List;

public class Disciplina {

    private Integer id;
    private String nome;

    private List<Aluno> alunos = new ArrayList<>();

    public Disciplina() { }

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

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public Double getMedia() {
        if (alunos == null || alunos.isEmpty()) {
            return 0.0;
        }

        return alunos.stream()
                .flatMap(aluno -> aluno.getNotas().stream())
                .mapToDouble(Nota::getNota)
                .average()
                .orElse(0.0);
    }
}
