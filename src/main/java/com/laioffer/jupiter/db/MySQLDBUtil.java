package com.laioffer.jupiter.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// 生成 MySQL instance 的地址
public class MySQLDBUtil {
    // private static final String INSTANCE = "laiproject-instance.cw8kc8mzwiml.us-east-2.rds.amazonaws.com";
    private static final String INSTANCE = "mysqldatabase.cw8kc8mzwiml.us-east-2.rds.amazonaws.com";
    // 如果是本机的 MySQL
    // private static final String INSTANCE = "localhost"；
    // http://localhost:3306
    private static final String PORT_NUM = "3306";  // 接收来自互联网的请求 需要对应一个端口
    // private static final String DB_NAME = "jupiter";  //  这里叫什么名字都可以 不需要跟AWS上的名字一致
    private static final String DB_NAME = "twitch";  //  这里叫什么名字都可以 不需要跟AWS上的名字一致

    public static String getMySQLAddress() throws IOException {
        // 读 property 文件
        Properties prop = new Properties();
        String propFileName = "config.properties";

        // 找到propFileName文件  然后打开文件  读里面的内容
        // 为什么是 stream? 万一文件很大
        // 读网络传输数据的时候 如果不确定有多长 需要用 stream 来读 一段一段来处理  fileIO
        InputStream inputStream = MySQLDBUtil.class.getClassLoader().getResourceAsStream(propFileName);
        prop.load(inputStream);     // 转换成 hashtable的形式， Properties extends HashTable

        String username = prop.getProperty("user");
        String password = prop.getProperty("password");

        // 固定格式
        // jdbc: java database connector
        return String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s&autoReconnect=true&serverTimezone=UTC&createDatabaseIfNotExist=true",
                INSTANCE, PORT_NUM, DB_NAME, username, password
        );

    }

}
// 如果不写 DB_NAME 那么在使用的过程中需要
// use jupiter;
// select * from users;
