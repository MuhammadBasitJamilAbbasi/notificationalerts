package com.bj.georgehousetrust.models;

public class ResponseCustomer {
    String id,cusname,cusemail,custphone,notiid,nottitle,notemessage,status;

    public ResponseCustomer(String id, String cusname, String cusemail, String custphone, String notiid, String nottitle, String notemessage,String status) {
        this.id = id;
        this.cusname = cusname;
        this.cusemail = cusemail;
        this.custphone = custphone;
        this.notiid = notiid;
        this.nottitle = nottitle;
        this.notemessage = notemessage;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    public String getCusemail() {
        return cusemail;
    }

    public void setCusemail(String cusemail) {
        this.cusemail = cusemail;
    }

    public String getCustphone() {
        return custphone;
    }

    public void setCustphone(String custphone) {
        this.custphone = custphone;
    }

    public String getNotiid() {
        return notiid;
    }

    public void setNotiid(String notiid) {
        this.notiid = notiid;
    }

    public String getNottitle() {
        return nottitle;
    }

    public void setNottitle(String nottitle) {
        this.nottitle = nottitle;
    }

    public String getNotemessage() {
        return notemessage;
    }

    public void setNotemessage(String notemessage) {
        this.notemessage = notemessage;
    }
}
