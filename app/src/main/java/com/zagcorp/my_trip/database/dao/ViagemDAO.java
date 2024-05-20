package com.zagcorp.my_trip.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zagcorp.my_trip.database.helper.DBOpenHelper;
import com.zagcorp.my_trip.database.model.ViagemModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViagemDAO extends AbstrataDAO{
    public final String colunas[] = {
            ViagemModel.COLUNA_ID,
            ViagemModel.COLUNA_USUARIO,
            ViagemModel.COLUNA_TITULO,
            ViagemModel.COLUNA_LOCAL,
            ViagemModel.COLUNA_DURACAO
    };

    public ViagemDAO(Context context) {
        db_helper = new DBOpenHelper(context);
    }

    public long Insert(ViagemModel model) throws SQLException {
        long rowAffect = 0;

        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(ViagemModel.COLUNA_USUARIO, model.getUsuario());
            values.put(ViagemModel.COLUNA_TITULO, model.getTitulo());
            values.put(ViagemModel.COLUNA_LOCAL, model.getLocal());
            values.put(ViagemModel.COLUNA_DURACAO, model.getDuracao());

            rowAffect = db.insert(ViagemModel.TABELA_NOME, null, values);
        } finally {
            Close();
        }

        return rowAffect;
    }

    public List<ViagemModel> buscaViagens(String usuario) throws SQLException {
        List<ViagemModel> lista = new ArrayList<>();

        try {
            Open();
            String selection = ViagemModel.COLUNA_USUARIO + " = ?";
            String[] selectionArgs = {usuario};

            Cursor cursor = db.query(ViagemModel.TABELA_NOME, colunas, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                lista.add(CursorToStructure(cursor));
            }
        } finally {
            Close();
        }

        return lista;
    }

    public final ViagemModel CursorToStructure(Cursor cursor) {
        ViagemModel model = new ViagemModel();
        model.setId(cursor.getLong(0));
        model.setUsuario(cursor.getLong(1));
        model.setTitulo(cursor.getString(2));
        model.setLocal(cursor.getString(3));
        model.setDuracao(cursor.getString(4));

        return model;
    }
}
