package edu.sc.seis.fissuresUtil.display;
import edu.iris.Fissures.IfNetwork.Channel;
import edu.iris.Fissures.IfNetwork.ChannelId;
import edu.iris.Fissures.network.ChannelIdUtil;
import edu.iris.Fissures.seismogramDC.LocalSeismogramImpl;
import edu.sc.seis.fissuresUtil.chooser.ChannelGrouperImpl;
import edu.sc.seis.fissuresUtil.xml.DataSetSeismogram;
import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import org.apache.log4j.Category;

/**
 * ParticleMotionDisplayThread.java
 *
 *
 * Created: Fri Jul 26 12:42:56 2002
 *
 * @author <a href="mailto:">Srinivasa Telukutla</a>
 * @version
 */

public class ParticleMotionDisplayThread{
    public ParticleMotionDisplayThread (DataSetSeismogram dataSetSeismogram,
					Registrar registrar,
					boolean advancedOption,
					boolean displayButtonPanel,
					ParticleMotionDisplay particleMotionDisplay) {

	this.dataSetSeismogram = new DataSetSeismogram[1];
	this.dataSetSeismogram[0] = dataSetSeismogram;
	this.registrar = registrar;
	this.advancedOption = advancedOption;
	this.displayButtonPanel = displayButtonPanel;
	this.particleMotionDisplay = particleMotionDisplay;
	
    }

    public ParticleMotionDisplayThread(DataSetSeismogram[] dataSetSeismogram,
				       Registrar registrar,
				       boolean advancedOption,
				       boolean displayButtonPanel,
				       ParticleMotionDisplay particleMotionDisplay) {
	
	
	this.dataSetSeismogram = new DataSetSeismogram[dataSetSeismogram.length];
	System.arraycopy(dataSetSeismogram,
			 0,
			 this.dataSetSeismogram,
			 0,
			 dataSetSeismogram.length);
	this.registrar = registrar;
	this.advancedOption = advancedOption;
	this.displayButtonPanel = displayButtonPanel;
	this.particleMotionDisplay = particleMotionDisplay;
    }

    
    public void execute() {
	if(dataSetSeismogram.length <= 1) {
	    dataSetSeismogram = retrieve_seismograms();
	    if(dataSetSeismogram.length < 3){// in case all the components aren't available, the thread fails
		completion = false;
		return;
	    }
	} else {
	    channelGroup = new ChannelId[dataSetSeismogram.length];
	    for(int counter = 0; counter < dataSetSeismogram.length; counter++) {

		channelGroup[counter] = dataSetSeismogram[counter].getRequestFilter().channel_id;
	    }
	}


	//decide whether to form the radioSetPanel or the checkBoxPanel.
	if(displayButtonPanel) {
	    if(!advancedOption) {
		particleMotionDisplay.formRadioSetPanel(channelGroup);
	    } else {
		particleMotionDisplay.formCheckBoxPanel(channelGroup);
	    }

	}
	Color displayColor = selectionColors[particleMotionDisplay.getView().getSelectedParticleMotion().length % selectionColors.length];

	for(int counter = 0; counter < dataSetSeismogram.length; counter++) {

	    for(int subcounter = counter+1; subcounter < dataSetSeismogram.length; subcounter++) {
		
		boolean horizPlane = isHorizontalPlane(dataSetSeismogram[counter].getRequestFilter().channel_id,
						       dataSetSeismogram[subcounter].getRequestFilter().channel_id,
						       dataSetSeismogram[counter].getDataSet());
		if(horizPlane) {
		    particleMotionDisplay.displayBackAzimuth(dataSetSeismogram[counter].getDataSet(), channelGroup[counter]);
		}
		particleMotionDisplay.getView().addParticleMotionDisplay(dataSetSeismogram[counter], 
									 dataSetSeismogram[subcounter], 
									 registrar, 
									 displayColor,
									 DisplayUtils.getOrientationName(channelGroup[counter].channel_code)+"-"+
									 DisplayUtils.getOrientationName(channelGroup[subcounter].channel_code),
									 horizPlane);
		//particleMotionDisplay.updateTimeRange();
		
	    }
	}
	if(displayButtonPanel) {
	    particleMotionDisplay.setInitialButton();
	}
	completion = true;

    }

    public DataSetSeismogram[] retrieve_seismograms() {
	LocalSeismogramImpl seis = null;///dataSetSeismogram[0].retrieveData();
	Date chanIdStartTime = Calendar.getInstance().getTime();
	//	ChannelId[] channelIds = ((edu.sc.seis.fissuresUtil.xml.XMLDataSet)dataSetSeismogram[0].getDataSet()).getChannelIds();

	edu.sc.seis.fissuresUtil.xml.XMLDataSet ds = 
	    (edu.sc.seis.fissuresUtil.xml.XMLDataSet)dataSetSeismogram[0].getDataSet();
	String[] paramNames = ds.getParameterNames();
	LinkedList list = new LinkedList();
	String origChannelIdStr = ChannelIdUtil.toString(seis.getChannelID());
	origChannelIdStr = origChannelIdStr.substring(0, origChannelIdStr.indexOf(seis.getChannelID().channel_code)+2);
	String chanGroupString = edu.sc.seis.fissuresUtil.xml.StdDataSetParamNames.CHANNEL +
	    origChannelIdStr;
	for (int i=0; i<paramNames.length; i++) {
	    if (paramNames[i].startsWith(chanGroupString)) {
	 	logger.debug("found match parameter channelId "+paramNames[i]);
		list.add(((Channel)ds.getParameter(paramNames[i])).get_id());
	    } // end of if (paramNames[i].startsWith(origChannelIdStr))
	    
	} // end of for (int i=0; i<paramNames[i]; i++)
	ChannelId[] channelIds = (ChannelId[])list.toArray(new ChannelId[0]);

	Date chanIdendTime = Calendar.getInstance().getTime();
	logger.debug(" Time for CHan ID is "+(chanIdendTime.getTime() - chanIdStartTime.getTime()));
	
	Date channelGroupStartTime = Calendar.getInstance().getTime();
	ChannelGrouperImpl channelProxy = new ChannelGrouperImpl();
	logger.debug("the original channel_code from the seismogram is "+seis.getChannelID().channel_code);
	channelGroup = channelProxy.retrieve_grouping(channelIds, seis.getChannelID());
	Date channelGroupEndTime = Calendar.getInstance().getTime();
	logger.debug(" Time for Chan Grouper is "+(channelGroupEndTime.getTime() - channelGroupStartTime.getTime()));
	logger.debug("THe length of the channel group is "+channelGroup.length);
	DataSetSeismogram[] seismograms = new DataSetSeismogram[3];
	try {
	    for(int counter = 0; counter < channelGroup.length; counter++) {
		seismograms[counter] = null;//new DataSetSeismogram();
		    /*new DataSetSeismogram(dataSetSeismogram[0].getDataSet().
							     getSeismogram(DisplayUtils.getSeismogramName(channelGroup[counter], 
													  dataSetSeismogram[0].getDataSet(),
													  new edu.iris.Fissures.TimeRange(seis.getBeginTime().getFissuresTime(), seis.getEndTime().getFissuresTime()))), dataSetSeismogram[0].getDataSet());
				       */
		//ChannelIdUtil.toStringNoDates(channelGroup[counter]));
		//registrar.addSeismogram(seismograms[counter]);
		//hAmpRangeConfigRegistrar.addSeismogram(seismograms[counter]);
		
	    }
	    if(channelGroup.length == 0){
		seismograms = new DataSetSeismogram[0];
	    }
	    return seismograms;
	    
	} catch(Exception e) {
	    
	    e.printStackTrace();//strack trace
	}
	return new DataSetSeismogram[0];
	

    }

    public boolean isHorizontalPlane(ChannelId channelIdone, 
				     ChannelId channelIdtwo,
				     edu.sc.seis.fissuresUtil.xml.DataSet dataset) {

	Channel channelOne = ((edu.sc.seis.fissuresUtil.xml.XMLDataSet)dataset).getChannel(channelIdone);
	Channel channelTwo = ((edu.sc.seis.fissuresUtil.xml.XMLDataSet)dataset).getChannel(channelIdtwo);
	if((Math.abs(channelOne.an_orientation.dip) == 0 &&
	    channelTwo.an_orientation.dip == 0))  {
	    return true; 
	}
	return false;
   }	

    /**
     *@returns true if the execution step of the thread finishes correctly.  
     *
     */
    public boolean getCompletion(){ return completion; }
    
    private boolean completion = false;

    private DataSetSeismogram[] dataSetSeismogram = new DataSetSeismogram[0];
    
    private Registrar registrar;

   private boolean advancedOption = false;

    private boolean displayButtonPanel = false;

    private ParticleMotionDisplay  particleMotionDisplay;

    private ChannelId[] channelGroup;

    private static Color[] selectionColors = { new Color(255, 0, 0),  
					       new Color(0, 0, 255),
					       Color.magenta,
					       Color.cyan,
					       Color.white,
					       Color.black};


    static Category logger = 
     Category.getInstance(ParticleMotionDisplayThread.class.getName());
    
}// ParticleMotionDisplayThread
