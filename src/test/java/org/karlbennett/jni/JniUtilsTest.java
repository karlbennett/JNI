package org.karlbennett.jni;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.karlbennett.jni.JniUtils.*;

/**
 * User: karl
 * Date: 19/11/11
 */
public class JniUtilsTest {

    /**
     *
     * @throws Exception
     */
    @Test
    public void testLoadNativeLibrary() throws Exception {


    }

    @Test
    public void testLoadNativeLibraryWithJustLibName() throws Exception {

    }

    @Test
    public void testFindNativeLibraryName() throws Exception {

    }

    @Test
    public void testFindNativeLibraryNameWithJustLibName() throws Exception {

    }

    @Test
    public void testGetNativeLibraryJarDir() throws Exception {

        assertEquals("default native library jar dir incorrect", DEFAULT_NATIVE_LIBRARY_JAR_DIR, getNativeLibraryJarDir());
    }

    @Test
    public void testGetNativeLibraryJarDirWithPropertySet() throws Exception {

        final String TEST_DIR = "test dir";

        // Record the old value so that this test doesn't effect subsequent tests.
        final String OLD_VALUE = System.getProperty(NATIVE_LIBRARY_JAR_DIR_PROPERTY);

        System.setProperty(NATIVE_LIBRARY_JAR_DIR_PROPERTY, TEST_DIR);

        assertEquals("set native library jar dir incorrect", TEST_DIR, getNativeLibraryJarDir());

        // Reset the system property back to it's old value.
        if (OLD_VALUE != null) System.setProperty(NATIVE_LIBRARY_JAR_DIR_PROPERTY, OLD_VALUE);
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
        if (OLD_VALUE != null) System.setProperty(NATIVE_LIBRARY_JAR_DIR_PROPERTY, OLD_VALUE);
    }

    @Test
    public void testWriteFile() throws Exception {

    }
}
