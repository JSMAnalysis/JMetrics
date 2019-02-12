package ground_truth.example2.domain.kitchen;

import ground_truth.example2.domain.kitchen.ingredients.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class Pizza {

    private BasePizza base;
    private PizzaSize pizzaSize;
    private PastaType pastaType;
    private List<Ingredient> ingredientList;
    private double price;

    public Pizza(BasePizza b, PizzaSize t, PastaType patePizza, double price) {
        this.base = b;
        this.pizzaSize = t;
        this.pastaType = patePizza;
        this.ingredientList = new ArrayList<>();
        this.price = price;
    }

    private void addIngredientToPizza(Ingredient i) {
        ingredientList.add(i);
    }

}
