package io.automation.tyrus.client;

import java.time.Instant;

public record WsMessage(String payload, Instant receivedAt) { }
