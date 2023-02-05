package com.jihad.aiwriter.feature_generate_text.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jihad.aiwriter.feature_generate_text.domain.model.ModelSection

@Dao
interface DaoSections {

    @Query("select * from table_section")
    fun getAllSections(): List<ModelSection>

    @Insert
    fun addSection(modelSection: ModelSection)

    @Delete
    fun deleteSection(modelSection: ModelSection)
}