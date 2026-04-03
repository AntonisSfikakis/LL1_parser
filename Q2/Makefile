LIB_DIR = ../lib
JFLEX = $(LIB_DIR)/jflex-full-1.9.1.jar
JCUP_RUNTIME = $(LIB_DIR)/java-cup-11b-runtime.jar
JCUP = $(LIB_DIR)/java-cup-11b.jar

OUTPUT_DIR = output
GEN_DIR = gen
TEST_DIR = tests
TEST_RESULT_DIR = $(TEST_DIR)/results
TEST_JAVA_CODE = $(TEST_DIR)/tests_java_code

all: compile

compile: | $(GEN_DIR)
	@ java -jar $(JFLEX) -d $(GEN_DIR) src/scanner.flex  > /dev/null 2>&1
	@ java -jar $(JCUP) -parser Parser -symbols sym -expect 3 -nowarn -destdir $(GEN_DIR) src/parser.cup   2>/dev/null
	@ javac -cp $(JCUP_RUNTIME) -d $(GEN_DIR) src/Runner.java $(GEN_DIR)/Parser.java $(GEN_DIR)/Scanner.java $(GEN_DIR)/sym.java  2>/dev/null

run: compile | $(OUTPUT_DIR)
	@ java -cp $(GEN_DIR):$(JCUP_RUNTIME) Runner > $(OUTPUT_DIR)/Main.java 
	@ javac $(OUTPUT_DIR)/Main.java
	@ java -cp $(OUTPUT_DIR) Main

test: compile | $(TEST_RESULT_DIR) $(TEST_JAVA_CODE)
	@ for test in $(TEST_DIR)/input/*.txt; do \
			name=$$(basename $$test .txt); \
			java -cp $(GEN_DIR):$(JCUP_RUNTIME) Runner < $$test > $(TEST_RESULT_DIR)/Main.java; \
			javac $(TEST_RESULT_DIR)/Main.java; \
			echo "=== $$name ==="; \
			java -cp $(TEST_RESULT_DIR) Main > $(TEST_RESULT_DIR)/$$name.txt; \
			cat $(TEST_RESULT_DIR)/$$name.txt; \
			mv $(TEST_RESULT_DIR)/Main.java $(TEST_JAVA_CODE)/$$name.java; \
	done
	@ rm -rf $(TEST_RESULT_DIR)/Main.class

clean:
	@ rm -rf $(GEN_DIR) $(OUTPUT_DIR) $(TEST_RESULT_DIR) $(TEST_JAVA_CODE)

$(OUTPUT_DIR):
	@ mkdir -p $@

$(GEN_DIR):
	@ mkdir -p $@

$(TEST_RESULT_DIR):
	@ mkdir -p $@

$(TEST_JAVA_CODE):
	@ mkdir -p $@

