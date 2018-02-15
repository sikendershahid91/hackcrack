package com.test;
import java.math.*;

public class HW1_P1 {
    private static final String cipherText = "GWN GANBDJAN HWNDG ZD EJAZNK WNAN:\n" +
            "DBZI QARL DFNINGRO ZDIBOK GR GWN NBDG-DRJGWNBDG.\n" +
            "BQGNA GPR KBVD, VRJ PZII QZOK B KNDNAGNK ZDIBOK.\n" +
            "HIZLE GR GWN WZTWNDG URZOG RQ GWN ZDIBOK BOK IRRF QRA B IBATN HIZQQ GR GWN ORAGW.\n" +
            "PBIF B WJOKANK VBAKD GRPBAKD GWN HIZQQ BOK DGRU BG GWN GBII GANN.\n" +
            "PBIF GNO QNNG GR GWN NBDG, BOK VRJ PZII QZOK B ARHF PZGW BO S UBZOGNK RO ZG.\n" +
            "GWN GANBDJAN ZD EJAZNK RON QRRG ENIRP GWN ARHF.\n";

    static final int alphaIndex = (int) 'A';
    static final int alphaLength = (int) 'Z' + 1 - (int) 'A';

    // needs to be filled out automatically based on the ciphertext
    static int[] frequency = new int[alphaLength];
    static int[] key_2 = new int[26];
    static int[] key_1 = new int[26];


    private static int decrypt(int index) {
        int _key_1 = 3;
        int _key_2 = 1;
        index = mod((invmod(_key_1, 26)*(index-_key_2)),26);
        return index;
    }

    private static void calculate_frequency(int index){
        frequency[index] +=1;
    }

    private static void set_keys(){
        for(int i =0 ; i < 26 ; i++){
            key_2[i] = i;
            if(i%2 != 0)
                key_1[i] = i;
            else
                key_1[i] = -1;
        }
    }

    private static void brute_check_keys(int cipher_num, int plain_num){
        for(int i = 0; i < 26 ; i++){
            for(int j = 0; j < 26; j++){
                if(key_1[j] != -1 && key_1[j] != 13) {
                   System.out.println(key_1[j]);
//                    if(cipher_num == mod(plain_num*key_1[j]+key_2[i],26)){
//                        System.out.println("found");
//                    }
                    if(plain_num == mod((invmod(key_1[j],26)*(cipher_num-key_2[i])),26))
                        System.out.println("found");
                }
            }
        }
    }

    private static void linear_pair_check_keys(int c1, int c2, int p1, int p2){
        // 2 value needs to be greater
        int k1 = mod((c1-c2)*invmod((p1-p2),26),26);
        int k2 = mod(c1-(p1*k1), 26);
        System.out.println(k1 + ","+ k2);
    }

    private static int mod(int a, int m){
        return ((a%m+m)%m);
    }

    private static int invmod(int a, int m){
        if(a%2==0){
            System.out.println("INVALID INVERSE OPERATION");
            return 0;
        }
        BigInteger _a = new BigInteger(Integer.toString(a));
        BigInteger _m = new BigInteger(Integer.toString(m));
        return (_a.modInverse(_m)).intValue();
    }

    public static void main(String [] args) {
        set_keys();

        for (char cipherChar : cipherText.toCharArray())
            if (Character.isLetter(cipherChar)) { // only letters are encrypted, punctuation marks and whitespace are not
                // following line converts letters to numbers between 0 and 25
                int cipher = (int) cipherChar - alphaIndex;
                calculate_frequency(cipher);
                int plain = decrypt(cipher);
                // following line coverts numbers between 0 and 25 to letters
                char plainChar = (char) (plain + alphaIndex);
                System.out.print(plainChar);
            }
            else
                System.out.print(cipherChar);

        for(int i = 0; i < alphaLength; i++){
            System.out.println(i + ":" + frequency[i]);
        }
        // most common single letter word used are a and i
        // most frequent in ascending order: E T A O I N
        // two letter words starting with A: AN, AT
        // guesses C:P
        //     B:A 1:0
        //     B:I 1:8
        // guesses two letter words: (c1,c2):(p1,p2)
        //     BO:AN   1,14:0,13
        //     BO:AT   1,14:0,19
        //     BO:IN   1.14:8,13
        //     BO:IT   1,14:8,19


//        linear_pair_check_keys(1,14,0,13);
//        linear_pair_check_keys(14,1,13,0);
        linear_pair_check_keys(1,14,0,19);
        linear_pair_check_keys(14,1,19,0);
        linear_pair_check_keys(1,14,8,13);
        linear_pair_check_keys(14,1,13,8);
        linear_pair_check_keys(1,14,8,19);
        linear_pair_check_keys(14,1,19,8);

        // most frequent letters are in descending order: 6,13,1 translate: G,N,B
        // guesses two letter words: (c1,c2):(p1,p2)
        //     GR:AN   6,17:0,13
        //     GR:AT   6,17:0,19
        //     GR:IN   6,17:8,13
        //     GR:IT   6,17:8,19
        linear_pair_check_keys(6,17,0,19);
        linear_pair_check_keys(17,6,19,0);
        linear_pair_check_keys(6,17,8,13);
        linear_pair_check_keys(17,6,13,8);
        linear_pair_check_keys(6,17,8,19);
        linear_pair_check_keys(17,6,19,8);

        // GWN -> THE
        linear_pair_check_keys(6,13,19,4);

        // wow LOL, this was good


    }
}
