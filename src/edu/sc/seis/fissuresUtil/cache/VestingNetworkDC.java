package edu.sc.seis.fissuresUtil.cache;

import edu.iris.Fissures.IfNetwork.NetworkFinder;
import edu.sc.seis.fissuresUtil.namingService.FissuresNamingService;

public class VestingNetworkDC extends AbstractProxyNetworkDC {

    protected VestingNetworkDC(ProxyNetworkDC netDC) {
        super(netDC);
        proxy = netDC;
    }

    public VestingNetworkDC(String dns,
                            String name,
                            FissuresNamingService fisName) {
        this(dns, name, fisName, new ClassicRetryStrategy());
    }

    public VestingNetworkDC(String dns,
                            String name,
                            FissuresNamingService fisName,
                            RetryStrategy handler) {
        this(dns, name, fisName, handler, BulletproofVestFactory.getDefaultNumRetry());
    }

    public VestingNetworkDC(String dns,
                            String name,
                            FissuresNamingService fisName,
                            RetryStrategy handler,
                            int numRetry) {
        this(new RetryNetworkDC(new NSNetworkDC(dns, name, fisName),
                                numRetry,
                                handler));
        this.numRetry = numRetry;
        this.handler = handler;
    }

    public NetworkFinder a_finder() {
        if (finder == null) {
            finder = new VestingNetworkFinder(proxy, numRetry, handler);
        }
        return finder;
    }

    private ProxyNetworkDC proxy;

    private int numRetry;

    private RetryStrategy handler = new ClassicRetryStrategy();
    
    private VestingNetworkFinder finder = null;
}
