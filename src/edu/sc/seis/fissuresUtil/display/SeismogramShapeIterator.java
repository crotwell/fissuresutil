package edu.sc.seis.fissuresUtil.display;

import edu.iris.Fissures.model.UnitRangeImpl;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
/**
 * SeismogramShapeIterator encapsulates a single plotting of a SeismogramShape.
 *
 *
 *
 * Created: Sun Jul 28 21:38:56 2002
 *
 * @author <a href="mailto:">Charlie Groves</a>
 * @version
 */

public class SeismogramShapeIterator implements PathIterator {
    public SeismogramShapeIterator(MicroSecondTimeRange time, UnitRangeImpl amp,
                                   Dimension size){
        this.time = time;
        this.amp = amp;
        this.size = size;
    }

    public boolean isDraggedFrom(SeismogramShapeIterator otherIterator){
        if(otherIterator == null ||
           !size.equals(otherIterator.getSize()) ||
           !time.getInterval().equals(otherIterator.getTime().getInterval())){
            return false;
        }
        return true;
    }

    //checks if amp changes between these two plot info objects are within
    //one pixel
    public boolean hasSimilarAmp(SeismogramShapeIterator otherIterator){
        if(otherIterator != null){
            double thisMax = amp.getMaxValue();
            double thisMin = amp.getMinValue();
            double thisRange = thisMax - thisMin;
            double pixelHeightPercentage = 1/(double)size.height;
            double otherMax = otherIterator.getAmp().getMaxValue();
            double otherMin = otherIterator.getAmp().getMinValue();
            if(Math.abs(otherMax - thisMax)/thisRange <= pixelHeightPercentage ||
               Math.abs(otherMin - thisMin)/thisRange <= pixelHeightPercentage){
                return true;
            }
        }
        return false;
    }

    public void copyBasicInfo(SeismogramShapeIterator iterator){
        setPointsPerPixel(iterator.getPointsPerPixel());
        setBaseSeisPoint(iterator.getBaseSeisPoint());
        setPoints(iterator.getPoints());
    }

    public Dimension getSize(){
        return size;
    }

    public void setTime(MicroSecondTimeRange time){
        this.time = time;
    }

    public MicroSecondTimeRange getTime(){
        return time;
    }

    public void setAmp(UnitRangeImpl amp){
        this.amp = amp;
    }

    public UnitRangeImpl getAmp(){
        return amp;
    }

    public void setPointsPerPixel(){
        setPointsPerPixel((seisPoints[1] - seisPoints[0])/(double)size.width);
    }

    public void setPointsPerPixel(double pointsPerPixel){
        this.pointsPerPixel = pointsPerPixel;
    }

    public double getPointsPerPixel()
    {
        return pointsPerPixel;
    }

    public void setDrawnPixels(int[] drawnPixels){
        this.drawnPixels = drawnPixels;
        startIndex = drawnPixels[0];
        endIndex = drawnPixels[1];
        currentIndex = startIndex;
    }

    public int[] getDrawnPixels(){
        return drawnPixels;
    }

    public void setPoints(int[][] points){
        this.points = points;
    }

    public int[][] getPoints(){
        return points;
    }

    public void setSeisPoints(int[] seisPoints){
        this.seisPoints = seisPoints;
    }

    public int[] getSeisPoints(){
        return seisPoints;
    }

    public void setLeftoverPixels(double leftoverPixels){
        this.leftoverPixels = leftoverPixels;
    }

    public double getLeftoverPixels(){
        return leftoverPixels;
    }

    public void setBaseSeisPoint(){
        setBaseSeisPoint(seisPoints[0]);
    }

    public void setBaseSeisPoint(int baseSeisPoint){
        this.baseSeisPoint = baseSeisPoint;
    }

    public int getBaseSeisPoint(){
        return baseSeisPoint;
    }

    public void setTotalShift(int totalShift){
        this.totalShift = totalShift;
    }

    public int getTotalShift(){
        return totalShift;
    }

    public void setAT(AffineTransform at){
        this.at = at;
    }

    public AffineTransform getAt(){
        return at;
    }

    public void next(){
        currentIndex++;
    }

    public int getWindingRule(){
        return WIND_NON_ZERO;
    }

    public boolean isDone(){
        if(currentIndex == endIndex){
            return true;
        }
        return false;
    }

    public int currentSegment(float[] coordinates){
        int i = 0;
        if(min){
            i = 1;
            currentIndex--;
        }
        min = !min;
        coordinates[0] = currentIndex;
        coordinates[1] = points[i][currentIndex];
        if(at != null){
            at.transform(coordinates, 0, coordinates, 0, 1);
        }
        if(currentIndex == startIndex){
            return SEG_MOVETO;
        }else{
            return SEG_LINETO;
        }
    }

    public int currentSegment(double[] coordinates){
        int i = 0;
        if(min){
            i = 1;
            currentIndex--;
        }
        min = !min;
        coordinates[0] = currentIndex;
        coordinates[1] = points[i][currentIndex];
        if(at != null){
            at.transform(coordinates, 0, coordinates, 0, 1);
        }
        if(currentIndex == startIndex){
            return SEG_MOVETO;
        }else{
            return SEG_LINETO;
        }
    }

    private boolean min = false;

    private int totalShift = 0;

    private double leftoverPixels = 0;

    private int baseSeisPoint;

    private int[] seisPoints;

    private int[][] points;

    private int[] drawnPixels;

    private double pointsPerPixel;

    private UnitRangeImpl amp;

    private MicroSecondTimeRange time;

    private Dimension size;

    protected int startIndex, endIndex, currentIndex;

    protected AffineTransform at;


}// SeismogramShapeIterator
