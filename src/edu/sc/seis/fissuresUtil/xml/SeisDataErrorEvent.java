package edu.sc.seis.fissuresUtil.xml;

import edu.sc.seis.fissuresUtil.exceptionHandlerGUI.WrappedException;

/**
 * SeisDataErrorEvent.java
 *
 *
 * Created: Fri Apr 11 16:49:29 2003
 *
 * @author <a href="mailto:crotwell@owl.seis.sc.edu">Philip Crotwell</a>
 * @version 1.0
 */
public class SeisDataErrorEvent extends SeisDataChangeEvent implements WrappedException {

    public SeisDataErrorEvent(Throwable e,
                             DataSetSeismogram source,
                             Object initiator){
        super(source, initiator);
        this.causalException = e;
    }

    public Throwable getCausalException() {
        return causalException;
    }

    Throwable causalException;
}
