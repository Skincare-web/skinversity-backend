package com.skinversity.backend.Exceptions;

public class EmptyCart extends RuntimeException {
    public EmptyCart(String message) {
        super(message);
    }
}
