package it.feio.android.omninotes.async.keepimporthelper;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class googleKeepZipExtractorTest {
    protected googleKeepZipExtractor extractor1, extractor2, extractor3;

    // Loading the ZIP file(s) for testing
    ClassLoader classLoader = getClass().getClassLoader();
    URL notKeep = classLoader.getResource("dummyZipNotKeep.zip");
    URL Keep = classLoader.getResource("dummyZipKeep.zip");
    URL keepWithData = classLoader.getResource("keepTakeoutTest.zip");

    public googleKeepZipExtractorTest() {
        extractor1 = new googleKeepZipExtractor(notKeep.getPath());
        extractor2 = new googleKeepZipExtractor(Keep.getPath());
        extractor3 = new googleKeepZipExtractor(keepWithData.getPath());
    }

    @Test
    public void test_keepVerify() throws IOException {
        assertFalse(extractor1.verifyKeepFolder());
        assertTrue(extractor2.verifyKeepFolder());
        assertTrue(extractor3.verifyKeepFolder());
    }

}