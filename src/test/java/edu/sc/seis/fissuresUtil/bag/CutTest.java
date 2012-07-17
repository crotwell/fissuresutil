package edu.sc.seis.fissuresUtil.bag;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;

import edu.iris.Fissures.FissuresException;
import edu.iris.Fissures.model.MicroSecondDate;
import edu.iris.Fissures.model.SamplingImpl;
import edu.iris.Fissures.seismogramDC.LocalSeismogramImpl;
import edu.sc.seis.fissuresUtil.mockFissures.IfSeismogramDC.MockSeismogram;

// JUnitDoclet end import
/**
 * Generated by JUnitDoclet, a tool provided by ObjectFab GmbH under LGPL.
 * Please see www.junitdoclet.org, www.gnu.org and www.objectfab.de for
 * informations about the tool, the licence and the authors.
 */
public class CutTest extends TestCase {

    LocalSeismogramImpl seis;

    edu.iris.Fissures.Time time = new edu.iris.Fissures.Time("20001231T235959.000Z",
                                                             -1);

    int[] data;
    static {
        BasicConfigurator.configure();
    }

    public CutTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        data = new int[101];
        for(int i = 0; i < data.length; i++) {
            data[i] = 0;
            // create test data creates 20sps data
            if(i % 20 == 0) {
                data[i] = 1;
            } // end of if ()
        } // end of for ()
        seis = MockSeismogram.createTestData("est", data, time);
    }

    protected void tearDown() throws Exception {
        seis = null;
        data = null;
        super.tearDown();
    }

    public void testShiftBegin() throws Exception {
        MicroSecondDate begin = new MicroSecondDate(time);
        MicroSecondDate end = new MicroSecondDate(seis.getEndTime());
        SamplingImpl samp = (SamplingImpl)seis.sampling_info;
        begin = begin.add(samp.getPeriod());
        Cut cut = new edu.sc.seis.fissuresUtil.bag.Cut(begin, end);
        LocalSeismogramImpl out = cut.apply(seis);
        assertTrue("Num points is one less " + out.num_points + " "
                + seis.num_points, out.num_points == seis.num_points - 1);
    }

    public void testShiftEnd() throws Exception {
        MicroSecondDate begin = new MicroSecondDate(time);
        MicroSecondDate end = new MicroSecondDate(seis.getEndTime());
        SamplingImpl samp = (SamplingImpl)seis.sampling_info;
        end = end.subtract(samp.getPeriod());
        Cut cut = new edu.sc.seis.fissuresUtil.bag.Cut(begin, end);
        LocalSeismogramImpl out = cut.apply(seis);
        assertEquals("Num points is one less " + out.num_points + " "
                + seis.num_points, out.num_points , seis.num_points - 1);
    }

    public void testOffBegin() throws Exception {
        MicroSecondDate begin = new MicroSecondDate(time);
        MicroSecondDate end = new MicroSecondDate(seis.getEndTime());
        SamplingImpl samp = (SamplingImpl)seis.sampling_info;
        begin = begin.subtract(samp.getPeriod());
        Cut cut = new edu.sc.seis.fissuresUtil.bag.Cut(begin, end);
        LocalSeismogramImpl out = cut.apply(seis);
        assertEquals("Num points is equal " + out.num_points + " "
                + seis.num_points, out.num_points , seis.num_points );
    }

    public void testOffEnd() throws Exception {
        MicroSecondDate begin = new MicroSecondDate(time);
        MicroSecondDate end = new MicroSecondDate(seis.getEndTime());
        SamplingImpl samp = (SamplingImpl)seis.sampling_info;
        end = end.add(samp.getPeriod());
        Cut cut = new edu.sc.seis.fissuresUtil.bag.Cut(begin, end);
        LocalSeismogramImpl out = cut.apply(seis);
        assertEquals("Num points is equal  " + out.num_points + " "
                + seis.num_points, out.num_points , seis.num_points );
    }
    
    public void testExactSeisTimes() throws FissuresException {
        MicroSecondDate begin = new MicroSecondDate(time);
        MicroSecondDate end = new MicroSecondDate(seis.getEndTime());
        Cut cut = new edu.sc.seis.fissuresUtil.bag.Cut(begin, end);
        LocalSeismogramImpl out = cut.apply(seis);
        assertEquals("Num points is same less " + out.num_points + " "
                + seis.num_points, out.num_points , seis.num_points );
    }
}
