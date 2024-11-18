package com.api.the_chef_backend.util;

public class CpfCnpjValidatorUtil {

    public static boolean isValidCpfOrCnpj(String cpfOrCnpj) {
        if (cpfOrCnpj == null || cpfOrCnpj.isEmpty()) {
            return false;
        }

        int length = cpfOrCnpj.length();
        return length == 11 || length == 14;
    }
}