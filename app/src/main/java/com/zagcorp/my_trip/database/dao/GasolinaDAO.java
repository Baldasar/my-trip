package com.zagcorp.my_trip.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zagcorp.my_trip.database.helper.DBOpenHelper;
import com.zagcorp.my_trip.database.model.UsuarioModel;
import com.zagcorp.my_trip.database.model.GasolinaModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GasolinaDAO extends AbstrataDAO{
    public final String colunas[] = {
            GasolinaModel.COLUNA_ID,
            GasolinaModel.COLUNA_VIAGEM,
            GasolinaModel.COLUNA_KM,
            GasolinaModel.COLUNA_KM_LITRO,
            GasolinaModel.COLUNA_CUSTO_MEDIO,
            GasolinaModel.COLUNA_ALUGUEL_VEICULO
    };

    public GasolinaDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    public long Insert(GasolinaModel model) throws SQLException {
        long rowAffect = 0;

        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(GasolinaModel.COLUNA_VIAGEM, model.getViagem());
            values.put(GasolinaModel.COLUNA_KM, model.getKm());
            values.put(GasolinaModel.COLUNA_KM_LITRO, model.getKm_litro());
            values.put(GasolinaModel.COLUNA_CUSTO_MEDIO, model.getCusto_medio());
            values.put(GasolinaModel.COLUNA_ALUGUEL_VEICULO, model.getAluguel_veiculo());

            rowAffect = db.insert(GasolinaModel.TABELA_NOME, null, values);
        } finally {
            Close();
        }

        return rowAffect;
    }

    public final GasolinaModel CursorToStructure(Cursor cursor) {
        GasolinaModel model = new GasolinaModel();
        model.setId(cursor.getLong(0));
        model.setViagem(cursor.getLong(1));
        model.setKm(cursor.getDouble(2));
        model.setKm_litro(cursor.getDouble(3));
        model.setCusto_medio(cursor.getDouble(2));
        model.setAluguel_veiculo(cursor.getDouble(2));

        return model;
    }

    public List<GasolinaModel> buscaGasolina(String usuario) throws SQLException {
        List<GasolinaModel> lista = new ArrayList<>();

        try {
            Open();
            String selection = GasolinaModel.COLUNA_VIAGEM + " = ?";
            String[] selectionArgs = {usuario};

            Cursor cursor = db.query(GasolinaModel.TABELA_NOME, colunas, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                lista.add(CursorToStructure(cursor));
            }
        } finally {
            Close();
        }

        return lista;
    }
}
