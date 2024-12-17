package org.example;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
//        App();
        MongoDB();
    }

    public static void MongoDB() {
        tools tool = new tools();

        DBGeneration dbg = new DBGeneration();
        dbg.search("들꽃",tool.transUNIXTime(2025, 5,10),tool.transUNIXTime(2023,5,10));
        System.out.println(tool.transUNIXTime(2025, 5,10));
        dbg.search("들꽃");

//        Document doc = new Document("name", "Alice")
//                .append("Age",22)
//                .append("city","Seoul");
//        Document filter = new Document("Data",
//                new Document("$elemMatch",
//                        new Document("Name", "화사한 들꽃")
//                )
//        );
//        Document projection = new Document("Data.$",1);
//        Document updateDoc = new Document("Name","들꽃");
//        Document update = new Document("$set",updateDoc);
//
//        MongoConfig mongoConfig = new MongoConfig();
//
//        mongoConfig.MongoDBCreate(doc);
//
//        MongoCursor<Document> data = mongoConfig.MongoDBRead(filter);
//        System.out.println(data.next());
//
//        mongoConfig.MongoDBUpdate(filter, update);
//
//        mongoConfig.MongoDBDelete(updateDoc);
//
//        MongoCursor<Document> data = mongoConfig.MongoDBRead(filter, projection);
//        if (data.hasNext()) {
//            System.out.println(data.next());
//        } else {
//            System.out.println("No data found");
//        }
    }

    public static class DBGeneration {
        public DBGeneration() {

        }
        /**
         * 그냥 파라메터 없는 서치는 모든 데이터 빼다 박음 ㅋㅋ 좆댔네 이건 진짜 쓰지말자 ㄹㅇ
         */
        public void search() {
            MongoConfig mongoConfig = new MongoConfig();
            MongoCursor<Document> data = mongoConfig.MongoDBRead();
            if (data.hasNext()) {
                System.out.println(data.next());
            } else {
                System.out.println("No data found");
            }
        }
        public void search(String Name) {
            Bson filter = Filters.eq("Data.Name", Name);
            Bson projection = Projections.fields(
                    Projections.include("time"),
                    Projections.elemMatch("Data", Filters.eq("Name", Name))
            );
            MongoConfig mongoConfig = new MongoConfig();
            MongoCursor<Document> data = mongoConfig.MongoDBRead(filter, projection);
            if (data.hasNext()) {
                System.out.println(data.next());
            } else {
                System.out.println("No data found");
            }
        }
        public void search(String Name, long upper, long lower) {
            Bson filter = Filters.and(
                    Filters.eq("Data.Name", Name),
                    Filters.gte("time",lower),
                    Filters.lte("time",upper)
            );
            Bson projection = Projections.fields(
                    Projections.include("time"),
                    Projections.elemMatch("Data", Filters.eq("Name", Name))
            );
            MongoConfig mongoConfig = new MongoConfig();
            MongoCursor<Document> data = mongoConfig.MongoDBRead(filter, projection);
            if (data.hasNext()) {
                System.out.println(data.next());
            } else {
                System.out.println("No data found");
            }
        }
    }

    public static void App() {
//        SocketConnecter socketConnecter = new SocketConnecter();
//        socketConnecter.socket();

        /**
         * 받아올 데이터 검색
         * jsonDatas 에 검색어 저장
         * categoryCodes 는 생활 재료, 밑에 독립적인건 융화재료
         */
        int[] categoryCodes = {90200, 90300, 90400, 90500, 90600, 90700};

        List<Map<String, Object>> jsonDatas = new ArrayList<>();
        Map<String, Object> jsonData;
        for (int categoryCode : categoryCodes) {
            jsonData = new HashMap<>();
            jsonData.put("Sort", "GRADE");
            jsonData.put("CategoryCode", categoryCode);
            jsonData.put("SortCondition", "ASC");
            jsonDatas.add(jsonData);
        }
        jsonData = new HashMap<>();
        jsonData.put("Sort", "GRADE");
        jsonData.put("SortCondition", "DESC");
        jsonData.put("ItemName", "융화");
        jsonData.put("CategoryCode", 50010);
        jsonDatas.add(jsonData); // <<----- 이새끼 jsonDatas 에 검색 조건이 다 들어 있음

        /**
         * dataSheet 형식으로 저장
         * Id, Name, YDayAvgPrice, RecentPrice 만 저장
         * GetLostarkData 를 이용해 jsonDatas 에 담긴 검색어 관련 데이터 전부 받아오기 -> 데이터를 원하는 키값을 가진 것들만 가져옴 -> dataSheet 에 저장
         */
        List<List<Map<String, Object>>> data = GetLostarkData.getData(jsonDatas); // 이새끼가 서버에서 받아온 데이터 저장하는 놈임
        DataSheet dataSheet = new DataSheet(); //쌈@뽕한 데이터 저장용 클래스

        /**
         * 받아온 데이터 정제하는 놈임
         * 받아온 데이터중에서 쓸모없는거 다 쳐내고 아이디, 이름, 어제가격, 현재가격만 저장함
         */
        for (List<Map<String, Object>> dataList : data) {
            for (Map<String, Object> dataMap : dataList) {
//                System.out.println(dataMap);
                List<String> keyToFind = Arrays.asList("Id","Name","YDayAvgPrice","RecentPrice");

                Map<String, Object> usefulData = keyToFind.stream()
                        .filter(dataMap::containsKey)
                        .collect(Collectors.toMap(key -> key, dataMap::get));

                usefulData.forEach(dataSheet::addData);
            }
        }
        for (Map<String, Object> dataMap : dataSheet.getData()) {
            System.out.println(dataMap);
        }
        dataSheet.saveData();
    }

    public static class DataSheet {
        public static class Data {
            String id;
            String name;
            String YesterdayAvgPrice;
            String RecentPrice;
            int state;
            public Data() {
                this.id = null;
                this.name = null;
                this.YesterdayAvgPrice = null;
                this.RecentPrice = null;
                this.state = 0;
            }

            public void clean () {
                this.id = null;
                this.name = null;
                this.YesterdayAvgPrice = null;
                this.RecentPrice = null;
                this.state = 0;
            }

            public String encoding () {
                String response = "Q";
                response += "i" + id;
                response += "n" + name;
                response += "y" + YesterdayAvgPrice;
                response += "r" + RecentPrice;

                return response;
            }

            public int getDataState () {
                state = 0;
                if (YesterdayAvgPrice != null) {
                    state += 1;
                }
                if (RecentPrice != null) {
                    state += 1;
                }
                if (name != null) {
                    state += 1;
                }
                if (id != null) {
                    state += 1;
                }
                return state;
            }

            public Map<String, Object> getData() {
                Map<String, Object> data = new HashMap<>();
                data.put("Id",id);
                data.put("Name",name);
                data.put("YdayAvgPrice",YesterdayAvgPrice);
                data.put("RecentPrice",RecentPrice);
                return data;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getYesterdayAvgPrice() {
                return YesterdayAvgPrice;
            }

            public void setYesterdayAvgPrice(String yesterdayAvgPrice) {
                YesterdayAvgPrice = yesterdayAvgPrice;
            }

            public String getRecentPrice() {
                return RecentPrice;
            }

            public void setRecentPrice(String recentPrice) {
                RecentPrice = recentPrice;
            }
        }
        List<Data> data;
        Data tempData;

        public String encoding () {
            String response = "뒓";

            LocalDateTime now = LocalDateTime.now();
//            response += now;
            for (Data data : data) {
                response += data.encoding();
            }

            return response;
        }

        public DataSheet() {
            data = new ArrayList<>();
            tempData = new Data();
        }

        public void addData(String key, Object value) {
//            System.out.println(key + " : " + value + " : " + tempData.getDataState());
            if (tempData == null) {
                tempData = new Data();
            }
            if (tempData.getDataState() >= 4) {
                data.add(tempData);
                tempData = new Data();
            }
            switch (key) {
                case "Id":
                    tempData.setId(String.valueOf(value));
                    break;
                case "Name":
                    tempData.setName((String) value);
                    break;
                case "YDayAvgPrice":
                    tempData.setYesterdayAvgPrice(String.valueOf(value));
                    break;
                case "RecentPrice":
                    tempData.setRecentPrice(String.valueOf(value));
                    break;
            }
        }

        public List<Map<String, Object>> getData() {
            List<Map<String, Object>> response = new ArrayList<>();
            for (Data data : data) {
                response.add(data.getData());
            }
            return response;
        }

        public void saveData() {
            List<Document> doc = new ArrayList<>();
            Document docs = new Document();
            LocalDateTime now = LocalDateTime.now();

            /**
             * sheet 형식으로 저장된 데이터를 List<Document> 형식으로 변환해야함
             */
            for (Data data : data) {
                doc.add(new Document(data.getData()));
            }

            /**
             * 시간은 년도 월 일 시 분 초 로 나누어 저장
             */
            Long time = now.toInstant(ZoneOffset.UTC).getEpochSecond();

            docs.append("time",time);
            docs.append("Data", doc);

            MongoConfig mongoConfig = new MongoConfig();
            mongoConfig.MongoDBCreate(docs);
        }
    }

    public static class tools{
        public tools() {

        }

        /**
         * 일반 시간을 유닉스 타임으로 변환
         * @param year 년
         * @param month 월
         * @param day 일
         * @param hour 시간 (24시간제)
         * @param min 분
         * @param sec 초
         * @return unix time 유닉스 타임
         */
        public long transUNIXTime (int year, int month, int day, int hour, int min, int sec) {
            LocalDateTime dataTime = LocalDateTime.of(year, month, day, hour, min, sec);
            return dataTime.toInstant(ZoneOffset.UTC).getEpochSecond();
        }

        /**
         * 일반 시간을 유닉스 타임으로 변환 (0시 0분 0초)
         * @param year 년
         * @param month 월
         * @param day 알
         * @return unix time 유닉스 타임
         */
        public long transUNIXTime (int year, int month, int day) {
            LocalDateTime dataTime = LocalDateTime.of(year, month, day, 0, 0, 0);
            return dataTime.toInstant(ZoneOffset.UTC).getEpochSecond();
        }

        /**
         * 유닉스 시간을 일반 시간으로 변환
         * @param UNIXTime 유닉스 시간
         * @return date time 일반 시간
         */
        public String transDateTime (long UNIXTime) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(UNIXTime), ZoneOffset.UTC);
            return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }
}