package edu.sc.seis.fissuresUtil.mseed;

import java.io.IOException;
import java.util.List;

import edu.iris.Fissures.FissuresException;
import edu.iris.Fissures.model.SamplingImpl;
import edu.iris.Fissures.model.TimeInterval;
import edu.iris.Fissures.model.UnitImpl;
import edu.iris.Fissures.seismogramDC.LocalSeismogramImpl;
import edu.iris.dmc.seedcodec.CodecException;
import edu.sc.seis.fissuresUtil.display.SimplePlotUtil;
import edu.sc.seis.fissuresUtil.hibernate.PlottableChunk;
import edu.sc.seis.fissuresUtil.mockFissures.IfSeismogramDC.MockSeismogram;
import edu.sc.seis.seisFile.mseed.DataRecord;
import edu.sc.seis.seisFile.mseed.SeedFormatException;
import junit.framework.TestCase;

public class TestFissuresConvert extends TestCase {

    public void testSamplingToMSeed() {
        TimeInterval sec = new TimeInterval(1, UnitImpl.SECOND);
        SamplingImpl samp = new SamplingImpl(1, sec);
        short[] out = FissuresConvert.calcSeedMultipilerFactor(samp);
        assertEquals(samp + " factor", (short)32766, out[0]);
        assertEquals(samp + " multi", (short)-32766, out[1]);
        samp = new SamplingImpl(1, (TimeInterval)sec.multiplyBy(1800));
        SamplingImpl[] sampleList = new SamplingImpl[] {new SamplingImpl(1,
                                                                         (TimeInterval)sec.multiplyBy(1800)),
                                                        new SamplingImpl(20,
                                                                         (TimeInterval)sec.multiplyBy(1)),
                                                        new SamplingImpl(1,
                                                                         (TimeInterval)sec.multiplyBy(0.05)),
                                                        new SamplingImpl(10,
                                                                         (TimeInterval)sec.multiplyBy(1)),
                                                        new SamplingImpl(1,
                                                                         (TimeInterval)sec.multiplyBy(0.05001)),
                                                        new SamplingImpl(100,
                                                                         (TimeInterval)sec.multiplyBy(1)),
                                                        new SamplingImpl(1,
                                                                         (TimeInterval)sec.multiplyBy(100))};
        for(int i = 0; i < sampleList.length; i++) {
            out = FissuresConvert.calcSeedMultipilerFactor(sampleList[i]);
            SamplingImpl converted = FissuresConvert.convertSampleRate(out[1],
                                                                       out[0]);
            assertEquals(sampleList[i] + " sampling converted=" + converted,
                         sampleList[i].getPeriod().getValue(UnitImpl.SECOND),
                         converted.getPeriod().getValue(UnitImpl.SECOND),
                         0.00001);
        }
        samp = new SamplingImpl(1, (TimeInterval)sec.multiplyBy(1800));
        out = FissuresConvert.calcSeedMultipilerFactor(samp);
        assertEquals(samp + " factor", (short)-32400, out[0]);
        assertEquals(samp + " multi", (short)18, out[1]);
        SamplingImpl converted = FissuresConvert.convertSampleRate(out[1], out[0]);
        assertEquals(samp + " sampling converted=" + converted,
                     samp.getPeriod().getValue(UnitImpl.SECOND),
                     converted.getPeriod().getValue(UnitImpl.SECOND),
                     0.00001);
    }
    
    public void testPlottableToMSeed() throws CodecException, IOException, SeedFormatException, FissuresException {
        LocalSeismogramImpl[] seis = new LocalSeismogramImpl[] {MockSeismogram.createDelta()};
        List<PlottableChunk> chunk = SimplePlotUtil.makePlottables(seis, 6000);
        List<DataRecord> drList = FissuresConvert.toMSeed(chunk, seis[0].getChannelID());
        List<PlottableChunk> outChunk = FissuresConvert.toPlottable(drList);
        assertEquals(chunk.get(0).getBeginPixel(), outChunk.get(0).getBeginPixel());
        PlottableChunk lastChunk = chunk.get(chunk.size()-1);
        PlottableChunk lastOutChunk = outChunk.get(outChunk.size()-1);
        assertEquals(lastChunk.getBeginPixel()+lastChunk.getNumPixels(), lastOutChunk.getBeginPixel()+lastOutChunk.getNumPixels());
    }
}
