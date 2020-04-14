public class Commands {
    String vmmFunction;
    String id;
    int value;

    Commands(String API, String identification, int number) {
        vmmFunction = API;
        id = identification;
        value=number;
    }
    
    Commands(String API, String identification) {
        vmmFunction = API;
        id = identification;
    }

    public String getVMMFunction() {
        return vmmFunction;
    }

    public String getID() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public void setVMMFunction(String a) {
        vmmFunction = a;
    }

    public void setID(String a) {
        id = a;
    }

    public void setValue(int a) {
        value = a;
    }
} 