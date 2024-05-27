package com.zagcorp.my_trip.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zagcorp.my_trip.database.helper.DBOpenHelper;
import com.zagcorp.my_trip.database.model.RefeicaoModel;
import com.zagcorp.my_trip.database.model.TarifaModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TarifaDAO extends AbstrataDAO{
    public final String colunas[] = {
            TarifaModel.COLUNA_ID,
            TarifaModel.COLUNA_VIAGEM,
            TarifaModel.COLUNA_CUSTO_PESSOA,
            TarifaModel.COLUNA_QTD_PESSOA,
            TarifaModel.COLUNA_CUSTO_VEICULO
    };

    public TarifaDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    public long Insert(TarifaModel model) throws SQLException {
        long rowAffect = 0;

        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(TarifaModel.COLUNA_VIAGEM, model.getViagem());
            values.put(TarifaModel.COLUNA_CUSTO_PESSOA, model.getCusto_pessoa());
            values.put(TarifaModel.COLUNA_QTD_PESSOA, model.getQtd_pessoa());
            values.put(TarifaModel.COLUNA_CUSTO_VEICULO, model.getCusto_veiculo());

            rowAffect = db.insert(TarifaModel.TABELA_NOME, null, values);
        } finally {
            Close();
        }

        return rowAffect;
    }

    public TarifaModel buscaTarifaPorIdViagem(Long idViagem) throws SQLException {
        TarifaModel tarifa = null;

        try {
            Open();
            String selection = TarifaModel.COLUNA_VIAGEM + " = ?";
            String[] selectionArgs = {idViagem.toString()};

            Cursor cursor = db.query(TarifaModel.TABELA_NOME, colunas, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                tarifa = CursorToStructure(cursor);
            }
        } finally {
            Close();
        }

        return tarifa;
    }


    public int update(TarifaModel model) throws SQLException {
        int rowAffect = 0;

        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(TarifaModel.COLUNA_CUSTO_PESSOA, model.getCusto_pessoa());
            values.put(TarifaModel.COLUNA_QTD_PESSOA, model.getQtd_pessoa());
            values.put(TarifaModel.COLUNA_CUSTO_VEICULO, model.getCusto_veiculo());

            String whereClause = TarifaModel.COLUNA_ID + " = ?";
            String[] whereArgs = {String.valueOf(model.getId())};

            rowAffect = db.update(TarifaModel.TABELA_NOME, values, whereClause, whereArgs);
        } finally {
            Close();
        }

        return rowAffect;
    }

    public int verificaTarifa(Long viagem) throws SQLException {
        int count = 0;

        try {
            Open();
            String selection = TarifaModel.COLUNA_VIAGEM + " = ?";
            String[] selectionArgs = {viagem.toString()};

            String query = "SELECT COUNT(*) FROM " + TarifaModel.TABELA_NOME + " WHERE " + selection;
            Cursor cursor = db.rawQuery(query, selectionArgs);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        } finally {
            Close();
        }

        return count;
    }

    public final TarifaModel CursorToStructure(Cursor cursor) {
        TarifaModel model = new TarifaModel();
        model.setId(cursor.getLong(0));
        model.setViagem(cursor.getLong(1));
        model.setCusto_pessoa(cursor.getDouble(2));
        model.setQtd_pessoa(cursor.getLong(3));
        model.setCusto_veiculo(cursor.getDouble(4));

        return model;
    }

    public List<TarifaModel> buscaTarifa(String viagem) throws SQLException {
        List<TarifaModel> lista = new ArrayList<>();

        try {
            Open();
            String selection = TarifaModel.COLUNA_VIAGEM + " = ?";
            String[] selectionArgs = {viagem};

            Cursor cursor = db.query(TarifaModel.TABELA_NOME, colunas, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                lista.add(CursorToStructure(cursor));
            }
        } finally {
            Close();
        }

        return lista;
    }
    public void deleteByViagemId(long idViagem) throws SQLException {
        try {
            Open();
            String whereClause = TarifaModel.COLUNA_VIAGEM + " = ?";
            String[] whereArgs = {String.valueOf(idViagem)};
            db.delete(TarifaModel.TABELA_NOME, whereClause, whereArgs);
        } finally {
            Close();
        }
    }
}
