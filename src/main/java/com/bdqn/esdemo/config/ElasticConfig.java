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
 * ClassName: ElasticConfig
 * create by:  xyf
 * description: TODO Elasticsearch核心配置类
 * create time: 2019/10/8 0008 下午 10:59
 */
@Configuration
public class ElasticConfig {


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
