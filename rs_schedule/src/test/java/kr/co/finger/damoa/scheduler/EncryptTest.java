package kr.co.finger.damoa.scheduler;

import com.finger.shinhandamoa.typehandlers.util.EncryptUtil;
import org.junit.Test;

public class EncryptTest {
    @Test
    public void test1() {

        String r = "03E912360B1191AA9A8A150D7A5F7C45";
        System.out.println("!! "+EncryptUtil.decrypt(r));
        System.out.println("?? "+EncryptUtil.isEncrypted(r));



    }
}
