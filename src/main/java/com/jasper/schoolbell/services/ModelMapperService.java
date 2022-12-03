package com.jasper.schoolbell.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMapperService {
    private ModelMapper modelMapper;

    @PostConstruct
    private void init() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public <S, T> T map(final S source, final Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    public <S, T> List<T> map(final List<S> source, final Class<T> targetClass) {
        return source.stream()
            .map(item -> modelMapper.map(item, targetClass))
            .collect(Collectors.toList());
    }
}
