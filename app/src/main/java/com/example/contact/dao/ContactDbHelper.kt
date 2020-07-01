package com.example.contact.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * @author caihn
 * @create 2020-06-29-14:31
 */
class ContactDbHelper(private val context: Context, private val dbName: String, val version: Int):
    SQLiteOpenHelper(context, dbName, null, version){

    private val createContact = "create table contact(" +
            "id integer primary key autoincrement, " +
            "name text, " +
            "phoneNumber text," +
            "headerImage integer," +
            "email text, " +
            "address text, " +
            "remark text, " +
            "letter text)"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createContact)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("drop table if exists contact")
        db?.execSQL(createContact)
    }
}