package Cripto;

public class CriptoCES {

    public CriptoCES() {

    }

    public static String criptografaCesar(String s) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            result += (char) (s.charAt(i) + 1);
        }
        return result;
    }

    /* decifra a cifra de cesar */
    public static String descriptografaCesar(String s) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            result += (char) (s.charAt(i) - 1);
        }
        return result;
    }

}