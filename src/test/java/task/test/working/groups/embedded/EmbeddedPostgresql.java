package task.test.working.groups.embedded;

import org.testcontainers.containers.PostgreSQLContainer;

public class EmbeddedPostgresql extends PostgreSQLContainer<EmbeddedPostgresql> {
    private static final String IMAGE_VERSION = "postgres:14.4";
    private static final String DATABASE_NAME = "db";
    private static final String DATABASE_PASSWORD = "123";
    private static final String USER_NAME = "user";

    private static EmbeddedPostgresql container;

    private EmbeddedPostgresql() {
        super(IMAGE_VERSION);
    }

    public static EmbeddedPostgresql getInstance() {
        if (container == null) {
            container = new EmbeddedPostgresql().withDatabaseName(DATABASE_NAME)
                    .withUsername(USER_NAME)
                    .withPassword(DATABASE_PASSWORD);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_POSTGRES_URL", container.getJdbcUrl());
        System.setProperty("DB_POSTGRES_USERNAME", container.getUsername());
        System.setProperty("DB_POSTGRES_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
    }
}
