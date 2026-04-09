package com.aihelp.ai;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TongyiProvider implements AIProvider {

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.model}")
    private String model;

    @Value("${ai.api.timeout}")
    private int timeout;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String chat(String prompt) {
        try {
            String requestBody = buildRequestBody(prompt);

            HttpResponse response = HttpRequest.post(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .timeout(timeout)
                    .execute();

            String body = response.body();

            if (response.isOk()) {
                return parseResponse(body);
            } else {
                log.error("AI API调用失败: {}", body);
                throw new RuntimeException("AI服务调用失败: " + body);
            }
        } catch (Exception e) {
            log.error("AI调用异常", e);
            throw new RuntimeException("AI服务调用异常: " + e.getMessage());
        }
    }

    private String buildRequestBody(String prompt) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"model\":\"").append(model).append("\",");
            sb.append("\"messages\":[");
            sb.append("{");
            sb.append("\"role\":\"user\",");
            sb.append("\"content\":\"").append(escapeJson(prompt)).append("\"");
            sb.append("}");
            sb.append("]");
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            log.error("构建请求体失败", e);
            return "{\"model\":\"" + model + "\",\"messages\":[{\"role\":\"user\",\"content\":\"" + escapeJson(prompt) + "\"}]}";
        }
    }

    private String parseResponse(String body) throws Exception {
        JsonNode rootNode = objectMapper.readTree(body);

        JsonNode choicesNode = rootNode.path("choices");
        if (choicesNode.isArray() && choicesNode.size() > 0) {
            JsonNode messageNode = choicesNode.get(0).path("message");
            String content = messageNode.path("content").asText();
            return content;
        }

        if (rootNode.has("output") && rootNode.get("output").has("text")) {
            return rootNode.get("output").get("text").asText();
        }

        throw new RuntimeException("无法解析AI响应: " + body);
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
