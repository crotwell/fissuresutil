package edu.sc.seis.fissuresUtil.display.registrar;
import edu.iris.Fissures.model.UnitImpl;
import edu.iris.Fissures.model.UnitRangeImpl;
import edu.sc.seis.fissuresUtil.display.DisplayUtils;
import edu.sc.seis.fissuresUtil.display.SeismogramIterator;
import edu.sc.seis.fissuresUtil.xml.DataSetSeismogram;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 * RMeanAmpConfig.java
 *
 *
 * Created: Thu Oct  3 09:46:23 2002
 *
 * @author <a href="mailto:">Charlie Groves</a>
 * @version
 */

public class RMeanAmpConfig extends BasicAmpConfig {
    public RMeanAmpConfig(){}

    public RMeanAmpConfig(DataSetSeismogram[] seismos){
        super(seismos);
    }

    protected AmpEvent recalculateAmp(){
        DataSetSeismogram[] seis = getSeismograms();
        double range = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < seis.length; i++){
            UnitRangeImpl current = getAmpData(seis[i]).getRange();
            if(current != null &&
               current.getMaxValue() - current.getMinValue() > range){
                range = current.getMaxValue() - current.getMinValue();
            }
        }
        DataSetSeismogram[] seismos = getSeismograms();
        UnitRangeImpl[] amps = new UnitRangeImpl[seismos.length];
        for(int i = 0; i < seismos.length; i++){
            amps[i] = setRange(getAmpData(seismos[i]).getRange(),range);
        }
        return new AmpEvent(seismos, amps);
    }

    protected boolean setAmpRange(DataSetSeismogram seismo){
        AmpConfigData data = getAmpData(seismo);
        SeismogramIterator it = data.getIterator();
        if ( !it.hasNext()) {
            return data.setRange(DisplayUtils.ZERO_RANGE);
        }
        double[] minMaxMean = it.minMaxMean();
        double meanDiff;
        double maxToMeanDiff = Math.abs(minMaxMean[2] - minMaxMean[1]);
        double minToMeanDiff = Math.abs(minMaxMean[2] - minMaxMean[0]);
        if(maxToMeanDiff > minToMeanDiff){
            meanDiff = maxToMeanDiff;
        }else{
            meanDiff = minToMeanDiff;
        }
        double min = minMaxMean[2] - meanDiff;
        double max = minMaxMean[2] + meanDiff;
        UnitImpl seisUnit = it.getSeismograms()[0].getUnit();
        return data.setRange(new UnitRangeImpl(min, max, seisUnit));
    }

    private UnitRangeImpl setRange(UnitRangeImpl currRange, double range){
        double middle = currRange.getMaxValue() - (currRange.getMaxValue() - currRange.getMinValue())/2;
        return new UnitRangeImpl(middle - range/2, middle + range/2, currRange.getUnit());
    }

    private static final Logger logger = Logger.getLogger(RMeanAmpConfig.class);
}// RMeanAmpConfig
