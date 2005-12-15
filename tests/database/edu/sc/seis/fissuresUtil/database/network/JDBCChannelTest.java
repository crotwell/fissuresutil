/**
 * JDBCChannelTest.java
 * 
 * @author Created by Omnicore CodeGuide
 */
package edu.sc.seis.fissuresUtil.database.network;

import java.sql.SQLException;
import edu.iris.Fissures.IfNetwork.Channel;
import edu.iris.Fissures.network.ChannelIdUtil;
import edu.sc.seis.fissuresUtil.database.JDBCTearDown;
import edu.sc.seis.fissuresUtil.database.NotFound;
import edu.sc.seis.fissuresUtil.mockFissures.IfNetwork.MockChannel;
import edu.sc.seis.fissuresUtil.mockFissures.IfNetwork.MockStationId;

public class JDBCChannelTest extends JDBCTearDown {

    public void setUp() throws SQLException {
        chanTable = new JDBCChannel();
    }

    public void testPutGet() throws SQLException, NotFound {
        int dbid = chanTable.put(chan);
        Channel out = chanTable.get(dbid);
        assertEquals(chan.sampling_info, out.sampling_info);
        assertTrue(ChannelIdUtil.areEqual(chan.get_id(), out.get_id()));
        assertEquals("site lat",
                     chan.my_site.my_location.latitude,
                     out.my_site.my_location.latitude,
                     0.0001f);
        assertEquals("site lon",
                     chan.my_site.my_location.longitude,
                     out.my_site.my_location.longitude,
                     0.0001f);
        assertEquals("station lat",
                     chan.my_site.my_station.my_location.latitude,
                     out.my_site.my_station.my_location.latitude,
                     0.0001f);
        assertEquals("station lon",
                     chan.my_site.my_station.my_location.longitude,
                     out.my_site.my_station.my_location.longitude,
                     0.0001f);
        assertEquals("station Name",
                     chan.my_site.my_station.name,
                     out.my_site.my_station.name);
        assertEquals("network name",
                     chan.my_site.my_station.my_network.name,
                     out.my_site.my_station.my_network.name);
    }

    public void testDoublePut() throws SQLException, NotFound {
        int dbidA = chanTable.put(chan);
        int dbidB = chanTable.put(chan);
        int gottenId = chanTable.getDBId(chan.get_id());
        assertEquals(dbidA, dbidB);
        assertEquals(dbidB, gottenId);
        assertEquals(chan.sampling_info, chanTable.get(dbidA).sampling_info);
        assertTrue(ChannelIdUtil.areEqual(chan.get_id(), chanTable.get(dbidA)
                .get_id()));
    }

    public void testGetByChannelId() throws SQLException, NotFound {
        int dbidA = chanTable.put(chan);
        int dbidB = chanTable.getDBId(chan.get_id());
        int gottenId = chanTable.getDBId(chan.get_id());
        assertEquals(dbidA, dbidB);
        assertEquals(dbidB, gottenId);
    }

    public void testGetAll() throws SQLException, NotFound {
        chanTable.put(chan);
        chanTable.put(other);
        chanTable.put(otherNet);
        Channel[] chans = chanTable.getAllChannels(MockStationId.createStationId());
        assertEquals(2, chans.length);
        Channel[] otherNetChans = chanTable.getAllChannels(MockStationId.createOtherStationId());
        assertEquals(1, otherNetChans.length);
        assertTrue(ChannelIdUtil.areEqual(otherNet.get_id(),
                                          otherNetChans[0].get_id()));
    }

    public void testPutIdThenChannel() throws SQLException {
        int idDbId = chanTable.put(chan.get_id());
        int chanDbId = chanTable.put(chan);
        assertEquals(idDbId, chanDbId);
    }

    public void testPutChannelThenId() throws SQLException {
        int chanDbId = chanTable.put(chan);
        int idDbId = chanTable.put(chan.get_id());
        assertEquals(idDbId, chanDbId);
    }

    public void testPutChanId() throws SQLException, NotFound {
        int dbid = chanTable.put(chan.get_id());
        int dbid2 = chanTable.put(chan.get_id());
        assertEquals(dbid, dbid2);
        assertEquals(dbid, chanTable.getDBId(chan.get_id()));
    }

    public void testPartialSitesOnlyDeletedAfterAllReferingChannelIdsHaveChannelsInserted()
            throws SQLException, NotFound {
        int chanDbId = chanTable.put(chan.get_id());
        int otherDbId = chanTable.put(other.get_id());
        assertEquals(1, chanTable.getSiteTable().size());
        assertEquals(chanDbId, chanTable.put(chan));
        assertEquals(2, chanTable.getSiteTable().size());
        chanTable.getId(otherDbId);
        assertEquals(otherDbId, chanTable.put(other));
        assertEquals(1, chanTable.getSiteTable().size());
    }


    public void testPartialSitesOnlyDeletedAfterAllReferingChannelIdsHaveChannelsInsertedWithDifferentSiteIds()
            throws SQLException, NotFound {
        int chanDbId = chanTable.put(chan.get_id());
        int otherDbId = chanTable.put(otherSiteSameStation.get_id());
        assertEquals(2, chanTable.getSiteTable().size());
        assertEquals(chanDbId, chanTable.put(chan));
        assertEquals(2, chanTable.getSiteTable().size());
        chanTable.getId(otherDbId);
        assertEquals(otherDbId, chanTable.put(otherSiteSameStation));
        assertEquals(2, chanTable.getSiteTable().size());
    }
    
    private JDBCChannel chanTable;

    private static Channel chan = MockChannel.createChannel();

    private static Channel other = MockChannel.createNorthChannel();

    private static Channel otherSiteSameStation = MockChannel.createOtherSiteSameStationChan();
    
    private static Channel otherNet = MockChannel.createOtherNetChan();
}