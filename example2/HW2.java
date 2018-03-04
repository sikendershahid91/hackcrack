// Sikender Shahid

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.BadPaddingException;
import java.security.Key;
import java.security.MessageDigest;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HW2 {
    static void P1() throws Exception {
        byte[] iv = new byte[] { 0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0 };
        byte[] key = Files.readAllBytes(Paths.get("P1_key"));
        byte[] cipherText = Files.readAllBytes(Paths.get("P1_cipher.txt"));
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        // BEGIN SOLUTION
        Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);
        cipherText = cipher.doFinal(cipherText);
        byte[] plainText = cipherText;
        // END SOLUTION

        System.out.println(new String(plainText, StandardCharsets.UTF_8));
        System.out.println("-Problem 1 done-");
    }

    static void P2() throws Exception {
        byte[] cipherBMP = Files.readAllBytes(Paths.get("P2_cipher.bmp"));

        // BEGIN SOLUTION
        byte[] P1_plaintext = "The quick brown fox jumps over the lazy dog.".getBytes(StandardCharsets.UTF_8);

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] message = md5.digest(P1_plaintext);

        Cipher cipher = Cipher.getInstance("AES/ECB/ISO10126Padding");
        SecretKeySpec secretkey = new SecretKeySpec(message, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretkey);
        cipherBMP = cipher.doFinal(cipherBMP);
        byte[] plainBMP = cipherBMP;
        // END SOLUTION

        Files.write(Paths.get("P2_plain.bmp"), plainBMP);
        System.out.println("-Problem 2 done-");
    }

    static void P3() throws Exception {
        byte[] cipherBMP = Files.readAllBytes(Paths.get("P3_cipher.bmp"));
        byte[] P2plainBMP = Files.readAllBytes(Paths.get("P2_plain.bmp"));

        // BEGIN SOLUTION
        System.arraycopy(P2plainBMP, 0, cipherBMP, 0, 34);//34
        byte[] modifiedBMP = cipherBMP;
        // END SOLUTION

        Files.write(Paths.get("P3_cipher_modified.bmp"), modifiedBMP);
        System.out.println("-Problem 3 done-");
    }

    static void P4() throws Exception {
        byte[] iv = new byte[] { 0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0 };
        byte[] cipherBMP = Files.readAllBytes(Paths.get("P4_cipher.bmp"));
        byte[] P2plainBMP = Files.readAllBytes(Paths.get("P2_plain.bmp"));
        byte[] P3plainBMPkey = {65,6,39,69,115};

        System.out.println(cipherBMP.length);

        // BEGIN SOLUTION
        byte hour = 0;
        byte minute = 0;
        byte second = 0;
        byte[] key = new byte[] {   0,   0,    0,   0,
                0,   hour, minute, second,
                0,   0,    0,   0,
                0,   0,    0,   0 };
        System.arraycopy(P3plainBMPkey, 0, key, 0, 5);
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        // brute-forcing
        outer_loop:
        for(hour = 0 ; hour < 24 ; hour++){
            for(minute = 0; minute < 60; minute++){
                for(second = 0; second < 60; second++){
                    byte [] cipher_temp = new byte[cipherBMP.length];
                    key[5] = hour;
                    key[6] = minute;
                    key[7] = second;
                    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
                    Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
                    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam);
                    try{
                        cipher_temp = cipher.doFinal(cipherBMP);
                    }catch( BadPaddingException e){
                        //ignore
                    }
                    if(compareByteArray(cipher_temp,P2plainBMP, 6)){
                        cipherBMP = cipher_temp;
                        break outer_loop;
                    }
                }
            }
        }
        byte[] plainBMP = cipherBMP;
        // END SOLUTION
        Files.write(Paths.get("P4_plain.bmp"), plainBMP);
        System.out.println("-Problem 4 done-");
    }

    private static boolean compareByteArray(byte[] a1, byte[] a2, int length){
        // comparing two byte arrays and returning false if any dont match within the length
        for (int i = 0 ; i < length ; i++){
            if(a1[i] != a2[i])
                return false;
            else
                System.out.println("-------------------GOOD ---[ "+ i);// log feedback
        }
        return true;
    }

    public static void main(String [] args) {
        try {
            P1();
            P2();
            P3();
            P4();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
