package com.melek;

import com.melek.model.Feature;
import com.melek.model.Scenario;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonToFeature {

    private JsonToFeature() {
        throw new UnsupportedOperationException("Utility class");
    }

    protected static List<Feature> parseFeatures(String jsonContent) {
        List<Feature> featureList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonContent);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject featureObject = jsonArray.getJSONObject(i);
            Feature feature = new Feature();
            feature.setName(featureObject.getString("description"));

            List<Scenario> scenarios = new ArrayList<>();
            JSONArray elementsArray = featureObject.getJSONArray("elements");

            for (int j = 0; j < elementsArray.length(); j++) {
                JSONObject scenarioObject = elementsArray.getJSONObject(j);
                String keyword = "keyword";
                // Elements (Scenarios) parsing, sadece "keyword" değeri "scenario ve scenario outline" olanlar eklenecek
                if ("Scenario".equalsIgnoreCase(scenarioObject.getString(keyword)) || "Scenario Outline".equalsIgnoreCase(scenarioObject.getString(keyword))) {
                    Scenario scenario = new Scenario();
                    scenario.setName(scenarioObject.getString("name"));
                    scenario.setKeyword(scenarioObject.getString(keyword));

                    // Steps parsing ve senaryo durumunu ve süresini belirle
                    JSONArray stepsArray = scenarioObject.getJSONArray("steps");
                    long totalDuration = 0;
                    String scenarioStatus = "passed"; // Başlangıçta passed kabul ediyoruz.

                    for (int k = 0; k < stepsArray.length(); k++) {
                        JSONObject stepObject = stepsArray.getJSONObject(k);

                        long duration = 0;
                        String status = "skipped";

                        if (stepObject.has("result")) {
                            JSONObject resultObject = stepObject.getJSONObject("result");
                            duration = resultObject.optLong("duration", 0); // Result içinde duration varsa al, yoksa 0 yap.
                            status = resultObject.optString("status", "skipped"); // Result içinde status varsa al, yoksa "skipped" yap.
                        }


                        // Steplerin toplam süresini hesapla
                        totalDuration += duration;

                        // Eğer bir step "failed" ise, senaryonun durumu da "failed" olmalı
                        if (!"passed".equalsIgnoreCase(status)) {
                            scenarioStatus = status;
                        }
                    }

                    // Senaryonun toplam süresini ve durumunu set et
                    scenario.setDuration(Util.getFormattedDuration(totalDuration));
                    scenario.setStatus(scenarioStatus);
                    scenarios.add(scenario);
                }
            }
            feature.setScenarios(scenarios);
            featureList.add(feature);
        }

        return featureList;
    }

}
