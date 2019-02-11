package fr.ubordeaux.jmetrics.datastructure;

public class AbstractnessData {

    int numberOfMethods;
    int numberOfAbstractMethods;

    public AbstractnessData(int numberOfMethods, int numberOfAbstractMethods){

        this.numberOfMethods = numberOfMethods;
        this.numberOfAbstractMethods = numberOfAbstractMethods;
    }
}
