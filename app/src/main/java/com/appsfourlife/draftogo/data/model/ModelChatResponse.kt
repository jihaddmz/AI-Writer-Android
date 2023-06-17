package com.appsfourlife.draftogo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_chat")
data class ModelChatResponse(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val role: String,
    val text: String,
    val color: Int // if 1 it is user role so blue color, otherwise it is system role so gray color
)
