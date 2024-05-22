package com.zagcorp.my_trip.database.model;

public class TarifaModel {
    public static final String TABELA_NOME = "tarifa";

    public static final String
            COLUNA_ID = "_id",
            COLUNA_VIAGEM = "id_viagem",
            COLUNA_CUSTO_PESSOA = "custo_pessoa",
            COLUNA_QTD_PESSOA = "qtd_pessoa",
            COLUNA_CUSTO_VEICULO = "custo_veiculo";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABELA_NOME + " (" +
                    COLUNA_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_VIAGEM + " INTEGER NOT NULL, " +
                    COLUNA_CUSTO_PESSOA  + " REAL NOT NULL, " +
                    COLUNA_QTD_PESSOA   + " INTEGER NOT NULL," +
                    COLUNA_CUSTO_VEICULO + " REAL NOT NULL, " +
                    "FOREIGN KEY(" + COLUNA_VIAGEM + ") REFERENCES viagem(_id));";

    public static final String DROP_TABLE = "drop table if exists " + TABELA_NOME + ";";

    private long id;
    private long viagem;
    private double custo_pessoa;
    private long qtd_pessoa;

    private double custo_veiculo;

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

    public double getCusto_pessoa() {
        return custo_pessoa;
    }

    public void setCusto_pessoa(double custo_pessoa) {
        this.custo_pessoa = custo_pessoa;
    }

    public long getQtd_pessoa() {
        return qtd_pessoa;
    }

    public void setQtd_pessoa(long qtd_pessoa) {
        this.qtd_pessoa = qtd_pessoa;
    }

    public double getCusto_veiculo() {
        return custo_veiculo;
    }

    public void setCusto_veiculo(double custo_veiculo) {
        this.custo_veiculo = custo_veiculo;
    }
}
