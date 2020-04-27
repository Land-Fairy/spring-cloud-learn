package com.example.myrule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MyRule extends AbstractLoadBalancerRule {
    private static Logger log = LoggerFactory.getLogger(MyRule.class);

    /* 下一Server 序号 */
    private AtomicInteger nextServerCyclicCounter;
    /* 当前服务使用次数 */
    private AtomicInteger curSvcUseCount;
    /* 每个服务限制次数 */
    private int limitCount = 5;
    public MyRule() {
        nextServerCyclicCounter = new AtomicInteger(0);
        curSvcUseCount = new AtomicInteger(0);
    }

    public MyRule(ILoadBalancer lb) {
        this();
        setLoadBalancer(lb);
    }
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        }

        Server server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            List<Server> reachableServers = lb.getReachableServers();
            List<Server> allServers = lb.getAllServers();
            int upCount = reachableServers.size();
            int serverCount = allServers.size();

            if ((upCount == 0) || (serverCount == 0)) {
                log.warn("No up servers available from load balancer: " + lb);
                return null;
            }

            int nextServerIndex = incrementAndGetModulo(serverCount);
            server = allServers.get(nextServerIndex);

            if (server == null) {
                /* Transient. */
                Thread.yield();
                continue;
            }

            if (server.isAlive() && (server.isReadyToServe())) {
                return (server);
            }

            // Next.
            server = null;
        }

        if (count >= 10) {
            log.warn("No available alive servers after 10 tries from load balancer: "
                    + lb);
        }
        return server;
    }

    private int incrementAndGetModulo(int modulo) {
        for (;;) {
            int curCount = curSvcUseCount.get();
            if (curCount < limitCount) {
                curSvcUseCount.set(curCount + 1);
                return nextServerCyclicCounter.get();
            }

            int current = nextServerCyclicCounter.get();
            curSvcUseCount.set(0);
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }

    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }
}
