package no.progconsult.secureclient;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
public class HomeRestController {

    private transient static final Logger LOG = getLogger(HomeRestController.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/")
	public String home() throws RestClientException, URISyntaxException{
        String forObject = restTemplate.getForObject(new URI("https://localhost:8443"), String.class);

        LOG.info("Response: {}", forObject);
        return forObject;
	}
	
}
