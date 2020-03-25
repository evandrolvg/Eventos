package com.example.eventos;

import java.util.Date;

public class Evento {
    private String id;
    private String nome;
    private String descricao;
    private String data;
    private Double valor;
    private int qtdeVagas;
    private String localRealizacao;

    public Evento() {
    }

    public Evento(String id, String nome, String descricao, String data, Double valor, int qtdeVagas, String localRealizacao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.valor = valor;
        this.qtdeVagas = qtdeVagas;
        this.localRealizacao = localRealizacao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getQtdeVagas() {
        return qtdeVagas;
    }

    public void setQtdeVagas(int qtdeVagas) {
        this.qtdeVagas = qtdeVagas;
    }

    public String getLocalRealizacao() {
        return localRealizacao;
    }

    public void setLocalRealizacao(String localRealizacao) {
        this.localRealizacao = localRealizacao;
    }
}

