package com.api.the_chef_backend.util;

public class CpfCnpjValidatorUtil {

    public static boolean isValidCpfOrCnpj(String cpfOrCnpj) {
        if (cpfOrCnpj == null || cpfOrCnpj.isEmpty()) {
            return true;
        }

        return cpfOrCnpj.length() != 11 && cpfOrCnpj.length() != 14;
    }
}