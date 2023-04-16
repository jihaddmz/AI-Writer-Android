package com.appsfourlife.draftogo.feature_generate_text.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_purchasehistory")
data class ModelPurchaseHistory(
    @PrimaryKey val date: String,
    var price: Float,
    val type: String
)
