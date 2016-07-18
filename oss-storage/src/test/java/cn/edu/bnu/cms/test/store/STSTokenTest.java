package cn.edu.bnu.cms.test.store;

import cn.edu.bnu.cms.common.Crypto;
import cn.edu.bnu.cms.store.OSSCredentials;
import cn.edu.bnu.cms.store.OSSSigner;
import org.junit.Test;

import java.util.Base64;
import java.util.Date;

/**
 * Created by dave on 16/7/15.
 */
public class STSTokenTest {
    String keyId = "0yDwwrETJRLzqteq";
    String keySecret = "ni2CHVEFzRb25zmXE7hTBJlxrsbNbw";

    /**
     * { "expiration": "2050-12-01T12:00:00.000Z",
     *   "conditions": [
     *      {"bucket": "tu-in"}
     *   ]
     * }
     */
    String policy = "eyAiZXhwaXJhdGlvbiI6ICIyMDUwLTEyLTAxVDEyOjAwOjAwLjAwMFoiLAogICJjb25kaXRpb25zIjogWwogICAgeyJidWNrZXQiOiAidHUtaW4ifQogIF0KfQ==";

    @Test
    public void testRefreshToken() throws Exception {
        OSSCredentials credentials = OSSCredentials.refreshCredentials(new OSSCredentials(keyId, keySecret),
                "acs:ram::1859365503745150:role/aliyunosstokengeneratorrole");
        System.out.println(credentials);
        System.out.println(Base64.getEncoder().encodeToString(Crypto.hmacSha1(
                credentials.getAccessKeySecret().getBytes(), policy.getBytes())));
    }

    @Test
    public void testSignUrl() throws Exception {
        OSSCredentials credentials = new OSSCredentials(keyId, keySecret);
        System.out.println(credentials);
        OSSSigner signer = new OSSSigner(credentials);
        Date date = new Date(new Date().getTime() + 3600 * 1000);
        String sign = signer.signLinkUrl("GET", "tu-in", "ab.jpg", date);
        System.out.println(sign);
        System.out.println(date.getTime());
    }

}
