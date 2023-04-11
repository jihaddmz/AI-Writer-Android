package com.appsfourlife.draftogo.feature_generate_text.data.repository

import androidx.room.*
import com.appsfourlife.draftogo.feature_generate_art.data.model.ModelArtHistory
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelTemplate

@Dao
interface DaoApp {

    @Insert
    suspend fun insertTemplate(modelTemplate: ModelTemplate)

    @Delete
    suspend fun deleteTemplate(modelTemplate: ModelTemplate)

    @Query("select * from table_template")
    suspend fun getAllTemplates(): List<ModelTemplate>

    @Query("select * from table_template where query=:query")
    suspend fun getTemplateByQuery(query: String): ModelTemplate?


    @Insert
    suspend fun insertArt(modelArtHistory: ModelArtHistory)

    @Update
    suspend fun updateArt(modelArtHistory: ModelArtHistory)

    @Delete
    suspend fun deleteArt(modelArtHistory: ModelArtHistory)

    @Query("select * from table_arthistory")
    suspend fun getAllArts(): List<ModelArtHistory>

    @Query("select * from table_arthistory where prompt=:prompt")
    suspend fun getArt(prompt: String): ModelArtHistory?
}