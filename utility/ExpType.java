package utility;

public class ExpType {

	public enum Type {INT, BOOLEAN, INTARR, ID};

	public Type m_type;
	public String id_name;

	public ExpType(Type t, String id) {
		m_type = t;
		id_name = id;

		// Check if the ID is a valid ID. 
		if (m_type == Type.ID) {
			if (!ClassTypes.isAClass(id_name))
			{
				System.out.println("Invalid class ID: " + id_name);
				return;
			}
		}
		return;
	}

	public ExpType(Type t) {
		m_type = t;
		id_name = "";
	}

	// Checks for type equality
	public boolean isEqual(ExpType t) {
		return (m_type == t.getType()) && id_name.equals(t.getID());
	}

	public Type getType() { return m_type; }
	public String getID() { return id_name; }

}