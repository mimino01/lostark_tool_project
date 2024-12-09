package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class MongoConfig {
    private static final String URL = "mongodb://localhost:27017";
    private static final String DATABASE = "Lostark";

    public void MongoClient () {

    }

    /**
     * 몽고 디비 연결 함수
     */
    public void MongoDBConnection (){
        try (MongoClient mongoClient = MongoClients.create(URL)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            System.out.println("Connected to database");

            System.out.println("Database name: " + database.getName());
        } catch (Exception e) {
            System.out.println("Error connecting to MongoDB: " + e.getMessage());
        }
    }

    public void MongoDBCreate (Document doc){
        try (MongoClient mongoClient = MongoClients.create(URL)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection("dataCol");
            System.out.println("Connected to database");

            collection.insertOne(doc);

            System.out.println("Document inserted successfully");
        } catch (Exception e) {
            System.out.println("Error connecting to MongoDB: " + e.getMessage());
        }
    }

    public void MongoDBRead (){

    }
}
