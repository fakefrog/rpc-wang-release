package com.wang.common.demo.domain;

import lombok.Data;

@Data
public class ExpResponse {

    private long value;
    private long costInNanos;

    public ExpResponse() {
    }

    public ExpResponse(long value, long costInNanos) {
        this.value = value;
        this.costInNanos = costInNanos;
    }

}
