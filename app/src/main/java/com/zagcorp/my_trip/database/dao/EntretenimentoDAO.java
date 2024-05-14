package com.zagcorp.my_trip.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zagcorp.my_trip.database.helper.DBOpenHelper;
import com.zagcorp.my_trip.database.model.EntretenimentoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntretenimentoDAO extends AbstrataDAO{
    public final String colunas[] = {
            EntretenimentoModel.COLUNA_ID,
            EntretenimentoModel.COLUNA_VIAGEM,
            EntretenimentoModel.COLUNA_ATIVIDADE,
            EntretenimentoModel.COLUNA_VALOR,
    };

    public EntretenimentoDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    public long Insert(EntretenimentoModel model) throws SQLException {
        long rowAffect = 0;

        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(EntretenimentoModel.COLUNA_VIAGEM, model.getViagem());
            values.put(EntretenimentoModel.COLUNA_ATIVIDADE, model.getAtividade());
            values.put(EntretenimentoModel.COLUNA_VALOR, model.getValor());

            rowAffect = db.insert(EntretenimentoModel.TABELA_NOME, null, values);
        } finally {
            Close();
        }

        return rowAffect;
    }

    public final EntretenimentoModel CursorToStructure(Cursor cursor) {
        EntretenimentoModel model = new EntretenimentoModel();
        model.setId(cursor.getLong(0));
        model.setViagem(cursor.getLong(1));
        model.setAtividade(cursor.getString(2));
        model.setValor(cursor.getLong(3));

        return model;
    }

    public List<EntretenimentoModel> buscaEntretenimento(String viagem) throws SQLException {
        List<EntretenimentoModel> lista = new ArrayList<>();

        try {
            Open();
            String selection = EntretenimentoModel.COLUNA_VIAGEM + " = ?";
            String[] selectionArgs = {viagem};

            Cursor cursor = db.query(EntretenimentoModel.TABELA_NOME, colunas, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                lista.add(CursorToStructure(cursor));
            }
        } finally {
            Close();
        }

        return lista;
    }
}