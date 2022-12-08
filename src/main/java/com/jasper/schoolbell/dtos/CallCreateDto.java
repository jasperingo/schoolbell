package com.jasper.schoolbell.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CallCreateDto {
    private List<Entry> entries;

    private String errorMessage;

    @Data
    public static class Entry {
        private String phoneNumber;

        private String status;

        private String sessionId;
    }
}
