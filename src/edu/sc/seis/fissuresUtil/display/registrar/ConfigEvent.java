package edu.sc.seis.fissuresUtil.display.registrar;
import edu.iris.Fissures.model.UnitRangeImpl;
import edu.sc.seis.fissuresUtil.display.MicroSecondTimeRange;
import edu.sc.seis.fissuresUtil.xml.DataSetSeismogram;

/**
 * ConfigEvents encapsulate the data generated by a config.
 *
 *
 * Created: Thu Aug 29 13:58:01 2002
 *
 * @author <a href="mailto:">Charlie Groves</a>
 * @version
 */

public class ConfigEvent{
    public ConfigEvent(DataSetSeismogram[] seismos, MicroSecondTimeRange[] times, UnitRangeImpl[] amps){
        this(seismos, new TimeEvent(seismos, times), new AmpEvent(seismos, amps));
    }

    public ConfigEvent(DataSetSeismogram[] seismos, TimeEvent time, AmpEvent amp){
        this.seismos = seismos;
        this.time = time;
        this.amp = amp;
    }

    public MicroSecondTimeRange getTime(DataSetSeismogram seis){ return time.getTime(seis); }

    public MicroSecondTimeRange getTime(){ return time.getTime(); }

    public TimeEvent getTimeEvent(){ return time; }

    public UnitRangeImpl getAmp(DataSetSeismogram seis){ return amp.getAmp(seis); }

    public UnitRangeImpl getAmp(){ return amp.getAmp(); }

    public AmpEvent getAmpEvent(){ return amp; }

    private DataSetSeismogram[] seismos;

    private TimeEvent time;

    private AmpEvent amp;

}// ConfigEvent
