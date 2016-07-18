package cn.edu.bnu.cms.store;

import cn.edu.bnu.cms.common.Crypto;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.edu.bnu.cms.store.OSSResponseHeaders.*;
import static com.aliyun.oss.internal.RequestParameters.*;
import static com.aliyun.oss.model.ResponseHeaderOverrides.*;

/**
 * Created by dave on 16/7/17.
 */
public class OSSSigner {

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

    private OSSCredentials credentials;

    public OSSSigner(OSSCredentials credentials) {
        this.credentials = credentials;
    }

    public String signPostObjectPolicy(String policy) {
        return Base64.getEncoder().encodeToString(Crypto.hmacSha1(
                credentials.getAccessKeySecret().getBytes(), policy.getBytes()));
    }

    /**
     * 生成上传或下载URL签名, 不能使用刷新Token方式生成的key进行URL签名, URL签名不需再URLEncode,直接拼接入URL查询字符串中即可
     * @param method PUT或GET
     * @param bucket
     * @param key
     * @param expires
     * @return
     */
    public String signLinkUrl(String method, String bucket, String key, Date expires) {
        String data = method + "\n\n\n" + expires.getTime() + "\n"
                + getCanonicalizedResource(bucket, key, null);
        System.out.println(data);
        return Base64.getEncoder().encodeToString(Crypto.hmacSha1(
                credentials.getAccessKeySecret().getBytes(), data.getBytes()));
    }

    /**
     * 生成OSS API请求Header签名
     * @param request
     * @param bucket
     * @param key
     * @return
     */
    public String signHeader(HttpServletRequest request, String bucket, String key) {
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
            String k = entry.getKey();
            Object value = entry.getValue();

            if (k.startsWith(OSS_PREFIX)) {
                sb.append(k).append(':').append(value);
            } else {
                sb.append(value);
            }
            sb.append('\n');
        }

        // 添加 CanonicalizedResource
        sb.append(getCanonicalizedResource(bucket, key, request));
        return Base64.getEncoder().encodeToString(
                Crypto.hmacSha1(credentials.getAccessKeySecret().getBytes(), sb.toString().getBytes()));
    }

    public static String getCanonicalizedResource(String bucket, String key, HttpServletRequest request) {
        StringBuilder builder = new StringBuilder("/");

        if (bucket != null && !"".equals(bucket)) {
            builder.append(bucket + "/");
        }
        if (key != null && !"".equals(key)) {
            builder.append(key);
        }

        if (request != null) {
            List<String> params = Collections.list(request.getParameterNames());
            Collections.sort(params);
            char separator = '?';
            for (String k : params) {
                if (!SIGNED_PARAMTERS.contains(k)) {
                    continue;
                }

                builder.append(separator);
                builder.append(k);
                String paramValue = request.getParameter(k);
                if (paramValue != null) {
                    builder.append("=" + paramValue);
                }
                separator = '&';
            }
        }

        return builder.toString();
    }
}
