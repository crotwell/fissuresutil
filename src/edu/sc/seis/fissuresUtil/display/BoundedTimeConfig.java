package edu.sc.seis.fissuresUtil.display;


import edu.iris.Fissures.model.*;
import edu.iris.Fissures.IfSeismogramDC.LocalSeismogram;
import edu.iris.Fissures.seismogramDC.*;

import java.util.*;
/**
 * BoundedTimeConfig is a TimeRangeConfig implementation that allows absolute time ranges to be set for the display.  It only displays
 * whatever is inside of a user defined time range.  If none is specified, it displays the entire time range of the first seismogram
 * it receives.
 *
 *
 * Created: Mon May 27 09:18:10 2002
 *
 * @author Charlie Groves
 * @version 0.1
 */

public class BoundedTimeConfig extends AbstractTimeRangeConfig{
    
    /** Merely returns the time range that has been set
     */
    public MicroSecondTimeRange getTimeRange(LocalSeismogram seis){
	return new MicroSecondTimeRange(this.beginTime, this.beginTime.add(displayInterval));
    }

    public MicroSecondTimeRange getTimeRange(){
	return new MicroSecondTimeRange(this.beginTime, this.beginTime.add(displayInterval));
    }

    public void addSeismogram(LocalSeismogram seis){
	if(beginTime == null)
	    this.beginTime = ((LocalSeismogramImpl)seis).getBeginTime();
	if(displayInterval == null)
	    this.displayInterval = new TimeInterval(((LocalSeismogramImpl)seis).getBeginTime(), ((LocalSeismogramImpl)seis).getEndTime());
	seismos.put(seis, ((LocalSeismogramImpl)seis).getBeginTime());
	this.updateTimeSyncListeners();
    }	
    
    /**  When BoundedTimeConfig receives a TimeSyncEvent, it merely changes the time range by the percentages contained in the 
     *   TimeSyncEvent
     */
    public void fireTimeRangeEvent(TimeSyncEvent e){
	double begin = e.getBegin();
	double end = e.getEnd();
	double intv = displayInterval.getValue();
	MicroSecondDate endTime = new MicroSecondDate((long)(beginTime.add(displayInterval).getMicroSecondTime() + intv*end));
	beginTime = new MicroSecondDate((long)(beginTime.getMicroSecondTime() + intv*begin));
	displayInterval = new TimeInterval(beginTime, endTime);
	this.updateTimeSyncListeners();
    }
    
}// BoundedTimeConfig
