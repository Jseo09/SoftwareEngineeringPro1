
import java.util.Stack;

public class ErrorManager {

    // Method to check for syntax errors
    public static String checkSyntaxErrors(String code) {
        Stack<Character> stack = new Stack<>();
        StringBuilder errors = new StringBuilder();
        int lineCount = 0;

        for (String line : code.split("\\n")) {
            lineCount++;
            line = line.trim();

            // Check for mismatched brackets
            for (char c : line.toCharArray()) {
                if (c == '{' || c == '(') {
                    stack.push(c);
                } else if (c == '}') {
                    if (stack.isEmpty() || stack.pop() != '{') {
                        errors.append("Error: Mismatched curly brace at line ").append(lineCount).append("\n");
                    }
                } else if (c == ')') {
                    if (stack.isEmpty() || stack.pop() != '(') {
                        errors.append("Error: Mismatched parenthesis at line ").append(lineCount).append("\n");
                    }
                }
            }

            // Check for missing semicolon
            if (!line.endsWith(";") && !line.endsWith("{") && !line.endsWith("}") && !line.isBlank() && !line.endsWith(")") && line.startsWith("'\\\\'")) {
                errors.append("Error: Missing semicolon at line ").append(lineCount).append("\n");
            }
        }

        // Final check for unmatched brackets or parentheses
        while (!stack.isEmpty()) {
            char unclosed = stack.pop();
            errors.append("Error: Unmatched ").append(unclosed == '{' ? "curly brace" : "parenthesis").append("\n");
        }

        return errors.toString();
    }
}
