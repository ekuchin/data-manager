package ru.ekuchin.datamanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties
public class Connections {
    private List<Connection> connections;

    public Connections(List<Connection> connections) {
        this.connections = connections;
    }

    public List<Connection> getAll(){
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public Connection getConnection(String name){
        return connections.stream().parallel().filter(el -> el.getName().equals(name)).findFirst().get();
    }
}
