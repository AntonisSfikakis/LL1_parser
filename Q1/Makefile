SRC_DIR = src
GEN_DIR = gen
TEST_DIR = tests
TEST_RESULT_DIR = $(TEST_DIR)/results

all: compile

compile: | $(GEN_DIR)
	@ javac -d $(GEN_DIR) $(SRC_DIR)/*.java

run: compile
	@ java -cp $(GEN_DIR) Main

test: compile | $(TEST_RESULT_DIR)
	@ for test in $(TEST_DIR)/input/*.txt; do \
		name=$$(basename $$test .txt); \
		echo "=== $$name ==="; \
		java -cp $(GEN_DIR) Main < $$test > $(TEST_RESULT_DIR)/$$name.txt; \
		cat $(TEST_RESULT_DIR)/$$name.txt; \
	done

clean:
	@ rm -rf $(GEN_DIR) $(TEST_RESULT_DIR)

$(GEN_DIR):
	@ mkdir -p $@

$(TEST_RESULT_DIR):
	@ mkdir -p $@
