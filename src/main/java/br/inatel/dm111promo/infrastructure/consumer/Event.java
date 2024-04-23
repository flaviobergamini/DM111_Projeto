package br.inatel.dm111promo.infrastructure.consumer;

public record Event(EventType type, Operation operation, SuperMarketListMessage data) {
}
