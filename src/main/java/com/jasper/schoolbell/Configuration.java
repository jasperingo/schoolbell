package com.jasper.schoolbell;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

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

    @PostConstruct
    public void init() throws IOException {
        final InputStream inputStream  = Configuration.class
                .getClassLoader()
                .getResourceAsStream("/application.properties");

        final Properties prop = new Properties();

        prop.load(inputStream);

        setJwtKey(prop.getProperty("jwtKey"));
        setJwtDuration(Integer.parseInt(prop.getProperty("jwtDuration")));
    }

    public ModelMapper getModelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
