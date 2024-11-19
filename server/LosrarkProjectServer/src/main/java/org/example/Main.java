package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        SocketConnecter socketConnecter = new SocketConnecter();
//        socketConnecter.socket();
        int[] categoryCodes = {90200, 90300, 90400, 90500, 90600, 90700};

        // Fetching data for each category code
        List<Map<String, Object>> jsonDatas = new ArrayList<>();
        Map<String, Object> jsonData;
        for (int categoryCode : categoryCodes) {
            jsonData = new HashMap<>();
            jsonData.put("Sort", "GRADE");
            jsonData.put("CategoryCode", categoryCode);
            jsonData.put("SortCondition", "ASC");
            jsonDatas.add(jsonData);
        }

        GetLostarkData getLostarkData = new GetLostarkData();
        List<List<Map<String, Object>>> data = GetLostarkData.getData(jsonDatas);

        for (List<Map<String, Object>> dataList : data) {
            for (Map<String, Object> dataMap : dataList) {
                System.out.println(dataMap);
            }
        }
    }
}