package com.inghub.StockOrder.dto;

import java.util.HashMap;
import java.util.Map;

public class BaseResponseDto {

    private boolean success = true;
    private int responseCode;
    private String responseMessage;
    private Map<String,Object> data = new HashMap<>();

    public BaseResponseDto() {

    }

    public BaseResponseDto(boolean success, int responseCode, String responseMessage, Map<String,Object> data) {
        this.success = success;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.data = data;
    }

    public BaseResponseDto(Map<String,Object> data) {
        this.data = data;
    }

    public BaseResponseDto addResponseData(String key,Object value) {
        this.data.put(key, value);
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponseDto{" +
                "success=" + success +
                ", responseCode=" + responseCode +
                ", responseMessage='" + responseMessage + '\'' +
                ", data=" + data +
                '}';
    }
}
