package com.jwtrestapi.beta.payload;

public class ResponseService {

    private Boolean success;

    private String data;

    public ResponseService() {
    }

    public ResponseService(Boolean success, String data) {
        this.success = success;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
