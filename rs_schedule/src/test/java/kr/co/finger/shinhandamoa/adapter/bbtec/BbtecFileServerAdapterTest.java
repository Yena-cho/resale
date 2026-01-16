package kr.co.finger.shinhandamoa.adapter.bbtec;

import kr.co.finger.shinhandamoa.test.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@Import(BbtecFileServerAdapter.class)
public class BbtecFileServerAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BbtecFileServerAdapterTest.class);

    @Autowired
    private BbtecFileServerAdapter bbtecFileServerAdapter;

    @Test
    public void store() throws IOException {
        File tempFile = File.createTempFile("bbtec-", ".tmp");
        bbtecFileServerAdapter.store(tempFile);
    }
}