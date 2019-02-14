package ground_truth.example2.kitchen;

import ground_truth.example2.kitchen.ingredients.Ingredient;

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

    public BasePizza getBase() {
        return base;
    }

    public PizzaSize getPizzaSize() {
        return pizzaSize;
    }

    public PastaType getPastaType() {
        return pastaType;
    }

    public void setBase(BasePizza base) {
        this.base = base;
    }

    public void setPizzaSize(PizzaSize pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public void setPastaType(PastaType pastaType) {
        this.pastaType = pastaType;
    }

}
