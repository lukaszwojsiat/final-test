package pl.kurs.finaltest.config;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import pl.kurs.finaltest.annotations.PersonCommandSubType;

import java.util.Set;

@Configuration
public class BeansConfig {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        SimpleModule module = new SimpleModule("deserializationCreatePersonCommandsSubType");

        Reflections reflections = new Reflections("pl.kurs.finaltest.models.commands");
        Set<Class<?>> subtypes = reflections.getTypesAnnotatedWith(PersonCommandSubType.class);

        for (Class<?> subType : subtypes) {
            PersonCommandSubType annotation = subType.getAnnotation(PersonCommandSubType.class);
            if (annotation != null) {
                String typeName = annotation.value();
                module.registerSubtypes(new NamedType(subType, typeName));
            }
        }

        return new Jackson2ObjectMapperBuilder().modules(module);
    }
}
