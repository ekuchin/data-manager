package ru.ekuchin.datamanager.datasource;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import ru.ekuchin.datamanager.config.Connection;
import ru.ekuchin.datamanager.config.Type;

import java.util.List;

public class MongoDBDataSource implements ru.ekuchin.datamanager.datasource.DataSource {

    private Connection connection;
    private Type type;
    private MongoDatabase mongodb;

    public MongoDBDataSource(Connection connection, Type type) {
        this.connection = connection;
        this.type = type;

        MongoClient mongoClient = MongoClients.create(connection.getHost());
            mongodb = mongoClient.getDatabase(type.getUri());

    }

    @Override
    public String create(String json) throws Exception {
        return create(json, "");
    }

    @Override
    public String create(String json, String params) throws Exception {
        Document doc = Document.parse(json);
        return mongodb.getCollection(type.getCollection()).insertOne(doc).getInsertedId().asObjectId().getValue().toString();
    }

    @Override
    public String read(String unid, String params) throws Exception {
        Bson flt = Filters.eq("_id", new ObjectId(unid));
        return mongodb.getCollection(type.getCollection()).find(flt).first().toJson();
     }

    @Override
    public String read(String unid) throws Exception {
        return read(unid,"");
    }

    @Override
    public boolean update(String unid, String json) throws Exception {
        return update(unid,json,false,"");
    }

    @Override
    public boolean update(String unid, String json, boolean replaceAllItems) throws Exception {
        return update(unid,json,replaceAllItems,"");
    }

    @Override
    public boolean update(String unid, String json, String params) throws Exception {
        return update(unid,json,false,params);
    }

    @Override
    public boolean update(String unid, String json, boolean replaceAllItems, String params) throws Exception {
        Bson flt = Filters.eq("_id", new ObjectId(unid));
        Document update = Document.parse(json);

        if (replaceAllItems){
            return mongodb.getCollection(type.getCollection()).replaceOne(flt,update).getModifiedCount()==1;
        }
        else{
            Document setUpdate=new Document("$set",update);
            return mongodb.getCollection(type.getCollection()).updateOne(flt,setUpdate).getModifiedCount()==1;
        }
    }

    @Override
    public boolean delete(String unid, String params) throws Exception {
        Bson flt = Filters.eq("_id", new ObjectId(unid));
        return mongodb.getCollection(type.getCollection()).deleteOne(flt).getDeletedCount()==1;
    }

    @Override
    public boolean delete(String unid) throws Exception {
        return delete(unid,"");
    }

    @Override
    public List<String> findAll(String collection, String params) throws Exception {
        return null;
    }

    public List<String> findAll(String collection) throws Exception {
        return findAll(collection, "");
    }

}
/*


public class MongoStore extends Store {

    @Override
    public List<String> findAll(String collection) throws Exception {
         return mongodb.getCollection(collection).find()
                .into(new ArrayList<>())
                .stream().map(Document::toJson)
                .collect(Collectors.toList());
    }

    //Short version with default collection
    public List<String> findAll() throws Exception {
        return findAll(type.getCollection());
    }

}
 */