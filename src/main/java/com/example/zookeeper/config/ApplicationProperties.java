package com.example.zookeeper.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Data
public class ApplicationProperties {
    private final static Logger logger = (Logger) LoggerFactory.getLogger(ApplicationProperties.class);
    private final static String configFile = "application.properties";
    private final static String applicationNameKey = "application.name";
    private final static String serverPortKey = "server.port";
    private String applicationName;
    private Integer serverPort;

    private static final ApplicationProperties prop = new ApplicationProperties();

    static {
        logger.info("开始从" + configFile + "加载应用配置信息.");
        try (InputStream ins = ApplicationProperties.class.getClassLoader()
                .getResourceAsStream(configFile)) {
            if (ins == null)
                throw new Exception("应用配置错误，在classpath下未发现'" + configFile + "文件！");
            Properties properties = new Properties();
            properties.load(ins);
            String applicationName = properties.getProperty(applicationNameKey);
            if (applicationName == null || "".equals(applicationName))
                throw new Exception("应用配置错误，'" + applicationNameKey + "'未配置！");
            else
                prop.applicationName = applicationName;
            String serverPort = properties.getProperty(serverPortKey);
            if (serverPort == null || "".equals(serverPort)) {
                throw new Exception("应用配置错误，'" + serverPortKey + "'未配置！");
            } else {
                prop.serverPort = Integer.valueOf(serverPort);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }
        logger.info("加载应用配置完成!");
    }

    private ApplicationProperties() {
    }

    public static ApplicationProperties config() {
        return prop;
    }
    //省略getter
}
