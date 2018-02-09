package com.example.spudydev.spudy.infraestrutura;

public class MpooAppException extends Exception {
    public MpooAppException(String msg) {
        super(msg);
    }
    public MpooAppException(String msg, Throwable cause) {
        super(msg, cause);
    }
}