package edu.sc.seis.fissuresUtil.display.borders;

import edu.iris.Fissures.model.UnitRangeImpl;
import edu.sc.seis.fissuresUtil.display.SeismogramDisplay;
import edu.sc.seis.fissuresUtil.display.UnitDisplayUtil;

public class AmpBorder extends UnitRangeBorder{
    public AmpBorder(SeismogramDisplay disp){ this(disp, LEFT); }

    public AmpBorder(SeismogramDisplay disp, int side){
        super(side, ASCENDING);
        this.disp = disp;
    }

    public UnitRangeImpl getRange() {
        last = disp.getAmpConfig().getAmp();
        return last;
    }

    public String getTitle(){
        return "Amplitude (" + UnitDisplayUtil.getNameForUnit(last.getUnit()) + ")";
    }


    private UnitRangeImpl last;
    private SeismogramDisplay disp;
}
