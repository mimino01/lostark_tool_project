package org.example;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.List;

/**
 * 몽고디비랑 통실 할 클래스 모음집
 * 알아서 쓰셈 ㅋㅋ
 */
public class MongoConfig {
    private static final String URL = "mongodb://localhost:27017";
    private static final String DATABASE = "Lostark";
    private static final String COLLECTION = "dataCal1";

    /**
     * 생성자인데 의미 없음 뭐 할꺼임?
     */
    public void MongoClient () {

    }

    /**
     * 몽고 디비 연결 함수
     * @return MongoDatabase
     */
    public MongoDatabase getDatabase () {
        MongoClient mongoClient = MongoClients.create(URL);
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        System.out.println("Connected to database");
        return database;
    }

    /**
     * Doucment 형식으로 데이터 추가 (필드 추가)
     * @param doc
     * doc 에는 테이터를 넣도록 예) Document ("name", "Alice"). append("Age",22). append("city","Seoul")
     */
    public void MongoDBCreate (Document doc){
        try {
            MongoDatabase database = getDatabase();
            MongoCollection<Document> collection = database.getCollection(COLLECTION);
            collection.insertOne(doc);

            System.out.println("Document inserted successfully");
        } catch (Exception e) {
            System.out.println("Error inserting to MongoDB: " + e.getMessage());
        }
    }

    /**
     * List<Document> 형식으로 데이터 추가 (도큐먼트 추가)
     * @param docs
     * docs 에는 도큐먼트를 넣도록 해
     * List<Document> documents = new ArrayList<>();
     * for (Stock stock : stocks) {
     *     Document doc = new Document("stockName", stock.getName())
     *                       .append("timestamp", stock.getTimestamp())
     *                       .append("previousPrice", stock.getPreviousPrice())
     *                       .append("currentPrice", stock.getCurrentPrice());
     *     documents.add(doc);
     * }
     * collection.insertMany(documents);
     */
    public void MongoDBCreate (List<Document> docs) {
        try {
            MongoDatabase database = getDatabase();
            MongoCollection<Document> collection = database.getCollection(COLLECTION);
            collection.insertMany(docs);

            System.out.println("Document inserted successfully");
        } catch (Exception e) {
            System.out.println("Error inserting to MongoDB: " + e.getMessage());
        }
    }

    /**
     * filter값 이용해 read
     * @param filter
     * @return cursor
     * filter 에는 쿼리문을 넣도록 예) and(gte("age",30), eq("city","seoul"))
     * cursor 에는 MongoCursor<Document> 형식으로 만듦
     * 리턴 사용법
     * while(cursor.hasNext()){
     *     function(cursor.next)
     * }
     */
    public MongoCursor<Document> MongoDBRead (Document filter) {
        MongoCursor<Document> cursor = null;
        try {
            MongoDatabase database = getDatabase();
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            cursor = collection.find(filter).iterator();
        } catch (Exception e) {
            System.out.println("Error reading to MongoDB: " + e.getMessage());
        }
        if (cursor.hasNext()) {
            System.out.println("Document found");
        } else {
            System.out.println("Document not found");
        }
        return cursor;
    }

    /**
     * filter값과 같은 데이터를 update 값으로 수정
     * @param filter
     * @param update
     * filter 에는 쿼리문을 넣도록 예) and(gte("age",30), eq("city","seoul"))
     * updata 에는 테이터를 넣도록 예) Document("$set",new Document("Age",25))
     */
    public void MongoDBUpdate (Document filter, Document update){
        try {
            MongoDatabase database = getDatabase();
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            collection.updateOne(filter, update);

            System.out.println("Document update successfully");
        } catch (Exception e) {
            System.out.println("Error updating to MongoDB: " + e.getMessage());
        }
    }

    /**
     * filter 값과 같은 데이터를 삭제
     * @param filter
     * filter 에는 쿼리문을 넣도록 예) and(gte("age",30), eq("city","seoul"))
     */
    public void MongoDBDelete (Document filter){
        try {
            MongoDatabase database = getDatabase();
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            collection.deleteOne(filter);

            System.out.println("Document delete successfully");
        } catch (Exception e) {
            System.out.println("Error deleting to MongoDB: " + e.getMessage());
        }
    }
}
