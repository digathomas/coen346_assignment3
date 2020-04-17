public class Commands {
    String vmmFunction;
    String variableID;
    int variableValue;

    //commands constructor for store
    Commands(String API, String identification, int number) {
        vmmFunction = API;
        variableID = identification;
        variableValue=number;

    }
    //commands constructor for lookup and release
    Commands(String API, String identification) {
        vmmFunction = API;
        variableID = identification;
 
    }
    //get function
    public String getVMMFunction() {
        return vmmFunction;
    }
    //get id
    public String getID() {
        return variableID;
    }
    //get value
    public int getValue() {
        return variableValue;
    }
    //set the function
    public void setVMMFunction(String a) {
        vmmFunction = a;
    }
    //set id
    public void setID(String a) {
        variableID = a;
    }
    //set value
    public void setValue(int a) {
        variableValue = a;
    }
} 