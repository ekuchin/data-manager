package ru.ekuchin.datamanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties
public class Types {
    private List<Type> types;

    public Types(List<Type> types) {
        this.types = types;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public Type getType(String name){
        return types.stream().parallel().filter(el -> el.getName().equals(name)).findFirst().get();
    }
}
