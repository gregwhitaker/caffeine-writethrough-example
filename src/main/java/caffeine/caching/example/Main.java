package caffeine.caching.example;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * Runs the Caffeine Write-Through Cache Example.
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) throws Exception {
        LOGGER.info("Starting the Caffeine Write-Through Cache Example");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setUsername("postgres");

        DataSource dataSource = new HikariDataSource(hikariConfig);

        LOGGER.info("Creating database connection: {}", hikariConfig.getJdbcUrl());

        final Cache<Integer, String> CACHE = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .removalListener(new DatabaseRemovalListener(dataSource))
                .writer(new DatabaseCacheWriter(dataSource))
                .build();

        LOGGER.info("Adding 1: This is the number 1");
        CACHE.put(1, "This is the number 1");

        LOGGER.info("Adding 2: This is the number 2");
        CACHE.put(2, "This is the number 2");

        LOGGER.info("Adding 3: This is the number 3");
        CACHE.put(2, "This is the number 3");

        LOGGER.info("Getting `1`: " + CACHE.getIfPresent(1));
        LOGGER.info("Getting `2`: " + CACHE.getIfPresent(1));
        LOGGER.info("Getting `3`: " + CACHE.getIfPresent(1));
    }

    /**
     *
     */
    static class DatabaseRemovalListener implements RemovalListener<Integer, String> {
        private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRemovalListener.class);

        private final DataSource dataSource;

        public DatabaseRemovalListener(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public void onRemoval(Integer key, String value, RemovalCause cause) {

        }
    }

    /**
     *
     */
    static class DatabaseCacheWriter implements CacheWriter<Integer, String> {
        private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseCacheWriter.class);

        private final DataSource dataSource;

        public DatabaseCacheWriter(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public void write(Integer key, String value) {

        }

        @Override
        public void delete(Integer key, String value, RemovalCause cause) {

        }
    }
}
