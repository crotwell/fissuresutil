package edu.sc.seis.fissuresUtil.mockFissures.IfNetwork;

import edu.iris.Fissures.Location;
import edu.iris.Fissures.IfNetwork.Station;
import edu.iris.Fissures.network.StationImpl;
import edu.sc.seis.fissuresUtil.mockFissures.MockLocation;

public class MockStation {

    public static Station createStation() {
        return new StationImpl(MockStationId.createStationId(),
                               "Test Station",
                               MockLocation.SIMPLE,
                               "Joe",
                               "this is a test",
                               "still, a test",
                               MockNetworkAttr.createNetworkAttr());
    }

    public static Station createRestartedStation() {
        return new StationImpl(MockStationId.createRestartedStationId(),
                               "Test Station",
                               MockLocation.SIMPLE,
                               "Joe",
                               "this is a test",
                               "still, a test",
                               MockNetworkAttr.createNetworkAttr());
    }

    public static Station createOtherStation() {
        return new StationImpl(MockStationId.createOtherStationId(),
                               "Noitats tset",
                               MockLocation.BERLIN,
                               "Frank",
                               "tset a si siht",
                               "tset a ,llits",
                               MockNetworkAttr.createOtherNetworkAttr());
    }

    public static Station[] createMultiSplendoredStations() {
        return createMultiSplendoredStations(4, 5);
    }

    public static Station[] createMultiSplendoredStations(int rows, int columns) {
        Station[] stations = new Station[rows * columns];
        Location[] locations = MockLocation.create(rows, columns);
        for(int i = 0; i < stations.length; i++) {
            stations[i] = new StationImpl(MockStationId.createMultiSplendoredId("MS"
                                                  + i),
                                          "Multi" + i,
                                          locations[i],
                                          "Charlie",
                                          "Grid of Stations",
                                          "Many Station Group",
                                          MockNetworkAttr.createMultiSplendoredAttr());
        }
        return stations;
    }
}