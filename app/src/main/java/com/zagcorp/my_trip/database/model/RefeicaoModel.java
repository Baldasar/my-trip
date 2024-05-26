package com.zagcorp.my_trip.database.model;

public class RefeicaoModel {
    public static final String TABELA_NOME = "refeicao";

    public static final String
            COLUNA_ID = "_id",
            COLUNA_VIAGEM = "id_viagem",
            COLUNA_CUSTO_ESTIMADO = "custo_estimado",
            COLUNA_QTD_REFEICAO = "qtd_refeicao",
            COLUNA_QNTD_VIAJANTES = "qtd_viajantes";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABELA_NOME + " (" +
                    COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_VIAGEM + " INTEGER NOT NULL, " +
                    COLUNA_CUSTO_ESTIMADO + " REAL NOT NULL, " +
                    COLUNA_QTD_REFEICAO + " INTEGER NOT NULL, " +
                    COLUNA_QNTD_VIAJANTES + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUNA_VIAGEM + ") REFERENCES viagem(_id));";

    public static final String DROP_TABLE = "drop table if exists " + TABELA_NOME + ";";

    private long id;
    private long viagem;
    private double custo_estimado;
    private long qtd_refeicao;
    private long qtd_viajantes;

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

    public double getCusto_estimado() {
        return custo_estimado;
    }

    public void setCusto_estimado(double custo_estimado) {
        this.custo_estimado = custo_estimado;
    }

    public long getQtd_refeicao() {
        return qtd_refeicao;
    }

    public void setQtd_refeicao(long qtd_refeicao) {
        this.qtd_refeicao = qtd_refeicao;
    }

    public long getQtd_viajantes() {
        return qtd_viajantes;
    }

    public void setQtd_viajantes(long qtd_viajantes) {
        this.qtd_viajantes = qtd_viajantes;
    }
}
