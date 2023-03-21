package com.appsfourlife.draftogo.feature_generate_text.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_template")
data class ModelTemplate(
    @PrimaryKey val query: String,
    val imageUrl: String,
    val userAdded: Int // 1 indicating predefined template was added, 0 indicates that it is user added, this field is for
    // sorting from user added to predefined added (increasing order)
)
