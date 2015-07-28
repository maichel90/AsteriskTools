package co.com.pulxar.addons;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encriptar {

	public String generate(String algorim, String message)  {

        MessageDigest md;
        byte[] buffer;
        byte[] digest;
        String hash = "";

        try {
            buffer = message.getBytes();
            md = MessageDigest.getInstance(algorim);
            md.update(buffer);
            digest = md.digest();
            for (byte aux : digest) {
                int b = aux & 0xff;
                if (Integer.toHexString(b).length() == 1) {
                    hash += "0";
                }
                hash += Integer.toHexString(b);
            }
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("ERROR::001::  Error en el metodo principal de la clase Encriptar");
        }
        return hash;
    }
}
