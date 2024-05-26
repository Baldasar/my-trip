package com.zagcorp.my_trip.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zagcorp.my_trip.database.helper.DBOpenHelper;
import com.zagcorp.my_trip.database.model.GasolinaModel;
import com.zagcorp.my_trip.database.model.TarifaModel;
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

    public List<ViagemModel> buscaViagensPorId(Long idViagem) throws SQLException {
        List<ViagemModel> lista = new ArrayList<>();

        try {
            Open();
            String selection = ViagemModel.COLUNA_ID + " = ?";
            String[] selectionArgs = {String.valueOf(idViagem)};

            Cursor cursor = db.query(ViagemModel.TABELA_NOME, colunas, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                lista.add(CursorToStructure(cursor));
            }

            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return lista;
    }

    public int update(ViagemModel model) throws SQLException {
        int rowsAffected = 0;

        try {
            Open();

            ContentValues values = new ContentValues();
            values.put(ViagemModel.COLUNA_USUARIO, model.getUsuario());
            values.put(ViagemModel.COLUNA_TITULO, model.getTitulo());
            values.put(ViagemModel.COLUNA_LOCAL, model.getLocal());
            values.put(ViagemModel.COLUNA_DURACAO, model.getDuracao());

            String whereClause = ViagemModel.COLUNA_ID + " = ?";
            String[] whereArgs = {String.valueOf(model.getId())};

            rowsAffected = db.update(ViagemModel.TABELA_NOME, values, whereClause, whereArgs);
        } finally {
            Close();
        }

        return rowsAffected;
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

    public void deleteByViagemId(long idViagem) throws SQLException {
        try {
            Open();
            String whereClause = ViagemModel.COLUNA_ID + " = ?";
            String[] whereArgs = {String.valueOf(idViagem)};
            db.delete(ViagemModel.TABELA_NOME, whereClause, whereArgs);
        } finally {
            Close();
        }
    }

    public String getDuracaoViagem(Long idViagem) throws SQLException {
        String duracao = null;

        try {
            Open();
            String selection = ViagemModel.COLUNA_ID + " = ?";
            String[] selectionArgs = {String.valueOf(idViagem)};

            Cursor cursor = db.query(ViagemModel.TABELA_NOME, new String[]{ViagemModel.COLUNA_DURACAO}, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                duracao = cursor.getString(0);
            }

            if (cursor != null) {
                cursor.close();
            }
        } finally {
            Close();
        }

        return duracao;
    }
}
