
import syntaxtree.*;
import visitor.*;
import utility.ClassTypes;
import utility.ExpType;
import utility.Pair;
import java.util.HashMap;

public class Parse {
	public static void main(String[] args) {
		try {
			// create the AST from the parse of stdin
			Goal g = new MiniJavaParser(System.in).Goal();

			// set up a new visitor to traverse the tree
			ClassIDVisitor cidvis = new ClassIDVisitor();
			g.accept(cidvis);

			System.out.println(ClassTypes.mainClass);
			ClassTypes.verifyClassHierarchy();
			for (String c: ClassTypes.classNames) {
				System.out.println(c);
			}
		
		} catch (ParseException e){
			System.out.println(e.toString());
        }
	}
}