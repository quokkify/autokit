package io.automation.websockets.client;

import java.time.Instant;

public record WsMessage(String payload, Instant receivedAt) { }
