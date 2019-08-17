package no.progconsult.aws.sqs;

/**
 * @author <a href="mailto:brynjar.norum@rejlers.no">Brynjar Norum</a> 2019-08-15.
 */
public enum AWSEnvironment {

    LAB("alias/embriq-flow", "embriq-flow.eu-west-1.965281606204", "embriq_admin_lab"),
    SYSTEST("alias/embriq-flow", "embriq-flow.eu-west-1.410767370853", "embriq_developer_test");

    private final String cmk;
    private final String bucket;
    private final String profile;

    AWSEnvironment(String cmk, String bucket, String profile) {
        this.cmk = cmk;
        this.bucket = bucket;
        this.profile = profile;
    }

    public String getBucket() {
        return bucket;
    }

    public String getCmk() {
        return cmk;
    }

    public String getProfile() {
        return profile;
    }

}
