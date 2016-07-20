package cn.edu.bnu.cms.test.store;

import cn.edu.bnu.cms.store.OSSClient;
import cn.edu.bnu.cms.store.OSSCredentials;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dave on 16/7/20.
 */
public class DeleteFileTest {
    String keyId = "0yDwwrETJRLzqteq";
    String keySecret = "ni2CHVEFzRb25zmXE7hTBJlxrsbNbw";

    @Test
    public void deleteFile() {
        OSSCredentials credentials = new OSSCredentials(keyId, keySecret);
        OSSClient client = new OSSClient(credentials);
        Assert.assertTrue(client.deleteFile("tu-in", "ab.jpg"));
    }
}
