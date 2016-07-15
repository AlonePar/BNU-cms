package test;

import cn.edu.bnu.cms.store.OSSCredentials;
import cn.edu.bnu.cms.store.STSToken;
import org.junit.Test;

/**
 * Created by dave on 16/7/15.
 */
public class STSTokenTest {

    @Test
    public void testRefreshToken() {
        String keyId = "0yDwwrETJRLzqteq";
        String keySecret = "ni2CHVEFzRb25zmXE7hTBJlxrsbNbw";

        OSSCredentials credentials = STSToken.refreshCredentials(new OSSCredentials(keyId, keySecret),
                "acs:ram::1859365503745150:role/aliyunosstokengeneratorrole");
        System.out.println(credentials);
    }

}
