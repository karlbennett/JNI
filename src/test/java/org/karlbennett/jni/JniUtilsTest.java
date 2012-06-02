package org.karlbennett.jni;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.karlbennett.jni.exception.IORuntimeException;
import org.karlbennett.jni.test.JniTestClassOne;
import org.karlbennett.jni.test.JniTestClassTwo;

import java.io.*;

import static org.junit.Assert.*;
import static org.karlbennett.jni.JniUtils.*;

/**
 * User: karl
 * Date: 19/11/11
 */
public class JniUtilsTest {

    private static final String TEST_STRING_ONE = "Test string from native JniTestClassOne.";

    private static final String TEST_STRING_TWO = "Test string from native JniTestClassTwo.";

    private static final String OTHER_LIB_DIR = "other-lib/";

    private static final String OTHER_TMP_DIR = "/tmp/jni-test/";

    private static final String TEST_CLASS_ONE_NAME = JniTestClassOne.class.getSimpleName();

    private static final String TEST_CLASS_TWO_NAME = JniTestClassTwo.class.getSimpleName();

    private static final String TEST_COPY_FILE_NAME = "TestFile.txt";


    private static String readLines(BufferedReader reader) throws IOException {

        String line;
        StringBuilder lines = new StringBuilder();

        while ((line = reader.readLine()) != null) lines.append(line).append('\n');

        lines.replace(lines.length() - 1, lines.length(), "");

        return lines.toString();
    }


    @Before
    public void setup() throws IOException {

        File otherTempDir = new File(OTHER_TMP_DIR);

        if (!otherTempDir.mkdirs()) throw new IOException("Could not create other temp directory.");
    }

    @After
    public void tearDown() throws IOException {

        File otherTempDir = new File(OTHER_TMP_DIR);

        for (File file : otherTempDir.listFiles()) {

            if (!file.delete()) throw new IOException("Could not delete file: " + file.getAbsolutePath());
        }

        if (!otherTempDir.delete()) throw new IOException("Could not delete other temp directory.");
    }

    @Test
    public void testGetNativeLibraryJarDir() throws Exception {

        assertEquals("default native library jar dir incorrect", DEFAULT_NATIVE_LIBRARY_JAR_DIR, getNativeLibraryJarDir());
    }

    @Test
    public void testGetNativeLibraryJarDirWithPropertySet() throws Exception {

        final String TEST_DIR = "test dir";

        // Record the old value so that this test doesn't affect subsequent tests.
        final String OLD_VALUE = System.getProperty(NATIVE_LIBRARY_JAR_DIR_PROPERTY);

        System.setProperty(NATIVE_LIBRARY_JAR_DIR_PROPERTY, TEST_DIR);

        assertEquals("set native library jar dir incorrect", TEST_DIR, getNativeLibraryJarDir());

        // Reset the system property back to it's old value.
        if (OLD_VALUE != null) System.setProperty(NATIVE_LIBRARY_JAR_DIR_PROPERTY, OLD_VALUE);
        else System.clearProperty(NATIVE_LIBRARY_JAR_DIR_PROPERTY);
    }

    @Test
    public void testGetNativeLibraryFSDir() throws Exception {

        final String DEFAULT_TEMPT_DIR = System.getProperty(DEFAULT_TEMP_DIR_PROPERTY);

        assertEquals("default local fs library dir incorrect", DEFAULT_TEMPT_DIR, getNativeLibraryFSDir());
    }

    @Test
    public void testGetNativeLibraryFSDirWithPropertySet() throws Exception {

        final String TEST_DIR = "test dir";

        final String DEFAULT_TEMPT_DIR = System.getProperty(DEFAULT_TEMP_DIR_PROPERTY);

        // Record the old value so that this test doesn't effect subsequent tests.
        final String OLD_VALUE = System.getProperty(NATIVE_LIBRARY_FS_DIR_PROPERTY);

        System.setProperty(NATIVE_LIBRARY_FS_DIR_PROPERTY, TEST_DIR);

        assertEquals("set local fs library dir incorrect", TEST_DIR, getNativeLibraryFSDir());

        assertEquals("default dir has changed", DEFAULT_TEMPT_DIR, System.getProperty(DEFAULT_TEMP_DIR_PROPERTY));

        // Reset the system property back to it's old value.
        if (OLD_VALUE != null) System.setProperty(NATIVE_LIBRARY_FS_DIR_PROPERTY, OLD_VALUE);
        else System.clearProperty(NATIVE_LIBRARY_FS_DIR_PROPERTY);
    }

    @Test
    public void testWriteFile() throws Exception {

        InputStream in = new BufferedInputStream(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_COPY_FILE_NAME));

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String lines = readLines(reader);

        reader.close();
        in.close();

        in = new BufferedInputStream(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_COPY_FILE_NAME));

        String outFileName = OTHER_TMP_DIR + TEST_COPY_FILE_NAME;

        OutputStream out = new FileOutputStream(outFileName);

        writeFile(in, out);

        reader = new BufferedReader(new InputStreamReader(new FileInputStream(outFileName)));

        String copiedLines = readLines(reader);

        reader.close();
        in.close();
        out.close();

        assertEquals("file not copied correctly", lines.toString(), copiedLines.toString());
    }

    @Test
    public void testFindNativeLibraryName() throws Exception {

        String name = findNativeLibraryName(OTHER_LIB_DIR, TEST_CLASS_ONE_NAME);

        assertNotNull("library name not found", name);
        assertEquals("library name incorrect", System.mapLibraryName(TEST_CLASS_ONE_NAME), name);
    }

    @Test
    public void testFindNativeLibraryNameWithNullDir() throws Exception {

        assertNull(findNativeLibraryName(null, TEST_CLASS_ONE_NAME));
    }

    @Test(expected = NullPointerException.class)
    public void testFindNativeLibraryNameWithNullLibName() throws Exception {

        assertNull(findNativeLibraryName(OTHER_LIB_DIR, null));
    }

    @Test
    public void testFindNativeLibraryNameWithJustLibName() throws Exception {

        String name = findNativeLibraryName(TEST_CLASS_TWO_NAME);

        assertNotNull("library name not found", name);
        assertEquals("library name incorrect", System.mapLibraryName(TEST_CLASS_TWO_NAME), name);
    }

    @Test
    public void testLoadNativeLibrary() throws Exception {

        loadNativeLibrary(OTHER_LIB_DIR, OTHER_TMP_DIR, TEST_CLASS_ONE_NAME);

        assertEquals("incorrect string returned from native method", TEST_STRING_ONE, JniTestClassOne.nativeMethod());
    }

    @Test
    public void testLoadNativeLibraryWithJustLibName() throws Exception {

        loadNativeLibrary(JniTestClassTwo.class.getSimpleName());

        assertEquals("incorrect string returned from native method", TEST_STRING_TWO, JniTestClassTwo.nativeMethod());
    }

    @Test(expected = NullPointerException.class)
    public void testLoadNativeLibraryWithNullLibName() throws Exception {

        loadNativeLibrary(null);
    }

    @Test(expected = IORuntimeException.class)
    public void testLoadNativeLibraryWithBadLibName() throws Exception {

        loadNativeLibrary("this lib should not exist");
    }
}
