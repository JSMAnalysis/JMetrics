package ground_truth.example2.store;

import ground_truth.example2.domain.kitchen.Pizza;

import java.util.ArrayList;
import java.util.List;

public class Pizzaiolo {
    private List<Pizza> pizzaToPrepare;

    public Pizzaiolo() {
        this.pizzaToPrepare = new ArrayList<>();
    }

    public void addPizzaToPrepare(Pizza p) {
        pizzaToPrepare.add(p);
    }


}
