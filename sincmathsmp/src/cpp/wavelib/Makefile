# Library name
LIB_NAME = libwavelib
# Source files
SRC = wt_helpers.c conv.c cwt.c cwtmath.c hsfft.c real.c wavefilt.c wavefunc.c wavelib.c wtmath.c
# Output directory
OUT_DIR = build
# Object files
OBJ_iphonesimulator_x86_64 = $(SRC:.c=_iphonesimulator_x86_64.o)
OBJ_iphoneos_arm64 = $(SRC:.c=_iphoneos_arm64.o)
ALL_OBJS = $(OBJ_iphonesimulator_x86_64) $(OBJ_iphoneos_arm64)
# Output file
OUT = $(OUT_DIR)/$(LIB_NAME).a
# Include directories
INCLUDES = -Iinclude
# Xcode setup
XCODE_STP = xcrun -sdk
# Compiler
CC = clang
# Compiler flags
FLAGS_IOS = -arch arm64 -mios-version-min=9.0 -fembed-bitcode
FLAGS_IOS_SIM = -arch x86_64 -mios-version-min=9.0 -fembed-bitcode

.PHONY: clean
.PHONY: directories
.PHONY: cleanup

default: directories $(OUT) cleanup

directories: ${OUT_DIR}

${OUT_DIR}:
	mkdir $@

$(OUT):$(ALL_OBJS)
	libtool -static -o $@ $^

$(OBJ_iphonesimulator_x86_64):%_iphonesimulator_x86_64.o:%.c
	$(XCODE_STP) iphonesimulator $(CC) $(FLAGS_IOS_SIM) $(INCLUDES) -c $< -o $@

$(OBJ_iphoneos_arm64):%_iphoneos_arm64.o:%.c
	$(XCODE_STP) iphoneos $(CC) $(FLAGS_IOS) $(INCLUDES) -c $< -o $@

clean:
	rm -f $(ALL_OBJS) $(OUT) Makefile.bak
	rm -r -f ${OUT_DIR}

cleanup:
	rm -f $(ALL_OBJS)