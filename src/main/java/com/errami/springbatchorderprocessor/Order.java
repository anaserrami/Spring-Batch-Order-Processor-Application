package com.errami.springbatchorderprocessor;

public record Order(Long orderId, String customerName, Double amount) {
}