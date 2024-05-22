package com.zagcorp.my_trip.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zagcorp.my_trip.database.model.GasolinaModel;
import com.zagcorp.my_trip.database.model.HospedagemModel;
import com.zagcorp.my_trip.database.model.TarifaModel;
import com.zagcorp.my_trip.database.model.UsuarioModel;
import com.zagcorp.my_trip.database.model.ViagemModel;


public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_trip.db";

    private static final int DATABASE_VERSION = 3;

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UsuarioModel.CREATE_TABLE);
        db.execSQL(ViagemModel.CREATE_TABLE);
        db.execSQL(GasolinaModel.CREATE_TABLE);
        db.execSQL(TarifaModel.CREATE_TABLE);
        db.execSQL(HospedagemModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UsuarioModel.CREATE_TABLE);
        db.execSQL(ViagemModel.CREATE_TABLE);
        db.execSQL(GasolinaModel.CREATE_TABLE);
        db.execSQL(TarifaModel.CREATE_TABLE);
        db.execSQL(HospedagemModel.CREATE_TABLE);
    }

}
