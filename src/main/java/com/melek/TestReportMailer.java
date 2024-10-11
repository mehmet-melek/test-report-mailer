package com.melek;

import com.melek.model.Feature;
import com.melek.model.Report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TestReportMailer {

    public static void sendExecutionReportAsEmail() {
        try {
            if (canSendEmailReport()) {
                String subject = creteExecutionReportForEmail();
                String toEmail = System.getProperty("package.org.unit.mail");
                MailSender.sendHtmlReportEmail(toEmail, subject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendExecutionReportAsEmail(String toEmail) {
        try {
            if (canSendEmailReport()) {
                String subject = creteExecutionReportForEmail();
                MailSender.sendHtmlReportEmail(toEmail, subject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String creteExecutionReportForEmail() throws IOException {
        List<Path> jsonFiles = FindTestReports.getJsonFiles();
        List<Feature> allFeatures = new ArrayList<>();
        for (Path jsonFile : jsonFiles) {
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFile.toUri())));
            List<Feature> featuresOnJson = JsonToFeature.parseFeatures(jsonContent);
            allFeatures.addAll(featuresOnJson);
        }

        Report report = GenerateReportData.createReportData(allFeatures, startTime);
        HtmlCreator.createHtmlReport(report);
        return String.format("Test Execution Report - Env:%s - Package:%s", report.getEnvironment(), report.getPackageName());
    }

    private static Instant startTime;
    public static void setTestExecutionTimer() {
        if (canSendEmailReport()) {
            startTime = Instant.now();
        }
    }

    private static boolean canSendEmailReport() {

        String osName = System.getProperty("os.name").toLowerCase();
        boolean isLinux = osName.contains("mac");
        String envCode = System.getProperty("env.code");
        String pkgName = System.getProperty("pkg.name");
        String mailUnit = System.getProperty("package.org.unit.mail");

        boolean propertiesPresent = isNotNullOrEmpty(envCode) && isNotNullOrEmpty(pkgName) && isNotNullOrEmpty(mailUnit);
        boolean isValidEnvCode = "TST".equalsIgnoreCase(envCode) || "UAT".equalsIgnoreCase(envCode);

        return isLinux && propertiesPresent && isValidEnvCode;
    }

    private static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }


}