package cn.edu.bnu.cms.store;

import cn.edu.bnu.cms.common.Crypto;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.edu.bnu.cms.store.OSSResponseHeaders.*;
import static com.aliyun.oss.internal.RequestParameters.*;
import static com.aliyun.oss.internal.RequestParameters.SUBRESOURCE_QOS;
import static com.aliyun.oss.model.ResponseHeaderOverrides.*;
import static com.aliyun.oss.model.ResponseHeaderOverrides.RESPONSE_HEADER_CONTENT_TYPE;
import static com.aliyun.oss.model.ResponseHeaderOverrides.RESPONSE_HEADER_EXPIRES;

/**
 * Created by dave on 16/7/13.
 */
public class OSSClient {
    public static final String OSS_PREFIX = "x-oss-";

    // RFC 822 Date Format
    public static final String RFC822_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

    public static final List<String> SIGNED_PARAMTERS = Arrays.asList(new String[] {
        SUBRESOURCE_ACL, SUBRESOURCE_UPLOADS, SUBRESOURCE_LOCATION,
        SUBRESOURCE_CORS, SUBRESOURCE_LOGGING, SUBRESOURCE_WEBSITE,
        SUBRESOURCE_REFERER, SUBRESOURCE_LIFECYCLE, SUBRESOURCE_DELETE,
        SUBRESOURCE_APPEND, SUBRESOURCE_TAGGING, SUBRESOURCE_OBJECTMETA,
        UPLOAD_ID, PART_NUMBER, SECURITY_TOKEN, POSITION, RESPONSE_HEADER_CACHE_CONTROL,
        RESPONSE_HEADER_CONTENT_DISPOSITION, RESPONSE_HEADER_CONTENT_ENCODING,
        RESPONSE_HEADER_CONTENT_LANGUAGE, RESPONSE_HEADER_CONTENT_TYPE,
        RESPONSE_HEADER_EXPIRES, SUBRESOURCE_IMG,SUBRESOURCE_STYLE,STYLE_NAME,
        SUBRESOURCE_REPLICATION, SUBRESOURCE_REPLICATION_PROGRESS,
        SUBRESOURCE_REPLICATION_LOCATION, SUBRESOURCE_CNAME,
        SUBRESOURCE_BUCKET_INFO, SUBRESOURCE_COMP, SUBRESOURCE_QOS,
        RESPONSE_CONTENT_TYPE, RESPONSE_CONTENT_LANGUAGE, RESPONSE_EXPIRES,
        RESPONSE_CACHE_CONTROL, RESPONSE_CONTENT_DISPOSITION, RESPONSE_CONTENT_ENCODING,
    });

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



    private final String endpoint;
    private final String accessKeyId;
    private final String accessKeySecret;
    private final String securityToken;

    public OSSClient(String endpoint, String accessKeyId, String accessKeySecret) {
        this(endpoint, accessKeyId, accessKeySecret, null);
    }

    public OSSClient(String endpoint, String accessKeyId, String accessKeySecret, String securityToken) {
        if (endpoint == null || endpoint.equals("")) {
            throw new IllegalArgumentException("Endpoint should not be null or empty.");
        }
        if (accessKeyId == null || accessKeyId.equals("")) {
            throw new IllegalArgumentException("Access key id should not be null or empty.");
        }
        if (accessKeySecret == null || accessKeyId.equals("")) {
            throw new IllegalArgumentException("Secret access key should not be null or empty.");
        }

        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.securityToken = securityToken;
    }

    // TODO 生成临时Security Token, 默认1小时即过期
    public String refreshSecurityToken(String roleArn, String sessionName, String policy) {
        return null;
    }

    /**
     * 生成上传或下载URL签名
     * @param method PUT或GET
     * @param bucketName
     * @param objectName
     * @param expires
     * @return
     */
    public String signLinkUrl(String method, String bucketName, String objectName, Date expires) {
        String data = method + "\n\n\n" + expires.getTime() + "\n"
                + getCanonicalizedResource(bucketName, objectName, null);
        return Base64.getEncoder().encodeToString(Crypto.hmacSha1(accessKeySecret.getBytes(), data.getBytes()));
    }

    /**
     * 生成OSS API请求Header签名
     * @param request
     * @param bucketName
     * @param objectName
     * @return
     */
    public String signHeader(HttpServletRequest request, String bucketName, String objectName) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getMethod() + '\n');
        String contentMd5 = request.getHeader("Content-MD5");
        sb.append(contentMd5 != null ? contentMd5 : "");
        sb.append('\n');
        String contentType = request.getHeader("Content-Type");
        sb.append(contentType != null ? contentType : "");
        sb.append('\n');
        String date = request.getHeader("Date");
        sb.append(date != null ? date : new SimpleDateFormat(RFC822_DATE_FORMAT).format(new Date()));
        sb.append('\n');

        // 处理 x-oss- 头
        TreeMap<String, String> headersToSign = new TreeMap<>();
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            if (header.startsWith(OSS_PREFIX)) {
                headersToSign.put(header, request.getHeader(header).trim());
            }
        }

        // 添加 x-oss- 头
        for (Map.Entry<String, String> entry : headersToSign.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.startsWith(OSS_PREFIX)) {
                sb.append(key).append(':').append(value);
            } else {
                sb.append(value);
            }
            sb.append('\n');
        }

        // 添加 CanonicalizedResource
        sb.append(getCanonicalizedResource(bucketName, objectName, request));
        return Base64.getEncoder().encodeToString(
                Crypto.hmacSha1(accessKeySecret.getBytes(), sb.toString().getBytes()));
    }

    public static String getCanonicalizedResource(String bucketName, String objectName, HttpServletRequest request) {
        StringBuilder builder = new StringBuilder("/");

        if (bucketName != null && !"".equals(bucketName)) {
            builder.append(bucketName + "/");
        }
        if (objectName != null && !"".equals(objectName)) {
            builder.append(objectName);
        }

        if (request != null) {
            List<String> params = Collections.list(request.getParameterNames());
            Collections.sort(params);
            char separator = '?';
            for (String key : params) {
                if (!SIGNED_PARAMTERS.contains(key)) {
                    continue;
                }

                builder.append(separator);
                builder.append(key);
                String paramValue = request.getParameter(key);
                if (paramValue != null) {
                    builder.append("=" + paramValue);
                }
                separator = '&';
            }
        }

        return builder.toString();
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public String getSecurityToken() {
        return securityToken;
    }

}
