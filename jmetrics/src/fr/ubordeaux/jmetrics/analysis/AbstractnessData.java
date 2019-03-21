package fr.ubordeaux.jmetrics.analysis;

import java.util.Objects;

public class AbstractnessData {

    private int numberOfMethods;
    private int numberOfAbstractMethods;

    public AbstractnessData(int numberOfMethods, int numberOfAbstractMethods) {
        this.numberOfMethods = numberOfMethods;
        this.numberOfAbstractMethods = numberOfAbstractMethods;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public int getNumberOfAbstractMethods() {
        return numberOfAbstractMethods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractnessData that = (AbstractnessData) o;
        return numberOfMethods == that.numberOfMethods &&
                numberOfAbstractMethods == that.numberOfAbstractMethods;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfMethods, numberOfAbstractMethods);
    }

}
