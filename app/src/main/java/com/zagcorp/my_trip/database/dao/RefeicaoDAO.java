package com.zagcorp.my_trip.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zagcorp.my_trip.database.helper.DBOpenHelper;
import com.zagcorp.my_trip.database.model.HospedagemModel;
import com.zagcorp.my_trip.database.model.RefeicaoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RefeicaoDAO extends AbstrataDAO{
    public final String colunas[] = {
            RefeicaoModel.COLUNA_ID,
            RefeicaoModel.COLUNA_VIAGEM,
            RefeicaoModel.COLUNA_CUSTO_ESTIMADO,
            RefeicaoModel.COLUNA_QTD_REFEICAO,
    };

    public RefeicaoDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    public long Insert(RefeicaoModel model) throws SQLException {
        long rowAffect = 0;

        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(RefeicaoModel.COLUNA_VIAGEM, model.getViagem());
            values.put(RefeicaoModel.COLUNA_CUSTO_ESTIMADO, model.getCusto_estimado());
            values.put(RefeicaoModel.COLUNA_QTD_REFEICAO, model.getQtd_refeicao());

            rowAffect = db.insert(RefeicaoModel.TABELA_NOME, null, values);
        } finally {
            Close();
        }

        return rowAffect;
    }

    public long Edit(RefeicaoModel model, Long viagem) throws  SQLException{
        long rowAffect = 0;

        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(RefeicaoModel.COLUNA_VIAGEM, model.getViagem());
            values.put(RefeicaoModel.COLUNA_CUSTO_ESTIMADO, model.getCusto_estimado());
            values.put(RefeicaoModel.COLUNA_QTD_REFEICAO, model.getQtd_refeicao());


            String selection = RefeicaoModel.COLUNA_VIAGEM + " = ?";
            String[] selectionArgs = {viagem.toString()};

            rowAffect = db.update(RefeicaoModel.TABELA_NOME, values, selection, selectionArgs);
        } finally {
            Close();
        }

        return rowAffect;
    }

    public int verificaRefeicao(Long viagem) throws SQLException {
        int count = 0;

        try {
            Open();
            String selection = RefeicaoModel.COLUNA_VIAGEM + " = ?";
            String[] selectionArgs = {viagem.toString()};

            String query = "SELECT COUNT(*) FROM " + RefeicaoModel.TABELA_NOME + " WHERE " + selection;
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

    public RefeicaoModel buscaRefeicaoPorIdViagem(Long idViagem) throws SQLException {
        RefeicaoModel refeicao = null;

        try {
            Open();
            String selection = RefeicaoModel.COLUNA_VIAGEM + " = ?";
            String[] selectionArgs = {idViagem.toString()};

            Cursor cursor = db.query(RefeicaoModel.TABELA_NOME, colunas, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                refeicao = CursorToStructure(cursor);
            }
        } finally {
            Close();
        }

        return refeicao;
    }


    public final RefeicaoModel CursorToStructure(Cursor cursor) {
        RefeicaoModel model = new RefeicaoModel();
        model.setId(cursor.getLong(0));
        model.setViagem(cursor.getLong(1));
        model.setCusto_estimado(cursor.getDouble(2));
        model.setQtd_refeicao(cursor.getLong(3));

        return model;
    }

    public List<RefeicaoModel> buscaRefeicao(String viagem) throws SQLException {
        List<RefeicaoModel> lista = new ArrayList<>();

        try {
            Open();
            String selection = RefeicaoModel.COLUNA_VIAGEM + " = ?";
            String[] selectionArgs = {viagem};

            Cursor cursor = db.query(RefeicaoModel.TABELA_NOME, colunas, selection, selectionArgs, null, null, null);

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
            String whereClause = RefeicaoModel.COLUNA_VIAGEM + " = ?";
            String[] whereArgs = {String.valueOf(idViagem)};
            db.delete(RefeicaoModel.TABELA_NOME, whereClause, whereArgs);
        } finally {
            Close();
        }
    }
}