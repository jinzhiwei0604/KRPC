package part3.Client.serviceCenter.zkWatcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import part3.Client.cache.ServiceCache;

public class WatchZK {

    private CuratorFramework client;

    ServiceCache cache;

    public WatchZK(CuratorFramework client,ServiceCache cache) {
        this.client = client;
        this.cache = cache;
    }

    public void watchToUpdate(String path) {
        CuratorCache curatorCache =  CuratorCache.build(client, "/");//用于监听节点变化的API，这里监听根路径
        //注册监听器
        curatorCache.listenable().addListener((new CuratorCacheListener() {
            //Type:事件类型（枚举）第二个参数：节点更新前的状态、数据 第三个参数：节点更新后的状态、数据
            @Override
            public void event(Type type, ChildData childData, ChildData childData1) {
                switch (type.name()) {
                    case "NODE_CREATED":// 监听器第一次执行时节点存在也会触发次事件
                        String[] pathList= pasrePath(childData1);
                        if (pathList.length <= 2) {
                            break;
                        } else {
                            String serviceName=pathList[1];
                            String address=pathList[2];
                            //将新注册的服务加入到本地缓存中
                            cache.addServcieToCache(serviceName,address);
                        }
                    case "NODE_CHANGED": // 节点更新
                        if (childData.getData() != null) {
                            System.out.println("修改前的数据: " + new String(childData.getData()));
                        } else {
                            System.out.println("节点第一次赋值!");
                        }
                        String[] oldPathList=pasrePath(childData);
                        String[] newPathList=pasrePath(childData1);
                        cache.replaceServiceAddress(oldPathList[1],oldPathList[2],newPathList[2]);
                        System.out.println("修改后的数据: " + new String(childData1.getData()));
                        break;
                    case "NODE_DELETED": // 节点删除
                        String[] pathList_d= pasrePath(childData);
                        if(pathList_d.length<=2) break;
                        else {
                            String serviceName=pathList_d[1];
                            String address=pathList_d[2];
                            //将新注册的服务加入到本地缓存中
                            cache.delete(serviceName,address);
                        }
                        break;
                    default:
                        break;
                }
            }
        }));
        curatorCache.start();
        
    }

    //解析节点对应地址
    public String[] pasrePath(ChildData childData){
        //获取更新的节点的路径
        String path=new String(childData.getPath());
        //按照格式 ，读取
        return path.split("/");
    }
}
