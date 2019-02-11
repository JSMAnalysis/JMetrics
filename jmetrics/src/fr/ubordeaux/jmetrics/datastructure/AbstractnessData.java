package fr.ubordeaux.jmetrics.datastructure;

public class AbstractnessData {

    private int numberOfMethods;
    private int numberOfAbstractMethods;

    public AbstractnessData(int numberOfMethods, int numberOfAbstractMethods){

        this.numberOfMethods = numberOfMethods;
        this.numberOfAbstractMethods = numberOfAbstractMethods;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public int getNumberOfAbstractMethods() {
        return numberOfAbstractMethods;
    }
}
