##################
# Setup the directories #
##################

TARGET = ../../../target/

TEST_CLASSES_DIR = $(TARGET)test-classes/

JNI_DIR = $(TARGET)jni/

LIB_DIR = $(TARGET)lib/

OTHER_LIB_DIR = $(TARGET)other-lib/


#################
# Setup JNI compiling #
#################

JAVAH = $(JAVA_HOME)bin/javah

TEST_JAVA_PACKAGE = org.karlbennett.jni.test

TEST_JAVA_CLASS_ONE = $(TEST_JAVA_PACKAGE).JniTestClassOne

TEST_JAVA_CLASS_TWO = $(TEST_JAVA_PACKAGE).JniTestClassTwo

JAVAH_CMD = $(JAVAH) -classpath $(TEST_CLASSES_DIR) -d $(JNI_DIR) $(TEST_JAVA_CLASS_ONE) $(TEST_JAVA_CLASS_TWO)


all :
	$(JAVAH_CMD)

clean :
	rm -f $(JNI_DIR)*.h
