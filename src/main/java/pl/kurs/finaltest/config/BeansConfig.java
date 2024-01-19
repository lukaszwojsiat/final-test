package pl.kurs.finaltest.config;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import pl.kurs.finaltest.annotations.PersonCommandSubType;

import java.time.format.DateTimeFormatter;
import java.util.Set;

@Configuration
public class BeansConfig {

    @Bean
    public ModelMapper getModelMapper(Set<Converter> converters) {
        ModelMapper mapper = new ModelMapper();
        converters.forEach(mapper::addConverter);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }


    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        SimpleModule sm1 = new SimpleModule("deserializationCreatePersonCommandsSubType");

        Reflections reflections = new Reflections("pl.kurs.finaltest.models.commands");
        Set<Class<?>> subtypes = reflections.getTypesAnnotatedWith(PersonCommandSubType.class);

        for (Class<?> subType : subtypes) {
            PersonCommandSubType annotation = subType.getAnnotation(PersonCommandSubType.class);
            if (annotation != null) {
                String typeName = annotation.value();
                sm1.registerSubtypes(new NamedType(subType, typeName));
            }
        }

        SimpleModule sm2 = new SimpleModule("serializationDateFormat");
        sm2.addSerializer(new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));

        return new Jackson2ObjectMapperBuilder().modules(sm1, sm2);
    }
}
