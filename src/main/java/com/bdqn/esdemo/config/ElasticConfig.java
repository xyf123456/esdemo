package com.bdqn.esdemo.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ClassName: {@link ElasticConfig}
 *
 * @author xyf
 * description:  Elasticsearch核心配置类
 * create time: 2020/3/9 17:01
 */
@Configuration
public class ElasticConfig {


    /**
     * description: 得到TransportClient组件（设置集群信息）
     * @date  2020/3/9 17:01
     * @param
     * @return org.elasticsearch.client.transport.TransportClient
     */
    @Bean
    public TransportClient client() throws UnknownHostException {
        InetSocketTransportAddress node = new InetSocketTransportAddress(
                InetAddress.getByName("127.0.0.1"), 9300
        );

        InetSocketTransportAddress node1 = new InetSocketTransportAddress(
                InetAddress.getByName("127.0.0.1"), 9301
        );
        InetSocketTransportAddress node2 = new InetSocketTransportAddress(
                InetAddress.getByName("127.0.0.1"), 9302
        );

        Settings settings = Settings.builder()
                .put("cluster.name", "wali")
                .build();
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(node);
        client.addTransportAddress(node1);
        client.addTransportAddress(node2);
        return client;
    }
}
