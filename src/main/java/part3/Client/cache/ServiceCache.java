package part3.Client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCache {

    private static Map<String, List<String>> cache = new HashMap<>();//存储服务名和地址列表

    //添加服务
    public void addServiceToCache(String serviceName,String address) {
        if (cache.containsKey(serviceName)) {
            List<String> addressList = cache.get(serviceName);
            addressList.add(address);
            System.out.println("将name为"+serviceName+"和地址为"+address+"的服务添加到本地缓存中");
        } else {
            List<String> addressList = new ArrayList<>();
            addressList.add(address);
            cache.put(serviceName, addressList);
        }
    }
    //修改服务
    public void replaceServiceAddress(String serviceName,String oldAddress,String newAddress) {
        if (cache.containsKey(serviceName)) {
            List<String> addressList = cache.get(serviceName);
            addressList.remove(oldAddress);
            addressList.add(newAddress);
        } else {
            System.out.println("修改失败，服务不存在");
        }
    }

    //从缓存中删除服务地址
    public void delete(String serviceName,String address) {
        List<String> addressList = cache.get(serviceName);
        addressList.remove(address);
        System.out.println("删除地址服务被调用");
    }

    public List<String> getServiceFromCache(String serviceName) {
        if (!cache.containsKey(serviceName)) {
            System.out.println("缓存中不存该服务");
            return null;
        }
        List<String> addressList = cache.get((serviceName));
        return addressList;
    }

    //添加进入缓存
    public void addServcieToCache(String serviceName, String address) {
    }
}
