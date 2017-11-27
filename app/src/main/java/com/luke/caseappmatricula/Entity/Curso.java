package com.luke.caseappmatricula.Entity;

import java.io.Serializable;

/**
 * Created by Lucas on 16/11/2017.
 */

public class Curso implements Serializable {

    private String id;
    private String curso;
    private String professor;
    private String descricao;

    public Curso() {
    }

    public Curso(String id, String curso, String professor, String descricao) {
        this.id = id;
        this.curso = curso;
        this.professor = professor;
        this.descricao = descricao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
