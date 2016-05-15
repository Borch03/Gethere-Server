package pl.edu.agh.gethere;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.gethere.model.Triple;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by Dominik on 14.05.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GethereServerApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=9000")
public class TripleControllerIntegrationTest {

    private RestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void getTriple() {
        ResponseEntity<Triple> entity = restTemplate.getForEntity("http://localhost:9000/triple", Triple.class);

        assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
    }

}
