package com.melek;

import com.melek.model.Feature;
import com.melek.model.Report;
import com.melek.model.Scenario;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.melek.Util.getFormattedDuration;

public class GenerateReportData {

    private GenerateReportData() {
        throw new UnsupportedOperationException("Utility class");
    }
    private static Report report = new Report();

    protected static Report createReportData(List<Feature> featureList, Instant startTime) {

        // set features
        report.setFeatures(featureList);
        // set duration as string
        Instant endTime = Instant.now();
        startTime = startTime == null ? endTime : startTime;
        Duration duration = Duration.between(startTime, endTime);
        report.setDuration(getFormattedDuration(duration.toMillis()));
        // set package name
        String packageName = System.getProperty("pkg.name");
        report.setPackageName(packageName);
        // set environment
        String environment = System.getProperty("env.code");
        report.setEnvironment(environment);
        // set date
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        report.setDate(dateTime);
        // set scenario counts
        setScenarioCounts(report);

        return report;
    }

    private static void setScenarioCounts(Report report) {

        int total = 0;
        int passed = 0;
        int failed = 0;
        int skipped = 0;

        for (Feature feature : report.getFeatures()) {
            for (Scenario scenario : feature.getScenarios()) {
                total++;
                switch (scenario.getStatus()) {
                    case "passed":
                        passed++;
                        break;
                    case "failed":
                        failed++;
                        break;
                    case "skipped":
                        skipped++;
                        break;
                    default:
                        skipped++;
                }
            }
        }

        report.setScenarioCount(total);
        report.setPassedCount(passed);
        report.setFailedCount(failed);
        report.setSkippedCount(skipped);

    }
}
