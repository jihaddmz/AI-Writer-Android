package com.appsfourlife.draftogo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_newchat")
data class ModelNewChat(
    @PrimaryKey() var id: Int = 0,
    var text: String
)
