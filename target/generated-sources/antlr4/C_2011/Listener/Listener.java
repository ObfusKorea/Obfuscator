package C_2011.Listener;

import C_2011.generated.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Listener extends CBaseListener {

    int outputCount;
    ParseTreeProperty<String> newTexts = new ParseTreeProperty<>();

    public Listener(int count) {
        this.outputCount = count;
    }

    @Override
    public void exitPrimaryExpression(CParser.PrimaryExpressionContext ctx) {
        String program = "";
        if (ctx.Identifier() != null) {
            program = newTexts.get(ctx.Identifier());
        } else if (ctx.Constant() != null) {
            program = newTexts.get(ctx.Constant());
        } else if (ctx.StringLiteral() != null) {
            for (int i = 0; i < ctx.StringLiteral().size(); i++) {
                program = newTexts.get(ctx.StringLiteral(i));
            }
        } else if (ctx.expression() != null) {
            program = String.format("(%s)", newTexts.get(ctx.expression()));
        }

        newTexts.put(ctx, program);
        System.out.println(program);

        File file = new File(String.format("result_C_%d.c", this.outputCount));

        try {
            FileWriter fw = new FileWriter(file);
            fw.write(program);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enterPrimaryExpression(CParser.PrimaryExpressionContext ctx) {
        super.enterPrimaryExpression(ctx);
    }

//    @Override
//    public void enterGenericSelection(CParser.GenericSelectionContext ctx) {
//        super.enterGenericSelection(ctx);
//    }
//
//    @Override
//    public void exitGenericSelection(CParser.GenericSelectionContext ctx) {
//        super.exitGenericSelection(ctx);
//    }
//
//    @Override
//    public void enterGenericAssocList(CParser.GenericAssocListContext ctx) {
//        super.enterGenericAssocList(ctx);
//    }
//
//    @Override
//    public void exitGenericAssocList(CParser.GenericAssocListContext ctx) {
//        super.exitGenericAssocList(ctx);
//    }
//
//    @Override
//    public void enterGenericAssociation(CParser.GenericAssociationContext ctx) {
//        super.enterGenericAssociation(ctx);
//    }
//
//    @Override
//    public void exitGenericAssociation(CParser.GenericAssociationContext ctx) {
//        super.exitGenericAssociation(ctx);
//    }

    @Override
    public void exitPostfixExpression(CParser.PostfixExpressionContext ctx) {
        String bf = "";
        String postfix = newTexts.get(ctx.postfixExpression());
        int child = ctx.children.size();
        if (child == 1) { //primaryExp
            bf = newTexts.get(ctx.primaryExpression());
        } else if (child == 2) { // postfixExp ++/--
            bf = String.format("%s%s", postfix, newTexts.get(ctx.getChild(1)));
        } else if (ctx.Identifier() != null) { //postfix -> / . Identifier
            bf = String.format("%s %s %s", postfix, newTexts.get(ctx.getChild(1)), newTexts.get(ctx.Identifier()));
        } else if (newTexts.get(ctx.getChild(1)).equals("[")) { // profixEXP [ expression ]
            bf = String.format("%s[%s]", postfix, newTexts.get(ctx.expression()));
        } else if (newTexts.get(ctx.getChild(1)).equals("(")) { // postfix ( argument? )
            String argument = (ctx.argumentExpressionList() != null) ? newTexts.get(ctx.argumentExpressionList()) : "";
            bf = String.format("%s(%s)", postfix, argument);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitArgumentExpressionList(CParser.ArgumentExpressionListContext ctx) {
        String bf;
        String assignExp = newTexts.get(ctx.assignmentExpression());

        if (ctx.argumentExpressionList() == null) {
            bf = assignExp;
        } else {
            bf = String.format("%s, %s", newTexts.get(ctx.argumentExpressionList()), assignExp);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitUnaryExpression(CParser.UnaryExpressionContext ctx) {
        String bf = "";
        if (ctx.postfixExpression() != null) { // postfixExp
            bf = newTexts.get(ctx.postfixExpression());
        } else if (ctx.unaryExpression() != null) { // ++ / -- / sizeof unaryExp
            bf = String.format("%s%s", newTexts.get(ctx.getChild(0)), newTexts.get(ctx.unaryExpression()));
        } else if (ctx.unaryOperator() != null) { //unaryOp castExp
            bf = String.format("%s%s", newTexts.get(ctx.unaryOperator()), newTexts.get(ctx.castExpression()));
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitUnaryOperator(CParser.UnaryOperatorContext ctx) {
        String bf = newTexts.get(ctx.getChild(0));
        newTexts.put(ctx, bf);
    }

//    @Override
//    public void enterCastExpression(CParser.CastExpressionContext ctx) {
//        super.enterCastExpression(ctx);
//    }
//
//    @Override
//    public void exitCastExpression(CParser.CastExpressionContext ctx) {
//        super.exitCastExpression(ctx);
//    }
//
//    @Override
//    public void enterMultiplicativeExpression(CParser.MultiplicativeExpressionContext ctx) {
//        super.enterMultiplicativeExpression(ctx);
//    }

    @Override
    public void exitMultiplicativeExpression(CParser.MultiplicativeExpressionContext ctx) {
        String bf;
        if (ctx.children.size() == 1) { // castExp
            bf = newTexts.get(ctx.castExpression());
        } else {
            String multi = newTexts.get(ctx.multiplicativeExpression());
            String op = newTexts.get(ctx.getChild(1));
            String cast = newTexts.get(ctx.castExpression());
            bf = String.format("%s %s %s", multi, op, cast);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitAdditiveExpression(CParser.AdditiveExpressionContext ctx) {
        String bf;
        String multi = newTexts.get(ctx.multiplicativeExpression());
        if (ctx.children.size() == 1) { // multiplicativeExp
            bf = multi;
        } else {
            String additive = newTexts.get(ctx.additiveExpression());
            String op = newTexts.get(ctx.getChild(1));
            bf = String.format("%s %s %s", additive, op, multi);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitShiftExpression(CParser.ShiftExpressionContext ctx) {
        String bf;
        String additive = newTexts.get(ctx.additiveExpression());
        if (ctx.children.size() == 1) { // additiveExp
            bf = additive;
        } else {
            String shift = newTexts.get(ctx.shiftExpression());
            String op = newTexts.get(ctx.getChild(1));
            bf = String.format("%s %s %s", shift, op, additive);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitRelationalExpression(CParser.RelationalExpressionContext ctx) {
        String bf;
        String shift = newTexts.get(ctx.shiftExpression());
        if (ctx.children.size() == 1) { // shiftExp
            bf = shift;
        } else {
            String relation = newTexts.get(ctx.relationalExpression());
            String op = newTexts.get(ctx.getChild(1));
            bf = String.format("%s %s %s", relation, op, shift);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitEqualityExpression(CParser.EqualityExpressionContext ctx) {
        String bf;
        String relation = newTexts.get(ctx.relationalExpression());
        if (ctx.children.size() == 1) { // relationalExpression
            bf = relation;
        } else {
            String equality = newTexts.get(ctx.equalityExpression());
            String op = newTexts.get(ctx.getChild(1));
            bf = String.format("%s %s %s", equality, op, relation);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitAndExpression(CParser.AndExpressionContext ctx) {
        String bf;
        String equal = newTexts.get(ctx.equalityExpression());
        if (ctx.children.size() == 1) { // equalExp
            bf = equal;
        } else {
            String and = newTexts.get(ctx.andExpression());
            String op = newTexts.get(ctx.getChild(1));
            bf = String.format("%s %s %s", and, op, equal);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitExclusiveOrExpression(CParser.ExclusiveOrExpressionContext ctx) {
        String bf;
        String and = newTexts.get(ctx.andExpression());
        if (ctx.children.size() == 1) { // andExp
            bf = and;
        } else {
            String exclusive = newTexts.get(ctx.exclusiveOrExpression());
            String op = newTexts.get(ctx.getChild(1));
            bf = String.format("%s %s %s", exclusive, op, and);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitInclusiveOrExpression(CParser.InclusiveOrExpressionContext ctx) {
        String bf;
        String exclusive = newTexts.get(ctx.exclusiveOrExpression());
        if (ctx.children.size() == 1) { // exclusiveOr
            bf = exclusive;
        } else {
            String inclusive = newTexts.get(ctx.inclusiveOrExpression());
            String op = newTexts.get(ctx.getChild(1));
            bf = String.format("%s %s %s", inclusive, op, exclusive);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitLogicalAndExpression(CParser.LogicalAndExpressionContext ctx) {
        String bf;
        String incluesive = newTexts.get(ctx.inclusiveOrExpression());
        if (ctx.children.size() == 1) { // inclusiveAnd
            bf = incluesive;
        } else {
            String logical = newTexts.get(ctx.logicalAndExpression());
            String op = newTexts.get(ctx.getChild(1));
            bf = String.format("%s %s %s", logical, op, incluesive);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitLogicalOrExpression(CParser.LogicalOrExpressionContext ctx) {
        String bf;
        String logicalAnd = newTexts.get(ctx.logicalAndExpression());
        if (ctx.children.size() == 1) { // logicalAnd
            bf = logicalAnd;
        } else {
            String logicalOr = newTexts.get(ctx.logicalOrExpression());
            String op = newTexts.get(ctx.getChild(1));
            bf = String.format("%s %s %s", logicalOr, op, logicalAnd);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitConditionalExpression(CParser.ConditionalExpressionContext ctx) {
        String bf;
        String logicalOr = newTexts.get(ctx.logicalOrExpression());
        if (ctx.children.size() == 1) {
            bf = logicalOr;
        } else {
            String exp = newTexts.get(ctx.expression());
            String condiExp = newTexts.get(ctx.conditionalExpression());
            bf = String.format("%s ? %s : %s", logicalOr, exp, condiExp);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitAssignmentExpression(CParser.AssignmentExpressionContext ctx) {
        String bf;
        if (ctx.conditionalExpression() != null) { //conditionalExpression
            bf = newTexts.get(ctx.conditionalExpression());
        } else if (ctx.unaryExpression() != null) { //unaryExpression assignmentOperator assignmentExpression
            String unary = newTexts.get(ctx.unaryExpression());
            String assignOP = newTexts.get(ctx.assignmentOperator());
            String assignExp = newTexts.get(ctx.assignmentExpression());
            bf = String.format("%s %s %s", unary, assignOP, assignExp);
        } else { //DigitSequence
            bf = newTexts.get(ctx.DigitSequence());
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitAssignmentOperator(CParser.AssignmentOperatorContext ctx) {
        newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
    }

    @Override
    public void exitExpression(CParser.ExpressionContext ctx) {
        String bf;
        String assignExp = newTexts.get(ctx.assignmentExpression());
        if (ctx.children.size() == 1) {
            bf = assignExp;
        } else {
            String exp = newTexts.get(ctx.expression());
            bf = String.format("%s, %s", exp, assignExp);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitConstantExpression(CParser.ConstantExpressionContext ctx) {
        newTexts.put(ctx, newTexts.get(ctx.conditionalExpression()));
    }

    @Override
    public void exitDeclaration(CParser.DeclarationContext ctx) {
        String bf;
        int cSize = ctx.children.size();
        if (cSize == 1) { //staticAssertDeclaration
            bf = newTexts.get(ctx.staticAssertDeclaration());
        } else if (cSize == 2) { // declarationSpecifiers ';'
            String declarSpeci = newTexts.get(ctx.declarationSpecifiers());
            bf = String.format("%s;", declarSpeci);
        } else { // declarationSpecifiers initDeclaratorList ';'
            String declarSpeci = newTexts.get(ctx.declarationSpecifiers());
            String initDecl = newTexts.get(ctx.initDeclaratorList());
            bf = String.format("%s %s;", declarSpeci, initDecl);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitDeclarationSpecifiers(CParser.DeclarationSpecifiersContext ctx) {
        StringBuilder bf = new StringBuilder();
        for (int i = 0; i < ctx.children.size(); i++) {
            bf.append(newTexts.get(ctx.declarationSpecifier(i)));
        }
        newTexts.put(ctx, bf.toString());
    }

    @Override
    public void exitDeclarationSpecifiers2(CParser.DeclarationSpecifiers2Context ctx) {
        StringBuilder bf = new StringBuilder();
        for (int i = 0; i < ctx.children.size(); i++) {
            bf.append(newTexts.get(ctx.declarationSpecifier(i)));
        }
        newTexts.put(ctx, bf.toString());
    }

    @Override
    public void exitDeclarationSpecifier(CParser.DeclarationSpecifierContext ctx) {
        String bf;
        if (ctx.storageClassSpecifier() != null) {
            bf = newTexts.get(ctx.storageClassSpecifier());
        } else if (ctx.typeSpecifier() != null) {
            bf = newTexts.get(ctx.typeSpecifier());
        } else if (ctx.typeQualifier() != null) {
            bf = newTexts.get(ctx.typeQualifier());
        } else if (ctx.functionSpecifier() != null) {
            bf = newTexts.get(ctx.functionSpecifier());
        } else {
            bf = newTexts.get(ctx.alignmentSpecifier());
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitInitDeclaratorList(CParser.InitDeclaratorListContext ctx) {
        String bf;
        String initDecl = newTexts.get(ctx.initDeclarator());
        if (ctx.children.size() == 1) {
            bf = initDecl;
        } else {
            String initDeclList = newTexts.get(ctx.initDeclaratorList());
            bf = String.format("%s, %s", initDeclList, initDecl);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitInitDeclarator(CParser.InitDeclaratorContext ctx) {
        String bf;
        String decl = newTexts.get(ctx.declarator());
        if (ctx.children.size() == 1) {
            bf = decl;
        } else {
            String init = newTexts.get(ctx.initializer());
            bf = String.format("%s = %s", decl, init);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitStorageClassSpecifier(CParser.StorageClassSpecifierContext ctx) {
        newTexts.put(ctx, newTexts.get(ctx.getChild(0)));
    }

    @Override
    public void enterTypeSpecifier(CParser.TypeSpecifierContext ctx) {
        String bf = "";
        if (ctx.atomicTypeSpecifier() != null) {
            bf = newTexts.get(ctx.atomicTypeSpecifier());
        } else if (ctx.structOrUnionSpecifier() != null) {
            bf = newTexts.get(ctx.structOrUnionSpecifier());
        } else if (ctx.enumSpecifier() != null) {
            bf = newTexts.get(ctx.enumSpecifier());
        } else if (ctx.typedefName() != null) {
            bf = newTexts.get(ctx.typedefName());
        } else if (ctx.children.size() == 1) {
            bf = newTexts.get(ctx.getChild(0));
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitTypeSpecifier(CParser.TypeSpecifierContext ctx) {

    }

    @Override
    public void enterStructOrUnionSpecifier(CParser.StructOrUnionSpecifierContext ctx) {
        super.enterStructOrUnionSpecifier(ctx);
    }

    @Override
    public void exitStructOrUnionSpecifier(CParser.StructOrUnionSpecifierContext ctx) {
        super.exitStructOrUnionSpecifier(ctx);
    }

    @Override
    public void enterStructOrUnion(CParser.StructOrUnionContext ctx) {
        super.enterStructOrUnion(ctx);
    }

    @Override
    public void exitStructOrUnion(CParser.StructOrUnionContext ctx) {
        super.exitStructOrUnion(ctx);
    }

    @Override
    public void enterStructDeclarationList(CParser.StructDeclarationListContext ctx) {
        super.enterStructDeclarationList(ctx);
    }

    @Override
    public void exitStructDeclarationList(CParser.StructDeclarationListContext ctx) {
        super.exitStructDeclarationList(ctx);
    }

    @Override
    public void enterStructDeclaration(CParser.StructDeclarationContext ctx) {
        super.enterStructDeclaration(ctx);
    }

    @Override
    public void exitStructDeclaration(CParser.StructDeclarationContext ctx) {
        super.exitStructDeclaration(ctx);
    }

    @Override
    public void enterSpecifierQualifierList(CParser.SpecifierQualifierListContext ctx) {
        super.enterSpecifierQualifierList(ctx);
    }

    @Override
    public void exitSpecifierQualifierList(CParser.SpecifierQualifierListContext ctx) {
        super.exitSpecifierQualifierList(ctx);
    }

    @Override
    public void enterStructDeclaratorList(CParser.StructDeclaratorListContext ctx) {
        super.enterStructDeclaratorList(ctx);
    }

    @Override
    public void exitStructDeclaratorList(CParser.StructDeclaratorListContext ctx) {
        super.exitStructDeclaratorList(ctx);
    }

    @Override
    public void enterStructDeclarator(CParser.StructDeclaratorContext ctx) {
        super.enterStructDeclarator(ctx);
    }

    @Override
    public void exitStructDeclarator(CParser.StructDeclaratorContext ctx) {
        super.exitStructDeclarator(ctx);
    }

    @Override
    public void enterEnumSpecifier(CParser.EnumSpecifierContext ctx) {
        super.enterEnumSpecifier(ctx);
    }

    @Override
    public void exitEnumSpecifier(CParser.EnumSpecifierContext ctx) {
        super.exitEnumSpecifier(ctx);
    }

    @Override
    public void enterEnumeratorList(CParser.EnumeratorListContext ctx) {
        super.enterEnumeratorList(ctx);
    }

    @Override
    public void exitEnumeratorList(CParser.EnumeratorListContext ctx) {
        super.exitEnumeratorList(ctx);
    }

    @Override
    public void enterEnumerator(CParser.EnumeratorContext ctx) {
        super.enterEnumerator(ctx);
    }

    @Override
    public void exitEnumerator(CParser.EnumeratorContext ctx) {
        super.exitEnumerator(ctx);
    }

    @Override
    public void enterEnumerationConstant(CParser.EnumerationConstantContext ctx) {
        super.enterEnumerationConstant(ctx);
    }

    @Override
    public void exitEnumerationConstant(CParser.EnumerationConstantContext ctx) {
        super.exitEnumerationConstant(ctx);
    }

    @Override
    public void enterAtomicTypeSpecifier(CParser.AtomicTypeSpecifierContext ctx) {
        super.enterAtomicTypeSpecifier(ctx);
    }

    @Override
    public void exitAtomicTypeSpecifier(CParser.AtomicTypeSpecifierContext ctx) {
        super.exitAtomicTypeSpecifier(ctx);
    }

    @Override
    public void enterTypeQualifier(CParser.TypeQualifierContext ctx) {
        super.enterTypeQualifier(ctx);
    }

    @Override
    public void exitTypeQualifier(CParser.TypeQualifierContext ctx) {
        super.exitTypeQualifier(ctx);
    }

    @Override
    public void enterFunctionSpecifier(CParser.FunctionSpecifierContext ctx) {
        super.enterFunctionSpecifier(ctx);
    }

    @Override
    public void exitFunctionSpecifier(CParser.FunctionSpecifierContext ctx) {
        super.exitFunctionSpecifier(ctx);
    }

    @Override
    public void enterAlignmentSpecifier(CParser.AlignmentSpecifierContext ctx) {
        super.enterAlignmentSpecifier(ctx);
    }

    @Override
    public void exitAlignmentSpecifier(CParser.AlignmentSpecifierContext ctx) {
        super.exitAlignmentSpecifier(ctx);
    }

    @Override
    public void exitDeclarator(CParser.DeclaratorContext ctx) {
        StringBuilder bf = new StringBuilder();
        String pointer = (ctx.pointer() != null) ? newTexts.get(ctx.pointer()) + " " : "";
        String directDecl = newTexts.get(ctx.directDeclarator());
        bf.append(pointer);
        bf.append(directDecl);

        for (int i = 0; i < ctx.gccDeclaratorExtension().size(); i++) {
            bf.append(newTexts.get(ctx.gccDeclaratorExtension(i)));
        }

        newTexts.put(ctx, bf.toString());
    }

    @Override
    public void exitDirectDeclarator(CParser.DirectDeclaratorContext ctx) {
        super.exitDirectDeclarator(ctx);
    }

    @Override
    public void exitGccDeclaratorExtension(CParser.GccDeclaratorExtensionContext ctx) {
        String bf;
        if (ctx.children.size() == 1) {
            bf = newTexts.get(ctx.gccAttributeSpecifier());
        } else {
            StringBuilder stringLiteral = new StringBuilder();
            for (int i = 0; i < ctx.StringLiteral().size(); i++) {
                stringLiteral.append(newTexts.get(ctx.StringLiteral(i)));
            }
            bf = String.format("__asm(%s)", stringLiteral);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void enterGccAttributeSpecifier(CParser.GccAttributeSpecifierContext ctx) {
        super.enterGccAttributeSpecifier(ctx);
    }

    @Override
    public void exitGccAttributeSpecifier(CParser.GccAttributeSpecifierContext ctx) {
        super.exitGccAttributeSpecifier(ctx);
    }

    @Override
    public void enterGccAttributeList(CParser.GccAttributeListContext ctx) {
        super.enterGccAttributeList(ctx);
    }

    @Override
    public void exitGccAttributeList(CParser.GccAttributeListContext ctx) {
        super.exitGccAttributeList(ctx);
    }

    @Override
    public void enterGccAttribute(CParser.GccAttributeContext ctx) {
        super.enterGccAttribute(ctx);
    }

    @Override
    public void exitGccAttribute(CParser.GccAttributeContext ctx) {
        super.exitGccAttribute(ctx);
    }

    @Override
    public void exitNestedParenthesesBlock(CParser.NestedParenthesesBlockContext ctx) {
        String bf = "";
        for (int i = 0; i < ctx.children.size(); i++) {
            if (newTexts.get(ctx.getChild(i)).startsWith("(")) { //애매쓰...
                bf = String.format("(%s)", newTexts.get(ctx.nestedParenthesesBlock(i)));
            } else {
                bf = newTexts.get(ctx.getChild(0));
            }
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitPointer(CParser.PointerContext ctx) {
        super.exitPointer(ctx);
    }

    @Override
    public void enterTypeQualifierList(CParser.TypeQualifierListContext ctx) {
        super.enterTypeQualifierList(ctx);
    }

    @Override
    public void exitTypeQualifierList(CParser.TypeQualifierListContext ctx) {
        super.exitTypeQualifierList(ctx);
    }

    @Override
    public void exitParameterTypeList(CParser.ParameterTypeListContext ctx) {
        String bf;
        String paramList = newTexts.get(ctx.parameterList());
        if (ctx.children.size() == 1) {
            bf = paramList;
        } else {
            bf = String.format("%s, ...", paramList);
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitParameterList(CParser.ParameterListContext ctx) {
        String bf;
        String paramlist = newTexts.get(ctx.parameterList());
        if (ctx.children.size() == 1) {
            bf = paramlist;
        } else {
            bf = String.format("%s, %s", paramlist, newTexts.get(ctx.parameterDeclaration()));
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitParameterDeclaration(CParser.ParameterDeclarationContext ctx) {
        String bf;
        if (ctx.declarationSpecifiers() != null) {
            String declSpec = newTexts.get(ctx.declarationSpecifiers());
            String decl = newTexts.get(ctx.declarator());
            bf = String.format("%s %s", declSpec, decl);
        } else {
            String declSpec2 = newTexts.get(ctx.declarationSpecifiers2());
            String abst = (ctx.abstractDeclarator() != null) ? newTexts.get(ctx.abstractDeclarator()) : "";
            bf = String.format("%s %s", declSpec2, abst);
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitIdentifierList(CParser.IdentifierListContext ctx) {
        String bf;
        String ident = newTexts.get(ctx.Identifier());

        if (ctx.children.size() == 1) {
            bf = ident;
        } else {
            String idList = newTexts.get(ctx.identifierList());
            bf = String.format("%s, %s", idList, ident);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitTypeName(CParser.TypeNameContext ctx) {
        String bf = (ctx.specifierQualifierList() != null) ? newTexts.get(ctx.specifierQualifierList()) : "";
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitAbstractDeclarator(CParser.AbstractDeclaratorContext ctx) {
        String bf;
        String pointer = (ctx.pointer() != null) ? newTexts.get(ctx.pointer()) : "";
        if (ctx.directAbstractDeclarator() == null) {
            bf = pointer;
        } else {
            String directdecl = newTexts.get(ctx.directAbstractDeclarator());
            bf = pointer + directdecl;
            for (int i = 0; i < ctx.gccDeclaratorExtension().size(); i++) {
                bf += newTexts.get(ctx.gccDeclaratorExtension(i));
            }
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitDirectAbstractDeclarator(CParser.DirectAbstractDeclaratorContext ctx) {
        super.exitDirectAbstractDeclarator(ctx);
    }

    @Override
    public void exitTypedefName(CParser.TypedefNameContext ctx) {
        String bf = newTexts.get(ctx.Identifier());
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitInitializer(CParser.InitializerContext ctx) {
        String bf;
        int size = ctx.children.size();
        if (size == 1) {
            bf = newTexts.get(ctx.assignmentExpression());
        } else if (size == 3) {
            String init = newTexts.get(ctx.initializerList());
            bf = String.format("{%s}", init);
        } else {
            String init = newTexts.get(ctx.initializerList());
            bf = String.format("{%s, }", init);
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitInitializerList(CParser.InitializerListContext ctx) {
        String bf;
        String design = (ctx.designation() != null) ? newTexts.get(ctx.designation()) : "";
        String init = newTexts.get(ctx.initializer());
        if (ctx.initializerList() != null) {
            String initlist = newTexts.get(ctx.initializerList());
            bf = String.format("%s, %s%s", initlist, design, init);
        } else {
            bf = design + init;
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitDesignation(CParser.DesignationContext ctx) {
        String bf = String.format("%s = ", newTexts.get(ctx.designatorList()));
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitDesignatorList(CParser.DesignatorListContext ctx) {
        String bf;
        String designator = newTexts.get(ctx.designator());
        if (ctx.children.size() == 1) {
            bf = designator;
        } else {
            bf = String.format("%s %s", newTexts.get(ctx.designatorList()), designator);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitDesignator(CParser.DesignatorContext ctx) {
        String bf;
        if (ctx.children.size() == 3) {
            bf = String.format("[%s]", newTexts.get(ctx.constantExpression()));
        } else {
            bf = String.format(".%s", newTexts.get(ctx.Identifier()));
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitStaticAssertDeclaration(CParser.StaticAssertDeclarationContext ctx) {
        super.exitStaticAssertDeclaration(ctx);
    }

    @Override
    public void exitStatement(CParser.StatementContext ctx) {
        String bf = "";
        if (ctx.labeledStatement() != null) {
            bf = newTexts.get(ctx.labeledStatement());
        } else if (ctx.compoundStatement() != null) {
            bf = newTexts.get(ctx.compoundStatement());
        } else if (ctx.expressionStatement() != null) {
            bf = newTexts.get(ctx.expressionStatement());
        } else if (ctx.selectionStatement() != null) {
            bf = newTexts.get(ctx.selectionStatement());
        } else if (ctx.iterationStatement() != null) {
            bf = newTexts.get(ctx.iterationStatement());
        } else if (ctx.jumpStatement() != null) {
            bf = newTexts.get(ctx.jumpStatement());
        } else {
            // todo
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitLabeledStatement(CParser.LabeledStatementContext ctx) {
        String bf;
        String stmt = newTexts.get(ctx.statement());
        if (ctx.children.size() == 3 && ctx.Identifier() != null) {
            String id = newTexts.get(ctx.Identifier());
            bf = String.format("%s : %s", id, stmt);
        } else if (ctx.children.size() == 3 && ctx.Identifier() == null) {
            bf = String.format("default : %s", stmt);
        } else {
            String constant = newTexts.get(ctx.constantExpression());
            bf = String.format("case %s : %s", constant, stmt);
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void enterCompoundStatement(CParser.CompoundStatementContext ctx) {
        super.enterCompoundStatement(ctx);
    }

    @Override
    public void exitCompoundStatement(CParser.CompoundStatementContext ctx) {
        String blocklist = (ctx.blockItemList() != null) ? newTexts.get(ctx.blockItemList()) : "";
        String bf = String.format("{%s}", blocklist);
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitBlockItemList(CParser.BlockItemListContext ctx) {
        String bf;
        String blckitem = newTexts.get(ctx.blockItem());
        if (ctx.children.size() == 1) {
            bf = blckitem;
        } else {
            String blckList = newTexts.get(ctx.blockItemList());
            bf = String.format("%s %s", blckList, blckitem);
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitBlockItem(CParser.BlockItemContext ctx) {
        String bf;
        if (ctx.statement() != null) {
            bf = newTexts.get(ctx.statement());
        } else {
            bf = newTexts.get(ctx.declaration());
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitExpressionStatement(CParser.ExpressionStatementContext ctx) {
        String exp = (ctx.expression() != null) ? newTexts.get(ctx.expression()) : "";
        String bf = String.format("%s;\n", exp);
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitSelectionStatement(CParser.SelectionStatementContext ctx) {
        String bf;
        String exp = newTexts.get(ctx.expression());
        String stmt1 = newTexts.get(ctx.statement(0));
        String stmt2 = (ctx.statement().size() > 1) ? newTexts.get(ctx.statement(1)) : null;
        if (newTexts.get(ctx.getChild(0)).equals("if")) {
            bf = String.format("if(%s)%s", exp, stmt1);
            if (stmt2 != null) {
                bf += String.format("else %s", stmt2);
            }
        } else {
            bf = String.format("switch (%s) %s", exp, stmt1);
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void exitIterationStatement(CParser.IterationStatementContext ctx) {
        String bf;
        String stmt = newTexts.get(ctx.statement());
        if (ctx.For() != null) {
            String forCond = newTexts.get(ctx.forCondition());
            String forT = newTexts.get(ctx.For());
            bf = String.format("%s (%s) %s", forT, forCond, stmt);
        } else {
            String exp = newTexts.get(ctx.expression());
            String whileT = newTexts.get(ctx.While());
            if (ctx.Do() == null) {
                bf = String.format("%s (%s) %s", whileT, exp, stmt);
            } else {
                String doT = newTexts.get(ctx.Do());
                bf = String.format("%s %s %s (%s);\n", doT, stmt, whileT, exp);
            }
        }
        newTexts.put(ctx, bf);
    }

    @Override
    public void enterForCondition(CParser.ForConditionContext ctx) {
        super.enterForCondition(ctx);
    }

    @Override
    public void exitForCondition(CParser.ForConditionContext ctx) {
        super.exitForCondition(ctx);
    }

    @Override
    public void enterForDeclaration(CParser.ForDeclarationContext ctx) {
        super.enterForDeclaration(ctx);
    }

    @Override
    public void exitForDeclaration(CParser.ForDeclarationContext ctx) {
        super.exitForDeclaration(ctx);
    }

    @Override
    public void enterForExpression(CParser.ForExpressionContext ctx) {
        super.enterForExpression(ctx);
    }

    @Override
    public void exitForExpression(CParser.ForExpressionContext ctx) {
        super.exitForExpression(ctx);
    }

    @Override
    public void enterJumpStatement(CParser.JumpStatementContext ctx) {
        super.enterJumpStatement(ctx);
    }

    @Override
    public void exitJumpStatement(CParser.JumpStatementContext ctx) {
        super.exitJumpStatement(ctx);
    }

    @Override
    public void enterCompilationUnit(CParser.CompilationUnitContext ctx) {
        super.enterCompilationUnit(ctx);
    }

    @Override
    public void exitCompilationUnit(CParser.CompilationUnitContext ctx) {
        super.exitCompilationUnit(ctx);
    }

    @Override
    public void enterTranslationUnit(CParser.TranslationUnitContext ctx) {
        super.enterTranslationUnit(ctx);
    }

    @Override
    public void exitTranslationUnit(CParser.TranslationUnitContext ctx) {
        super.exitTranslationUnit(ctx);
    }

    @Override
    public void enterExternalDeclaration(CParser.ExternalDeclarationContext ctx) {
        super.enterExternalDeclaration(ctx);
    }

    @Override
    public void exitExternalDeclaration(CParser.ExternalDeclarationContext ctx) {
        super.exitExternalDeclaration(ctx);
    }

    @Override
    public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        super.enterFunctionDefinition(ctx);
    }

    @Override
    public void exitFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        super.exitFunctionDefinition(ctx);
    }

    @Override
    public void enterDeclarationList(CParser.DeclarationListContext ctx) {
        super.enterDeclarationList(ctx);
    }

    @Override
    public void exitDeclarationList(CParser.DeclarationListContext ctx) {
        super.exitDeclarationList(ctx);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        super.enterEveryRule(ctx);
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        super.exitEveryRule(ctx);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        super.visitTerminal(node);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }
}
