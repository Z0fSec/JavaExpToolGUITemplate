package com.z0fsec.javaexptoolguitemplate.model;

import java.time.LocalDateTime;


public class VulnData {
    private String vulnName;
    private String targetUrl;
    private String remark;
    private String vulnType;
    private LocalDateTime createTime;
    private LocalDateTime lastUsedTime;
    private String vulnStatus;
    private String pocFilePath;
    private boolean isValid;

    public VulnData(String vulnName, String cve, String vulnType, String url) {
        this.vulnName = vulnName;
        this.vulnType = vulnType;
        this.targetUrl = url;
        this.createTime = LocalDateTime.now();
        this.lastUsedTime = LocalDateTime.now();
        this.vulnStatus = "未验证";
        this.pocFilePath = "";
        this.isValid = false;
        this.remark = cve;
    }

    public String getVulnName() {
        return vulnName;
    }

    public void setVulnName(String vulnName) {
        this.vulnName = vulnName;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVulnType() {
        return vulnType;
    }

    public void setVulnType(String vulnType) {
        this.vulnType = vulnType;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(LocalDateTime lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }

    public String getVulnStatus() {
        return vulnStatus;
    }

    public void setVulnStatus(String vulnStatus) {
        this.vulnStatus = vulnStatus;
    }

    public String getPocFilePath() {
        return pocFilePath;
    }

    public void setPocFilePath(String pocFilePath) {
        this.pocFilePath = pocFilePath;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }
}