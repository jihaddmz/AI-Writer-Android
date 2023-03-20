package com.appsfourlife.draftogo.feature_generate_text.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelTemplate

@Dao
interface DaoTemplates {

    @Insert
    suspend fun insertTemplate(modelTemplate: ModelTemplate)

    @Delete
    suspend fun deleteTemplate(modelTemplate: ModelTemplate)

    @Query("select * from table_template")
    suspend fun getAllTemplates(): List<ModelTemplate>

    @Query("select * from table_template where query=:query")
    suspend fun getTemplateByQuery(query: String): ModelTemplate?
}