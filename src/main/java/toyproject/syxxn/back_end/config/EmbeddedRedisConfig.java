package toyproject.syxxn.back_end.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@Profile({"test", "local"})
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    public EmbeddedRedisConfig() {
        this.redisServer = RedisServer.builder()
                .port(redisPort)
                .setting("maxheap 128M")
                .build();
    }

    @PostConstruct
    public void runRedis() {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {미쳤지난 이거밖에 없어 노트북 업다고,,하하하하하ㅏ하하하하하하하하하하하하ㅏ핳하하핳하하하핳하하하하하하하하하하하핳하핳핳
        if (redisServer != null) {
            redisServer.stop();
        }
    }

}

