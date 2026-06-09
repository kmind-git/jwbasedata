package com.jw.jwbasedata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 基础数据查询服务启动类
 *
 * @author jw
 */
@SpringBootApplication
public class JwBaseDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwBaseDataApplication.class, args);
        System.out.println("==========================================");
        System.out.println("       JwBaseData Service Started!");
        System.out.println("  Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("==========================================");
    }
}
