package com.example.mymidialist.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mymidialist.model.Midia
import kotlinx.coroutines.flow.Flow

@Dao
interface MidiaDao {
    @Query("SELECT *FROM tabela_midia")
    fun getTodasMidias(): Flow<List<Midia>>

    @Insert
    suspend fun inserir(midia: Midia)

    @Delete fun deletar(midia: Midia)

    @Query("DELETE FROM tabela_midia")
    suspend fun deletarTudo()
}