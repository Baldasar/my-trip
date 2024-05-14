package com.zagcorp.my_trip.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zagcorp.my_trip.database.helper.DBOpenHelper;
import com.zagcorp.my_trip.database.model.HospedagemModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HospedagemDAO extends AbstrataDAO{
    public final String colunas[] = {
            HospedagemModel.COLUNA_ID,
            HospedagemModel.COLUNA_VIAGEM,
            HospedagemModel.COLUNA_CUSTO_NOITE,
            HospedagemModel.COLUNA_QTD_NOITE,
            HospedagemModel.COLUNA_QTD_QUARTO,
    };

    public HospedagemDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    public long Insert(HospedagemModel model) throws SQLException {
        long rowAffect = 0;

        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(HospedagemModel.COLUNA_VIAGEM, model.getViagem());
            values.put(HospedagemModel.COLUNA_CUSTO_NOITE, model.getCusto_noite());
            values.put(HospedagemModel.COLUNA_QTD_NOITE, model.getQtd_noite());
            values.put(HospedagemModel.COLUNA_QTD_QUARTO, model.getQtd_quarto());

            rowAffect = db.insert(HospedagemModel.TABELA_NOME, null, values);
        } finally {
            Close();
        }

        return rowAffect;
    }

    public final HospedagemModel CursorToStructure(Cursor cursor) {
        HospedagemModel model = new HospedagemModel();
        model.setId(cursor.getLong(0));
        model.setViagem(cursor.getLong(1));
        model.setCusto_noite(cursor.getDouble(2));
        model.setQtd_noite(cursor.getLong(3));
        model.setQtd_quarto(cursor.getLong(4));

        return model;
    }

    public List<HospedagemModel> buscaHospedagem(String viagem) throws SQLException {
        List<HospedagemModel> lista = new ArrayList<>();

        try {
            Open();
            String selection = HospedagemModel.COLUNA_VIAGEM + " = ?";
            String[] selectionArgs = {viagem};

            Cursor cursor = db.query(HospedagemModel.TABELA_NOME, colunas, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                lista.add(CursorToStructure(cursor));
            }
        } finally {
            Close();
        }

        return lista;
    }
}