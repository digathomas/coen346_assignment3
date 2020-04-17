public class Variable {

	private String id; // variable ID
	private int lastAccess; // Last accessed time
	private int value; // Stored value

	// Constructor
	Variable(String id, int value) { 
		this.id = id;
		this.value = value;
	}

	public void setLastAccess(int lastAccess) {
		this.lastAccess = lastAccess;
	}
	
	public int getLastAccess() {
		return lastAccess;
	}

	public String getId() {
		return id;
	}

	public int getValue() {
		return value;
	}
}
