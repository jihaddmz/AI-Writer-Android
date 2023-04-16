package com.appsfourlife.draftogo.feature_generate_art.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_arthistory")
data class ModelArtHistory(
    @PrimaryKey val prompt: String,
    val imageUrl: String,
    val dateTime: Long
)
