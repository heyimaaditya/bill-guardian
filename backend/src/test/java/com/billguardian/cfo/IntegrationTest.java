package com.billguardian.cfo;

import com.billguardian.cfo.domain.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository repository;

    @Test
    void testFullUploadFlow() {
        // 1. Create a dummy file
        String csvContent = "Date,Amount,Description\n2026-02-08,100,Test Bill";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource fileAsResource = new ByteArrayResource(csvContent.getBytes()) {
            @Override
            public String getFilename() {
                return "test.csv";
            }
        };
        body.add("file", fileAsResource);

        // 2. Upload via API
        ResponseEntity<String> response = restTemplate.postForEntity("/api/statements/upload", new HttpEntity<>(body, headers), String.class);

        // 3. Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        
        // Wait for Kafka to process (Senior practice: Polling instead of Thread.sleep)
        long startTime = System.currentTimeMillis();
        while (repository.count() == 0 && System.currentTimeMillis() - startTime < 5000) {
            // wait...
        }

        assertThat(repository.count()).isGreaterThan(0);
    }
}
