package utility;

import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

public class ClassTypes {

	// Main class ID.
	public static String mainClass = "";

	// Vector of all the class names.
	public static Vector<String> classNames = new Vector<String>();

	// Class name maps to a vector of Method IDs and their type.
	// HashMap<[Class ID], List of pairs of method IDs and lists of their arguments, arg IDs, and 
	// types of the arguments. 
	public static HashMap<String, Vector<Pair<String, Vector<Pair<String, ExpType>>>>> methodMap = 
					new HashMap<String, Vector<Pair<String, Vector<Pair<String, ExpType>>>>>();

					/* Aside: the first item in the list is an empty ID with a type of the method. */

	// Class name maps to a vector of Field IDs and their type. 
	public static HashMap<String, Vector<Pair<String, ExpType>>> fieldMap = 
					new HashMap<String, Vector<Pair<String, ExpType>>>();

	// Class -> Parent Class
	public static HashMap<String, String> parentMap = new HashMap<String, String>();

										/* START CLASS METHOD FUNCTIONS */

	// Returns method type and args if the method exists in c or in parents of c.
	// If it fails, then it exits the program with a type error.
	public static Vector<Pair<String, ExpType>> getMethodInfo(String c, String m) {
		if (c.equals("")) {
			System.out.println("Type Error");
			System.exit(1);
		}

		// Checks for the correct method. If it exists, returns info. Else, recurses. 
		Vector<Pair<String, Vector<Pair<String, ExpType>>>> methods = methodMap.get(c);
		for (Pair<String, Vector<Pair<String, ExpType>>> mds: methods) {
			if (mds.x.equals(m))
				return mds.y;
		}

		return ClassTypes.getMethodInfo(parentMap.get(c), m);
	}

	// Add arguments and their types to a method. 
	public static void addMethodName(String c, String m, String id, ExpType t) {
		System.out.println(c + " " + m + " " + id + " " + t.getID());

		Vector<Pair<String, Vector<Pair<String, ExpType>>>> v = methodMap.get(c);
		Vector<Pair<String, ExpType>> v2 = new Vector<Pair<String, ExpType>>();
		boolean v2defined = false;

		// Get the correct method name
		int methodnum;
		for (methodnum = 0; methodnum < v.size(); methodnum++) {
			Pair<String, Vector<Pair<String, ExpType>>> p = v.elementAt(methodnum);
			if (p.x.equals(m)) {
				v2defined = true;
				v2 = p.y;
				v.remove(methodnum);
				break;
			}
		}

		// Tried to add arg to not yet defined method
		if (!v2defined) {
			System.out.println("Did not define function before adding args");
			return;
		}

		// Ensures that all parameters are unique
		for (Pair<String, ExpType> args: v2) {
			if (id.equals(args.x)) {
				System.out.println("Type Error");
				System.exit(1);
			}
		}

		Pair<String, ExpType> newp = new Pair<String, ExpType>(id, t);
		v2.add(newp);
		Pair<String, Vector<Pair<String, ExpType>>> method = 
			new Pair<String, Vector<Pair<String, ExpType>>>(m, v2);
		v.add(method);
		methodMap.put(c, v);
		return;
	}

	// Add a method & its type
	public static void addMethodName(String c, String m, ExpType t) {
		System.out.println(c + " " + m + " " + t.getID());
		Vector<Pair<String, Vector<Pair<String, ExpType>>>> v = methodMap.get(c);

		// If the method already exists, then there is a type error.
		for (Pair<String, Vector<Pair<String, ExpType>>> p: v) {
			if (p.x.equals(m)) {
				System.out.println("Type Error");
				System.exit(1);
			}
		}

		Vector<Pair<String, ExpType>> methodvec = new Vector<Pair<String, ExpType>>();
		Pair<String, ExpType> newp = new Pair<String, ExpType>("", t);
		methodvec.add(newp);
		Pair<String, Vector<Pair<String, ExpType>>> method = 
			new Pair<String, Vector<Pair<String, ExpType>>>(m, methodvec);
		v.add(method);
		methodMap.put(c, v);


	}

	// Checks if a method is defined in parent classes & if it is, it verifies that
	// the arguments of both are the same.

	// Overriding methods need the same id's and types. 
	public static void verifyMethodNames() {
		for (String c: classNames) {
			Vector<Pair<String, Vector<Pair<String, ExpType>>>> methodL = methodMap.get(c);
			String parentC = parentMap.get(c);
			for (Pair<String, Vector<Pair<String, ExpType>>> method: methodL) {
				checkMethodInParent(method, parentC);
			}
		}
	}

	// Check if method is in parent & if it type checks. If it doesn't, print a type error. 
	public static void checkMethodInParent(Pair<String, Vector<Pair<String, ExpType>>> method, String c) {
		if (c.equals(""))
			return;

		// Check if method is in the method list of class c.
		Vector<Pair<String, Vector<Pair<String, ExpType>>>> mList = methodMap.get(c);
		for (Pair<String, Vector<Pair<String, ExpType>>> m: mList) {
			if (m.x.equals(method.x)) {
				if (m.y.size() != method.y.size()) {
					System.out.println("Error with function overloading");
					System.out.println("Type Error");
					System.exit(1);
				}
				for (int i = 0; i < method.y.size(); i++) {
					if (m.y.elementAt(i).x != method.y.elementAt(i).x || 
						!m.y.elementAt(i).y.isEqual(method.y.elementAt(i).y)) {
						System.out.println("Error with function overloading");
						System.out.println("Type Error");
						System.exit(1);
					}
				}
				return;
			}
		}

		String newc = parentMap.get(c);
		checkMethodInParent(method, newc);
	}

										/* END CLASS METHOD FUNCTIONS */

										/* START CLASS FIELD FUNCTIONS */	

	public static void addFieldName(String c, String id, ExpType t) {
		// Ensures that field ID is unique
		Vector<Pair<String, ExpType>> v = fieldMap.get(c);
		System.out.println("Var name: " + id);
		for (Pair<String, ExpType> p: v) {
			if (p.x.equals(id)) {
				System.out.println("Variable name " + id + " already exists.");
				System.out.println("Type Error");
				System.exit(1);
			}
		}

		// Adds it to available field names
		Pair<String, ExpType> np = new Pair<String, ExpType>(id, t);
		v.add(np);
		fieldMap.put(c, v);
	}

	public static boolean isFieldOf(String c, String id) {
		Vector<Pair<String, ExpType>> v = fieldMap.get(c);
		for (Pair<String, ExpType> p: v) {
			if (p.x.equals(id)) {
				return true;
			}
		}

		return false;
	}

	public static ExpType getFieldType(String c, String id) {
		Vector<Pair<String, ExpType>> v = fieldMap.get(c);
		for (Pair<String, ExpType> p: v) {
			if (p.x.equals(id)) {
				return p.y;
			}
		}

		ExpType empty = new ExpType(ExpType.Type.ID, "");
		return empty;
	}

	public static ExpType getExtFieldType(String c, String id) {
		// If we hit the bottom, then we know there's no field to access.
		if (c.equals("")) {
			System.out.println("Type Error");
        	System.exit(1);
		}

		// Searches for the field in class c.
		Vector<Pair<String, ExpType>> v = fieldMap.get(c);
		for (Pair<String, ExpType> p: v) {
			if (p.x.equals(id)) {
				return p.y;
			}
		}

		return getExtFieldType(parentMap.get(c), id);

	}

										/* END CLASS FIELD FUNCTIONS */	


										/* START CLASS FUNCTIONS */	

	// Class -> Child Subclasses
	//public static HashMap<String, String> childMap = new HashMap<String, Vector<String>>();

	// Checks if the ID nclass is a registered type
	public static boolean isAClass(String nclass) {
		for (String c: classNames) {
			if (c.equals(nclass))
				return true;
		}
		return false;

	}

	// Returns true if the current is a subtype of the parent class. 
	public static boolean isASubtype(String c, String pC) {
		if (c.equals(""))
			return false;

		if (c.equals(pC))
			return true;

		String newc = parentMap.get(c);
		return ClassTypes.isASubtype(newc, pC);
	}

	// Adds a class to the class list if possible. If this returns false, should cause an error.
	public static void addClass(String c, String pC) {

		// If the class already exists or extends itself, return false
		if (ClassTypes.isAClass(c) || c.equals(pC)) {
			System.out.println("Error adding class.");
			System.out.println("Type Error");
			System.exit(1);
		}

		// If class has a parent, adds the child to the parent's child directory.
		/* I DON'T THINK I NEED THIS.
		if (!pC.equals("")){
			Vector<String> childIDs;
			if (childMap.containsKey(pC))
				childIDs = childMap.get(pC);
			else
				childIDs = new Vector<String>;
			childIDs.add(c);
			// Adds elements to maps.
			childMap.put(pC, childIDs);
		}
		*/
		
		Vector<Pair<String, Vector<Pair<String, ExpType>>>> mv = 
			new Vector<Pair<String, Vector<Pair<String, ExpType>>>>();
		Vector<Pair<String, ExpType>> v = new Vector<Pair<String, ExpType>>();
		fieldMap.put(c, v);
		methodMap.put(c, mv);
		parentMap.put(c, pC);
		classNames.add(c);
		return;
	}

	// This function will make sure there are no cycles.
	// Additionally, it will make sure that the main class is not subtyped anywhere. 
	public static void verifyClassHierarchy() {
		boolean invalidClass = false;
		for (String c: classNames) {
			if ((!ClassTypes.verifyClass(parentMap.get(c), c)) || c.equals(mainClass)) {
				System.out.println("Invalid Class: " + c);
				System.out.println("Type Error");
				System.exit(1);
			}
		}
	}

	public static boolean verifyClass(String curClass, String initClass) {
		if (curClass.equals(initClass))
			return false;

		// Class is empty class
		if (curClass.equals(""))
			return true;

		// class extends another class that doesn't exist.
		if (!parentMap.containsKey(curClass))
			return false;

		String newcurClass = parentMap.get(curClass);
		return verifyClass(newcurClass, initClass);
	}

										/* END CLASS FUNCTIONS */				

										/* START HELPER FUNCTIONS */
	public static boolean areNamesUnique(Vector<Pair<String, ExpType>> names) {
		if (names == null)
			return true;

		for (int i = 0; i < names.size(); i++) {
			for (int j = i + 1; j < names.size(); j++) {
				if (names.elementAt(i).x.equals(names.elementAt(j).x))
					return false;
			}
		}
	}

	public static ExpType getType(String c, String id, Vector<Pair<String, ExpType>> names) {
		if (names != null) {
			for (Pair<String, ExpType> local: names) {
				if (local.x.equals(id))
					return local.y;
			}
		}

		return getExtFieldType(c, id);
	}





	
}