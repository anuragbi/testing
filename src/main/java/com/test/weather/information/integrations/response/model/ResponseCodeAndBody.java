package com.test.weather.information.integrations.response.model;

public class ResponseCodeAndBody {
    private int responseCode;
    private String body;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
