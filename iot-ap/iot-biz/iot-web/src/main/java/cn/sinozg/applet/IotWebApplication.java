package cn.sinozg.applet;

import cn.sinozg.applet.quartz.config.EnableQuartzRegister;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * iot 项目
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-08-20 18:28
 */
@EnableScheduling
@EnableQuartzRegister
@SpringBootApplication
@MapperScan("cn.sinozg.applet.**.mapper")
public class IotWebApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(IotWebApplication.class, args);
        System.out.println("🍎🍎🍎🍎🍎🍎   项目启动成功   🍎🍎🍎🍎🍎🍎");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(IotWebApplication.class);
    }
}
