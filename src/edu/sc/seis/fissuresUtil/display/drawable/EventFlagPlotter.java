package edu.sc.seis.fissuresUtil.display.drawable;

import edu.iris.Fissures.IfEvent.EventAccess;
import edu.iris.Fissures.IfEvent.EventAccessOperations;
import edu.iris.Fissures.IfEvent.Origin;
import edu.iris.Fissures.model.MicroSecondDate;
import edu.sc.seis.fissuresUtil.display.PlottableDisplay;
import edu.sc.seis.fissuresUtil.display.registrar.AmpEvent;
import edu.sc.seis.fissuresUtil.display.registrar.TimeEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * EventFlagPlotter.java
 *
 *
 * Created: Fri Mar 28 14:20:17 2003
 *
 * @author <a href="mailto:">Srinivasa Telukutla</a>
 * @version
 */

public class EventFlagPlotter implements Plotter{
    public EventFlagPlotter (PlottableDisplay plottableDisplay,
                             EventAccessOperations eventAccess){

        this.plottableDisplay = plottableDisplay;
        this.eventAccess = eventAccess;
    }

    public void draw(Graphics2D canvas, java.awt.Dimension size, TimeEvent currentTime, AmpEvent currentAmp) {
        //        addEventInfo(canvas);
        drawEvents(canvas);
    }

    public void toggleVisibility() {

    }

    public void setVisibility(boolean b) {

    }

    public void addEventInfo(Graphics g) {
        //   int[] rows = new int[eventAccess.length];
        //         int[] cols = new int[eventAccess.length];
        //         for(int  counter = 0; counter < eventAccess.length; counter++) {
        //             rows[counter] = getEventRow(eventAccess[counter]);
        //             cols[counter] = getEventColumn(eventAccess[counter]);
        //         }
        //         // drawEvents(eventAccess, rows, cols, g);
    }



    private void drawEvents(Graphics g) {
        // get new graphics to avoid messing up original
        Graphics2D newG = (Graphics2D)g.create();

        if(g != plottableDisplay.getCurrentImageGraphics()) {
            newG.translate(PlottableDisplay.LABEL_X_SHIFT,
                           PlottableDisplay.TITLE_Y_SHIFT);
            newG.clipRect(0, 0,
                          plottableDisplay.plot_x/plottableDisplay.plotrows,
                          plottableDisplay.plot_y +(plottableDisplay.plotoffset * (plottableDisplay.plotrows-1)));
        }

        int xShift = plottableDisplay.plot_x/plottableDisplay.plotrows;
        int eventrow = getEventRow(eventAccess);
        int eventcolumn = getEventColumn(eventAccess);

        java.awt.geom.AffineTransform original = newG.getTransform();
        java.awt.geom.AffineTransform affine = newG.getTransform();

        affine.concatenate(affine.getTranslateInstance(-1*xShift*eventrow,
                                                       plottableDisplay.plot_y/2+plottableDisplay.plotoffset*eventrow));

        java.awt.geom.AffineTransform tempAffine = newG.getTransform();
        tempAffine.concatenate(tempAffine.getTranslateInstance(-1*xShift*eventrow,
                                                               plottableDisplay.plot_y/2+plottableDisplay.plotoffset*eventrow));
        curreventrow = eventrow;
        curreventcolumn = eventcolumn;
        //tempAffine.concatenate(tempAffine.getScaleInstance(1, -1));
        //tempAffine.concatenate(tempAffine.getRotateInstance(-Math.PI, xShift*eventrow + eventcolumn, - 20));
        newG.setTransform(tempAffine);
        newG.setPaint(Color.black);

        newG.drawString(this.eventName, xShift*eventrow + eventcolumn,  -25);

        //account for graphics y positive down
        affine.concatenate(affine.getScaleInstance(1, -1));

        newG.setTransform(affine);
        newG.setPaint(this.currentColor);

        AlphaComposite originalComposite = (AlphaComposite)newG.getComposite();
        AlphaComposite newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                           .4f);

        newG.setComposite(newComposite);

        newG.drawRect( xShift*eventrow + eventcolumn - 5, 20, eventName.length() * 8, 20);
        newG.fillRect( xShift*eventrow + eventcolumn - 5, 20, eventName.length() * 8,20);
        newG.drawLine(xShift*eventrow + eventcolumn - 5,20,xShift*eventrow + eventcolumn - 5, 0);
        newG.dispose();
    }


    public boolean isSelected(int x, int y) {
        System.out.println("called the method isSelected on event "+this.eventName);
        System.out.println("** x = "+x+" y= "+y);
         int rx = curreventcolumn + PlottableDisplay.LABEL_X_SHIFT;
        int ry = plottableDisplay.plot_y/2+plottableDisplay.plotoffset*curreventrow;
        System.out.println(" rx = "+rx+" ry = "+ry);

        if(x >= rx && x <= (rx + eventName.length() * 8) &&
           y >= ry && y <= (ry + 20)) {
            System.out.println("** true");
            return true;
        }
        System.out.println("** false");
        return false;
    }

    public void setSelected(boolean value) {
        if(value) {
            this.currentColor = ACTIVECOLOR;
        } else {
            this.currentColor = INACTIVECOLOR;
        }

    }


     private int getEventRow(EventAccessOperations eventAccess) {
    try {
        if(eventOrigin == null) {
            eventOrigin = eventAccess.get_preferred_origin();
        }
        edu.iris.Fissures.Time time = eventOrigin.origin_time;
        System.out.println("ORIGIN TIME: "+new MicroSecondDate(time));

        long microSeconds =  ( new MicroSecondDate(time)).getMicroSecondTime();
        float colhours = microSeconds/(1000 * 1000 * 60 * 60);
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(microSeconds/1000);
        System.out.println("The date rebuilt ORIGIN TIME: "+new MicroSecondDate(date));
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("THe hour of the day is "+hours);
        return hours/2;

    } catch(Exception e) {

    }
    return -1;

    }


     private int getEventColumn(EventAccessOperations eventAccess) {
    try {
        if(eventOrigin == null) {
            eventOrigin = eventAccess.get_preferred_origin();
        }

        edu.iris.Fissures.Time time = eventOrigin.origin_time;
        if(this.eventName == null) {
            this.eventName = eventAccess.get_attributes().name;
        }
        long microSeconds =  ( new MicroSecondDate(time)).getMicroSecondTime();
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(microSeconds/1000);
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        float colhours = (minutes * 60 + seconds) / (float) (60 * 60);

        int rowvalue = 24/plottableDisplay.plotrows;
        int plotwidth = plottableDisplay.plot_x/plottableDisplay.plotrows;
        int  rtnvalue = (int)(((float)plotwidth/rowvalue) * colhours);

        return rtnvalue;

    } catch(Exception e) {

    }
    return -1;

    }

     private int curreventrow;

     private int curreventcolumn;

    private String eventName;

    private PlottableDisplay plottableDisplay;

    private EventAccessOperations eventAccess;

    private Origin eventOrigin;

    private static final Color ACTIVECOLOR = Color.green;

    private static final Color INACTIVECOLOR = Color.red;

    private Color currentColor = INACTIVECOLOR;

}// EventFlagPlotter
