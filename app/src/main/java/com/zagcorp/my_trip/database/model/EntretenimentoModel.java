package com.zagcorp.my_trip.database.model;

public class EntretenimentoModel {
    public static final String TABELA_NOME = "entretenimento";

    public static final String
            COLUNA_ID = "_id",
            COLUNA_VIAGEM = "id_viagem",
            COLUNA_ATIVIDADE = "atividade",
            COLUNA_VALOR = "valor";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABELA_NOME + " (" +
                    COLUNA_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_VIAGEM + " INTEGER NOT NULL, " +
                    COLUNA_ATIVIDADE  + " TEXT NOT NULL, " +
                    COLUNA_VALOR   + " REAL NOT NULL," +
                    "FOREIGN KEY(" + COLUNA_VIAGEM + ") REFERENCES viagem(_id));";

    public static final String DROP_TABLE = "drop table if exists " + TABELA_NOME + ";";

    private long id;
    private long viagem;
    private String atividade;
    private double valor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getViagem() {
        return viagem;
    }

    public void setViagem(long viagem) {
        this.viagem = viagem;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
