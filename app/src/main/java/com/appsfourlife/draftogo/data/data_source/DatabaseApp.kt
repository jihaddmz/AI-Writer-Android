package com.appsfourlife.draftogo.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.appsfourlife.draftogo.data.model.ModelChatResponse
import com.appsfourlife.draftogo.data.model.ModelFavoriteTemplate
import com.appsfourlife.draftogo.data.model.ModelPurchaseHistory
import com.appsfourlife.draftogo.data.model.ModelTemplate
import com.appsfourlife.draftogo.data.repository.DaoApp
import com.appsfourlife.draftogo.feature_generate_art.data.model.ModelArtHistory


@Database(
    entities = [ModelTemplate::class, ModelArtHistory::class, ModelFavoriteTemplate::class, ModelPurchaseHistory::class, ModelChatResponse::class],
    version = 3
)
abstract class DatabaseApp : RoomDatabase() {

    abstract val daoApp: DaoApp

    companion object {

        private var INSTANCE: DatabaseApp? = null

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // create table_arthistory table
//                database.execSQL("CREATE TABLE IF NOT EXISTS `table_purchasehistory` (`date` TEXT NOT NULL, `price` FLOAT NOT NULL, 'type' TEXT NOT NULL, PRIMARY KEY(`date`))")
                database.execSQL("CREATE TABLE IF NOT EXISTS `table_chat` (`id` INT NOT NULL, `role` text not null, `text` text not null, `color` int not null, primary key(`id`))")
            }
        }

        fun getInstance(context: Context): DatabaseApp? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    DatabaseApp::class.java,
                    "db_generate_text"
                ).allowMainThreadQueries().addMigrations(MIGRATION_2_3).build()
            }

            return INSTANCE
        }
    }
}