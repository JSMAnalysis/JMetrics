package fr.ubordeaux.jmetrics.analysis;

public class IntrospectionParserFactory implements ParserFactory {

    @Override
    public AbstractnessParser getAbstractnessParser() {
        return new IntrospectionAbstractnessParser();
    }

    @Override
    public CouplingParser getCouplingParser() {
        return new IntrospectionCouplingParser();
    }

}
