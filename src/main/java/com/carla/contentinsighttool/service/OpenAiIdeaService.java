package com.carla.contentinsighttool.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class OpenAiIdeaService {

    public Map<String, Object> generateIdeas(Map<String, Integer> themes) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            StringBuilder themeSummary = new StringBuilder();

            for (Map.Entry<String, Integer> entry : themes.entrySet()) {
                themeSummary.append("- ")
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\\n");
            }

            String prompt =
                    "You are helping a food relationship and body image coach create content.\n\n" +
                            "Based on these recurring client themes, generate content ideas.\n\n" +
                            "IMPORTANT: Return ONLY valid JSON. If you do not, the system will fail.\n\n" +
                            "Use this exact format:\n" +
                            "{\n" +
                            "  \"contentIdeas\": [\n" +
                            "    \"Idea 1\",\n" +
                            "    \"Idea 2\",\n" +
                            "    \"Idea 3\",\n" +
                            "    \"Idea 4\",\n" +
                            "    \"Idea 5\"\n" +
                            "  ],\n" +
                            "  \"podcastIdeas\": [\n" +
                            "    \"Idea 1\",\n" +
                            "    \"Idea 2\",\n" +
                            "    \"Idea 3\"\n" +
                            "  ],\n" +
                            "  \"resourceIdeas\": [\n" +
                            "    \"Idea 1\",\n" +
                            "    \"Idea 2\"\n" +
                            "  ]\n" +
                            "}\n\n" +
                            "Make the ideas practical, specific, and relevant to coaching clients.\n\n" +
                            "Themes this week:\n" +
                            themeSummary;

            String escapedPrompt = prompt
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n");

            String requestBody =
                    "{"
                            + "\"model\":\"gpt-4.1-mini\","
                            + "\"input\":\"" + escapedPrompt + "\""
                            + "}";

            URL url = new URL("https://api.openai.com/v1/responses");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + System.getenv("OPENAI_API_KEY"));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            os.write(requestBody.getBytes("UTF-8"));
            os.close();

            int status = connection.getResponseCode();

            BufferedReader reader;
            if (status >= 200 && status < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            if (status >= 200 && status < 300) {
                String fullResponse = response.toString();

                JsonNode root = mapper.readTree(fullResponse);

                JsonNode textNode = root.path("output")
                        .get(0)
                        .path("content")
                        .get(0)
                        .path("text");

                if (textNode == null || textNode.isMissingNode() || textNode.isNull()) {
                    Map<String, Object> errorMap = new LinkedHashMap<>();
                    errorMap.put("error", "Could not extract AI text cleanly.");
                    return errorMap;
                }

                String aiText = textNode.asText();

                return mapper.readValue(aiText, new TypeReference<Map<String, Object>>() {});
            } else {
                Map<String, Object> errorMap = new LinkedHashMap<>();
                errorMap.put("error", "Error calling AI: HTTP " + status);
                errorMap.put("details", response.toString());
                return errorMap;
            }

        } catch (Exception e) {
            Map<String, Object> errorMap = new LinkedHashMap<>();
            errorMap.put("error", "Error calling AI: " + e.getMessage());
            return errorMap;
        }
    }
}