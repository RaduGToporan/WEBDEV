package com.green.marketplace.user;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Codec {
    private SCryptPasswordEncoder encoder;

    public Codec() {
        this.encoder = new SCryptPasswordEncoder(64, 8, 1, 32, 16);
    }

    public SCryptPasswordEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(SCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }
}
