package org.karlbennett.jni;

import org.karlbennett.jni.exception.IORuntimeException;

import java.io.*;

/**
 * User: karl
 * Date: 19/11/11
 * <p/>
 * Utility class that provide methods for loading and finding details for native JNI library files.
 * <p/>
 * The code for this has been heavily borrowed from the <code>NativeLoader</code> class created by Richard van der Hoff, the code for that
 * class can be found at the link below.
 *
 * @see <a href="http://opensource.mxtelecom.com/maven/repo/com/wapmx/native/mx-native-loader/1.2/">
 *      <code>NativeLoader.java</code></a>
 */
public class JniUtils {

    /**
     * The default location for native code within a JAR file.
     */
    public static final String DEFAULT_NATIVE_LIBRARY_JAR_DIR = "lib/";

    /**
     * The name of the Java temp dir property.
     */
    public static final String DEFAULT_TEMP_DIR_PROPERTY = "java.io.tmpdir";

    /**
     * The name of the property that is used to set the path for the directory where native files are stored within a JAR file.
     */
    public static final String NATIVE_LIBRARY_JAR_DIR_PROPERTY = "native.library.jar.dir";

    /**
     * The name of the property that is used to set the path for the directory where native files are extracted to the local file system.
     */
    public static final String NATIVE_LIBRARY_FS_DIR_PROPERTY = "native.library.fs.dir";


    /**
     * The default constructor is private because this class should never be instantiated.
     */
    private JniUtils() {
    }


    /**
     * Make sure that the given directory path ends with a trailing slash '/'.
     *
     * @param dirPath - the directory path to be checked.
     * @return the directory path with a guaranteed trailing slash.
     */
    private static String checkDirSlash(String dirPath) {

        // Make sure the dirPath is not null and has a trailing slash.
        return dirPath == null || dirPath.length() == 0 ? "" : dirPath.endsWith("/") ? dirPath : dirPath + '/';
    }


    /**
     * Load the native library with the provided name.
     * <p/>
     * This name could be the full name of the library e.g. <code>"libnative.so"</code>,<code> "libnative.dll"</code>,
     * <code>"libnative.dylib"</code>.
     * <p/>
     * Or the simple name minus the library prefix ans suffix e.g <code>"native"</code>.
     * <p/>
     * The native library is first extracting it to the provided directory on the local file system then loaded.
     *
     * @param jarDir  - the directory within the JAR where the native library files can be found.
     * @param fsDir   - the directory within the filesystem where the native library files will be extracted to
     * @param libName - the full or simple name of a native library.
     */
    public static void loadNativeLibrary(String jarDir, String fsDir, String libName) {

        // Get the full name of the native library file.
        libName = findNativeLibraryName(jarDir, libName);

        // If the native library wasn't found then blow up cause odds are nothing else is going to work from here on.
        // This is a runtime exception, which isn't the nicest thing in the world but I wanted to mirror the System.load() method as closely as
        // possible and that doesn't explicitly throw any exceptions.
        if (libName == null) throw new IORuntimeException(
                "org.karlbennett.jni.JniUtils.loadNativeLibrary(jarDir, fsDir, libName) - Unable to find library " +
                        libName + " on classpath");

        // Make sure the fsDir has a trailing slash.
        fsDir = checkDirSlash(fsDir);

        // Make sure that the native library extraction directory exists, if it doesn't try and create it.
        File fsLibraryDir = new File(fsDir);
        if (!fsLibraryDir.exists()) {

            if (!fsLibraryDir.mkdirs()) throw new IORuntimeException(
                    "org.karlbennett.jni.JniUtils.loadNativeLibrary(jarDir, fsDir, libName) - Unable to create the " +
                            "native library extraction directory: " + fsDir);

            // Otherwise if the directory already exists then make sure that we can write to it.
        } else if (!fsLibraryDir.canWrite()) throw new IORuntimeException(
                "org.karlbennett.jni.JniUtils.loadNativeLibrary(jarDir, fsDir, libName) - Unable to write to the " +
                        "native library extraction directory: " + fsDir);

        jarDir = checkDirSlash(jarDir); // Make sure the jarDir contains a trailing slash.

        String libJarPath = jarDir + libName; // Build the path to the native library file on the class path.

        try {

            // Get the input stream for the native library file.
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(libJarPath);

            File fsFile = new File(fsLibraryDir, libName); // Create the local file system native library file.

            OutputStream out = null; // Stream variables need to be outside the try so they can be accessed within the finally.

            try {
                // Then get the output stream for the local file system native library file.
                out = new FileOutputStream(fsFile);

                // Now that we are sure that we can extract the native library file write it to the local filesystem directory.
                writeFile(in, out);

                // Finally load the local file system native library file.
                System.load(fsFile.getAbsolutePath());

            } finally {

                if (in != null) in.close();
                if (out != null) out.close();
            }

        } catch (IOException e) {

            // If anything goes wrong here throw the resultant exception as our custom IO runtime exception.
            throw new IORuntimeException(e);
        }
    }

    /**
     * Convenience method, same as calling <code>loadNativeLibrary(getNativeLibraryJarDir(), getNativeLibraryFSDir(), "nativeLibFileName");</code>
     *
     * @param libName - the full or simple name of a native library.
     */
    public static void loadNativeLibrary(String libName) {

        loadNativeLibrary(getNativeLibraryJarDir(), getNativeLibraryFSDir(), libName);
    }

    /**
     * Convenience method, same as calling <code>findNativeLibraryName(getNativeLibraryJarDir(), "nativeLibFileName");</code>
     *
     * @param libName - the full or simple name of a native library.
     * @return the full file name of the native related native library if it exists, otherwise null.
     */
    public static String findNativeLibraryName(String libName) {

        return findNativeLibraryName(getNativeLibraryJarDir(), libName);
    }

    /**
     * Find the actual name of the provided library name within the class path under the provided director path.
     * <p/>
     * That is if the full name is provided ("libnative.so") and a file with that name can be found within the class path then the same name will be
     * returned ("libnative.so").
     * <p/>
     * Otherwise if a simple name is provided ("native") and a related native library file can be found then the full name for the provided simple
     * name is returned ("libnative.so").
     *
     * @param jarDir  - the directory within the JAR where the native library files can be found.
     * @param libName - the full or simple name of a native library.
     * @return the full file name of the native related native library if it exists, otherwise null.
     */
    public static String findNativeLibraryName(String jarDir, String libName) {

        // libName cannot be null, if it is something has gone very wrong so blow up, but at least with an informative message.
        if (libName == null) {

            throw new NullPointerException(
                    "org.karlbennett.jni.JniUtils.findNativeLibraryName(jarDir, libName) - libName cannot be null.");
        }

        // Make sure the jarDir has a trailing slash.
        jarDir = checkDirSlash(jarDir);

        // Build the path to the library file.
        String libPath = jarDir + libName;

        // If the provided path does not relate to an actual file within the class path then we will have to do some more in depth searching.
        if (Thread.currentThread().getContextClassLoader().getResource(libPath) == null) {

            // Odds are that if we couldn't find the library file with the provided libName then it must have been a simple name so find the full
            // library name and use that to create the path for the native library file.
            libName = System.mapLibraryName(libName);
            libPath = jarDir + libName;

            /*
                    Mac OS X dynamic libraries have the extension ".dylib", unfortunately under Mac OS X the
                    System.mapLibraryName(String) function returns a file name with the extension ".jnilib" which of course more often that not will not
                    be the extension of the dynamic library. So because of this we need to check if the ".jnilib" file actually exists, if it doesn't then
                    change the extension to ".dylib" and try again.
                    */
            final String MAC_OS_X_JNI_EXT = ".jnilib";
            final String MAC_OS_X_EXT = ".dylib";
            if (libName.endsWith(MAC_OS_X_JNI_EXT)) { // Are we looking for a Mac OS X native library?

                // If so does it exist as it's default ".jnilib" name?
                if (Thread.currentThread().getContextClassLoader().getResource(libPath) == null) {
                    // If not replace the extension with the normal Mac OS X dynamic library extension.
                    libName = libName.substring(0, libName.length() - MAC_OS_X_JNI_EXT.length()) + MAC_OS_X_EXT;
                    // Then recreate the library path.
                    libPath = jarDir + libName;
                }
            }

            // Now that we hopefully have the correct native library name see if a related resource exists within the class path.
            // If it doesn't clear the libPath variable because we can't return anything of use.
            if (Thread.currentThread().getContextClassLoader().getResource(libPath) == null) libName = null;
        }

        return libName;
    }

    /**
     * Returns the directory within the jar where the native JNI library files can be found.
     * <p/>
     * By default the directory is <code>"lib/"</code> but it can be overridden by setting the <code>"native.library.jar.dir"</code> Java property.
     *
     * @return the current globally set location for where native JNI files can be found within a JAR file.
     */
    public static String getNativeLibraryJarDir() {

        return System.getProperty(NATIVE_LIBRARY_JAR_DIR_PROPERTY, DEFAULT_NATIVE_LIBRARY_JAR_DIR);
    }

    /**
     * Returns the directory within the local filesystem where the native library files will be extracted before they are loaded.
     * <p/>
     * By default the directory is the systems default temp directory, this is the value set in the<code>"java.io.tmpdir"</code> Java property.
     * Though this can be overridden by setting the <code>"native.library.fs.dir"</code> Java property.
     *
     * @return the current globally set location for where native JNI files will be extracted.
     */
    public static String getNativeLibraryFSDir() {

        return System.getProperty(NATIVE_LIBRARY_FS_DIR_PROPERTY, System.getProperty(DEFAULT_TEMP_DIR_PROPERTY));
    }

    /**
     * Convenience method for writing an input stream out into an output stream.
     *
     * @param in  - the input stream that is to be read.
     * @param out - the output stream that is to be written to.
     * @throws IOException if there is a problem when reading and writing.
     */
    public static void writeFile(InputStream in, OutputStream out) throws IOException {

        final int ARRAY_SIZE = 1024; // Size of the input read buffer.

        int bytesRead = 0; // The number of bytes read in each iteration.

        byte[] bytes = new byte[ARRAY_SIZE]; // The buffer to store the bytes that are to be written.

        // Iterate writing bytes until we have read less than zero bytes which indicates the end of the input stream.
        while ((bytesRead = in.read(bytes)) > 0) {

            out.write(bytes, 0, bytesRead);
        }
    }
}
