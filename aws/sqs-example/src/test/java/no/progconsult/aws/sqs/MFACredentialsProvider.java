package no.progconsult.aws.sqs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;

import javax.swing.*;

public class MFACredentialsProvider implements AWSCredentialsProvider {

    private final String profileName;
    private static AWSCredentials credentials;

    public MFACredentialsProvider(String profileName) {
        this.profileName = profileName;
        if (credentials == null) {
            createCredentials();
        }
    }

    @Override
    public AWSCredentials getCredentials() {
        return credentials;
    }

    @Override
    public void refresh() {
        createCredentials();
    }

    private void createCredentials() {
        ProfilesConfigFile profilesConfigFile = new ProfilesConfigFile();
        AWSCredentials longLivedCredentials = profilesConfigFile.getCredentials("default");

        BasicProfile profile = profilesConfigFile.getAllBasicProfiles().get(profileName);
        if (profile == null) {
            throw new RuntimeException("Profile '" + profileName + "' not found in ~/.aws/credentials");
        }

        String mfaArn = profile.getPropertyValue("mfa_serial");
        if (mfaArn == null) {
            throw new RuntimeException("Property 'mfa_serial' not found for profile '" + profileName + "' in ~/.aws/credentials");
        }

        String roleArn = profile.getPropertyValue("role_arn");
        if (roleArn == null) {
            throw new RuntimeException("Property 'role_arn' not found for profile '" + profileName + "' in ~/.aws/credentials");
        }

        String mfaCode = JOptionPane.showInputDialog(null, "Enter MFA code", "Multi Factor Authentication", JOptionPane.QUESTION_MESSAGE);

        AWSSecurityTokenService sts = AWSSecurityTokenServiceClientBuilder.standard()
                                                                          .withCredentials(new AWSStaticCredentialsProvider(longLivedCredentials))
                                                                          .withRegion(Regions.EU_WEST_1)
                                                                          .build();

        AssumeRoleResult result = sts.assumeRole(new AssumeRoleRequest().withRoleArn(roleArn)
                                                                        .withRoleSessionName("qf-systemtest")
                                                                        .withTokenCode(mfaCode)
                                                                        .withSerialNumber(mfaArn));

        credentials = new BasicSessionCredentials(result.getCredentials().getAccessKeyId(),
                                                  result.getCredentials().getSecretAccessKey(),
                                                  result.getCredentials().getSessionToken());
    }
}
