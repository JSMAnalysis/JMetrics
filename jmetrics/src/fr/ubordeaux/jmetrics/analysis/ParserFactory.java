package fr.ubordeaux.jmetrics.analysis;

public interface ParserFactory {

    AbstractnessParser getAbstractnessParser();

    CouplingParser getCouplingParser();

}
