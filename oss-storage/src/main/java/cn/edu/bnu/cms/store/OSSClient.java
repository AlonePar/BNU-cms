package cn.edu.bnu.cms.store;

import cn.edu.bnu.cms.common.NetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dave on 16/7/13.
 */
public class OSSClient {
    private final OSSCredentials credentials;

    private static final Logger LOG = LoggerFactory.getLogger(OSSClient.class);

    public static final String OSSHost = "http://tu-in.oss-cn-hangzhou.aliyuncs.com";

    public OSSClient(OSSCredentials credentials) {
        this.credentials = credentials;
    }

    public boolean deleteFile(String bucket, String key) {
        Map<String, String> headers = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat(NetUtil.GMT_DATE_FORMAT, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = sdf.format(new Date());
        headers.put("Date", date);
        String sign = new OSSSigner(credentials).signHeader("DELETE", bucket, key, headers, null);
        HttpURLConnection connection = null;
        System.out.println(date);
        System.out.println("OSS " + credentials.getAccessKeyId() + ":" + sign);

        try {
            URL url = new URL(OSSHost + "/" + key);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("DELETE");
            connection.setUseCaches(false);
            connection.setRequestProperty("Date", date);
            connection.setRequestProperty("Authorization", "OSS " + credentials.getAccessKeyId() + ":" + sign);
            connection.connect();
            System.out.println(connection.getResponseMessage());
            return connection.getResponseCode() == 204;
        } catch (IOException e) {
            LOG.error(e.toString());
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public OSSCredentials getCredentials() {
        return credentials;
    }
}
