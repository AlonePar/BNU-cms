package cn.edu.bnu.cms.store;

import java.util.Date;

/**
 * Created by dave on 16/7/15.
 */
public class OSSCredentials {
    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private Date expiration; // 过期时间

    public OSSCredentials() {}

    public OSSCredentials(String accessKeyId, String accessKeySecret) {
        this(accessKeyId, accessKeySecret, null);
    }

    public OSSCredentials(String accessKeyId, String accessKeySecret, String securityToken) {
        this(accessKeyId, accessKeySecret, securityToken, new Date(new Date().getTime() + 3600 * 1000));
    }

    public OSSCredentials(String accessKeyId, String accessKeySecret, String securityToken, Date expiration) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.securityToken = securityToken;
        this.expiration = expiration;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @Override
    public String toString() {
        return "OSSCredentials{"
                + "accessKeyId='" + accessKeyId + '\''
                + ", accessKeySecret='" + accessKeySecret + '\''
                + ", securityToken='" + securityToken + '\''
                + ", expiration=" + expiration
                + '}';
    }
}
