
package visitor;
import syntaxtree.*;
import java.util.*;
import utility.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class VarVisitor extends GJNoArguDepthFirst<Pair<String, ExpType>> {
   //
   // Auto class visitors--probably don't need to be overridden.
   //

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public Pair<String, ExpType> visit(VarDeclaration n) {
      Pair<String, ExpType> p = n.f0.accept(this);
      String id = n.f1.f0.toString();
      Pair<String, ExpType> pf = new Pair<String, ExpType>(id, p.y);
      return pf;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public Pair<String, ExpType> visit(Type n) {
      return n.f0.accept(this);
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public Pair<String, ExpType> visit(ArrayType n) {
      ExpType t = new ExpType(ExpType.Type.INTARR);
      Pair<String, ExpType> pf = new Pair<String, ExpType>("", t);
      return pf;
   }

   /**
    * f0 -> "boolean"
    */
   public Pair<String, ExpType> visit(BooleanType n) {
      ExpType t = new ExpType(ExpType.Type.BOOLEAN);
      Pair<String, ExpType> pf = new Pair<String, ExpType>("", t);
      return pf;
   }

   /**
    * f0 -> "int"
    */
   public Pair<String, ExpType> visit(IntegerType n) {
      ExpType t = new ExpType(ExpType.Type.INT);
      Pair<String, ExpType> pf = new Pair<String, ExpType>("", t);
      return pf;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
  public Pair<String, ExpType> visit(Identifier n) {
      if (!ClassTypes.isAClass(n.f0.toString())) {
        System.out.println(n.f0.toString());
        System.out.println("Variable identifier error");
        System.out.println("Type Error");
        System.exit(1);
      }

      ExpType t = new ExpType(ExpType.Type.ID, n.f0.toString());
      Pair<String, ExpType> pf = new Pair<String, ExpType>("", t);
      return pf;
   }
}
