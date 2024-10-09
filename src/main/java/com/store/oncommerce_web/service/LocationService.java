package com.store.oncommerce_web.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class LocationService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    public List<Object> searchLocation(String query) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_URL)
                .queryParam("q", query)
                .queryParam("format", "json")
                .queryParam("addressdetails", 1)
                .queryParam("limit", 5)
                .toUriString();

        List<Object> response = restTemplate.getForObject(url, List.class);
        return response;
    }
}

