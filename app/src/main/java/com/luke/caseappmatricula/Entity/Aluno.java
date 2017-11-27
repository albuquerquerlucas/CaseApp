package com.luke.caseappmatricula.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lucas on 16/11/2017.
 */

public class Aluno implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String endereço;
    private String numero;
    private String telFixo;
    private String celular;
    private String urlFoto;
    private List<String> cursos;

    public Aluno() {
    }

    public Aluno(String id, String nome, String email, String senha, String endereço, String numero, String telFixo, String celular, String urlFoto, List<String> cursos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.endereço = endereço;
        this.senha = senha;
        this.numero = numero;
        this.telFixo = telFixo;
        this.celular = celular;
        this.urlFoto = urlFoto;
        this.cursos = cursos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEndereço() {
        return endereço;
    }

    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTelFixo() {
        return telFixo;
    }

    public void setTelFixo(String telFixo) {
        this.telFixo = telFixo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public List<String> getCursos() {
        return cursos;
    }

    public void setCursos(List<String> cursos) {
        this.cursos = cursos;
    }
}
