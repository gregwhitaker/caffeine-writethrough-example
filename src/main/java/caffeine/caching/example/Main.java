package caffeine.caching.example;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        final LoadingCache<Integer, String> CACHE = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .writer(new DatabaseCacheWriter(dataSource))
                .build(new DatabaseCacheLoader(dataSource));

        LOGGER.info("Adding 1: This is the number 1");
        CACHE.put(1, "This is the number 1");

        LOGGER.info("Adding 2: This is the number 2");
        CACHE.put(2, "This is the number 2");

        LOGGER.info("Adding 3: This is the number 3");
        CACHE.put(3, "This is the number 3");

        LOGGER.info("Getting `1` From Cache: " + CACHE.get(1));
        LOGGER.info("Getting `2` From Cache: " + CACHE.get(2));
        LOGGER.info("Getting `3` From Cache: " + CACHE.get(3));
        LOGGER.info("Getting `4` From Cache: " + CACHE.get(4));
    }

    /**
     * Loads items not in the cache from the backing database.
     */
    static class DatabaseCacheLoader implements CacheLoader<Integer, String> {

        private final DataSource dataSource;

        public DatabaseCacheLoader(final DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public String load(Integer key) throws Exception {
            LOGGER.info("Loading value from database: [key: {}]", key);

            try (Connection conn = dataSource.getConnection()) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM public.cache_example WHERE ce_id = ?")) {
                    ps.setInt(1, key);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return rs.getString("ce_value");
                        } else {
                            return null;
                        }
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("An error occurred while adding cache value to database", e);
                return null;
            }
        }
    }

    /**
     * Writes items added to the cache to the backing database.
     */
    static class DatabaseCacheWriter implements CacheWriter<Integer, String> {
        private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseCacheWriter.class);

        private final DataSource dataSource;

        public DatabaseCacheWriter(final DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public void write(Integer key, String value) {
            LOGGER.info("Adding cache value to database: [key: {}, value: {}]", key, value);

            try (Connection conn = dataSource.getConnection()) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO public.cache_example (ce_id, ce_value) VALUES (?, ?)")) {
                    ps.setInt(1, key);
                    ps.setString(2, value);

                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                LOGGER.error("An error occurred while adding cache value to database", e);
            }
        }

        @Override
        public void delete(Integer key, String value, RemovalCause cause) {
            LOGGER.info("Removing cache value from database: [key: {}, value: {}]", key, value);

            try (Connection conn = dataSource.getConnection()) {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM public.cache_example WHERE ce_id = ?")) {
                    ps.setInt(1, key);

                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                LOGGER.error("An error occurred while removing cache value from database", e);
            }
        }
    }
}
