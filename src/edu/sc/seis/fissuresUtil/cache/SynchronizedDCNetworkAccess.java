/**
 * SynchronizedDCNetworkAccess.java
 *
 * @author Created by Omnicore CodeGuide
 */

package edu.sc.seis.fissuresUtil.cache;

import edu.iris.Fissures.IfNetwork.*;

import edu.iris.Fissures.Area;
import edu.iris.Fissures.AuditElement;
import edu.iris.Fissures.NotImplemented;
import edu.iris.Fissures.Time;
import edu.iris.Fissures.TimeRange;

public class SynchronizedDCNetworkAccess extends ProxyNetworkAccess {

    public SynchronizedDCNetworkAccess(NetworkAccess netAC, NetworkDCOperations netDC){
        super(netAC);
        this.netDC = netDC;
    }

    public void reset(){
        synchronized(netDC){
            super.reset();
        }
    }

    public NetworkAccess getNetworkAccess(){
        synchronized(netDC){
            return super.getNetworkAccess();
        }
    }

    public NetworkAttr get_attributes(){
        synchronized(netDC){
            return super.get_attributes();
        }
    }

    public Station[] retrieve_stations(){
        synchronized(netDC){
            return super.retrieve_stations();
        }
    }

    public Channel[] retrieve_for_station(StationId stationId){
        synchronized(netDC){
            return super.retrieve_for_station(stationId);
        }
    }

    public ChannelId[] retrieve_grouping(ChannelId chanId) throws ChannelNotFound{
        synchronized(netDC){
            return super.retrieve_grouping(chanId);
        }
    }

    public ChannelId[][] retrieve_groupings(){
        synchronized(netDC){
            return super.retrieve_groupings();
        }
    }

    public Channel retrieve_channel(ChannelId chanId) throws ChannelNotFound{
        synchronized(netDC){
            return super.retrieve_channel(chanId);
        }
    }

    public Channel[] retrieve_channels_by_code(String station_code,
                                               String site_code,
                                               String channel_code)
        throws ChannelNotFound{

        synchronized(netDC){
            return super.retrieve_channels_by_code(station_code,
                                                   site_code,
                                                   channel_code);
        }
    }

    public Channel[] locate_channels(Area the_area,
                                     SamplingRange sampling,
                                     OrientationRange orientation){
        synchronized(netDC){
            return super.locate_channels(the_area, sampling, orientation);
        }
    }

    public Instrumentation retrieve_instrumentation(ChannelId chanId, Time time)
        throws ChannelNotFound{

        synchronized(netDC){
            return super.retrieve_instrumentation(chanId, time);
        }
    }

    public Calibration[] retrieve_calibrations(ChannelId chanId, TimeRange timeRange)
        throws ChannelNotFound, NotImplemented{

        synchronized(netDC){
            return super.retrieve_calibrations(chanId, timeRange);
        }
    }

    public TimeCorrection[] retrieve_time_corrections(ChannelId chanId,
                                                      TimeRange timeRange)
        throws ChannelNotFound, NotImplemented{

        synchronized(netDC){
            return super.retrieve_time_corrections(chanId, timeRange);
        }
    }

    public ChannelId[] retrieve_all_channels(int seq_max, ChannelIdIterHolder iter){
        synchronized(netDC){
            return super.retrieve_all_channels(seq_max, iter);
        }
    }

    public AuditElement[] get_audit_trail_for_channel(ChannelId chanId)
        throws ChannelNotFound, NotImplemented{

        synchronized(netDC){
            return super.get_audit_trail_for_channel(chanId);
        }
    }

    public AuditElement[] get_audit_trail() throws NotImplemented{
        synchronized(netDC){
            return super.get_audit_trail();
        }
    }

    protected NetworkDCOperations netDC;
}

