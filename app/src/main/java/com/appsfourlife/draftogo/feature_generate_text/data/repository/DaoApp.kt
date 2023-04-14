package com.appsfourlife.draftogo.feature_generate_text.data.repository

import androidx.room.*
import com.appsfourlife.draftogo.feature_generate_art.data.model.ModelArtHistory
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelFavoriteTemplate
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelPurchaseHistory
import com.appsfourlife.draftogo.feature_generate_text.data.model.ModelTemplate

@Dao
interface DaoApp {

    /**
     * templates
     **/
    @Insert
    suspend fun insertTemplate(modelTemplate: ModelTemplate)

    @Delete
    suspend fun deleteTemplate(modelTemplate: ModelTemplate)

    @Query("select * from table_template")
    suspend fun getAllTemplates(): List<ModelTemplate>

    @Query("select * from table_template where query=:query")
    suspend fun getTemplateByQuery(query: String): ModelTemplate?


    /**
     * favorite templates
     **/
    @Insert
    suspend fun insertFavoriteTemplate(modelFavoriteTemplate: ModelFavoriteTemplate)

    @Delete
    suspend fun deleteFavoriteTemplate(modelFavoriteTemplate: ModelFavoriteTemplate)

    @Query("select * from table_favoritetemplates")
    suspend fun getAllFavoriteTemplates(): List<ModelFavoriteTemplate>

    @Query("select * from table_favoritetemplates where query=:query")
    suspend fun getFavoriteTemplate(query: String): ModelFavoriteTemplate?


    /**
     * arts
     **/
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


    /**
     * purchase history
     **/
    @Insert
    suspend fun insertPurchaseHistory(modelPurchaseHistory: ModelPurchaseHistory)

    @Update
    suspend fun updatePurchaseHistory(modelPurchaseHistory: ModelPurchaseHistory)

    @Query("select * from table_purchasehistory")
    suspend fun getAllPurchaseHistory(): List<ModelPurchaseHistory>

    @Query("select * from table_purchasehistory where date=:date")
    suspend fun getPurchaseHistory(date: String): ModelPurchaseHistory?
}