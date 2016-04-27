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
		
		parentMap.put(c, pC);
		classNames.add(c);
		return;
	}

	// This function will make sure there are no cycles.
	// Additionally, it will make sure that the main class is not subtyped anywhere. 
	public static void verifyClassHierarchy() {
		boolean invalidClass = false;
		for (String c: classNames) {
			if ((!ClassTypes.verifyClass(parentMap.get(c), c)) || c.equals("mainClass")) {
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

	// Checks if the name is the same as another method in current class
	// Then does so for all its parent classes. 
	public static boolean verifyMethodNames() {
		return true;
	}

	// Verifies that field names are all unique.
	public static boolean verifyFieldNames() {
		return true;
	}



	
}