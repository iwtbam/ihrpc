package cn.iwtbam.ih.rpc.manager;

import cn.iwtbam.ih.rpc.constant.ZooConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

@Slf4j
public class ServiceDiscovery {

    private CuratorFramework client = null;
    private static ServiceDiscovery instance;
    private Object lock;

    public static ServiceDiscovery getInstance() {
        if(instance == null){
            synchronized (ServiceDiscovery.class){
                if(instance == null){
                    instance = new ServiceDiscovery();
                }
                return instance;
            }
        }
        return instance;
    }

    private ServiceDiscovery(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, ZooConstant.MAX_RETRY_TIMES);
        client = CuratorFrameworkFactory.builder()
                .connectString(ZooConstant.CONNECT_STRING)
                .sessionTimeoutMs(ZooConstant.SESSION_TIME_OUT)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    public CuratorFrameworkState start() {
        client.start();
        return client.getState();
    }

    public CuratorFrameworkState close() {
        client.close();
        return client.getState();
    }

    public String getServiceAddress(String serviceName){
        String zooPath = ZooConstant.RPC_ZOO_ROOT_HOME + "/" + serviceName;

        List<String> ips = null;
        try{
            ips = client.getChildren().forPath(zooPath);
        }catch (Exception e) {
            log.error(e.toString());
        }finally {
            if(CollectionUtils.isEmpty(ips))
                return null;
            return ips.get(new Random().nextInt(ips.size()));
        }
    }

    public static void main(String[] args) throws Exception{
        ServiceDiscovery sd = new ServiceDiscovery();
        System.out.println(sd.getServiceAddress("cn.iwtbam.ih.rpc.HelloService"));
    }
}
