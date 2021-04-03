# Library name
LIB_NAME = libtinyexpr
# Source files
SRC = tinyexpr.c
# Output directory
OUT_DIR = build
# Object files
OBJ_iphonesimulator_x86_64 = $(SRC:.c=_iphonesimulator_x86_64.o)
OBJ_iphoneos_arm64 = $(SRC:.c=_iphoneos_arm64.o)
OBJ_macosx_x86_64 = $(SRC:.c=_macosx_x86_64.o)
ALL_OBJS = $(OBJ_iphonesimulator_x86_64) $(OBJ_iphoneos_arm64) $(OBJ_macosx_x86_64)
# Output file
OUT = $(OUT_DIR)/$(LIB_NAME).a
# Include directories
INCLUDES = -Iinclude
# Compiler
CC = clang
# Platform setup
PLT_STP = xcrun -sdk

.PHONY: clean
.PHONY: directories
.PHONY: cleanup

default: directories $(OUT) cleanup

directories: ${OUT_DIR}

${OUT_DIR}:
	mkdir $@

$(OUT):$(ALL_OBJS)
	libtool -static -o $@ $^

$(OBJ_iphonesimulator_x86_64):$(SRC)
	$(PLT_STP) iphonesimulator $(CC) -arch x86_64 $(INCLUDES) -c $< -o $@

$(OBJ_iphoneos_arm64):$(SRC)
	$(PLT_STP) iphoneos $(CC) -arch arm64 $(INCLUDES) -c $< -o $@

$(OBJ_macosx_x86_64):$(SRC)
	$(PLT_STP) macosx $(CC) -arch x86_64 $(INCLUDES) -c $< -o $@

clean:
	rm -f $(ALL_OBJS) $(OUT) Makefile.bak
	rm -r -f ${OUT_DIR}

cleanup:
	rm -f $(ALL_OBJS)