package cis.kotlin_pizzarecyclerstart.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cis.kotlin_pizzarecyclerstart.Pizza
import cis.kotlin_pizzarecyclerstart.PizzaTypeConverters

@Database(
    entities = [Pizza::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(PizzaTypeConverters::class)
abstract class PizzaDatabase : RoomDatabase() {
    abstract fun pizzaDao(): PizzaDao

    companion object {
        @Volatile
        private var INSTANCE: PizzaDatabase? = null

        fun getInstance(context: Context): PizzaDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    PizzaDatabase::class.java,
                    "pizza_db"
                )
                    // For teaching/demos you can allow main thread queries,
                    // but it's better to keep this off in real apps.
                    // .allowMainThreadQueries()
                    // If you change the schema in class, bump version and add a Migration.
                    .fallbackToDestructiveMigration() // OK for student projects
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}