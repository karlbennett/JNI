##################
# Setup the directories #
##################

TARGET = ../../../target/

TEST_CLASSES_DIR = $(TARGET)test-classes/

JNI_DIR = $(TEST_CLASSES_DIR)jni/

LIB_DIR = $(TEST_CLASSES_DIR)lib/

OTHER_LIB_DIR = $(TEST_CLASSES_DIR)other-lib/


#################
# Setup JNI compiling #
#################

JAVAH = $(JAVA_HOME)bin/javah

TEST_JAVA_PACKAGE = org.karlbennett.jni.test

TEST_JAVA_CLASS_ONE = $(TEST_JAVA_PACKAGE).JniTestClassOne

TEST_JAVA_CLASS_TWO = $(TEST_JAVA_PACKAGE).JniTestClassTwo

JAVAH_CMD = $(JAVAH) -classpath $(TEST_CLASSES_DIR) -d $(JNI_DIR) $(TEST_JAVA_CLASS_ONE) $(TEST_JAVA_CLASS_TWO)


###################
# Setup native compiling #
###################

CCC = g++

INCLUDES = -I$(JNI_DIR) -I$(JAVA_HOME)include/ -I$(JAVA_HOME)include/linux/

SRC_FILE_ONE = JniTestClassOne.cpp

SRC_FILE_TWO = JniTestClassTwo.cpp

MAKE_OTHER_LIB_DIR_CMD = mkdir -p $(OTHER_LIB_DIR)

MAKE_LIB_DIR_CMD = mkdir -p $(LIB_DIR)

COMPILE_NATIVE_ONE_CMD = $(CCC) -shared $(INCLUDES) $(SRC_FILE_ONE) -o $(OTHER_LIB_DIR)lib$(SRC_FILE_ONE:.cpp=.so)

COMPILE_NATIVE_TWO_CMD = $(CCC) -shared $(INCLUDES) $(SRC_FILE_TWO) -o $(LIB_DIR)lib$(SRC_FILE_TWO:.cpp=.so)


all :
	$(JAVAH_CMD)
	$(MAKE_OTHER_LIB_DIR_CMD)
	$(COMPILE_NATIVE_ONE_CMD)
	$(MAKE_LIB_DIR_CMD)
	$(COMPILE_NATIVE_TWO_CMD)

clean :
	rm -f $(JNI_DIR)*.h
	rm -f $(OTHER_LIB_DIR)*.so
	rm -f $(LIB_DIR)*.so
