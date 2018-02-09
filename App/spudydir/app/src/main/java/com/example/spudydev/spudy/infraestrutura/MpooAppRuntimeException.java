package com.example.spudydev.spudy.infraestrutura;

/**
 * Created by gabri on 22/01/2018.
 */

public class MpooAppRuntimeException extends RuntimeException {
    public MpooAppRuntimeException(String msg) {
        super(msg);
    }
    public MpooAppRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
