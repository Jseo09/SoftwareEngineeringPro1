
public class CodeParser {

    private String code;

    public CodeParser(String code) {
        this.code = code;
    }

    public String analyze() {
        String[] lines = code.split("\n");
        int ifCount = 0, loopCount = 0, tryCatchCount = 0;
        int inputCount = 0, outputCount = 0, comparisonCount = 0, operationCount = 0;
        int nestedLoopLevel = 0, maxNestedLoopLevel = 0;
        int nestedConditionLevel = 0, maxNestedConditionLevel = 0;

        boolean inTryBlock = false; // Track whether we are inside a try block

        for (String line : lines) {
            line = line.trim();

            // Skip comments and empty lines
            if (line.isEmpty() || line.startsWith("//")) {
                continue;
            }

            // Detect if/else conditions
            if (line.startsWith("if")) {
                ifCount++;
                nestedConditionLevel++;
                maxNestedConditionLevel = Math.max(maxNestedConditionLevel, nestedConditionLevel);
            }

            // Detect comparisons in conditions
            comparisonCount += countComparisons(line);

            // Detect loops (for/while)
            if (line.startsWith("for") || line.startsWith("while")) {
                loopCount++;
                nestedLoopLevel++;
                maxNestedLoopLevel = Math.max(maxNestedLoopLevel, nestedLoopLevel);
            }

            // Detect try-catch blocks
            if (line.contains("try")) {
                if (!inTryBlock) {
                    tryCatchCount++;
                    inTryBlock = true; // Entering a try block
                }
            }
            if (line.contains("catch")) {
                inTryBlock = false; // Exiting the try-catch block
            }

            // Detect input handling (e.g., Scanner)
            if (line.contains("Scanner") || line.contains(".read")) {
                inputCount++;
            }

            // Detect output handling (e.g., System.out.println)
            if (line.contains("System.out.println") || line.contains(".write")) {
                outputCount++;
            }

            // Detect arithmetic operations
            operationCount += countArithmeticOperations(line);

            // Handle the end of blocks
            if (line.equals("}")) {
                nestedLoopLevel = Math.max(0, nestedLoopLevel - 1);
                nestedConditionLevel = Math.max(0, nestedConditionLevel - 1);
            }
        }

        // Build the analysis result string
        StringBuilder result = new StringBuilder();
        result.append("Detailed Control Structures Found:\n");
        result.append("- If/Else Statements: ").append(ifCount).append("\n");
        result.append("- Loops (For/While): ").append(loopCount).append("\n");
        result.append("- Maximum Nested Loop Level: ").append(maxNestedLoopLevel).append("\n");
        result.append("- Maximum Nested Condition Level: ").append(maxNestedConditionLevel).append("\n");
        result.append("- Try/Catch Blocks: ").append(tryCatchCount).append("\n");
        result.append("- Input Statements: ").append(inputCount).append("\n");
        result.append("- Output Statements: ").append(outputCount).append("\n");
        result.append("- Comparisons: ").append(comparisonCount).append("\n");
        result.append("- Arithmetic Operations: ").append(operationCount).append("\n");

        return result.toString();
    }

    // Helper method to count occurrences of comparison operators
    private int countComparisons(String line) {
        String[] comparisons = {">", "<", "==", ">=", "<="};
        int count = 0;
        for (String comp : comparisons) {
            count += line.split(comp, -1).length - 1;
        }
        return count;
    }

    // Helper method to count occurrences of arithmetic operators
    private int countArithmeticOperations(String line) {
        // Exclude arithmetic operators in comments or strings
        if (line.startsWith("//") || line.contains("\"")) {
            return 0;
        }
        return countOccurrences(line, '+') + countOccurrences(line, '-') +
               countOccurrences(line, '*') + countOccurrences(line, '/');
    }

    // General helper method to count occurrences of a character in a string
    private int countOccurrences(String str, char ch) {
        return (int) str.chars().filter(c -> c == ch).count();
    }
}
