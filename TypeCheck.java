
import syntaxtree.*;
import visitor.*;
import utility.ClassTypes;
import utility.ExpType;
import utility.Pair;
import java.util.HashMap;

public class TypeCheck {
	public static void main(String[] args) {
		try {
			// create the AST from the parse of stdin
			Goal g = new MiniJavaParser(System.in).Goal();

			// Verify that all class IDs are correct
			ClassIDVisitor cidvis = new ClassIDVisitor();
			g.accept(cidvis);
			ClassTypes.verifyClassHierarchy();

			// Verify class field types
			ClassFieldVisitor cfvis = new ClassFieldVisitor();
			Pair<String, String> empty = new Pair<String, String>("","");
			g.accept(cfvis, empty);

			// Verify class field types 
			ClassMethodVisitor cmvis = new ClassMethodVisitor();
			g.accept(cmvis, empty); 
			ClassTypes.verifyMethodNames();
		
		} catch (ParseException e){
			System.out.println(e.toString());
        }
	}
}