package org.example;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
//        App();
        MongoDB();
    }

    public static void MongoDB() {
        MongoConfig mongoConfig = new MongoConfig();
        mongoConfig.MongoDBCreate();
    }

    public static void App() {
//        SocketConnecter socketConnecter = new SocketConnecter();
//        socketConnecter.socket();

        /**
         * 받아올 데이터 검색
         * jsonDatas 에 검색어 저장
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
        jsonDatas.add(jsonData);

        /**
         * dataSheet 형식으로 저장
         * Id, Name, YDayAvgPrice, RecentPrice 만 저장
         * GetLostarkData 를 이용해 jsonDatas 에 담긴 검색어 관련 데이터 전부 받아오기 -> 데이터를 원하는 키값을 가진 것들만 가져옴 -> dataSheet 에 저장
         */
        List<List<Map<String, Object>>> data = GetLostarkData.getData(jsonDatas);
        DataSheet dataSheet = new DataSheet();

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
    }
}