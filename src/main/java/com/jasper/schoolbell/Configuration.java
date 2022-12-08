package com.jasper.schoolbell;

import lombok.Data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
@ApplicationScoped
public class Configuration {
    @PersistenceContext(name = "default")
    private EntityManager entityManager;

    private String jwtKey;

    private int jwtDuration;

    private String africansTalkingApiKey;

    private String africansTalkingApiEndPoint;

    private String africansTalkingAppUsername;

    private String africansTalkingPhoneNumber;

    @PostConstruct
    public void init() throws IOException {
        final InputStream inputStream  = Configuration.class
                .getClassLoader()
                .getResourceAsStream("/application.properties");

        final Properties prop = new Properties();

        prop.load(inputStream);

        setJwtKey(prop.getProperty("jwtKey"));
        setJwtDuration(Integer.parseInt(prop.getProperty("jwtDuration")));
        setAfricansTalkingApiKey(prop.getProperty("afAPIKey"));
        setAfricansTalkingApiEndPoint(prop.getProperty("afAPIEndPoint"));
        setAfricansTalkingAppUsername(prop.getProperty("afAppUsername"));
        setAfricansTalkingPhoneNumber(prop.getProperty("afPhoneNumber"));
    }
}
