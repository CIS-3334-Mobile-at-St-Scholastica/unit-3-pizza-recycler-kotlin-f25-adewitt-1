package cis.kotlin_pizzarecyclerstart


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

// --- File: Pizza.kt ---
// Keep enums readable for beginners
enum class PizzaSize { SMALL, MEDIUM, LARGE, XLARGE }
enum class Topping { CHICKEN, PEPPERONI, GREEN_PEPPERS }

/**
 * Make this a Room entity.
 * - Auto-generated Long primary key
 * - Use @TypeConverters so Room can store enums and the Set<Topping>
 */
@Entity(tableName = "pizzas")
@TypeConverters(PizzaTypeConverters::class)
data class Pizza(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val size: PizzaSize = PizzaSize.MEDIUM,
    val toppings: Set<Topping> = emptySet()
) {
    fun description(): String {
        val sizeText = when (size) {
            PizzaSize.SMALL -> "Small"
            PizzaSize.MEDIUM -> "Medium"
            PizzaSize.LARGE -> "Large"
            PizzaSize.XLARGE -> "X-Large"
        }
        val toppingText = if (toppings.isEmpty()) "Cheese" else toppings
            .map {
                when (it) {
                    Topping.CHICKEN -> "Chicken"
                    Topping.PEPPERONI -> "Pepperoni"
                    Topping.GREEN_PEPPERS -> "Green Peppers"
                }
            }
            .joinToString(", ")
        return "$sizeText pizza with $toppingText"
    }
}

/**
 * Converters so Room can persist enums and a Set<Topping>.
 * - Enums are stored by name (e.g., "MEDIUM")
 * - Toppings Set is stored as a pipe-delimited string (e.g., "CHICKEN|PEPPERONI")
 */
object PizzaTypeConverters {

    // PizzaSize <-> String
    @TypeConverter
    @JvmStatic
    fun fromPizzaSize(size: PizzaSize?): String? = size?.name

    @TypeConverter
    @JvmStatic
    fun toPizzaSize(raw: String?): PizzaSize? = raw?.let { PizzaSize.valueOf(it) }

    // Set<Topping> <-> String
    @TypeConverter
    @JvmStatic
    fun fromToppingsSet(toppings: Set<Topping>?): String =
        toppings?.joinToString("|") { it.name } ?: ""

    @TypeConverter
    @JvmStatic
    fun toToppingsSet(raw: String?): Set<Topping> =
        if (raw.isNullOrBlank()) emptySet()
        else raw.split("|").map { Topping.valueOf(it) }.toSet()
}