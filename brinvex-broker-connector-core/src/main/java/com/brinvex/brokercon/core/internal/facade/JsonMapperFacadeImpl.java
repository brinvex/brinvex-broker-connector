package com.brinvex.brokercon.core.internal.facade;

import com.brinvex.brokercon.core.api.facade.JsonMapperFacade;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

import java.nio.file.Path;

public class JsonMapperFacadeImpl implements JsonMapperFacade {

    private static class Lazy {
        private static final JsonMapper jsonMapper = JsonMapper.builder()
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .build();
    }

    @Override
    public <T> T readFromJson(Path jsonFilePath, Class<T> type) {
        return Lazy.jsonMapper.readValue(jsonFilePath.toFile(), type);
    }

    @Override
    public <T> T readFromJson(String jsonContent, Class<T> type) {
        return Lazy.jsonMapper.readValue(jsonContent, type);
    }
}
