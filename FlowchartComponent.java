

public class FlowchartComponent {
    private String label;
    private String type;
    private int nestingLevel;

    // Constructor with three parameters
    public FlowchartComponent(String label, String type, int nestingLevel) {
        this.label = label;
        this.type = type;
        this.nestingLevel = nestingLevel;
    }

    // Constructor with two parameters, defaulting nestingLevel to 0
    public FlowchartComponent(String label, String type) {
        this(label, type, 0);  // Calls the three-parameter constructor
    }

    // Getters
    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }
}
