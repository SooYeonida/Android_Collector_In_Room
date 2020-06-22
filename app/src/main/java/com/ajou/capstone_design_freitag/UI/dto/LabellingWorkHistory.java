package com.ajou.capstone_design_freitag.UI.dto;

public class LabellingWorkHistory {
    private static LabellingWorkHistory labellingWorkHistory = null;

    public static LabellingWorkHistory getInstance(){
        if(labellingWorkHistory==null){
            labellingWorkHistory = new LabellingWorkHistory();
        }
        return labellingWorkHistory;
    }

    private int historyId;
    private String userId;
    private String dataType;

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

}
