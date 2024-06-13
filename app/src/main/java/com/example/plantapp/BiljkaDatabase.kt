package com.example.plantapp

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context

@Database(entities = arrayOf(Biljka::class, BiljkaBitmap::class), version = 2)
@TypeConverters(Konverteri::class)
abstract class BiljkaDatabase : RoomDatabase() {
    abstract fun biljkaDAO(): BiljkaDAO

    companion object {
        private var INSTANCE: BiljkaDatabase? = null

        fun getDatabase(context: Context): BiljkaDatabase {
            if (INSTANCE==null){
                synchronized(BiljkaDatabase::class){
                    INSTANCE=buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }
        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BiljkaDatabase::class.java,
                "biljke-db").fallbackToDestructiveMigration()
                .build()
    }
}