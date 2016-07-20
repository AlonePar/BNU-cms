package cn.edu.bnu.cms.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by dave on 16/7/15.
 */
public class NetUtil {
    // RFC 822 Date Format
    public static final String RFC822_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

    // GMT Date Format
    public static final String GMT_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";

    // ISO8601 Date Format
    public static final String ISO08601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String buildQueryString(Map<String, String> params) throws IOException {
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
                query.append(name).append("=").append(value);
            }
        }
        return query.toString();
    }

    public static String percentEncode(String value) {
        String ret = value;
        if (ret != null) {
            try {
                ret = URLEncoder.encode(ret, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return ret;
    }
}
