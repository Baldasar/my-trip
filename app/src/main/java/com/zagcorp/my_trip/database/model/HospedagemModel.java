package com.zagcorp.my_trip.database.model;

public class HospedagemModel {
    public static final String TABELA_NOME = "hospedagem";

    public static final String
            COLUNA_ID = "_id",
            COLUNA_VIAGEM = "id_viagem",
            COLUNA_CUSTO_NOITE = "custo_noite",
            COLUNA_QTD_NOITE = "qtd_noite",
            COLUNA_QTD_QUARTO = "qtd_quarto";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABELA_NOME + " (" +
                    COLUNA_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_VIAGEM + " INTEGER NOT NULL, " +
                    COLUNA_CUSTO_NOITE  + " REAL NOT NULL, " +
                    COLUNA_QTD_NOITE   + " INTEGER NOT NULL," +
                    COLUNA_QTD_QUARTO   + " INTEGER NOT NULL," +
                    "FOREIGN KEY(" + COLUNA_VIAGEM + ") REFERENCES viagem(_id));";

    public static final String DROP_TABLE = "drop table if exists " + TABELA_NOME + ";";

    private long id;
    private long viagem;
    private double custo_noite;
    private long qtd_noite;
    private long qtd_quarto;

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

    public double getCusto_noite() {
        return custo_noite;
    }

    public void setCusto_noite(double custo_noite) {
        this.custo_noite = custo_noite;
    }

    public long getQtd_noite() {
        return qtd_noite;
    }

    public void setQtd_noite(long qtd_noite) {
        this.qtd_noite = qtd_noite;
    }

    public long getQtd_quarto() {
        return qtd_quarto;
    }

    public void setQtd_quarto(long qtd_quarto) {
        this.qtd_quarto = qtd_quarto;
    }
}
