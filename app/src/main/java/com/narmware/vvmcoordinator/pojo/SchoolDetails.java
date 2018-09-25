package com.narmware.vvmcoordinator.pojo;

import io.realm.RealmObject;

/**
 * Created by rohitsavant on 19/09/18.
 */

public class SchoolDetails extends RealmObject {

private String inst_name,inst_mobile,inst_id,nameofprincipal,exam_coordinator,inst_email,payment_status,city;
    private String call_count,last_call, total_count, paid_count, unpaid_count;
    private boolean isCalled;

    public boolean isCalled() {
        return isCalled;
    }

    public void setCalled(boolean called) {
        isCalled = called;
    }

    public String getLast_call() {
        return last_call;
    }

    public void setLast_call(String last_call) {
        this.last_call = last_call;
    }

    public String getCall_count() {
        return call_count;
    }

    public void setCall_count(String call_count) {
        this.call_count = call_count;
    }

    public String getExam_coordinator() {
        return exam_coordinator;
    }

    public void setExam_coordinator(String exam_coordinator) {
        this.exam_coordinator = exam_coordinator;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInst_name() {
        return inst_name;
    }

    public void setInst_name(String inst_name) {
        this.inst_name = inst_name;
    }

    public String getInst_mobile() {
        return inst_mobile;
    }

    public void setInst_mobile(String inst_mobile) {
        this.inst_mobile = inst_mobile;
    }

    public String getInst_id() {
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
    }

    public String getNameofprincipal() {
        return nameofprincipal;
    }

    public void setNameofprincipal(String nameofprincipal) {
        this.nameofprincipal = nameofprincipal;
    }

    public String getInst_email() {
        return inst_email;
    }

    public void setInst_email(String inst_email) {
        this.inst_email = inst_email;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public String getUnpaid_count() {
        return unpaid_count;
    }

    public void setUnpaid_count(String unpaid_count) {
        this.unpaid_count = unpaid_count;
    }

    public String getPaid_count() {
        return paid_count;
    }

    public void setPaid_count(String paid_count) {
        this.paid_count = paid_count;
    }
}
