
public class Variable {
    String variableId;
    int variableValue;
    int lastAccessTime;

    Variable(String variableId, int variableValue, int lat) {
        this.variableId = variableId;
        this.variableValue = variableValue;
        this.lastAccessTime = lastAccessTime;
    }

    public String getVariableId() {
        return this.variableId;
    }

    public int getValue() {
        return this.variableValue;
    }

    public int getLastAccessTime() {
        return this.lastAccessTime;
    }
}