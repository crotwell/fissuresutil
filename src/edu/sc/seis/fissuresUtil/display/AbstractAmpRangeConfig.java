package edu.sc.seis.fissuresUtil.display;

import edu.iris.Fissures.model.*;
import org.apache.log4j.*;
import java.util.*;
/**
 * AbstractAmpRangeConfig.java
 *
 *
 * Created: Mon Jun  3 13:16:18 2002
 *
 * @author Charlie Groves
 * @version
 */

public abstract class AbstractAmpRangeConfig implements AmpRangeConfig{
         
    public AbstractAmpRangeConfig(){}

    public AbstractAmpRangeConfig(LinkedList seismos){
	this.seismos = seismos;
	Iterator e = seismos.iterator();
	while(e.hasNext()){
	    this.addSeismogram(((DataSetSeismogram)e.next()));
	}
    }

    /**
     * Returns the amplitude range for a given seismogram
     *
     * 
     */
    public abstract UnitRangeImpl getAmpRange(DataSetSeismogram seis);

    /**
     * Returns the amplitude range for a given seismogram over a time range
     *
     * 
     */
    public abstract UnitRangeImpl getAmpRange(DataSetSeismogram seis, MicroSecondTimeRange calcIntv);
    
    /**
     * Returns the amplitude range for the whole area being displayed.
     *
     */
    public UnitRangeImpl getAmpRange(){ return ampRange; }
    
    /**
     * Calculates the amplitudes for all seismograms currently held by the configurator based on the time range rules for them held 
     * in the TimeRangeConfig object passed
     *
     */
    public void visibleAmpCalc(TimeConfigRegistrar timeRegistrar){
	this.timeRegistrar = timeRegistrar;
	timeRegistrar.addTimeSyncListener(this);
	this.updateTimeRange();
    }

    /**
     * Adds a seismogram to the current amplitude configurator
     *
     */
    public void addSeismogram(DataSetSeismogram seis){ seismos.add(seis); }

    /**
     * Removes a seismogram from this amplitude configurator
     *
     * @param seis a <code>DataSetSeismogram</code> value
     */
    public void removeSeismogram(DataSetSeismogram seis){ 
	seismos.remove(seis); 
	if (seismos.isEmpty()) {
	    ampRange = new UnitRangeImpl(-1, 1, UnitImpl.COUNT);
	    updateAmpSyncListeners();
	} // end of if (seismos.isEmpty())
	
    }

    public void addAmpSyncListener(AmpSyncListener a){ ampListeners.add(a); }

    public void removeAmpSyncListener(AmpSyncListener a){ ampListeners.remove(a); }

    /**
     * Sends a message to all AmpSyncListeners held by the configurator that the amplitude range has changed
     *
     */
    public void updateAmpSyncListeners(){
	Iterator e = ampListeners.iterator();
	while (e.hasNext()) {
	    ((AmpSyncListener)e.next()).updateAmpRange();
	} // end of while (e.hasNext())
    }

    public void updateTimeRange(){
	UnitRangeImpl tempRange = ampRange;
	ampRange = null;
	Iterator e = seismos.iterator();
	while(e.hasNext()){
	    DataSetSeismogram current = (DataSetSeismogram)e.next();
	    getAmpRange(current, timeRegistrar.getTimeRange(current));
	}
	if(ampRange == null){
	    ampRange = tempRange;
	    return;
	}
	this.updateAmpSyncListeners();
    }

    public void fireAmpRangeEvent(AmpSyncEvent event){
	double begin = event.getBegin();
	double end = event.getEnd();
	if(this.ampRange == null) {

	    this.ampRange = new UnitRangeImpl(begin, end, UnitImpl.COUNT);
	} else {
	    this.ampRange = new UnitRangeImpl(begin, end, UnitImpl.COUNT);
	}
	this.updateAmpSyncListeners();
    }

    public LinkedList getSeismograms(){ return seismos; }

    public void addSeismograms(LinkedList newSeismos){
	Iterator e = newSeismos.iterator();
	while(e.hasNext())
	    this.addSeismogram((DataSetSeismogram)e.next());
    }

    public synchronized AmpSnapshot takeSnapshot(){
	HashMap seismoAmpRange = new HashMap();
	Iterator e = seismos.iterator();
	while(e.hasNext()){
	    DataSetSeismogram current = (DataSetSeismogram)e.next();
	    seismoAmpRange.put(current, this.getAmpRange(current));
	}
	return new AmpSnapshot(seismoAmpRange, this.getAmpRange());
    }

    protected boolean intvCalc = false;
    
    protected UnitRangeImpl ampRange;

    protected TimeConfigRegistrar timeRegistrar = null;

    protected LinkedList seismos = new LinkedList();
    
    protected Set ampListeners = new HashSet();

    protected Category logger = Category.getInstance(AbstractAmpRangeConfig.class.getName());
}// AbstractAmpRangeConfig
