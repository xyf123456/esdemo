package com.bdqn.esdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * ClassName: {@link EsdemoApplication}
 * @author xyf
 * description: Elastic应用的启动类
 * create time: 2020/3/9 17:02
 */
@SpringBootApplication
public class EsdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsdemoApplication.class, args);
    }

}
