package cis.kotlin_pizzarecyclerstart

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Repository contract so your ViewModel depends on an interface,
 * not Room directly (easier to unit test).
 */
interface PizzaRepository {
    // Create/Update
    suspend fun upsert(pizza: Pizza): Long
    suspend fun upsertAll(pizzas: List<Pizza>): List<Long>

    // Read/Observe
    fun observeAll(): Flow<List<Pizza>>
    fun observeById(id: Long): Flow<Pizza?>
    fun count(): Flow<Int>


    // Delete
    suspend fun delete(pizza: Pizza)
    suspend fun deleteAll()
}

/**
 * Concrete implementation backed by Room's PizzaDao.
 * Write operations are dispatched to IO.
 */
class PizzaRepositoryImpl(
    private val dao: PizzaDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PizzaRepository {

    // --- Create/Update ---
    override suspend fun upsert(pizza: Pizza): Long = withContext(ioDispatcher) {
        dao.upsert(pizza)
    }

    override suspend fun upsertAll(pizzas: List<Pizza>): List<Long> = withContext(ioDispatcher) {
        dao.upsertAll(pizzas)
    }

    // --- Read/Observe ---
    override fun observeAll(): Flow<List<Pizza>> = dao.getAll()

    override fun observeById(id: Long): Flow<Pizza?> = dao.getById(id)

    override fun count(): Flow<Int> = dao.count()


    // --- Delete ---
    override suspend fun delete(pizza: Pizza) = withContext(ioDispatcher) {
        dao.delete(pizza)
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        dao.deleteAll()
    }
}