
public class Variable {
    String variableID;
    int variableValue;
    int lastAccessTime;

    Variable (String id, int val, int lat) {
        variableID = id;
        variableValue=val;
        lastAccessTime=lat;
    }
    public String getVariableId() {
        return this.variableID;
    }

    public int getValue() {
        return this.variableValue;
    }

    public int getLastAccessTime() {
        return this.lastAccessTime;
    }

    public void setVariableId(String id) {
        variableID=id;

    }
    public void setValue(int val) {
        variableValue = val;
    }
}