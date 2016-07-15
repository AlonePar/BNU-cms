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
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dave on 16/7/15.
 */
public class STSToken {
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
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
            String url = "https://sts.aliyuncs.com/?" + NetUtil.buildQueryString(params);
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

    public static String buildQueryString(Map<String, String> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder query = new StringBuilder();
        boolean firstParam = true;
        for (Map.Entry<String, String> entry: params.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (!"".equals(name) && value != null && !"".equals(value)) {
                if (!firstParam) {
                    query.append("&");
                } else {
                    firstParam = false;
                }

                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }
        return query.toString();
    }


}
