package com.zagcorp.my_trip.database.model;

public class ViagemModel {
    public static final String TABELA_NOME = "viagem";

    public static final String
            COLUNA_ID = "_id",
            COLUNA_USUARIO = "id_usuario",
            COLUNA_TITULO = "titulo",
            COLUNA_LOCAL = "local",
            COLUNA_DURACAO = "duracao";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABELA_NOME + " (" +
                    COLUNA_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_USUARIO + " INTEGER NOT NULL, " +
                    COLUNA_TITULO  + " TEXT NOT NULL, " +
                    COLUNA_LOCAL   + " TEXT NOT NULL, " +
                    COLUNA_DURACAO + " TEXT NOT NULL, " +
                    "FOREIGN KEY(" + COLUNA_USUARIO + ") REFERENCES usuario(_id));";

    public static final String DROP_TABLE = "drop table if exists " + TABELA_NOME + ";";

    private long id;
    private long usuario;
    private String titulo;
    private String local;
    private String duracao;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUsuario() {
        return usuario;
    }

    public void setUsuario(long usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }
}
