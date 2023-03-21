package com.appsfourlife.draftogo.feature_generate_text.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_template")
data class ModelTemplate(
    @PrimaryKey val query: String,
    val imageUrl: String,
    val userAdded: Int
)
