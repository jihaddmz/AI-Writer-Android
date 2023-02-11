package com.appsfourlife.draftogo.feature_generate_text.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_section")
data class ModelSection(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val iconID: Int
)
