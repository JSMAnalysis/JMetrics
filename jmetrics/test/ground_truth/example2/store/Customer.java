package ground_truth.example2.store;

import ground_truth.example2.kitchen.Pizza;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private String name;
    private List<Pizza> pizzas;

    public Customer(String name) {
        this.name = name;
        pizzas = new ArrayList<>();
    }

    public void addPizzaToOrder(Pizza p) {
        pizzas.add(p);
    }

}
