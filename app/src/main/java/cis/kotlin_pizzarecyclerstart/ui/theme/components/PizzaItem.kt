package cis.kotlin_pizzarecyclerstart.ui.theme.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cis.kotlin_pizzarecyclerstart.Pizza
import cis.kotlin_pizzarecyclerstart.PizzaSize
import cis.kotlin_pizzarecyclerstart.Topping

@Composable
fun PizzaItem(pizza: Pizza, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Fun pizza emoji "icon"
            Text(
                text = "🍕",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                // Show size nicely
                Text(
                    text = when (pizza.size) {
                        PizzaSize.SMALL -> "Small Pizza"
                        PizzaSize.MEDIUM -> "Medium Pizza"
                        PizzaSize.LARGE -> "Large Pizza"
                        PizzaSize.XLARGE -> "X-Large Pizza"
                    },
                    style = MaterialTheme.typography.titleMedium
                )

                // Show toppings or fallback to cheese
                val toppingText = if (pizza.toppings.isEmpty()) {
                    "Cheese"
                } else {
                    pizza.toppings.joinToString(", ") {
                        when (it) {
                            Topping.CHICKEN -> "🍗 Chicken"
                            Topping.PEPPERONI -> "🥓 Pepperoni"
                            Topping.GREEN_PEPPERS -> "🥦 Green Peppers"
                        }
                    }
                }

                Text(
                    text = toppingText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}