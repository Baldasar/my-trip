package com.zagcorp.my_trip.database.model;

public class GasolinaModel {
    public static final String TABELA_NOME = "gasolina";

    public static final String
            COLUNA_ID = "_id",
            COLUNA_VIAGEM = "id_viagem",
            COLUNA_KM = "km",
            COLUNA_KM_LITRO = "km_litro",
            COLUNA_CUSTO_MEDIO = "custo_medio",
            COLUNA_ALUGUEL_VEICULO = "aluguel_veiculo";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABELA_NOME + " (" +
                    COLUNA_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_VIAGEM + " INTEGER NOT NULL, " +
                    COLUNA_KM  + " REAL NOT NULL, " +
                    COLUNA_KM_LITRO   + " REAL NOT NULL," +
                    COLUNA_CUSTO_MEDIO + " REAL NOT NULL," +
                    COLUNA_ALUGUEL_VEICULO + " REAL NOT NULL," +
                    "FOREIGN KEY(" + COLUNA_VIAGEM + ") REFERENCES viagem(_id));";

    public static final String DROP_TABLE = "drop table if exists " + TABELA_NOME + ";";

    private long id;
    private long viagem;
    private double km;
    private double km_litro;
    private double custo_medio;
    private double aluguel_veiculo;

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

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public double getKm_litro() {
        return km_litro;
    }

    public void setKm_litro(double km_litro) {
        this.km_litro = km_litro;
    }

    public double getCusto_medio() {
        return custo_medio;
    }

    public void setCusto_medio(double custo_medio) {
        this.custo_medio = custo_medio;
    }

    public double getAluguel_veiculo() {
        return aluguel_veiculo;
    }

    public void setAluguel_veiculo(double aluguel_veiculo) {
        this.aluguel_veiculo = aluguel_veiculo;
    }
}
