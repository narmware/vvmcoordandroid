package com.narmware.vvmcoordinator.pojo;

/**
 * Created by rohitsavant on 20/09/18.
 */

public class SchoolResponse {
    String status_code,status_message;
    SchoolDetails[] result;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public SchoolDetails[] getResult() {
        return result;
    }

    public void setResult(SchoolDetails[] result) {
        this.result = result;
    }
}
