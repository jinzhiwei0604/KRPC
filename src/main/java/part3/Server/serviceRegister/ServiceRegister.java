package part3.Server.serviceRegister;

import java.net.InetSocketAddress;

public interface ServiceRegister {

    void register(String serviceName, InetSocketAddress serviceAddress);
}
