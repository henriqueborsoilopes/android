package com.example.projetodecadastro2pt.model;

public class Nota {
    private String ra;
    private String disciplina;
    private int bimestre;
    private double nota;


    public Nota(String ra, String disciplina, int bimestre, double nota) {
        this.ra = ra;
        this.disciplina = disciplina;
        this.bimestre = bimestre;
        this.nota = nota;
    }

    public String getRa() { return ra; }
    public String getDisciplina() { return disciplina; }
    public int getBimestre() { return bimestre; }
    public double getNota() { return nota; }
}
