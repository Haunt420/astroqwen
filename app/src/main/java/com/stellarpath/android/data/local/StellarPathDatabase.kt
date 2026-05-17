package com.stellarpath.android.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ProfileEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(RoomTypeConverters::class)
abstract class StellarPathDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object {
        fun build(context: Context): StellarPathDatabase = Room
            .databaseBuilder(context, StellarPathDatabase::class.java, "stellarpath.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}
