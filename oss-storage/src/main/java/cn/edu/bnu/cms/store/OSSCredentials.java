package cn.edu.bnu.cms.store;

import cn.edu.bnu.cms.common.Crypto;
import cn.edu.bnu.cms.common.NetUtil;
import cn.edu.bnu.cms.common.VerifyCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dave on 16/7/15.
 */
public class OSSCredentials {
    public static final String STS_ASSUME_POLICY = "{\n"
            + "  \"Statement\": [\n"
            + "    {\n"
            + "      \"Action\": \"sts:AssumeRole\",\n"
            + "      \"Effect\": \"Allow\",\n"
            + "      \"Resource\": \"*\"\n"
            + "    }\n"
            + "  ],\n"
            + "  \"Version\": \"1\"\n"
            + "}";

    public static final String OSS_READONLY_POLICY = "{\n"
            + "  \"Statement\": [\n"
            + "    {\n"
            + "      \"Action\": [\n"
            + "        \"oss:Get*\",\n"
            + "        \"oss:List*\"\n"
            + "      ],\n"
            + "      \"Effect\": \"Allow\",\n"
            + "      \"Resource\": \"*\"\n"
            + "    }\n"
            + "  ],\n"
            + "  \"Version\": \"1\"\n"
            + "}";

    public static final String OSS_FULL_POLICY = "{\n"
            + "  \"Statement\": [\n"
            + "    {\n"
            + "      \"Action\": \"oss:*\",\n"
            + "      \"Effect\": \"Allow\",\n"
            + "      \"Resource\": \"*\"\n"
            + "    }\n"
            + "  ],\n"
            + "  \"Version\": \"1\"\n"
            + "}";

    private static final String ENDPOINT = "https://sts.aliyuncs.com";

    private static final String API_VERSION = "2015-04-01";

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

    // TODO 生成临时Security Token, 默认1小时即过期
    public static OSSCredentials refreshCredentials(OSSCredentials credentials, String roleArn) {
        TreeMap<String, String> params = new TreeMap<>();
        // 公共参数
        params.put("Format", "JSON");
        params.put("Version", API_VERSION);
        params.put("AccessKeyId", credentials.getAccessKeyId());
        params.put("SignatureMethod", "HMAC-SHA1");
        params.put("SignatureVersion", "1.0");
        params.put("SignatureNonce", "" + VerifyCode.generateVerifyCode(5));
        SimpleDateFormat sdf = new SimpleDateFormat(NetUtil.ISO08601_DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        params.put("Timestamp", sdf.format(new Date()));
        // AssumeRole 参数
        params.put("Action", "AssumeRole");
        params.put("RoleArn", roleArn);
        params.put("RoleSessionName", "TokenUser-" + VerifyCode.generateVerifyCode(5));
        params.put("DurationSeconds", "3600");

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append("&" + entry.getKey() + "=" + NetUtil.percentEncode(entry.getValue()));
        }
        String sign = Base64.getEncoder().encodeToString(Crypto.hmacSha1((credentials.getAccessKeySecret() + "&").getBytes(),
                ("POST&%2F&" + NetUtil.percentEncode(sb.toString().substring(1))).getBytes()));
        params.put("Signature", sign);

        String response;
        try {
            String url = ENDPOINT + "?" + NetUtil.buildQueryString(params);
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.connect();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                response = builder.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            JsonNode credNode = rootNode.path("Credentials");
            OSSCredentials cred = new OSSCredentials(credNode.path("AccessKeyId").asText(),
                    credNode.path("AccessKeySecret").asText(), credNode.path("SecurityToken").asText(),
                    sdf.parse(credNode.path("Expiration").asText()));
            return cred;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getEndpoint() {
        return ENDPOINT;
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
