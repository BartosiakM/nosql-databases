package com.rental.repository;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class AbstractRedisRepository implements AutoCloseable {
    protected static JedisPooled pool;

    public void initDbConnection() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/redis.properties")) {
            properties.load(fis);

            String host = properties.getProperty("redis.host");
            int port = Integer.parseInt(properties.getProperty("redis.port"));

            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
            pool = new JedisPooled(new HostAndPort(host, port), clientConfig);

            System.out.println("Redis connection initialized: " + host + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Redis properties", e);
        }
    }
    @Override
    public void close() {
        if (pool != null) {
            pool.close();
        }
    }
}
