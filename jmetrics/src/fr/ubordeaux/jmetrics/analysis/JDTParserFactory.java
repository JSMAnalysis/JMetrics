package fr.ubordeaux.jmetrics.analysis;

public class JDTParserFactory implements ParserFactory {

    @Override
    public AbstractnessParser getAbstractnessParser() {
        return new JDTAbstractnessParser();
    }

    @Override
    public CouplingParser getCouplingParser() {
        return new JDTCouplingParser();
    }

}
