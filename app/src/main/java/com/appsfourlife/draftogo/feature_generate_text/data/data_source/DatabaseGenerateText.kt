package com.appsfourlife.draftogo.feature_generate_text.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelTemplate
import com.appsfourlife.draftogo.feature_generate_text.data.repository.DaoTemplates

@Database(entities = [ModelTemplate::class], version = 1)
abstract class DatabaseGenerateText: RoomDatabase() {

    abstract val daoTemplates: DaoTemplates

    companion object {

        private var INSTANCE: DatabaseGenerateText? = null

        fun getInstance(context: Context): DatabaseGenerateText? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    DatabaseGenerateText::class.java,
                    "db_generate_text"
                ).build()
            }

            return INSTANCE
        }
    }
}