package com.qhapaq.oreo.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiSummaryResponseDto {
    private String id;
    private String model;
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}