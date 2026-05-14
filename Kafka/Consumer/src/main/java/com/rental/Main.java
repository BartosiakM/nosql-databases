package com.rental;

import com.rental.kafka.ConsumerGroup;

public class Main {
    public static void main(String[] args) {
        ConsumerGroup consumerGroup = new ConsumerGroup();
        consumerGroup.init();
        consumerGroup.groupConsume();
    }

}
