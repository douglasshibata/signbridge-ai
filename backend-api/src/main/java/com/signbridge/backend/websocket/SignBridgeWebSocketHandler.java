package com.signbridge.backend.websocket;

import com.signbridge.backend.client.AiEngineClient;
import com.signbridge.backend.model.KeypointRequest;
import com.signbridge.backend.model.PredictionResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.List;

@Component
public class SignBridgeWebSocketHandler extends TextWebSocketHandler {

    private final AiEngineClient aiEngineClient;

    public SignBridgeWebSocketHandler(AiEngineClient aiEngineClient) {
        this.aiEngineClient = aiEngineClient;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // In a real implementation, 'message' would contain frame data or keypoints.
        // Here we mock the processing and call the AI Engine.

        KeypointRequest mockRequest = new KeypointRequest(Collections.singletonList(0.0));
        PredictionResponse response = aiEngineClient.predict(List.of(mockRequest));

        session.sendMessage(new TextMessage("Prediction: " + response.sign() + " (" + response.confidence() + ")"));
    }
}
