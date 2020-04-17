
public class Variable {
    String variableID;
    int variableValue;
    int lastAccessTime;
    //variable constructor, with its id, value and last access time
    Variable(String id, int val, int lat) {
        variableID = id;
        variableValue = val;
        lastAccessTime = lat;
    }

    public String getVariableId() { //get variable id
        return this.variableID;
    }

    public int getValue() { //get value
        return this.variableValue;
    }

    public int getLastAccessTime() { //get last access time
        return this.lastAccessTime;
    }
    public void setLastAccessTime(int lat) { //set last access time
        lastAccessTime =lat;

    }
}