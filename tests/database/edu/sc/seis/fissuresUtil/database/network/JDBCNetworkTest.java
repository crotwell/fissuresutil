/**
 * JDBCNetworkTest.java
 *
 * @author Created by Omnicore CodeGuide
 */

package edu.sc.seis.fissuresUtil.database.network;

import edu.iris.Fissures.IfNetwork.NetworkAttr;
import edu.iris.Fissures.IfNetwork.NetworkId;
import edu.sc.seis.fissuresUtil.database.NotFound;
import edu.sc.seis.mockFissures.IfNetwork.MockNetworkAttr;
import edu.sc.seis.mockFissures.IfNetwork.MockNetworkId;
import java.sql.SQLException;
import junit.framework.TestCase;

public class JDBCNetworkTest extends TestCase{

    public void testDoublePut() throws SQLException, NotFound{
        JDBCNetwork net = new JDBCNetwork();
        NetworkAttr attr = MockNetworkAttr.createNetworkAttr();
        int dbidA = net.put(attr.get_id());
        int dbidB = net.put(attr);
        int gottenId = net.getDBId(attr.get_id());
        assertEquals(dbidA, dbidB);
        assertEquals(dbidB, gottenId);
    }
}

