package cis.kotlin_pizzarecyclerstart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: PizzaRepository
) : ViewModel() {

    // --- Private mutable, public read-only (prevents setter name clashes) ---
    private var _size by mutableStateOf(PizzaSize.MEDIUM)
    val size: PizzaSize get() = _size

    private var _chicken by mutableStateOf(false)
    val chicken: Boolean get() = _chicken

    private var _pepperoni by mutableStateOf(false)
    val pepperoni: Boolean get() = _pepperoni

    private var _greenPeppers by mutableStateOf(false)
    val greenPeppers: Boolean get() = _greenPeppers

    // Mutators with non-conflicting names
    fun updateSize(newSize: PizzaSize) { _size = newSize }
    fun setChickenChecked(checked: Boolean) { _chicken = checked }
    fun setPepperoniChecked(checked: Boolean) { _pepperoni = checked }
    fun setGreenPeppersChecked(checked: Boolean) { _greenPeppers = checked }

    fun toggleChicken() { _chicken = !_chicken }
    fun togglePepperoni() { _pepperoni = !_pepperoni }
    fun toggleGreenPeppers() { _greenPeppers = !_greenPeppers }

    // --- Source of truth: DB via repository ---
    val pizzas: StateFlow<List<Pizza>> =
        repository.observeAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val orderCount: StateFlow<Int> =
        pizzas.map { it.size }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val orderText: StateFlow<String> =
        pizzas.map { list ->
            if (list.isEmpty()) "No pizzas in your order yet."
            else list.joinToString("\n") { it.description() }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "No pizzas in your order yet.")

    // --- Actions ---
    fun addToOrder() {
        val toppings = buildSet {
            if (chicken) add(Topping.CHICKEN)
            if (pepperoni) add(Topping.PEPPERONI)
            if (greenPeppers) add(Topping.GREEN_PEPPERS)
        }
        val pizza = Pizza(size = size, toppings = toppings)

        viewModelScope.launch {
            repository.upsert(pizza)
            // clear toppings for the next pizza; keep size
            _chicken = false
            _pepperoni = false
            _greenPeppers = false
        }
    }

    fun clearOrder() {
        viewModelScope.launch {
            repository.deleteAll()
            resetSelectionsToDefaults()
        }
    }

    fun deletePizza(pizza: Pizza) {
        viewModelScope.launch { repository.delete(pizza) }
    }

    private fun resetSelectionsToDefaults() {
        _size = PizzaSize.MEDIUM
        _chicken = false
        _pepperoni = false
        _greenPeppers = false
    }
}

/** Simple factory for wiring without DI. */
class MainViewModelFactory(
    private val repository: PizzaRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
