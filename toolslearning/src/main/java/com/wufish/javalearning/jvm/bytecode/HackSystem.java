package com.wufish.javalearning.jvm.bytecode;

import sun.reflect.CallerSensitive;
import sun.security.util.SecurityConstants;

import java.io.*;
import java.util.Properties;
import java.util.PropertyPermission;

/**
 * The type Hack system.
 *
 * @Author wzj
 * @Create time : 2018/07/03 14:17
 * @Description:
 */
public class HackSystem {
    /**
     * The constant in.
     */
    public static final InputStream in = System.in;
    private static ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    /**
     * The constant out.
     */
    public static final PrintStream out = new PrintStream(buffer);
    /**
     * The constant err.
     */
    public static final PrintStream err = out;

    /**
     * Gets buffer string.
     *
     * @return the buffer string
     */
    public static String getBufferString() {
        return buffer.toString();
    }

    /**
     * Clear buffer.
     */
    public static void clearBuffer() {
        buffer.reset();
    }

    /**
     * Sets security manager.
     *
     * @param securityManager the security manager
     */
    public static void setSecurityManager(final SecurityManager securityManager) {
        System.setSecurityManager(securityManager);
    }

    /**
     * Gets security manager.
     *
     * @return the security manager
     */
    public static SecurityManager getSecurityManager() {
        return getSecurityManager();
    }

    /**
     * Current time mills long.
     *
     * @return the long
     */
    public static long currentTimeMills() {
        return System.currentTimeMillis();
    }

    /**
     * Array copy.
     *
     * @param src    the src
     * @param srcPos the src pos
     * @param dest   the dest
     * @param desPos the des pos
     * @param length the length
     */
    public static void arrayCopy(Object src, int srcPos, Object dest, int desPos, int length) {
        System.arraycopy(src, srcPos, dest, desPos, length);
    }

    /**
     * Identity hash code int.
     *
     * @param x the x
     * @return the int
     */
    public static int identityHashCode(Object x) {
        return System.identityHashCode(x);
    }

    private static Properties props;

    private static native Properties initProperties(Properties props);

    /**
     * Determines the current system properties.
     * <p>
     * First, if there is a security manager, its
     * <code>checkPropertiesAccess</code> method is called with no
     * arguments. This may result in a security exception.
     * <p>
     * The current set of system properties for use by the
     * {@link #getProperty(String)} method is returned as a
     * <code>Properties</code> object. If there is no current set of
     * system properties, a set of system properties is first created and
     * initialized. This set of system properties always includes values
     * for the following keys:
     * <table summary="Shows property keys and associated values">
     * <tr><th>Key</th>
     * <th>Description of Associated Value</th></tr>
     * <tr><td><code>java.version</code></td>
     * <td>Java Runtime Environment version</td></tr>
     * <tr><td><code>java.vendor</code></td>
     * <td>Java Runtime Environment vendor</td></tr>
     * <tr><td><code>java.vendor.url</code></td>
     * <td>Java vendor URL</td></tr>
     * <tr><td><code>java.home</code></td>
     * <td>Java installation directory</td></tr>
     * <tr><td><code>java.vm.specification.version</code></td>
     * <td>Java Virtual Machine specification version</td></tr>
     * <tr><td><code>java.vm.specification.vendor</code></td>
     * <td>Java Virtual Machine specification vendor</td></tr>
     * <tr><td><code>java.vm.specification.name</code></td>
     * <td>Java Virtual Machine specification name</td></tr>
     * <tr><td><code>java.vm.version</code></td>
     * <td>Java Virtual Machine implementation version</td></tr>
     * <tr><td><code>java.vm.vendor</code></td>
     * <td>Java Virtual Machine implementation vendor</td></tr>
     * <tr><td><code>java.vm.name</code></td>
     * <td>Java Virtual Machine implementation name</td></tr>
     * <tr><td><code>java.specification.version</code></td>
     * <td>Java Runtime Environment specification  version</td></tr>
     * <tr><td><code>java.specification.vendor</code></td>
     * <td>Java Runtime Environment specification  vendor</td></tr>
     * <tr><td><code>java.specification.name</code></td>
     * <td>Java Runtime Environment specification  name</td></tr>
     * <tr><td><code>java.class.version</code></td>
     * <td>Java class format version number</td></tr>
     * <tr><td><code>java.class.path</code></td>
     * <td>Java class path</td></tr>
     * <tr><td><code>java.library.path</code></td>
     * <td>List of paths to search when loading libraries</td></tr>
     * <tr><td><code>java.io.tmpdir</code></td>
     * <td>Default temp file path</td></tr>
     * <tr><td><code>java.compiler</code></td>
     * <td>Name of JIT compiler to use</td></tr>
     * <tr><td><code>java.ext.dirs</code></td>
     * <td>Path of extension directory or directories
     * <b>Deprecated.</b> <i>This property, and the mechanism
     * which implements it, may be removed in a future
     * release.</i> </td></tr>
     * <tr><td><code>os.name</code></td>
     * <td>Operating system name</td></tr>
     * <tr><td><code>os.arch</code></td>
     * <td>Operating system architecture</td></tr>
     * <tr><td><code>os.version</code></td>
     * <td>Operating system version</td></tr>
     * <tr><td><code>file.separator</code></td>
     * <td>File separator ("/" on UNIX)</td></tr>
     * <tr><td><code>path.separator</code></td>
     * <td>Path separator (":" on UNIX)</td></tr>
     * <tr><td><code>line.separator</code></td>
     * <td>Line separator ("\n" on UNIX)</td></tr>
     * <tr><td><code>user.name</code></td>
     * <td>User's account name</td></tr>
     * <tr><td><code>user.home</code></td>
     * <td>User's home directory</td></tr>
     * <tr><td><code>user.dir</code></td>
     * <td>User's current working directory</td></tr>
     * </table>
     * <p>
     * Multiple paths in a system property value are separated by the path
     * separator character of the platform.
     * <p>
     * Note that even if the security manager does not permit the
     * <code>getProperties</code> operation, it may choose to permit the
     * {@link #getProperty(String)} operation.
     *
     * @return the system properties
     * @throws SecurityException if a security manager exists and its                           <code>checkPropertiesAccess</code> method doesn't allow access                           to the system properties.
     * @see #setProperties #setProperties
     * @see java.lang.SecurityException
     * @see java.lang.SecurityManager#checkPropertiesAccess() java.lang.SecurityManager#checkPropertiesAccess()
     * @see java.util.Properties
     */
    public static Properties getProperties() {
        SecurityManager sm = getSecurityManager();
        if (sm != null) {
            sm.checkPropertiesAccess();
        }

        return props;
    }

    /**
     * Returns the system-dependent line separator string.  It always
     * returns the same value - the initial value of the {@linkplain
     * #getProperty(String) system property}* {@code line.separator}.
     * <p>
     * <p>On UNIX systems, it returns {@code "\n"}; on Microsoft
     * Windows systems it returns {@code "\r\n"}.
     *
     * @return the system-dependent line separator string
     * @since 1.7
     */
    public static String lineSeparator() {
        return lineSeparator;
    }

    private static String lineSeparator;

    /**
     * Sets the system properties to the <code>Properties</code>
     * argument.
     * <p>
     * First, if there is a security manager, its
     * <code>checkPropertiesAccess</code> method is called with no
     * arguments. This may result in a security exception.
     * <p>
     * The argument becomes the current set of system properties for use
     * by the {@link #getProperty(String)} method. If the argument is
     * <code>null</code>, then the current set of system properties is
     * forgotten.
     *
     * @param props the new system properties.
     * @throws SecurityException if a security manager exists and its                           <code>checkPropertiesAccess</code> method doesn't allow access                           to the system properties.
     * @see #getProperties #getProperties
     * @see java.util.Properties
     * @see java.lang.SecurityException
     * @see java.lang.SecurityManager#checkPropertiesAccess() java.lang.SecurityManager#checkPropertiesAccess()
     */
    public static void setProperties(Properties props) {
        SecurityManager sm = getSecurityManager();
        if (sm != null) {
            sm.checkPropertiesAccess();
        }
        if (props == null) {
            props = new Properties();
            initProperties(props);
        }
        HackSystem.props = props;
    }

    /**
     * Gets the system property indicated by the specified key.
     * <p>
     * First, if there is a security manager, its
     * <code>checkPropertyAccess</code> method is called with the key as
     * its argument. This may result in a SecurityException.
     * <p>
     * If there is no current set of system properties, a set of system
     * properties is first created and initialized in the same manner as
     * for the <code>getProperties</code> method.
     *
     * @param key the name of the system property.
     * @return the string value of the system property, or <code>null</code> if there is no property with that key.
     * @throws SecurityException        if a security manager exists and its                                  <code>checkPropertyAccess</code> method doesn't allow                                  access to the specified system property.
     * @throws NullPointerException     if <code>key</code> is                                  <code>null</code>.
     * @throws IllegalArgumentException if <code>key</code> is empty.
     * @see #setProperty #setProperty
     * @see java.lang.SecurityException
     * @see java.lang.SecurityManager#checkPropertyAccess(java.lang.String) java.lang.SecurityManager#checkPropertyAccess(java.lang.String)
     * @see java.lang.System#getProperties() java.lang.System#getProperties()
     */
    public static String getProperty(String key) {
        checkKey(key);
        SecurityManager sm = getSecurityManager();
        if (sm != null) {
            sm.checkPropertyAccess(key);
        }

        return props.getProperty(key);
    }

    /**
     * Gets the system property indicated by the specified key.
     * <p>
     * First, if there is a security manager, its
     * <code>checkPropertyAccess</code> method is called with the
     * <code>key</code> as its argument.
     * <p>
     * If there is no current set of system properties, a set of system
     * properties is first created and initialized in the same manner as
     * for the <code>getProperties</code> method.
     *
     * @param key the name of the system property.
     * @param def a default value.
     * @return the string value of the system property, or the default value if there is no property with that key.
     * @throws SecurityException        if a security manager exists and its                                  <code>checkPropertyAccess</code> method doesn't allow                                  access to the specified system property.
     * @throws NullPointerException     if <code>key</code> is                                  <code>null</code>.
     * @throws IllegalArgumentException if <code>key</code> is empty.
     * @see #setProperty #setProperty
     * @see java.lang.SecurityManager#checkPropertyAccess(java.lang.String) java.lang.SecurityManager#checkPropertyAccess(java.lang.String)
     * @see java.lang.System#getProperties() java.lang.System#getProperties()
     */
    public static String getProperty(String key, String def) {
        checkKey(key);
        SecurityManager sm = getSecurityManager();
        if (sm != null) {
            sm.checkPropertyAccess(key);
        }

        return props.getProperty(key, def);
    }

    /**
     * Sets the system property indicated by the specified key.
     * <p>
     * First, if a security manager exists, its
     * <code>SecurityManager.checkPermission</code> method
     * is called with a <code>PropertyPermission(key, "write")</code>
     * permission. This may result in a SecurityException being thrown.
     * If no exception is thrown, the specified property is set to the given
     * value.
     * <p>
     *
     * @param key   the name of the system property.
     * @param value the value of the system property.
     * @return the previous value of the system property, or <code>null</code> if it did not have one.
     * @throws SecurityException        if a security manager exists and its                                  <code>checkPermission</code> method doesn't allow                                  setting of the specified property.
     * @throws NullPointerException     if <code>key</code> or                                  <code>value</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>key</code> is empty.
     * @see #getProperty #getProperty
     * @see java.lang.System#getProperty(java.lang.String) java.lang.System#getProperty(java.lang.String)
     * @see java.lang.System#getProperty(java.lang.String, java.lang.String) java.lang.System#getProperty(java.lang.String, java.lang.String)
     * @see java.util.PropertyPermission
     * @see SecurityManager#checkPermission SecurityManager#checkPermission
     * @since 1.2
     */
    public static String setProperty(String key, String value) {
        checkKey(key);
        SecurityManager sm = getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new PropertyPermission(key,
                    SecurityConstants.PROPERTY_WRITE_ACTION));
        }

        return (String) props.setProperty(key, value);
    }

    /**
     * Removes the system property indicated by the specified key.
     * <p>
     * First, if a security manager exists, its
     * <code>SecurityManager.checkPermission</code> method
     * is called with a <code>PropertyPermission(key, "write")</code>
     * permission. This may result in a SecurityException being thrown.
     * If no exception is thrown, the specified property is removed.
     * <p>
     *
     * @param key the name of the system property to be removed.
     * @return the previous string value of the system property, or <code>null</code> if there was no property with that key.
     * @throws SecurityException        if a security manager exists and its                                  <code>checkPropertyAccess</code> method doesn't allow                                  access to the specified system property.
     * @throws NullPointerException     if <code>key</code> is                                  <code>null</code>.
     * @throws IllegalArgumentException if <code>key</code> is empty.
     * @see #getProperty #getProperty
     * @see #setProperty #setProperty
     * @see java.util.Properties
     * @see java.lang.SecurityException
     * @see java.lang.SecurityManager#checkPropertiesAccess() java.lang.SecurityManager#checkPropertiesAccess()
     * @since 1.5
     */
    public static String clearProperty(String key) {
        checkKey(key);
        SecurityManager sm = getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new PropertyPermission(key, "write"));
        }

        return (String) props.remove(key);
    }

    private static void checkKey(String key) {
        if (key == null) {
            throw new NullPointerException("key can't be null");
        }
        if (key.equals("")) {
            throw new IllegalArgumentException("key can't be empty");
        }
    }

    /**
     * Terminates the currently running Java Virtual Machine. The
     * argument serves as a status code; by convention, a nonzero status
     * code indicates abnormal termination.
     * <p>
     * This method calls the <code>exit</code> method in class
     * <code>Runtime</code>. This method never returns normally.
     * <p>
     * The call <code>System.exit(n)</code> is effectively equivalent to
     * the call:
     * <blockquote><pre>
     * Runtime.getRuntime().exit(n)
     * </pre></blockquote>
     *
     * @param status exit status.
     * @throws SecurityException if a security manager exists and its <code>checkExit</code>                           method doesn't allow exit with the specified status.
     * @see java.lang.Runtime#exit(int) java.lang.Runtime#exit(int)
     */
    public static void exit(int status) {
        Runtime.getRuntime().exit(status);
    }

    /**
     * Runs the garbage collector.
     * <p>
     * Calling the <code>gc</code> method suggests that the Java Virtual
     * Machine expend effort toward recycling unused objects in order to
     * make the memory they currently occupy available for quick reuse.
     * When control returns from the method call, the Java Virtual
     * Machine has made a best effort to reclaim space from all discarded
     * objects.
     * <p>
     * The call <code>System.gc()</code> is effectively equivalent to the
     * call:
     * <blockquote><pre>
     * Runtime.getRuntime().gc()
     * </pre></blockquote>
     *
     * @see java.lang.Runtime#gc() java.lang.Runtime#gc()
     */
    public static void gc() {
        Runtime.getRuntime().gc();
    }

    /**
     * Runs the finalization methods of any objects pending finalization.
     * <p>
     * Calling this method suggests that the Java Virtual Machine expend
     * effort toward running the <code>finalize</code> methods of objects
     * that have been found to be discarded but whose <code>finalize</code>
     * methods have not yet been run. When control returns from the
     * method call, the Java Virtual Machine has made a best effort to
     * complete all outstanding finalizations.
     * <p>
     * The call <code>System.runFinalization()</code> is effectively
     * equivalent to the call:
     * <blockquote><pre>
     * Runtime.getRuntime().runFinalization()
     * </pre></blockquote>
     *
     * @see java.lang.Runtime#runFinalization() java.lang.Runtime#runFinalization()
     */
    public static void runFinalization() {
        Runtime.getRuntime().runFinalization();
    }

    /**
     * Enable or disable finalization on exit; doing so specifies that the
     * finalizers of all objects that have finalizers that have not yet been
     * automatically invoked are to be run before the Java runtime exits.
     * By default, finalization on exit is disabled.
     * <p>
     * <p>If there is a security manager,
     * its <code>checkExit</code> method is first called
     * with 0 as its argument to ensure the exit is allowed.
     * This could result in a SecurityException.
     *
     * @param value indicating enabling or disabling of finalization
     * @throws SecurityException if a security manager exists and its <code>checkExit</code>                           method doesn't allow the exit.
     * @see java.lang.Runtime#exit(int) java.lang.Runtime#exit(int)
     * @see java.lang.Runtime#gc() java.lang.Runtime#gc()
     * @see java.lang.SecurityManager#checkExit(int) java.lang.SecurityManager#checkExit(int)
     * @since JDK1.1
     * @deprecated This method is inherently unsafe.  It may result in finalizers being called on live objects while other threads are concurrently manipulating those objects, resulting in erratic behavior or deadlock.
     */
    @Deprecated
    public static void runFinalizersOnExit(boolean value) {
        Runtime.runFinalizersOnExit(value);
    }

    /**
     * Loads the native library specified by the filename argument.  The filename
     * argument must be an absolute path name.
     * <p>
     * If the filename argument, when stripped of any platform-specific library
     * prefix, path, and file extension, indicates a library whose name is,
     * for example, L, and a native library called L is statically linked
     * with the VM, then the JNI_OnLoad_L function exported by the library
     * is invoked rather than attempting to load a dynamic library.
     * A filename matching the argument does not have to exist in the
     * file system.
     * See the JNI Specification for more details.
     * <p>
     * Otherwise, the filename argument is mapped to a native library image in
     * an implementation-dependent manner.
     * <p>
     * <p>
     * The call <code>System.load(name)</code> is effectively equivalent
     * to the call:
     * <blockquote><pre>
     * Runtime.getRuntime().load(name)
     * </pre></blockquote>
     *
     * @param filename the file to load.
     * @throws SecurityException    if a security manager exists and its                              <code>checkLink</code> method doesn't allow                              loading of the specified dynamic library
     * @throws UnsatisfiedLinkError if either the filename is not an                              absolute path name, the native library is not statically                              linked with the VM, or the library cannot be mapped to                              a native library image by the host system.
     * @throws NullPointerException if <code>filename</code> is                              <code>null</code>
     * @see java.lang.Runtime#load(java.lang.String) java.lang.Runtime#load(java.lang.String)
     * @see java.lang.SecurityManager#checkLink(java.lang.String) java.lang.SecurityManager#checkLink(java.lang.String)
     */
    @CallerSensitive
    public static void load(String filename) {
        Runtime.getRuntime().load(filename);
    }

    /**
     * Loads the native library specified by the <code>libname</code>
     * argument.  The <code>libname</code> argument must not contain any platform
     * specific prefix, file extension or path. If a native library
     * called <code>libname</code> is statically linked with the VM, then the
     * JNI_OnLoad_<code>libname</code> function exported by the library is invoked.
     * See the JNI Specification for more details.
     * <p>
     * Otherwise, the libname argument is loaded from a system library
     * location and mapped to a native library image in an implementation-
     * dependent manner.
     * <p>
     * The call <code>System.loadLibrary(name)</code> is effectively
     * equivalent to the call
     * <blockquote><pre>
     * Runtime.getRuntime().loadLibrary(name)
     * </pre></blockquote>
     *
     * @param libname the name of the library.
     * @throws SecurityException    if a security manager exists and its                              <code>checkLink</code> method doesn't allow                              loading of the specified dynamic library
     * @throws UnsatisfiedLinkError if either the libname argument                              contains a file path, the native library is not statically                              linked with the VM,  or the library cannot be mapped to a                              native library image by the host system.
     * @throws NullPointerException if <code>libname</code> is                              <code>null</code>
     * @see java.lang.Runtime#loadLibrary(java.lang.String) java.lang.Runtime#loadLibrary(java.lang.String)
     * @see java.lang.SecurityManager#checkLink(java.lang.String) java.lang.SecurityManager#checkLink(java.lang.String)
     */
    @CallerSensitive
    public static void loadLibrary(String libname) {
        Runtime.getRuntime().loadLibrary(libname);
    }

    /**
     * Maps a library name into a platform-specific string representing
     * a native library.
     *
     * @param libname the name of the library.
     * @return a platform-dependent native library name.
     * @throws NullPointerException if <code>libname</code> is                              <code>null</code>
     * @see java.lang.System#loadLibrary(java.lang.String) java.lang.System#loadLibrary(java.lang.String)
     * @see java.lang.ClassLoader#findLibrary(java.lang.String) java.lang.ClassLoader#findLibrary(java.lang.String)
     * @since 1.2
     */
    public static native String mapLibraryName(String libname);

    /**
     * Create PrintStream for stdout/err based on encoding.
     */
    private static PrintStream newPrintStream(FileOutputStream fos, String enc) {
        if (enc != null) {
            try {
                return new PrintStream(new BufferedOutputStream(fos, 128), true, enc);
            } catch (UnsupportedEncodingException uee) {
            }
        }
        return new PrintStream(new BufferedOutputStream(fos, 128), true);
    }

    private static native void setIn0(InputStream in);
    private static native void setOut0(PrintStream out);
    private static native void setErr0(PrintStream err);
}
