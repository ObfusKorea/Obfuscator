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
			program = ctx.Identifier().getText();
			program = obfus_ID(program);
		} else if (ctx.Constant() != null) {
			program = ctx.Constant().getText();
		} else if (ctx.expression() != null) {
			program = String.format("(%s)", newTexts.get(ctx.expression()));
		} else if (ctx.StringLiteral() != null) {
			for (int i = 0; i < ctx.StringLiteral().size(); i++) {
				program = ctx.StringLiteral(i).getText();
			}
		}

		newTexts.put(ctx, program);
	}

	@Override
	public void exitGenericSelection(CParser.GenericSelectionContext ctx) {
		String bf;
		String assign = newTexts.get(ctx.assignmentExpression());
		String generic = newTexts.get(ctx.genericAssocList());
		String _gen = ctx.Generic().getText();
		bf = String.format("%s (%s, %s)", _gen, assign, generic);
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitGenericAssocList(CParser.GenericAssocListContext ctx) {
		String bf;
		String genAssociation = newTexts.get(ctx.genericAssociation());
		if (ctx.children.size() == 1) {
			bf = genAssociation;
		} else {
			String list = newTexts.get(ctx.genericAssocList());
			bf = String.format("%s, %s", list, genAssociation);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitGenericAssociation(CParser.GenericAssociationContext ctx) {
		String bf;
		String assignExp = newTexts.get(ctx.assignmentExpression());
		String st;
		if (ctx.typeName() != null) {
			st = newTexts.get(ctx.typeName());
		} else {
			st = ctx.Default().getText();
		}
		bf = String.format("%s : %s", st, assignExp);
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitPostfixExpression(CParser.PostfixExpressionContext ctx) {
		String bf = "";
		String postfix = newTexts.get(ctx.postfixExpression());
		int child = ctx.children.size();
		if (child == 1) { // primaryExp
			bf = newTexts.get(ctx.primaryExpression());
		} else if(ctx.postfixExpression()!=null){
			if (child == 2) { // postfixExp ++/--
				bf = String.format("%s%s", postfix, ctx.getChild(1).getText());
			} else if (ctx.Identifier() != null) { // postfix -> / . Identifier
				String id = ctx.Identifier().getText();
				id = obfus_ID(id);
				bf = String.format("%s %s %s", postfix, ctx.getChild(1).getText(), id);
			} else if (ctx.getChild(1).getText().equals("[")) { // profixEXP [ expression ]
				bf = String.format("%s[%s]", postfix, newTexts.get(ctx.expression()));
			} else if (ctx.getChild(1).getText().equals("(")) { // postfix ( argument? )
				String argument = (ctx.argumentExpressionList() != null) ? newTexts.get(ctx.argumentExpressionList()) : "";
				bf = String.format("%s(%s)", postfix, argument);
			}
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
			bf = String.format("%s%s", ctx.getChild(0).getText(), newTexts.get(ctx.unaryExpression()));
		} else if (ctx.unaryOperator() != null) { // unaryOp castExp
			bf = String.format("%s%s", newTexts.get(ctx.unaryOperator()), newTexts.get(ctx.castExpression()));
		} else if (ctx.typeName() != null) { // typename
			bf = String.format("%s(%s)", ctx.getChild(0).getText(), newTexts.get(ctx.typeName()));
		} else {
			String id = ctx.Identifier().getText();
			id = obfus_ID(id);
			bf = String.format("%s%s", ctx.getChild(0).getText(), id);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitUnaryOperator(CParser.UnaryOperatorContext ctx) {
		String bf = ctx.getChild(0).getText();
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitCastExpression(CParser.CastExpressionContext ctx) {
		String bf;
		if (ctx.unaryExpression() != null) {
			String unary = newTexts.get(ctx.unaryExpression());
			bf = unary;
		} else if (ctx.DigitSequence() != null) {
			String digit = ctx.DigitSequence().getText();
			bf = digit;
		} else {
			String type = newTexts.get(ctx.typeName());
			String castExp = newTexts.get(ctx.castExpression());
			if (ctx.children.size() == 4) {
				bf = String.format("(%s) %s", type, castExp);
			} else {
				bf = String.format("%s (%s) %s", ctx.getChild(0).getText(), type, castExp);
			}
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitMultiplicativeExpression(CParser.MultiplicativeExpressionContext ctx) {
		String bf;
		if (ctx.children.size() == 1) { // castExp
			bf = newTexts.get(ctx.castExpression());
		} else {
			String multi = newTexts.get(ctx.multiplicativeExpression());
			String op = ctx.getChild(1).getText();
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
			String op = ctx.getChild(1).getText();
			bf = obfus_binaryExp(additive, op, multi);
//			bf = String.format("%s %s %s", additive, op, multi);
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
			String op = ctx.getChild(1).getText();
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
			String op = ctx.getChild(1).getText();
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
			String op = ctx.getChild(1).getText();
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
			String op = ctx.getChild(1).getText();
			bf = obfus_binaryExp(and, op, equal);
//			bf = String.format("%s %s %s", and, op, equal);
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
			String op = ctx.getChild(1).getText();
			bf = obfus_binaryExp(exclusive, op, and);
//			bf = String.format("%s %s %s", exclusive, op, and);
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
			String op = ctx.getChild(1).getText();
			bf = obfus_binaryExp(inclusive, op, exclusive);
//			bf = String.format("%s %s %s", inclusive, op, exclusive);
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
			String op = ctx.getChild(1).getText();
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
			String op = ctx.getChild(1).getText();
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
		if (ctx.conditionalExpression() != null) { // conditionalExpression
			bf = newTexts.get(ctx.conditionalExpression());
			obfus_arg(bf);
		} else if (ctx.unaryExpression() != null) { // unaryExpression assignmentOperator assignmentExpression
			String unary = newTexts.get(ctx.unaryExpression());
			String assignOP = newTexts.get(ctx.assignmentOperator());
			String assignExp = newTexts.get(ctx.assignmentExpression());
			bf = String.format("%s %s %s", unary, assignOP, assignExp);
		} else { // DigitSequence
			bf = ctx.DigitSequence().getText();
		}

		newTexts.put(ctx, bf);
	}

	@Override
	public void exitAssignmentOperator(CParser.AssignmentOperatorContext ctx) {
		newTexts.put(ctx, ctx.getChild(0).getText());
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
		if (cSize == 1) { // staticAssertDeclaration
			bf = newTexts.get(ctx.staticAssertDeclaration());
		} else if (cSize == 2) { // declarationSpecifiers ';'
			String declarSpeci = newTexts.get(ctx.declarationSpecifiers());
			bf = String.format("%s;\n", declarSpeci);
		} else { // declarationSpecifiers initDeclaratorList ';'
			String declarSpeci = newTexts.get(ctx.declarationSpecifiers());
			String initDecl = newTexts.get(ctx.initDeclaratorList());
			bf = String.format("%s %s;\n", declarSpeci, initDecl);
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
		newTexts.put(ctx, ctx.getChild(0).getText());
	}

	@Override
	public void exitTypeSpecifier(CParser.TypeSpecifierContext ctx) {
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
			bf = ctx.getChild(0).getText();
		}else if(ctx.typeSpecifier()!=null){
			String type = newTexts.get(ctx.typeSpecifier());
			String p = newTexts.get(ctx.pointer());
			bf = String.format("%s %s", type, p);
		} else if(ctx.constantExpression()!=null){
			String con = newTexts.get(ctx.constantExpression());
			bf = String.format("__typeof__(%s)", con);
		}else{
			String con = ctx.getChild(2).getText();
			bf = String.format("__extension__(%s)", con);
		}

		newTexts.put(ctx, bf);
//		super.exitTypeSpecifier(ctx);
	}

	@Override
	public void enterStructOrUnionSpecifier(CParser.StructOrUnionSpecifierContext ctx) {
		super.enterStructOrUnionSpecifier(ctx);
	}

	@Override
	public void exitStructOrUnionSpecifier(CParser.StructOrUnionSpecifierContext ctx) {
		String bf;
		String struct = newTexts.get(ctx.structOrUnion());
		String id = (ctx.Identifier() != null) ? ctx.Identifier().getText() : "";
		id = obfus_ID(id);
		if (ctx.children.size() == 2) {
			bf = String.format("%s %s", struct, id);
		} else {
			String stlist = newTexts.get(ctx.structDeclarationList());
			bf = String.format("%s %s{\n%s\n}\n", struct, id, stlist);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitStructOrUnion(CParser.StructOrUnionContext ctx) {
		String bf = ctx.getChild(0).getText();
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitStructDeclarationList(CParser.StructDeclarationListContext ctx) {
		String bf;
		String stDecl = newTexts.get(ctx.structDeclaration());
		if (ctx.children.size() == 1) {
			bf = stDecl;
		} else {
			String stList = newTexts.get(ctx.structDeclarationList());
			bf = String.format("%s %s", stDecl, stList);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitStructDeclaration(CParser.StructDeclarationContext ctx) {
		String bf;
		if (ctx.children.size() == 1) {
			String spQuilifierList = newTexts.get(ctx.specifierQualifierList());
			String structDeclList = (ctx.structDeclaratorList() != null) ? (newTexts.get(ctx.structDeclaratorList()))
					: "";
			bf = String.format("%s %s;\n", spQuilifierList, structDeclList);
		} else {
			String assertDecl = newTexts.get(ctx.staticAssertDeclaration());
			bf = assertDecl;
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitSpecifierQualifierList(CParser.SpecifierQualifierListContext ctx) {
		String bf, type;
		String specQlist = (ctx.specifierQualifierList() != null) ? newTexts.get(ctx.specifierQualifierList()) : "";
		if (ctx.typeSpecifier() != null) {
			type = newTexts.get(ctx.typeSpecifier());
		} else {
			type = newTexts.get(ctx.typeQualifier());
		}
		bf = String.format("%s %s", type, specQlist);
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitStructDeclaratorList(CParser.StructDeclaratorListContext ctx) {
		String bf;
		String stctDecl = newTexts.get(ctx.structDeclarator());
		if (ctx.children.size() == 1) {
			bf = stctDecl;
		} else {
			String strctList = newTexts.get(ctx.structDeclaratorList());
			bf = String.format("%s, %s", strctList, stctDecl);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitStructDeclarator(CParser.StructDeclaratorContext ctx) {
		String bf;
		String decl = (ctx.declarator() != null) ? newTexts.get(ctx.declarator()) : "";
		if (ctx.children.size() == 1) {
			bf = decl;
		} else {
			String constExp = newTexts.get(ctx.constantExpression());
			bf = String.format("%s : %s", decl, constExp);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitEnumSpecifier(CParser.EnumSpecifierContext ctx) {
		String bf;
		String id = (ctx.Identifier() != null) ? (ctx.Identifier().getText()) : "";
		id = obfus_ID(id);
		String enumList = (ctx.enumeratorList() != null) ? (newTexts.get(ctx.enumeratorList())) : "";
		int size = ctx.children.size();
		if (size == 2) {
			bf = String.format("enum %s", id);
		} else if (ctx.Comma() == null) {
			bf = String.format("enum %s{\n%s\n}\n", id, enumList);
		} else {
			bf = String.format("enum %s{\n%s,\n}\n", id, enumList);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitEnumeratorList(CParser.EnumeratorListContext ctx) {
		String bf;
		String enumerator = newTexts.get(ctx.enumerator());
		if (ctx.children.size() == 1) {
			bf = enumerator;
		} else {
			String enumList = newTexts.get(ctx.enumeratorList());
			bf = String.format("%s, %s", enumList, enumerator);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitEnumerator(CParser.EnumeratorContext ctx) {
		String bf;
		String enumConst = newTexts.get(ctx.enumerationConstant());
		if (ctx.children.size() == 1) {
			bf = enumConst;
		} else {
			String constExp = newTexts.get(ctx.constantExpression());
			bf = String.format("%s = %s", enumConst, constExp);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitEnumerationConstant(CParser.EnumerationConstantContext ctx) { //todo : id
		newTexts.put(ctx, ctx.Identifier().getText());
	}

	@Override
	public void exitAtomicTypeSpecifier(CParser.AtomicTypeSpecifierContext ctx) {
		String bf = String.format("%s(%s)",ctx.Atomic().getText(), newTexts.get(ctx.typeName()));
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitTypeQualifier(CParser.TypeQualifierContext ctx) {
		String bf = ctx.getChild(0).getText();
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitFunctionSpecifier(CParser.FunctionSpecifierContext ctx) {
		String bf;
		if (ctx.gccAttributeSpecifier() != null) {
			bf = newTexts.get(ctx.gccAttributeSpecifier());
		} else if (ctx.children.size() == 1) {
			bf = ctx.getChild(0).getText();
		} else {
			String id = ctx.Identifier().getText();
			bf = String.format("%s(%s)", ctx.getChild(0).getText(), id);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitAlignmentSpecifier(CParser.AlignmentSpecifierContext ctx) {
		String bf, st;
		if (ctx.typeName() != null) {
			st = newTexts.get(ctx.typeName());
		} else {
			st = newTexts.get(ctx.constantExpression());
		}
		bf = String.format("%s(%s)", ctx.Alignas().getText(), st);
		newTexts.put(ctx, bf);
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
	public void enterDirectDeclarator(CParser.DirectDeclaratorContext ctx) {
		if (ctx.Identifier() != null) {
			String id = ctx.Identifier().getText();
			obfuscateIdent(id);
		}
	}
	@Override
	public void exitDirectDeclarator(CParser.DirectDeclaratorContext ctx) {
		String bf;
		if (ctx.declarator() != null) { // '(' declarator ')'
			String decl = newTexts.get(ctx.declarator());
			bf = String.format("(%s)", decl);
		} else if (ctx.Identifier() != null) {
			String id = ctx.Identifier().getText();
			id = obfus_ID(id);
			if (ctx.children.size() == 1) { // Identifier
				bf = id;
			} else { // Identifier ':' DigitSequence
				String digitSeq = ctx.DigitSequence().getText();
				bf = String.format("%s : %s", id, digitSeq);
			}
		} else {
			String directDecl = newTexts.get(ctx.directDeclarator());
			if (ctx.pointer() != null) { // '(' typeSpecifier? pointer directDeclarator ')'
				String typeSpec = (ctx.typeSpecifier() != null) ? newTexts.get(ctx.typeSpecifier()) + " " : "";
				String pointer = newTexts.get(ctx.pointer());
				bf = String.format("(%s%s %s)", typeSpec, pointer, directDecl);
			} else if (ctx.parameterTypeList() != null) { // directDeclarator '(' parameterTypeList ')'
				String param = newTexts.get(ctx.parameterTypeList());
				bf = String.format("%s(%s)", directDecl, param);
			} else if (ctx.getChild(1).getText().equals("(")) { // directDeclarator '(' identifierList? ')'
				String id = (ctx.identifierList() != null) ? newTexts.get(ctx.identifierList()) : "";
				bf = String.format("%s(%s)", directDecl, id);
			} else {
				String typeQList = (ctx.typeQualifierList() != null) ? newTexts.get(ctx.typeQualifierList()) : "";
				String assignExp = (ctx.assignmentExpression() != null) ? newTexts.get(ctx.assignmentExpression()) : "";
				if (ctx.Star() != null) { // directDeclarator '[' typeQualifierList? '*' ']'
					bf = String.format("%s[%s*]", directDecl, typeQList);
				} else if (ctx.Static() == null) {// directDeclarator '[' typeQualifierList? assignmentExpression? ']'
					bf = String.format("%s[%s %s]", directDecl, typeQList, assignExp);
				} else {
					if (ctx.children.size() == 6) { // directDeclarator '[' typeQualifierList 'static'
													// assignmentExpression ']'
						bf = String.format("%s[%s static %s]", directDecl, typeQList, assignExp);
					} else {
						bf = String.format("%s[static %s %s]", directDecl, typeQList, assignExp);
					}
				}
			}
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitGccDeclaratorExtension(CParser.GccDeclaratorExtensionContext ctx) {
		String bf;
		if (ctx.children.size() == 1) {
			bf = newTexts.get(ctx.gccAttributeSpecifier());
		} else {
			StringBuilder stringLiteral = new StringBuilder();
			for (int i = 0; i < ctx.StringLiteral().size(); i++) {
				stringLiteral.append(ctx.StringLiteral(i).getText());
			}
			bf = String.format("__asm(%s)", stringLiteral);
		}

		newTexts.put(ctx, bf);
	}

	@Override
	public void exitGccAttributeSpecifier(CParser.GccAttributeSpecifierContext ctx) {
		String bf;
		String gcc = newTexts.get(ctx.gccAttributeList());
		String att = ctx.getChild(0).getText();
		bf = String.format("%s((%s))", att, gcc);
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitGccAttributeList(CParser.GccAttributeListContext ctx) {
		String bf = "";
		if (ctx.children.size() > 0) {
			StringBuilder gcc = new StringBuilder();
			for (int i = 0; i < ctx.gccAttribute().size(); i++) {
				if (i != 0) {
					gcc.append(", ");
				}
				gcc.append(newTexts.get(ctx.gccAttribute(i)));
			}
			bf = gcc.toString();
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitGccAttribute(CParser.GccAttributeContext ctx) {
		String bf = "";
		if (ctx.children.size() > 0) {
			bf = ctx.getChild(0).getText();
			if (ctx.getChild(1) != null) {
				String arguList = (ctx.argumentExpressionList() != null) ? newTexts.get(ctx.argumentExpressionList())
						: "";
				String t = String.format("(%s)", arguList);
				bf += t;
			}
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitNestedParenthesesBlock(CParser.NestedParenthesesBlockContext ctx) {
		String bf = "";
		for (int i = 0; i < ctx.children.size(); i++) {
			if (ctx.getChild(i).getText().startsWith("(")) { // 애매쓰...
				bf = String.format("(%s)", newTexts.get(ctx.nestedParenthesesBlock(i)));
			} else {
				bf = ctx.getChild(0).getText();
			}
		}

		newTexts.put(ctx, bf);
	}

	@Override
	public void exitPointer(CParser.PointerContext ctx) {
		String bf;
		String t = ctx.getChild(0).getText();
		String typeList = (ctx.typeQualifierList() != null) ? newTexts.get(ctx.typeQualifierList()) : "";

		if (ctx.pointer() != null) {
			String pointer = newTexts.get(ctx.pointer());
			bf = String.format("%s%s %s", t, typeList, pointer);
		} else {
			bf = String.format("%s%s", t, typeList);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitTypeQualifierList(CParser.TypeQualifierListContext ctx) {
		String bf;
		String typeQ = newTexts.get(ctx.typeQualifier());
		if (ctx.children.size() == 1) {
			bf = typeQ;
		} else {
			String typeList = newTexts.get(ctx.typeQualifierList());
			bf = String.format("%s %s", typeList, typeQ);
		}
		newTexts.put(ctx, bf);
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
		String paramdecl = newTexts.get(ctx.parameterDeclaration());
		if (ctx.children.size() == 1) {
			bf = paramdecl;
		} else {
			bf = String.format("%s, %s", newTexts.get(ctx.parameterList()), paramdecl);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void enterParameterDeclaration(CParser.ParameterDeclarationContext ctx) {
		if (ctx.declarationSpecifiers() != null && ctx.declarator()!=null) {
			String decl = newTexts.get(ctx.declarator());
			obfuscateIdent(decl);
		}
	}

	@Override
	public void exitParameterDeclaration(CParser.ParameterDeclarationContext ctx) {
		String bf;
		if (ctx.declarationSpecifiers() != null) {
			String declSpec = newTexts.get(ctx.declarationSpecifiers());
			String decl = newTexts.get(ctx.declarator());
//			obfuscateIdent(decl);
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
		String id = ctx.Identifier().getText();
		id = obfus_ID(id);
		if (ctx.children.size() == 1) {
			bf = id;
		} else {
			String idList = newTexts.get(ctx.identifierList());
			bf = String.format("%s, %s", idList, id);
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
//todo
	}

	@Override
	public void exitTypedefName(CParser.TypedefNameContext ctx) {
		String bf = ctx.Identifier().getText();
		bf = obfus_ID(bf);
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
			String id = ctx.Identifier().getText();
			id = obfus_ID(id);
			bf = String.format(".%s", id);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitStaticAssertDeclaration(CParser.StaticAssertDeclarationContext ctx) {
		String bf;
		String constantExp = newTexts.get(ctx.constantExpression());
		StringBuilder stringliteral = new StringBuilder();
		for (int i = 0; i < ctx.StringLiteral().size(); i++) {
			stringliteral.append(ctx.StringLiteral(i).getText());
		}
		bf = String.format("%s(%s,%s);\n", ctx.StaticAssert().getText(), constantExp, stringliteral);
		newTexts.put(ctx, bf);
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

		if(ctx.jumpStatement()==null){
			bf = obfus_opaque(bf);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitLabeledStatement(CParser.LabeledStatementContext ctx) {
		String bf;
		String stmt = newTexts.get(ctx.statement());
		if (ctx.children.size() == 3 && ctx.Identifier() != null) {
			String id = ctx.Identifier().getText();
			id = obfus_ID(id); //todo : id 난독화 해도 되는지 체크
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
	public void exitCompoundStatement(CParser.CompoundStatementContext ctx) {
		String blocklist = (ctx.blockItemList() != null) ? newTexts.get(ctx.blockItemList()) : "";
		String bf = String.format("{\n %s}\n\n", blocklist);
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
		String bf = "";
		if(ctx.children.size()>2){
			for (int i = 0; i < ctx.expressionStatement().size(); i++) {
				bf += newTexts.get(ctx.expressionStatement(i));
			}
		}else{
			String exp = (ctx.expression() != null) ? newTexts.get(ctx.expression()) : "";
			bf = String.format("%s;\n", exp);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitSelectionStatement(CParser.SelectionStatementContext ctx) {
		String bf;
		String exp = newTexts.get(ctx.expression());
		String stmt1 = newTexts.get(ctx.statement(0));
		String stmt2 = (ctx.statement().size() > 1) ? newTexts.get(ctx.statement(1)) : null;
		if (ctx.getChild(0).getText().equals("if")) {
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
			String forT = ctx.For().getText();
			bf = String.format("%s (%s) %s", forT, forCond, stmt);
		} else {
			String exp = newTexts.get(ctx.expression());
			String whileT = ctx.While().getText();
			if (ctx.Do() == null) {
				bf = String.format("%s (%s) %s", whileT, exp, stmt);
			} else {
				String doT = ctx.Do().getText();
				bf = String.format("%s %s %s (%s);\n", doT, stmt, whileT, exp);
			}
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitForCondition(CParser.ForConditionContext ctx) {
		// todo
        String bf;
        String s1, forExp1, forExp2;
        if(ctx.forDeclaration()!=null){
            if(ctx.getChild(2).getText().equals(";")){
                forExp1 = "" ;
                forExp2 = (ctx.forExpression()!=null) ? newTexts.get(ctx.forExpression(0)) : "";
            }else{
                forExp1 = newTexts.get(ctx.forExpression(0));
                forExp2 = (ctx.forExpression().size()>1) ? newTexts.get(ctx.forExpression(1)) : "";
            }
            s1 = newTexts.get(ctx.forDeclaration());
        }else{
        	if(ctx.expression()!=null){
				if(ctx.getChild(2).getText().equals(";")){
					forExp1 = "" ;
					forExp2 = (ctx.forExpression()!=null) ? newTexts.get(ctx.forExpression(0)) : "";
				}else{
					forExp1 = newTexts.get(ctx.forExpression(0));
					forExp2 = (ctx.forExpression().size()>1) ? newTexts.get(ctx.forExpression(1)) : "";
				}
			}else{
				if(ctx.getChild(1).getText().equals(";")){
					forExp1 = "" ;
					forExp2 = (ctx.forExpression()!=null) ? newTexts.get(ctx.forExpression(0)) : "";
				}else{
					forExp1 = newTexts.get(ctx.forExpression(0));
					forExp2 = (ctx.forExpression().size()>1) ? newTexts.get(ctx.forExpression(1)) : "";
				}
			}
            s1 = (ctx.expression()!=null) ? newTexts.get(ctx.expression()) : "";
        }
        bf = String.format("%s ; %s ; %s", s1, forExp1, forExp2);
        newTexts.put(ctx, bf);
	}

	@Override
	public void exitForDeclaration(CParser.ForDeclarationContext ctx) {
		String bf;
		String declSpec = newTexts.get(ctx.declarationSpecifiers());
		if (ctx.children.size() == 2) {
			String initDecl = newTexts.get(ctx.initDeclaratorList());
			bf = String.format("%s %s", declSpec, initDecl);
		} else {
			bf = declSpec;
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitForExpression(CParser.ForExpressionContext ctx) {
		String bf;
		String assignExp = newTexts.get(ctx.assignmentExpression());
		if (ctx.children.size() == 1) {
			bf = assignExp;
		} else {
			String forExp = newTexts.get(ctx.forExpression());
			bf = String.format("%s, %s", forExp, assignExp);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitJumpStatement(CParser.JumpStatementContext ctx) {
		String bf;
		if (ctx.Goto() != null) {
			if (ctx.Identifier() != null) {
				String id = ctx.Identifier().getText();
				id = obfus_ID(id);
				bf = String.format("goto %s ;\n", id);
			} else {
				String unaryExp = newTexts.get(ctx.unaryExpression());
				bf = String.format("goto %s ;\n", unaryExp);
			}
		} else if (ctx.Continue() != null) {
			bf = "continue ;\n";
		} else if (ctx.Break() != null) {
			bf = "break ;\n";
		} else {
			String exp = (ctx.expression() != null) ? newTexts.get(ctx.expression()) : "";
			bf = String.format("return %s;\n", exp);
			bf = Obfus_RtStmt(bf);
		}

		newTexts.put(ctx, bf);
	}

	@Override
	public void enterCompilationUnit(CParser.CompilationUnitContext ctx) {

	}

	// 프로그램 시작부분
	@Override
	public void exitCompilationUnit(CParser.CompilationUnitContext ctx) {
		String bf;
		String transUnit = (ctx.translationUnit() != null) ? newTexts.get(ctx.translationUnit()) + "\n" : "";
//        String eof = newTexts.get(ctx.EOF());
//        bf = String.format("%s%s", transUnit, eof);
		bf = String.format("%s", transUnit);
		bf = insertINIT(bf);
		newTexts.put(ctx, bf);

		System.out.println(bf);

		File file = new File(String.format("result_C_%d.c", this.outputCount));

		try {
			FileWriter fw = new FileWriter(file);
			fw.write(bf);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exitTranslationUnit(CParser.TranslationUnitContext ctx) {
		String bf;
		String externDecl = newTexts.get(ctx.externalDeclaration());
		if (ctx.children.size() == 1) {
			bf = externDecl;
		} else {
			String transUnit = newTexts.get(ctx.translationUnit());
			bf = String.format("%s%s", transUnit, externDecl);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitExternalDeclaration(CParser.ExternalDeclarationContext ctx) {
		String bf;
		if (ctx.functionDefinition() != null) {
			bf = newTexts.get(ctx.functionDefinition());
		} else if (ctx.declaration() != null) {
			bf = newTexts.get(ctx.declaration());
		} else {
			bf = ";";
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
		String bf;
		String declSpec = (ctx.declarationSpecifiers() != null) ? newTexts.get(ctx.declarationSpecifiers()) : "";
		String declarator = newTexts.get(ctx.declarator());
		String declList = (ctx.declarationList() != null) ? newTexts.get(ctx.declarationList()) : "";
		String compStmt = newTexts.get(ctx.compoundStatement());
		bf = String.format("%s %s %s %s", declSpec, declarator, declList, compStmt);
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitDeclarationList(CParser.DeclarationListContext ctx) {
		String bf;
		String decl = newTexts.get(ctx.declaration());
		if (ctx.children.size() == 1) {
			bf = decl;
		} else {
			String declList = newTexts.get(ctx.declarationList());
			bf = String.format("%s %s", declList, decl);
		}
		newTexts.put(ctx, bf);
	}

	public String obfus_binaryExp(String additive, String op, String multi) {
		return String.format("%s %s %s", additive, op, multi);
	}

	public String Obfus_RtStmt(String s1){
		return s1;
	}

	public String obfus_ID(String id){
		return id;
	}

	public void obfuscateIdent(String ident) {
	}

	public void obfus_arg(String arg){

	}

	public String obfus_opaque(String stmt){
		return stmt;
	}

	public String insertINIT(String bf) {
		return bf;
	}
}