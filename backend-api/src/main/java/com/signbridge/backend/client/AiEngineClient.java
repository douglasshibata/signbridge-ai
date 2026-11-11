package com.signbridge.backend.client;

import com.signbridge.backend.model.KeypointRequest;
import com.signbridge.backend.model.PredictionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ai-engine", url = "${AI_ENGINE_URL:http://localhost:8000}")
public interface AiEngineClient {

    @PostMapping("/predict")
    PredictionResponse predict(@RequestBody List<KeypointRequest> data);
}
