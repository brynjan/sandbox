package no.progconsult.springbootsqs.health;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Properties;

@RestController
public class HealthController {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(HealthController.class);

    private final String mavenGroupId = "no.progconsult";
    private final String mavenArtifactId = "spring-boot-sqs";
    private final String version;
    private final String runningSince;

    public HealthController() {
        version = getVersion();
        runningSince = getRunningSince();
    }

    @RequestMapping("${health.path}")
    public HashMap index() {
        HashMap<Object, Object> healthProperties = new HashMap<>();
        healthProperties.put("service", mavenArtifactId);
        healthProperties.put("timestamp", Instant.now().toString());
        healthProperties.put("version", version);
        healthProperties.put("runningSince", runningSince);
        return healthProperties;
    }

    private String getVersion() {
        Properties mavenProperties = new Properties();
        String resourcePath = "/META-INF/maven/" + this.mavenGroupId + "/" + this.mavenArtifactId + "/pom.properties";
        URL mavenVersionResource = this.getClass().getResource(resourcePath);
        if (mavenVersionResource != null) {
            try {
                mavenProperties.load(mavenVersionResource.openStream());
                return mavenProperties.getProperty("version", "missing version info in " + resourcePath);
            } catch (IOException var5) {
                LOG.warn("Problem reading version resource from classpath: ", var5);
            }
        }

        return "(DEV VERSION)";
    }

    private String getRunningSince() {
        long uptimeInMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        return Instant.now().minus(uptimeInMillis, ChronoUnit.MILLIS).toString();
    }

}
