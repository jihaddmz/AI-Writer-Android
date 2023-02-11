package com.appsfourlife.draftogo.feature_generate_text.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appsfourlife.draftogo.feature_generate_text.domain.model.ModelSection

@Database(entities = [ModelSection::class], version = 1)
abstract class DatabaseSections: RoomDatabase() {

    abstract fun dao(): DaoSections

    companion object {
        private var INSTANCE: DatabaseSections? = null

        fun getInstance(context: Context): DatabaseSections{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    DatabaseSections::class.java,
                    "db_options.db"
                ).build()
            }

            return INSTANCE!!
        }
    }
}