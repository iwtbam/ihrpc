package cn.iwtbam.ih.rpc.manager;

import cn.iwtbam.ih.rpc.constant.ZooConstant;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;


public class ServiceRegister {

    private CuratorFramework client = null;

    public ServiceRegister() throws Exception{
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, ZooConstant.MAX_RETRY_TIMES);
        client = CuratorFrameworkFactory.builder()
                .connectString(ZooConstant.CONNECT_STRING)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(ZooConstant.SESSION_TIME_OUT)
                .build();
        client.start();
    }

    public boolean registe(String serviceName, String serverAddress, int port) throws Exception{
        String zooPath = ZooConstant.RPC_ZOO_ROOT_HOME + "/" + serviceName;
        System.out.println(zooPath);
        if(client.checkExists().forPath(zooPath) == null) {
            set(ZooConstant.RPC_ZOO_ROOT_HOME, serviceName, CreateMode.PERSISTENT);
        }
        set(zooPath, serverAddress + ":" + String.valueOf(port), CreateMode.EPHEMERAL);
        return true;
    }

    private boolean set(String path, String node, CreateMode mode) throws Exception{
        String newNode = path + "/" + node;
        if(client.checkExists().forPath(newNode) != null)
            return true;
        client.create()
                .creatingParentsIfNeeded()
                .withMode(mode)
                .forPath(path + "/" + node);
        return true;
    }


    public static void main(String[] args) throws Exception{
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

        ServiceRegister sr = new ServiceRegister();
        sr.registe("cn.iwtbam.ih.rpc.HelloService", "127.0.0.1:", 7777);
        Thread.sleep(1000 * 30);
    }
}
