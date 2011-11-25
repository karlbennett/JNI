#########################
# Setup the directories #
#########################

# The target directory of the maven build, all the compiled code will be placed under this directory.
TARGET = ../../../target/

# The directory that will contain the compiled java test classes.
TEST_CLASSES_DIR = $(TARGET)test-classes/

# The directory that will contain the generated JNI header files.
JNI_DIR = $(TARGET)jni/

# The directory that will contain the compiled native files.
LIB_DIR = $(TEST_CLASSES_DIR)lib/

# The other directory that will contain the compiled native files. This has been set up for testing.
OTHER_LIB_DIR = $(TEST_CLASSES_DIR)other-lib/


#######################
# Setup JNI compiling #
#######################

# The javah binary.
JAVAH = $(JAVA_HOME)bin/javah

# The package for the java JNI test classes.
TEST_JAVA_PACKAGE = org.karlbennett.jni.test

# The first java JNI test class.
TEST_JAVA_CLASS_ONE = $(TEST_JAVA_PACKAGE).JniTestClassOne

# The second java JNI test class.
TEST_JAVA_CLASS_TWO = $(TEST_JAVA_PACKAGE).JniTestClassTwo

# The command that will generate the JNI header files from the java JNI test classes.
JAVAH_CMD = $(JAVAH) -classpath $(TEST_CLASSES_DIR) -d $(JNI_DIR) $(TEST_JAVA_CLASS_ONE) $(TEST_JAVA_CLASS_TWO)


##########################
# Setup native compiling #
##########################

# The C++ compiler that will be used.
CCC = g++

# The required header files for the native code.
INCLUDES = -I$(JNI_DIR) -I$(JAVA_HOME)include/ -I$(JAVA_HOME)include/linux/

# The first C++ test source file.
SRC_FILE_ONE = JniTestClassOne.cpp

# The second C++ test source file.
SRC_FILE_TWO = JniTestClassTwo.cpp

# The compile command for the first C++ test source file. This places the source file into a non-standard lib directory.
COMPILE_NATIVE_ONE_CMD = $(CCC) -shared $(INCLUDES) $(SRC_FILE_ONE) -o $(OTHER_LIB_DIR)lib$(SRC_FILE_ONE:.cpp=.so)

# The compile command for the second C++ test source file. This places the source file into the standard lib directory.
COMPILE_NATIVE_TWO_CMD = $(CCC) -shared $(INCLUDES) $(SRC_FILE_TWO) -o $(LIB_DIR)lib$(SRC_FILE_TWO:.cpp=.so)


# The build.
all :
	$(JAVAH_CMD) # First generate the JNI header files because these are required to compile the C++ source files.
	$(COMPILE_NATIVE_ONE_CMD) # Compile the first C++ source file.
	$(COMPILE_NATIVE_TWO_CMD) # Compile the second C++ source file.

clean :
	rm -f $(JNI_DIR)*.h
	rm -f $(OTHER_LIB_DIR)*.so
	rm -f $(LIB_DIR)*.so
