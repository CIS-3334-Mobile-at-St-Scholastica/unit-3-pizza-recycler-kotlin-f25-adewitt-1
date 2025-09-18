package cis.kotlin_pizzarecyclerstart.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import cis.kotlin_pizzarecyclerstart.Pizza
import cis.kotlin_pizzarecyclerstart.PizzaSize
import kotlinx.coroutines.flow.Flow

@Dao
interface PizzaDao {

    // --- Create/Update ---
    @Upsert
    suspend fun upsert(pizza: Pizza): Long

    @Upsert
    suspend fun upsertAll(pizzas: List<Pizza>): List<Long>

    // --- Read ---
    @Query("SELECT * FROM pizzas ORDER BY id DESC")
    fun getAll(): Flow<List<Pizza>>

    @Query("SELECT * FROM pizzas WHERE id = :id LIMIT 1")
    fun getById(id: Long): Flow<Pizza?>

    @Query("SELECT COUNT(*) FROM pizzas")
    fun count(): Flow<Int>

    // Example filtered queries (handy for demos/assignments)
    @Query("SELECT * FROM pizzas WHERE size = :size ORDER BY id DESC")
    fun findBySize(size: PizzaSize): Flow<List<Pizza>>

    @Query("SELECT * FROM pizzas WHERE toppings LIKE '%' || :toppingName || '%' ORDER BY id DESC")
    fun findWithTopping(toppingName: String): Flow<List<Pizza>>

    // --- Delete ---
    @Delete
    suspend fun delete(pizza: Pizza)

    @Query("DELETE FROM pizzas")
    suspend fun deleteAll()
}