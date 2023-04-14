package com.appsfourlife.draftogo.feature_generate_text.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.appsfourlife.draftogo.feature_generate_art.data.model.ModelArtHistory
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelFavoriteTemplate
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelPurchaseHistory
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelTemplate
import com.appsfourlife.draftogo.feature_generate_text.data.repository.DaoApp


@Database(
    entities = [ModelTemplate::class, ModelArtHistory::class, ModelFavoriteTemplate::class, ModelPurchaseHistory::class],
    version = 2
)
abstract class DatabaseApp : RoomDatabase() {

    abstract val daoApp: DaoApp

    companion object {

        private var INSTANCE: DatabaseApp? = null

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // create table_arthistory table
                database.execSQL("CREATE TABLE IF NOT EXISTS `table_arthistory` (`prompt` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, dateTime INTEGER NOT NULL, PRIMARY KEY(`prompt`))")
                database.execSQL("CREATE TABLE IF NOT EXISTS `table_favoritetemplates` (`query` TEXT NOT NULL, `imageUrl` TEXT NULL, iconID INTEGER NULL, PRIMARY KEY(`query`))")
                database.execSQL("CREATE TABLE IF NOT EXISTS `table_purchasehistory` (`date` TEXT NOT NULL, `price` FLOAT NOT NULL, PRIMARY KEY(`date`))")
            }
        }

        fun getInstance(context: Context): DatabaseApp? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    DatabaseApp::class.java,
                    "db_generate_text"
                ).addMigrations(MIGRATION_1_2).build()
            }

            return INSTANCE
        }
    }
}