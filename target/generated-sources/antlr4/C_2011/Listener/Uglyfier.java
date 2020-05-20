package C_2011.Listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import C_2011.generated.CParser;
import listener.main.Obfuscator;

public class Uglyfier extends Listener {

	public Uglyfier(int count) {
		super(count);
	}

	@Override
	public void exitGenericSelection(CParser.GenericSelectionContext ctx) {
		String bf;
		String assign = newTexts.get(ctx.assignmentExpression());
		String generic = newTexts.get(ctx.genericAssocList());
		String _gen = ctx.Generic().getText();
		bf = String.format("%s(%s,%s)", _gen, assign, generic);
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
			bf = String.format("%s,%s", list, genAssociation);
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
		bf = String.format("%s:%s", st, assignExp);
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitArgumentExpressionList(CParser.ArgumentExpressionListContext ctx) {
		String bf;
		String assignExp = newTexts.get(ctx.assignmentExpression());

		if (ctx.argumentExpressionList() == null) {
			bf = assignExp;
		} else {
			bf = String.format("%s,%s", newTexts.get(ctx.argumentExpressionList()), assignExp);
		}

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
				bf = String.format("(%s)%s", type, castExp);
			} else {
				bf = String.format("%s(%s)%s", ctx.getChild(0).getText(), type, castExp);
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
			bf = String.format("%s%s%s", multi, op, cast);
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
			bf = String.format("%s%s%s", additive, op, multi);
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
			bf = String.format("%s%s%s", shift, op, additive);
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
			bf = String.format("%s%s%s", relation, op, shift);
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
			bf = String.format("%s%s%s", equality, op, relation);
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
			bf = String.format("%s%s%s", and, op, equal);
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
			bf = String.format("%s%s%s", exclusive, op, and);
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
			bf = String.format("%s%s%s", inclusive, op, exclusive);
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
			bf = String.format("%s%s%s", logical, op, incluesive);
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
			bf = String.format("%s%s%s", logicalOr, op, logicalAnd);
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
			bf = String.format("%s?%s:%s", logicalOr, exp, condiExp);
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
			bf = String.format("%s%s%s", unary, assignOP, assignExp);
		} else { // DigitSequence
			bf = ctx.DigitSequence().getText();
		}

		newTexts.put(ctx, bf);
	}

	@Override
	public void exitExpression(CParser.ExpressionContext ctx) {
		String bf;
		String assignExp = newTexts.get(ctx.assignmentExpression());
		if (ctx.children.size() == 1) {
			bf = assignExp;
		} else {
			String exp = newTexts.get(ctx.expression());
			bf = String.format("%s,%s", exp, assignExp);
		}

		newTexts.put(ctx, bf);
	}

	@Override
	public void exitDeclaration(CParser.DeclarationContext ctx) {
		String bf;
		int cSize = ctx.children.size();
		if (cSize == 1) { // staticAssertDeclaration
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
	public void exitInitDeclaratorList(CParser.InitDeclaratorListContext ctx) {
		String bf;
		String initDecl = newTexts.get(ctx.initDeclarator());
		if (ctx.children.size() == 1) {
			bf = initDecl;
		} else {
			String initDeclList = newTexts.get(ctx.initDeclaratorList());
			bf = String.format("%s,%s", initDeclList, initDecl);
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
			bf = String.format("%s=%s", decl, init);
		}

		newTexts.put(ctx, bf);
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
			bf = String.format("%s %s{%s}\n", struct, id, stlist);
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
			bf = String.format("%s %s;", spQuilifierList, structDeclList);
		} else {
			String assertDecl = newTexts.get(ctx.staticAssertDeclaration());
			bf = assertDecl;
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
			bf = String.format("enum %s{%s}\n", id, enumList);
		} else {
			bf = String.format("enum %s{%s,}\n", id, enumList);
		}
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
	public void exitParameterTypeList(CParser.ParameterTypeListContext ctx) {
		String bf;
		String paramList = newTexts.get(ctx.parameterList());
		if (ctx.children.size() == 1) {
			bf = paramList;
		} else {
			bf = String.format("%s,...", paramList);
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
			bf = String.format("%s,%s", newTexts.get(ctx.parameterList()), paramdecl);
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
		bf = String.format("%s(%s,%s);", ctx.StaticAssert().getText(), constantExp, stringliteral);
		newTexts.put(ctx, bf);
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
			bf = String.format("%s%s", blckList, blckitem);
		}
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitExpressionStatement(CParser.ExpressionStatementContext ctx) {
		String exp = (ctx.expression() != null) ? newTexts.get(ctx.expression()) : "";
		String bf = String.format("%s;", exp);
		newTexts.put(ctx, bf);
	}

	@Override
	public void exitIterationStatement(CParser.IterationStatementContext ctx) {
		String bf;
		String stmt = newTexts.get(ctx.statement());
		if (ctx.For() != null) {
			String forCond = newTexts.get(ctx.forCondition());
			String forT = ctx.For().getText();
			bf = String.format("%s(%s)%s", forT, forCond, stmt);
		} else {
			String exp = newTexts.get(ctx.expression());
			String whileT = ctx.While().getText();
			if (ctx.Do() == null) {
				bf = String.format("%s(%s)%s", whileT, exp, stmt);
			} else {
				String doT = ctx.Do().getText();
				bf = String.format("%s %s %s(%s);", doT, stmt, whileT, exp);
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
        bf = String.format("%s;%s;%s", s1, forExp1, forExp2);
        newTexts.put(ctx, bf);
	}

	@Override
	public void exitJumpStatement(CParser.JumpStatementContext ctx) {
		String bf;
		if (ctx.Goto() != null) {
			if (ctx.Identifier() != null) {
				String id = ctx.Identifier().getText();
				id = obfus_ID(id);
				bf = String.format("goto %s;", id);
			} else {
				String unaryExp = newTexts.get(ctx.unaryExpression());
				bf = String.format("goto %s;", unaryExp);
			}
		} else if (ctx.Continue() != null) {
			bf = "continue;";
		} else if (ctx.Break() != null) {
			bf = "break;";
		} else {
			String exp = (ctx.expression() != null) ? newTexts.get(ctx.expression()) : "";
			bf = String.format("return %s;", exp);
			bf = Obfus_RtStmt(bf);
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
				bf += String.format("else%s", stmt2);
			}
		} else {
			bf = String.format("switch(%s) %s", exp, stmt1);
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
		bf = String.format("%s %s%s%s", declSpec, declarator, declList, compStmt);
		newTexts.put(ctx, bf);
	}

}