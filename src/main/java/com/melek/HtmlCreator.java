package com.melek;

import com.melek.model.Feature;
import com.melek.model.Report;
import com.melek.model.Scenario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class HtmlCreator {

    private HtmlCreator() {
        throw new UnsupportedOperationException("Utility class");
    }

    protected static void createHtmlReport(Report report) throws IOException {



        String summaryHtmlPath = "html/summary.html";
        String featureHtmlPath = "html/feature.html";
        String scenarioHtmlPath = "html/scenario.html";
        String outputFilePath = "target/test-report-mail/report.html";

        String summaryHtml = HtmlLoader.loadResourceFile(summaryHtmlPath);


        // Replace işlemleri
        summaryHtml = summaryHtml
                .replace("replaceRepoName", report.getPackageName())
                .replace("replaceDate", report.getDate())
                .replace("replaceEnvironment", report.getEnvironment())
                .replace("replaceTotalDuration", report.getDuration())
                .replace("replaceTC", String.valueOf(report.getScenarioCount()))
                .replace("replacePC", String.valueOf(report.getPassedCount()))
                .replace("replaceFC", String.valueOf(report.getFailedCount()))
                .replace("replaceSC", String.valueOf(report.getSkippedCount()));

        String featureHtml = HtmlLoader.loadResourceFile(featureHtmlPath);
        String scenarioHtml = HtmlLoader.loadResourceFile(scenarioHtmlPath);

        StringBuilder finalHtmlContentBuilder = new StringBuilder(summaryHtml);

        for (Feature feature : report.getFeatures()) {
            String addedFeatureHtmlContent = featureHtml.replace("replaceFeatureName", feature.getName());
            finalHtmlContentBuilder.append("\n").append(addedFeatureHtmlContent);

            for (Scenario scenario : feature.getScenarios()) {
                String addedScenarioHtmlContent = scenarioHtml
                        .replace("replaceScenarioName", scenario.getName())
                        .replace("replaceScenarioDuration", scenario.getDuration())
                        .replace("replaceScenarioStatus", scenario.getStatus())
                        .replace("replaceScenarioClassStatus", scenario.getStatus());
                finalHtmlContentBuilder.append("\n").append(addedScenarioHtmlContent);
            }
        }

        String close = """
        </div>
        </body></html>
        """;
        finalHtmlContentBuilder.append(close);

        String finalHtmlContent = finalHtmlContentBuilder.toString();
        createHtmlFile(outputFilePath,finalHtmlContent);

    }

    protected static void createHtmlFile(String outputFilePath, String htmlContent) throws IOException {

        Path path = Paths.get(outputFilePath);
        Path parentDir = path.getParent();

        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        Files.write(path, htmlContent.getBytes(), StandardOpenOption.CREATE);
    }


}
