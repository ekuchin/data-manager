package ru.ekuchin.datamanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ekuchin.datamanager.config.Connection;
import ru.ekuchin.datamanager.config.Connections;
import ru.ekuchin.datamanager.config.Type;
import ru.ekuchin.datamanager.config.Types;
import ru.ekuchin.datamanager.datasource.DataSource;
import ru.ekuchin.datamanager.datasource.DominoDataSource;
import ru.ekuchin.datamanager.datasource.MongoDBDataSource;
import ru.ekuchin.datamanager.exceptions.InvalidConnectionException;
import ru.ekuchin.datamanager.exceptions.InvalidTypeException;
import ru.ekuchin.datamanager.exceptions.UnsupportedDataSourceException;

@RestController
public class DataManagerController {

    @Autowired
    private Connections connections;

    @Autowired
    private Types types;

    private String result;

    private DataSource getDataSource(String typeName)
            throws UnsupportedDataSourceException, InvalidConnectionException, InvalidTypeException {
        Type type = null;
        Connection connection = null;

        try{
            type = types.getType(typeName);
        }
        catch (Exception e){
            throw new InvalidTypeException("Invalid type - "+typeName);
        }

        try{
            connection = connections.getConnection(type.getConnection());
        }
        catch (Exception e){
            throw new InvalidConnectionException("Invalid datasource - "+type.getConnection());
        }

        switch(connection.getType()) {
            case "domino":
                return new DominoDataSource(connection, type);
            case "mongodb":
                return new MongoDBDataSource(connection, type);
            default:
                throw new UnsupportedDataSourceException("Unsupported datasource - "+connection.getType());
        }
    }

    @PostMapping("/{type}")
    public ResponseEntity<?> create (@PathVariable String type, @RequestBody String payload) {
        try{
            DataSource storage = getDataSource(type);
            result = storage.create(payload);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<?> read (@PathVariable String type, @PathVariable String id) {
        try{
            DataSource storage = getDataSource(type);
            result = storage.read(id);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/{type}/{id}")
    public ResponseEntity<?> replace (@PathVariable String type, @PathVariable String id, @RequestBody String payload) {
        try{
            DataSource storage = getDataSource(type);
            boolean result = storage.update(id,payload,true);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/{type}/{id}")
    public ResponseEntity<?> update (@PathVariable String type, @PathVariable String id, @RequestBody String payload) {
        try{
            DataSource storage = getDataSource(type);
            boolean result = storage.update(id,payload,false);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<?> delete (@PathVariable String type, @PathVariable String id) {
        try{
            DataSource storage = getDataSource(type);
            boolean result = storage.delete(id);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}