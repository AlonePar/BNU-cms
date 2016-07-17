package cn.edu.bnu.cms.store;

/**
 * Created by dave on 16/7/13.
 */
public class OSSClient {
    private final OSSCredentials credentials;

    public OSSClient(OSSCredentials credentials) {
        this.credentials = credentials;
    }


    public OSSCredentials getCredentials() {
        return credentials;
    }
}
