package com.anshibo.faxing_lib.bean;

/**
 * @createtime：2019/5/24 19:12
 * @author: 赵盼龙
 */
public class DeviceBean {

    /**
     * success : true
     * message : null
     * id : 171
     * channelId : 0
     * sn : 4101173808600024
     * se : 4101170801600024
     * companyId : 0
     * obuModel : null
     * obuType : 1
     * createDateTime : 2019-05-24 05:36:50
     * modifyDateTime : 2019-05-24 05:37:27
     * lastSearchTime : 2019-05-24 07:05:23
     * lastSearchChannelId : null
     */

    private boolean success;
    private String message;
    private int id;
    private String channelId;
    private String sn;
    private String se;
    private String companyId;
    private Object obuModel;
    private String obuType;
    private String createDateTime;
    private String modifyDateTime;
    private String lastSearchTime;
    private Object lastSearchChannelId;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSe() {
        return se;
    }

    public void setSe(String se) {
        this.se = se;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Object getObuModel() {
        return obuModel;
    }

    public void setObuModel(Object obuModel) {
        this.obuModel = obuModel;
    }

    public String getObuType() {
        return obuType;
    }

    public void setObuType(String obuType) {
        this.obuType = obuType;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(String modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public String getLastSearchTime() {
        return lastSearchTime;
    }

    public void setLastSearchTime(String lastSearchTime) {
        this.lastSearchTime = lastSearchTime;
    }

    public Object getLastSearchChannelId() {
        return lastSearchChannelId;
    }

    public void setLastSearchChannelId(Object lastSearchChannelId) {
        this.lastSearchChannelId = lastSearchChannelId;
    }
}
