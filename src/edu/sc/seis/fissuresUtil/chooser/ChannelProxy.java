package edu.sc.seis.fissuresUtil.chooser;

import edu.sc.seis.fissuresUtil.namingService.*;
import edu.iris.Fissures.IfNetwork.*;

import org.omg.CORBA.*;

/**
 * ChannelProxy.java
 *
 *
 * Created: Thu Jun 27 09:01:49 2002
 *
 * @author <a href="mailto:">Srinivasa Telukutla</a>
 * @version
 */

public class ChannelProxy implements ChannelGrouper{
    public ChannelProxy (){
	
    }

    public  Channel[] retrieve_grouping(org.omg.CORBA_2_3.ORB orb, ChannelId channelId) {
	Channel[] group;
	try {
	    FissuresNamingServiceImpl fissuresNamingService = new FissuresNamingServiceImpl(orb);
	    NetworkDC[] networkReferences = fissuresNamingService.getNetworkDCObjects();
	    // ChannelId channelId = channel.get_id();
	    
	    for(int counter = 0; counter < networkReferences.length; counter++) {
		try {
		     NetworkAccess networkAccess = networkReferences[counter].a_finder().retrieve_by_id(channelId.network_id);
		    Channel channel = networkAccess.retrieve_channel(channelId);
		    Channel[] channels = networkAccess.retrieve_for_station(channel.my_site.my_station.get_id());
		    ChannelGrouperImpl channelGrouperImpl = new ChannelGrouperImpl();
		    group = channelGrouperImpl.retrieve_grouping(channels, channel);
		    if(group.length == 3) return group;
		} catch(ChannelNotFound e) {
		    continue;
		}
	    }
	} catch(Exception e) {
	    e.printStackTrace();
	    return new Channel[0];
	}
	    return null;
    }
    

 
    
   
}// ChannelProxy
