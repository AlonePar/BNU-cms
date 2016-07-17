package cn.edu.bnu.cms.store;

/**
 * Created by dave on 16/7/13.
 */
public class OSSClient {
    private final String endpoint;
    private final OSSCredentials credentials;

    public OSSClient(String endpoint, OSSCredentials credentials) {
        this.endpoint = endpoint;
        this.credentials = credentials;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public OSSCredentials getCredentials() {
        return credentials;
    }
}
