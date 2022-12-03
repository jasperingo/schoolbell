package com.jasper.schoolbell.services;

import com.password4j.Password;

import javax.security.enterprise.identitystore.PasswordHash;

public class PasswordHashService implements PasswordHash {
    @Override
    public String generate(char[] password) {
        return Password.hash(String.valueOf(password)).withBcrypt().getResult();
    }

    @Override
    public boolean verify(char[] password, String hash) {
        return Password.check(String.valueOf(password), hash).withBcrypt();
    }
}
