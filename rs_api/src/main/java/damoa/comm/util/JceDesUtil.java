package damoa.comm.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JceDesUtil {
    private static JceDesUtil ourInstance = new JceDesUtil();
    private Cipher cipher;
    private SecretKey key;
    private static String secret = "tvnw63ufg9gh5392"; // secret key length must be 16
    public static JceDesUtil getInstance() {
        return ourInstance;
    }

    private JceDesUtil() {
        init();
    }

    private void init() {
        try {
            key = new SecretKeySpec("1234567890654321".getBytes(), "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encode(String text) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(text.getBytes("UTF-8"));
        return  MyBase64.encode(cipherText);
    }

    public String decode(String text) throws Exception {
        byte[] encypted = MyBase64.decode(text);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(encypted),"UTF-8");
    }

    public static void main(String[] args) throws Exception {
        String value = JceDesUtil.getInstance().encode("damoa12343");
        System.out.println(value);

        System.out.println(JceDesUtil.getInstance().decode(value));
    }
}
