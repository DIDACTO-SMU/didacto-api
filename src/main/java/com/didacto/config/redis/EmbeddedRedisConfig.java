//package com.didacto.config.redis;
//
//import io.jsonwebtoken.io.IOException;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.redis.connection.RedisServer;
//import org.springframework.util.StringUtils;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//
//@Slf4j
//@Profile("local")
//@Configuration
//public class EmbeddedRedisConfig {
//
//    @Value("${redis.port}")
//    private int redisPort;
//
//    private RedisServer redisServer;
//
//    @PostConstruct
//    public void startRedis() throws IOException {
//        int port = isRedisRunning() ? findAvailablePort() : redisPort;
//        redisServer = new RedisServer(port);
//        redisServer.start();
//    }
//
//    @PreDestroy
//    public void stopRedis() {
//        redisServer.stop();
//    }
//
//    public int findAvailablePort() throws IOException {
//        for (int port = 10000; port <= 65535; port++) {
//            Process process = executeGrepProcessCommand(port);
//            if (!isRunning(process)) {
//                return port;
//            }
//        }
//
//        throw new BusinessLogicException(ExceptionCode.NOT_FOUND_AVAILABLE_PORT);
//    }
//
//    /**
//     * Embedded Redis가 현재 실행중인지 확인
//     */
//    private boolean isRedisRunning() throws IOException {
//        return isRunning(executeGrepProcessCommand(redisPort));
//    }
//
//    /**
//     * 해당 port를 사용중인 프로세스를 확인하는 sh 실행
//     */
//    private Process executeGrepProcessCommand(int redisPort) throws IOException {
//        String command = String.format("netstat -nat | grep LISTEN|grep %d", redisPort);
//        String[] shell = {"/bin/sh", "-c", command};
//
//        return Runtime.getRuntime().exec(shell);
//    }
//
//    /**
//     * 해당 Process가 현재 실행중인지 확인
//     */
//    private boolean isRunning(Process process) {
//        String line;
//        StringBuilder pidInfo = new StringBuilder();
//
//        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//            while ((line = input.readLine()) != null) {
//                pidInfo.append(line);
//            }
//        } catch (Exception e) {
//            throw new BusinessLogicException(ExceptionCode.ERROR_EXECUTING_EMBEDDED_REDIS);
//        }
//        return StringUtils.hasText(pidInfo.toString());
//    }
//}
