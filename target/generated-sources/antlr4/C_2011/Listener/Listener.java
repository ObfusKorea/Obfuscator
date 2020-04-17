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
        } else if (ctx.getChild(1).equals(CParser.LeftBracket)) { // profixEXP [ expression ]
            bf = String.format("%s[%s]", postfix, newTexts.get(ctx.expression()));
        } else if (ctx.getChild(1).equals(CParser.LeftParen)) { // postfix ( argument? )
            String argument = (ctx.argumentExpressionList() != null) ? newTexts.get(ctx.argumentExpressionList()) : "";
            bf = String.format("%s(%s)", postfix, argument);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void exitArgumentExpressionList(CParser.ArgumentExpressionListContext ctx) {
        String bf = "";
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
        String bf = "";
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
        String bf = "";
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
        String bf = "";
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
        String bf = "";
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
        String bf = "";
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
        String bf = "";
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
        String bf = "";
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
        String bf = "";
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
        String bf = "";
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
        String bf = "";
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
        String bf = "";
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
        String bf = "";
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
        String bf = "";
        String assignExp = newTexts.get(ctx.assignmentExpression());
        if(ctx.children.size() == 1){
            bf = assignExp;
        }else{
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
        String bf = "";
        int cSize = ctx.children.size();
        if(cSize==1){ //staticAssertDeclaration
            bf = newTexts.get(ctx.staticAssertDeclaration());
        }else if(cSize==2){ // declarationSpecifiers ';'
            String declarSpeci = newTexts.get(ctx.declarationSpecifiers());
            bf = String.format("%s;", declarSpeci);
        }else{ // declarationSpecifiers initDeclaratorList ';'
            String declarSpeci = newTexts.get(ctx.declarationSpecifiers());
            String initDecl = newTexts.get(ctx.initDeclaratorList());
            bf = String.format("%s %s;", declarSpeci, initDecl);
        }

        newTexts.put(ctx, bf);
    }

    @Override
    public void enterDeclarationSpecifiers(CParser.DeclarationSpecifiersContext ctx) {
        super.enterDeclarationSpecifiers(ctx);
    }

    @Override
    public void exitDeclarationSpecifiers(CParser.DeclarationSpecifiersContext ctx) {
        super.exitDeclarationSpecifiers(ctx);
    }

    @Override
    public void enterDeclarationSpecifiers2(CParser.DeclarationSpecifiers2Context ctx) {
        super.enterDeclarationSpecifiers2(ctx);
    }

    @Override
    public void exitDeclarationSpecifiers2(CParser.DeclarationSpecifiers2Context ctx) {
        super.exitDeclarationSpecifiers2(ctx);
    }

    @Override
    public void enterDeclarationSpecifier(CParser.DeclarationSpecifierContext ctx) {
        super.enterDeclarationSpecifier(ctx);
    }

    @Override
    public void exitDeclarationSpecifier(CParser.DeclarationSpecifierContext ctx) {
        super.exitDeclarationSpecifier(ctx);
    }

    @Override
    public void enterInitDeclaratorList(CParser.InitDeclaratorListContext ctx) {
        super.enterInitDeclaratorList(ctx);
    }

    @Override
    public void exitInitDeclaratorList(CParser.InitDeclaratorListContext ctx) {
        super.exitInitDeclaratorList(ctx);
    }

    @Override
    public void enterInitDeclarator(CParser.InitDeclaratorContext ctx) {
        super.enterInitDeclarator(ctx);
    }

    @Override
    public void exitInitDeclarator(CParser.InitDeclaratorContext ctx) {
        super.exitInitDeclarator(ctx);
    }

    @Override
    public void enterStorageClassSpecifier(CParser.StorageClassSpecifierContext ctx) {
        super.enterStorageClassSpecifier(ctx);
    }

    @Override
    public void exitStorageClassSpecifier(CParser.StorageClassSpecifierContext ctx) {
        super.exitStorageClassSpecifier(ctx);
    }

    @Override
    public void enterTypeSpecifier(CParser.TypeSpecifierContext ctx) {
        super.enterTypeSpecifier(ctx);
    }

    @Override
    public void exitTypeSpecifier(CParser.TypeSpecifierContext ctx) {
        super.exitTypeSpecifier(ctx);
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
    public void enterDeclarator(CParser.DeclaratorContext ctx) {
        super.enterDeclarator(ctx);
    }

    @Override
    public void exitDeclarator(CParser.DeclaratorContext ctx) {
        super.exitDeclarator(ctx);
    }

    @Override
    public void enterDirectDeclarator(CParser.DirectDeclaratorContext ctx) {
        super.enterDirectDeclarator(ctx);
    }

    @Override
    public void exitDirectDeclarator(CParser.DirectDeclaratorContext ctx) {
        super.exitDirectDeclarator(ctx);
    }

    @Override
    public void enterGccDeclaratorExtension(CParser.GccDeclaratorExtensionContext ctx) {
        super.enterGccDeclaratorExtension(ctx);
    }

    @Override
    public void exitGccDeclaratorExtension(CParser.GccDeclaratorExtensionContext ctx) {
        super.exitGccDeclaratorExtension(ctx);
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
    public void enterNestedParenthesesBlock(CParser.NestedParenthesesBlockContext ctx) {
        super.enterNestedParenthesesBlock(ctx);
    }

    @Override
    public void exitNestedParenthesesBlock(CParser.NestedParenthesesBlockContext ctx) {
        super.exitNestedParenthesesBlock(ctx);
    }

    @Override
    public void enterPointer(CParser.PointerContext ctx) {
        super.enterPointer(ctx);
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
    public void enterParameterTypeList(CParser.ParameterTypeListContext ctx) {
        super.enterParameterTypeList(ctx);
    }

    @Override
    public void exitParameterTypeList(CParser.ParameterTypeListContext ctx) {
        super.exitParameterTypeList(ctx);
    }

    @Override
    public void enterParameterList(CParser.ParameterListContext ctx) {
        super.enterParameterList(ctx);
    }

    @Override
    public void exitParameterList(CParser.ParameterListContext ctx) {
        super.exitParameterList(ctx);
    }

    @Override
    public void enterParameterDeclaration(CParser.ParameterDeclarationContext ctx) {
        super.enterParameterDeclaration(ctx);
    }

    @Override
    public void exitParameterDeclaration(CParser.ParameterDeclarationContext ctx) {
        super.exitParameterDeclaration(ctx);
    }

    @Override
    public void enterIdentifierList(CParser.IdentifierListContext ctx) {
        super.enterIdentifierList(ctx);
    }

    @Override
    public void exitIdentifierList(CParser.IdentifierListContext ctx) {
        super.exitIdentifierList(ctx);
    }

    @Override
    public void enterTypeName(CParser.TypeNameContext ctx) {
        super.enterTypeName(ctx);
    }

    @Override
    public void exitTypeName(CParser.TypeNameContext ctx) {
        super.exitTypeName(ctx);
    }

    @Override
    public void enterAbstractDeclarator(CParser.AbstractDeclaratorContext ctx) {
        super.enterAbstractDeclarator(ctx);
    }

    @Override
    public void exitAbstractDeclarator(CParser.AbstractDeclaratorContext ctx) {
        super.exitAbstractDeclarator(ctx);
    }

    @Override
    public void enterDirectAbstractDeclarator(CParser.DirectAbstractDeclaratorContext ctx) {
        super.enterDirectAbstractDeclarator(ctx);
    }

    @Override
    public void exitDirectAbstractDeclarator(CParser.DirectAbstractDeclaratorContext ctx) {
        super.exitDirectAbstractDeclarator(ctx);
    }

    @Override
    public void enterTypedefName(CParser.TypedefNameContext ctx) {
        super.enterTypedefName(ctx);
    }

    @Override
    public void exitTypedefName(CParser.TypedefNameContext ctx) {
        super.exitTypedefName(ctx);
    }

    @Override
    public void enterInitializer(CParser.InitializerContext ctx) {
        super.enterInitializer(ctx);
    }

    @Override
    public void exitInitializer(CParser.InitializerContext ctx) {
        super.exitInitializer(ctx);
    }

    @Override
    public void enterInitializerList(CParser.InitializerListContext ctx) {
        super.enterInitializerList(ctx);
    }

    @Override
    public void exitInitializerList(CParser.InitializerListContext ctx) {
        super.exitInitializerList(ctx);
    }

    @Override
    public void enterDesignation(CParser.DesignationContext ctx) {
        super.enterDesignation(ctx);
    }

    @Override
    public void exitDesignation(CParser.DesignationContext ctx) {
        super.exitDesignation(ctx);
    }

    @Override
    public void enterDesignatorList(CParser.DesignatorListContext ctx) {
        super.enterDesignatorList(ctx);
    }

    @Override
    public void exitDesignatorList(CParser.DesignatorListContext ctx) {
        super.exitDesignatorList(ctx);
    }

    @Override
    public void enterDesignator(CParser.DesignatorContext ctx) {
        super.enterDesignator(ctx);
    }

    @Override
    public void exitDesignator(CParser.DesignatorContext ctx) {
        super.exitDesignator(ctx);
    }

    @Override
    public void enterStaticAssertDeclaration(CParser.StaticAssertDeclarationContext ctx) {
        super.enterStaticAssertDeclaration(ctx);
    }

    @Override
    public void exitStaticAssertDeclaration(CParser.StaticAssertDeclarationContext ctx) {
        super.exitStaticAssertDeclaration(ctx);
    }

    @Override
    public void enterStatement(CParser.StatementContext ctx) {
        super.enterStatement(ctx);
    }

    @Override
    public void exitStatement(CParser.StatementContext ctx) {
        super.exitStatement(ctx);
    }

    @Override
    public void enterLabeledStatement(CParser.LabeledStatementContext ctx) {
        super.enterLabeledStatement(ctx);
    }

    @Override
    public void exitLabeledStatement(CParser.LabeledStatementContext ctx) {
        super.exitLabeledStatement(ctx);
    }

    @Override
    public void enterCompoundStatement(CParser.CompoundStatementContext ctx) {
        super.enterCompoundStatement(ctx);
    }

    @Override
    public void exitCompoundStatement(CParser.CompoundStatementContext ctx) {
        super.exitCompoundStatement(ctx);
    }

    @Override
    public void enterBlockItemList(CParser.BlockItemListContext ctx) {
        super.enterBlockItemList(ctx);
    }

    @Override
    public void exitBlockItemList(CParser.BlockItemListContext ctx) {
        super.exitBlockItemList(ctx);
    }

    @Override
    public void enterBlockItem(CParser.BlockItemContext ctx) {
        super.enterBlockItem(ctx);
    }

    @Override
    public void exitBlockItem(CParser.BlockItemContext ctx) {
        super.exitBlockItem(ctx);
    }

    @Override
    public void enterExpressionStatement(CParser.ExpressionStatementContext ctx) {
        super.enterExpressionStatement(ctx);
    }

    @Override
    public void exitExpressionStatement(CParser.ExpressionStatementContext ctx) {
        super.exitExpressionStatement(ctx);
    }

    @Override
    public void enterSelectionStatement(CParser.SelectionStatementContext ctx) {
        super.enterSelectionStatement(ctx);
    }

    @Override
    public void exitSelectionStatement(CParser.SelectionStatementContext ctx) {
        super.exitSelectionStatement(ctx);
    }

    @Override
    public void enterIterationStatement(CParser.IterationStatementContext ctx) {
        super.enterIterationStatement(ctx);
    }

    @Override
    public void exitIterationStatement(CParser.IterationStatementContext ctx) {
        super.exitIterationStatement(ctx);
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
