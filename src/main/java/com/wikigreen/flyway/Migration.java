package com.wikigreen.flyway;


import org.flywaydb.core.Flyway;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Migration {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        FileReader fileReader = new FileReader("src/main/resources/db/config/flyway.properties");
        properties.load(fileReader);

        Flyway flyway = Flyway.configure().dataSource(
                properties.getProperty("flyway.url"),
                properties.getProperty("flyway.user"),
                properties.getProperty("flyway.password")
        ).load();

        flyway.migrate();

    }
}
