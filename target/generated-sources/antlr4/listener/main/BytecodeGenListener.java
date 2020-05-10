package listener.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import oldMiniCFiles.MiniCBaseListener;
import oldMiniCFiles.MiniCParser;
import oldMiniCFiles.MiniCParser.ExprContext;
import oldMiniCFiles.MiniCParser.Fun_declContext;
import oldMiniCFiles.MiniCParser.Local_declContext;
import oldMiniCFiles.MiniCParser.ParamsContext;
import oldMiniCFiles.MiniCParser.ProgramContext;
import oldMiniCFiles.MiniCParser.StmtContext;
import oldMiniCFiles.MiniCParser.Type_specContext;
import oldMiniCFiles.MiniCParser.Var_declContext;

import static listener.main.BytecodeGenListenerHelper.*;
import static listener.main.SymbolTable.*;

public class BytecodeGenListener extends MiniCBaseListener implements ParseTreeListener {
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	SymbolTable symbolTable = new SymbolTable();

	int tab = 0;
	int label = 0;
	int func_count = 0;

	// program : decl+

	@Override
	public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
		symbolTable.initFunDecl();
		func_count++;
		
		String fname = getFunName(ctx);
		ParamsContext params;

		if (fname.equals("main")) {
			symbolTable.putLocalVar("args", Type.INTARRAY);
		} else {
			symbolTable.putFunSpecStr(ctx);
			params = (MiniCParser.ParamsContext) ctx.getChild(3);
			symbolTable.putParams(params);
		}
	}

	// var_decl : type_spec IDENT ';' | type_spec IDENT '=' LITERAL ';' | type_spec
	// IDENT '[' LITERAL ']' ';'
	@Override
	public void enterVar_decl(MiniCParser.Var_declContext ctx) {
		String varName = ctx.IDENT().getText();

		if (isArrayDecl(ctx)) {
			symbolTable.putGlobalVar(varName, getArrayType((MiniCParser.Type_specContext) ctx.getChild(0)));
		} else if (isDeclWithInit(ctx)) {
			symbolTable.putGlobalVarWithInitVal(varName, getType(ctx.type_spec()), initVal(ctx));
		} else { // simple decl
			symbolTable.putGlobalVar(varName, getType(ctx.type_spec()));
		}
	}

	@Override
	public void enterLocal_decl(MiniCParser.Local_declContext ctx) {
		if (isArrayDecl(ctx)) { // type_spec IDENT '[' LITERAL ']' ';'
			symbolTable.putLocalVar(getLocalVarName(ctx), getArrayType((MiniCParser.Type_specContext) ctx.type_spec()));
			symbolTable.putArray(getLocalVarName(ctx), ctx.getChild(3).getText());
		} else if (isArrayDeclWithInit(ctx)) { // type_spec IDENT '[' ']' '=' '{' array_init_val '}' ';' | type_spec
												// IDENT '[' LITERAL ']' '=' '{' array_init_val '}' ';'
			symbolTable.putLocalVar(getLocalVarName(ctx), getArrayType((MiniCParser.Type_specContext) ctx.type_spec()));
			String arrayLength = "";
			if (!ctx.getChild(3).getText().equals("]")) {
				arrayLength = ctx.getChild(3).getText();
			} // [6]과 같이 길이를 지정해줬을 때는 길이를 넣어줌
			symbolTable.putArrayInitVal(getLocalVarName(ctx), arrayLength, ctx.array_init_val());
		} else if (isDeclWithInit(ctx)) {
			if (initVal(ctx) instanceof Integer) {
				symbolTable.putLocalVarWithInitVal(getLocalVarName(ctx), getType(ctx.type_spec()), (int) initVal(ctx));
			}
			if (initVal(ctx) instanceof Double) {
				symbolTable.putLocalVarWithInitVal(getLocalVarName(ctx), getType(ctx.type_spec()),
						(double) initVal(ctx));
			}
			if (initVal(ctx) instanceof Character) { // 값이 문자인 경우 int로 cast해서 table에 저장하도록 함
				int ascii = (int) ((char) initVal(ctx));
				symbolTable.putLocalVarWithInitVal(getLocalVarName(ctx), getType(ctx.type_spec()), ascii);
			}
		} else { // simple decl
			symbolTable.putLocalVar(getLocalVarName(ctx), getType(ctx.type_spec()));
		}
	}

	@Override
	public void exitProgram(MiniCParser.ProgramContext ctx) {
		String classProlog = getFunProlog();

		String fun_decl = "", var_decl = "";

		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (isFunDecl(ctx, i))
				fun_decl += newTexts.get(ctx.decl(i));
			else
				var_decl += newTexts.get(ctx.decl(i));
		}

		newTexts.put(ctx, classProlog + var_decl + fun_decl);

		System.out.println(newTexts.get(ctx));

		try (FileWriter fw = new FileWriter("Test.j");) {
			fw.write(newTexts.get(ctx));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// decl : var_decl | fun_decl
	@Override
	public void exitDecl(MiniCParser.DeclContext ctx) {
		String decl = "";
		if (ctx.getChildCount() == 1) {
			if (ctx.var_decl() != null) // var_decl
				decl += newTexts.get(ctx.var_decl());
			else // fun_decl
				decl += newTexts.get(ctx.fun_decl());
		}
		newTexts.put(ctx, decl);
	}

	// stmt : expr_stmt | compound_stmt | if_stmt | while_stmt | for_stmt |
	// return_stmt
	@Override
	public void exitStmt(MiniCParser.StmtContext ctx) {
		String stmt = "";
		if (ctx.getChildCount() > 0) {
			if (ctx.expr_stmt() != null) // expr_stmt
				stmt += newTexts.get(ctx.expr_stmt());
			else if (ctx.compound_stmt() != null) // compound_stmt
				stmt += newTexts.get(ctx.compound_stmt());
			// <(0) Fill here>
			else if (ctx.if_stmt() != null) // if_stmt
				stmt += newTexts.get(ctx.if_stmt());
			else if (ctx.while_stmt() != null) // while_stmt
				stmt += newTexts.get(ctx.while_stmt());
			else if (ctx.for_stmt() != null) { // for_stmt
				stmt += newTexts.get(ctx.for_stmt());
			} else // return_stmt
				stmt += newTexts.get(ctx.return_stmt());
		}
		newTexts.put(ctx, stmt);
	}

	// expr_stmt : expr ';'
	@Override
	public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		String stmt = "";
		if (ctx.getChildCount() == 2) {
			stmt += newTexts.get(ctx.expr()); // expr
		}
		newTexts.put(ctx, stmt);
		
	}

	// while_stmt : WHILE '(' expr ')' stmt
	@Override
	public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		// <(1) Fill here!>
		String indentation = tab(func_count);
		String condExpr = newTexts.get(ctx.expr());
		String doStmt = newTexts.get(ctx.stmt());

		String loop = symbolTable.newLabel();
		String lend = symbolTable.newLabel();

		String stmt = indentation + loop + ": " + "\n" + condExpr + "ifeq " + lend + "\n" + doStmt + indentation
				+ "goto " + loop + "\n" + indentation + lend + ": " + "\n";
		newTexts.put(ctx, stmt);
	}

	@Override
	public void exitFor_stmt(MiniCParser.For_stmtContext ctx) {
		String stmt = "";
		String indentation = tab(func_count);

		String l_decl = null, condExpr = null, incremental = null;
		String loop = symbolTable.newLabel();
		String lend = symbolTable.newLabel();
		String doStmt = newTexts.get(ctx.stmt());

		switch (ctx.getChildCount()) {
		case 7:
			if (ctx.getChild(3) instanceof MiniCParser.ExprContext) {
				// FOR '(' local_decl expr ';' ')' stmt; 증감식 생략된 for문
				l_decl = newTexts.get(ctx.local_decl());
				condExpr = newTexts.get(ctx.expr(0));
				stmt = l_decl + indentation + loop + ": " + "\n" + condExpr + indentation + "ifeq " + lend + "\n"
						+ doStmt + indentation + "goto " + loop + "\n" + indentation + lend + ": " + "\n";
				newTexts.put(ctx, stmt);
				break;
			}
		case 8:
			if (ctx.getChild(2) instanceof MiniCParser.Local_declContext) {
				// FOR '(' local_decl expr ';' expr ')' stmt;
				l_decl = newTexts.get(ctx.local_decl());
				condExpr = newTexts.get(ctx.expr(0));
				incremental = newTexts.get(ctx.expr(1));

				stmt = l_decl + indentation + loop + ": " + "\n" + condExpr + indentation + "ifeq " + lend + "\n"
						+ doStmt + incremental + indentation + "goto " + loop + "\n" + indentation + lend + ": " + "\n";
				newTexts.put(ctx, stmt);
			} else {
				// FOR '(' ';' expr ';' expr ')' stmt; 초기화식 생략된 for문
				condExpr = newTexts.get(ctx.expr(0));
				incremental = newTexts.get(ctx.expr(1));

				stmt = indentation + loop + ": " + "\n" + condExpr + indentation + "ifeq " + lend + "\n" + doStmt
						+ incremental + indentation + "goto " + loop + "\n" + indentation + lend + ": " + "\n";
				newTexts.put(ctx, stmt);
			}
			break;
		}

	}

	@Override
	public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
		// <(2) Fill here!>
		String stmt = funcHeader(ctx, ctx.IDENT().getText()) + newTexts.get(ctx.compound_stmt());
		String fname = getFunName(ctx);
		if (fname.equals("main")) {
			stmt += "   return\n";
		}
		func_count--;
		stmt += ".end method\n";
		newTexts.put(ctx, stmt);
	}

	private String funcHeader(MiniCParser.Fun_declContext ctx, String fname) {
		return ".method public static " + symbolTable.getFunSpecStr(fname) + "\n" + "   .limit stack "
				+ getStackSize(ctx) + "\n" + "   .limit locals " + getLocalVarSize(ctx) + "\n";
	}

	@Override
	public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		String varName = ctx.IDENT().getText();
		String varDecl = "";

		if (isDeclWithInit(ctx)) {
			varDecl += "putfield " + varName + "\n";
			// v. initialization => Later! skip now..:
		}
		newTexts.put(ctx, varDecl);
	}

	@Override
	public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
		String indentation = tab(func_count);
		String varDecl = "";
		if (isArrayDecl(ctx)) { // type_spec IDENT '[' LITERAL ']' ';'
			String vId = symbolTable.getVarId(ctx);
			varDecl += "ldc " + ctx.LITERAL().getText() + "\n" + "newarray " + ctx.type_spec().getText() + "\nastore "
					+ vId + "\n";
		} else if (isArrayDeclWithInit(ctx)) { // type_spec IDENT '[' ']' '=' '{' array_init_val '}' ';'
												// | type_spec IDENT '[' LITERAL ']' '=' '{' array_init_val '}' ';'
			String vId = symbolTable.getVarId(ctx);
			String arrayLength = "";
			if (ctx.getChild(3).getText().equals("]")) {
				arrayLength = (ctx.getChild(6).getChildCount()) / 2 + 1 + "";
			} else {
				arrayLength = ctx.LITERAL().getText();
			} // 어레이 길이를 가져오는것
			varDecl += "ldc " + arrayLength + "\n" + "newarray " + ctx.type_spec().getText() + "\n" + "astore " + vId
					+ "\n";
			if (ctx.type_spec().getText().equals("int")) {
				int[] arrayVal = (int[]) symbolTable.getArrayInitVal(ctx.getChild(1).getText());
				for (int i = 0; i < arrayVal.length; i++) {
					varDecl += "aload " + vId + "\n" + "ldc " + i + "\n" + "ldc " + arrayVal[i] + "\n" + "iastore\n";
				}
			} else if (ctx.type_spec().getText().equals("double")) {
				double[] arrayVal = (double[]) symbolTable.getArrayInitVal(ctx.getChild(1).getText());
				for (int i = 0; i < arrayVal.length; i++) {
					varDecl += "aload " + vId + "\n" + "ldc " + i + "\n" + "ldc " + arrayVal[i] + "\n" + "dastore\n";
				}
			}
		} else if (isDeclWithInit(ctx)) {
			String num = "";
			String ldc = "";
			String vId = symbolTable.getVarId(ctx);
			String vType = getTypeLowerCase(ctx.type_spec());
			String store = "store_";
			if (Integer.parseInt(vId) > 3) {
				store = "store ";
			}
			if (vType.equals("i")) { // int 일 경우
				num = ctx.LITERAL().getText();
				ldc = "ldc ";
			}
			if (vType.equals("c")) { // char 일 경우
				vType = "i";
				ldc = "ldc ";
				num = ctx.CHARACTER().getText(); // 'x'형태
				int a = (int) num.charAt(1); // x부분의 문자를 int로 cast
				num = Integer.toString(a);
			}
			if (vType.equals("d")) { // double 일 경우
				num = ctx.DOUBLE_Lit().getText();
				ldc = "ldc2_w ";
			}
			varDecl += indentation + ldc + num + "\n" + indentation + vType + store + vId + "\n";
		}

		newTexts.put(ctx, varDecl);
	}

	// compound_stmt : '{' local_decl* stmt* '}'
	@Override
	public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		// <(3) Fill here>
		int l_decl_size = ctx.local_decl().size();
		int stmt_size = ctx.stmt().size();
		String l_decl = "", stmt = "";
		for (int i = 0; i < l_decl_size; i++) {
			l_decl += newTexts.get(ctx.local_decl(i));
		}
		for (int i = 0; i < stmt_size; i++) {
			stmt += newTexts.get(ctx.stmt(i));
		}
		newTexts.put(ctx, l_decl + stmt);
	}

	// if_stmt : IF '(' expr ')' stmt | IF '(' expr ')' stmt ELSE stmt;
	@Override
	public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
		String indentation = tab(func_count);
		String stmt = "";
		String condExpr = newTexts.get(ctx.expr());
		String thenStmt = newTexts.get(ctx.stmt(0));

		String lend = symbolTable.newLabel();
		String lelse = symbolTable.newLabel();

		if (noElse(ctx)) {
			stmt += condExpr + "\n" + indentation + "ifeq " + lend + "\n" + thenStmt + "\n" + indentation + lend + ":"
					+ "\n";
		} else {
			String elseStmt = newTexts.get(ctx.stmt(1));
			stmt += condExpr + "\n" + indentation + "ifeq " + lelse + "\n" + thenStmt + "\n" + indentation + "goto "
					+ lend + "\n" + indentation + lelse + ": " + "\n" + elseStmt + "\n" + indentation + lend + ":"
					+ "\n";
		}

		newTexts.put(ctx, stmt);
	}

	// return_stmt : RETURN ';' | RETURN expr ';'
	@Override
	public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		// <(4) Fill here>
		String indentation = tab(func_count);
		String ret = "return\n";
		if (ctx.getChildCount() == 2) { // RETURN ';'
			newTexts.put(ctx, indentation + ret);
		} else { // RETURN expr ';'
			String expr = newTexts.get(ctx.expr());
			newTexts.put(ctx, expr + indentation + "i" + ret);
		}
	}

	@Override
	public void exitExpr(MiniCParser.ExprContext ctx) {
		String expr = "";
		String indentation = tab(func_count);

		if (ctx.getChildCount() <= 0) {
			newTexts.put(ctx, "");
			return;
		}

		if (ctx.getChildCount() == 1) { // IDENT | LITERAL | DOUBLE_Lit | CHARACTER
			if (ctx.IDENT() != null) {
				String idName = ctx.IDENT().getText();
				String load = "load_";
				if (Integer.parseInt(symbolTable.getVarId(idName)) > 3) {
					load = "load ";
				}
				if (symbolTable.getVarType(idName) == Type.INT) {
					expr += indentation + "i" + load + symbolTable.getVarId(idName) + " \n";
				} else if (symbolTable.getVarType(idName) == Type.DOUBLE) {
					expr += indentation + "d" + load + symbolTable.getVarId(idName) + " \n";
				} else if (symbolTable.getVarType(idName) == Type.CHAR) {
					expr += indentation + "i" + load + symbolTable.getVarId(idName) + " \n" + indentation + "i2c\n";
				}
				// else // Type int array => Later! skip now..
				// expr += " lda " + symbolTable.get(ctx.IDENT().getText()).value + " \n";
			} else if (ctx.LITERAL() != null) {
				String literalStr = ctx.LITERAL().getText();
				expr += indentation + "ldc " + literalStr + " \n";
			} else if (ctx.DOUBLE_Lit() != null) {
				String d_litStr = ctx.DOUBLE_Lit().getText();
				expr += indentation + "ldc2_w " + d_litStr + "\n";
			} else if (ctx.CHARACTER() != null) { // char일 경우
				char charC = ctx.CHARACTER().getText().charAt(1);
				String charStr = Integer.toString(charC);
				expr += indentation + "ldc " + charStr + "\ni2c\n";
			}
		} else if (ctx.getChildCount() == 2) { // UnaryOperation
			// add store instruction
			expr = handleUnaryExpr(ctx, expr) + indentation + "istore_"
					+ symbolTable.getVarId(ctx.expr(0).IDENT().getText()) + "\n";
		} else if (ctx.getChildCount() == 3) {
			if (ctx.getChild(0).getText().equals("(")) { // '(' expr ')'
				expr = newTexts.get(ctx.expr(0));

			} else if (ctx.getChild(1).getText().equals("=")) { // IDENT '=' expr
				expr = newTexts.get(ctx.expr(0)) + indentation + "istore_" + symbolTable.getVarId(ctx.IDENT().getText())
						+ " \n";
			} else { // binary operation
				expr = handleBinExpr(ctx, expr);
			}
		}
		// IDENT '(' args ')' | IDENT '[' expr ']'
		else if (ctx.getChildCount() == 4) {
			if (ctx.args() != null) { // function calls
				expr = handleFunCall(ctx, expr);
			} else if (ctx.getChild(1).getText().equals("[")) { // IDENT '[' expr ']'
				String arrayName = getArrayName(ctx);
				expr += indentation + "aload " + symbolTable.getVarId(arrayName) + "\n";
				expr += newTexts.get(ctx.getChild(2));
				if (symbolTable.getArrayInitType(ctx.getChild(0).getText()).equals("int")) {
					expr += indentation + "iaload\n";
				} else if (symbolTable.getArrayInitType(ctx.getChild(0).getText()).equals("double")) {
					expr += indentation + "daload\n";
				}
			} else {
			}
		} else if (ctx.getChild(1).getText().equals("[")) {// IDENT '[' expr ']' '=' expr
			String arrayName = getArrayName(ctx);
			expr += indentation + "aload " + symbolTable.getVarId(arrayName) + "\n";
			expr += newTexts.get(ctx.getChild(2));
			expr += newTexts.get(ctx.getChild(5));
			if (symbolTable.getArrayInitType(ctx.getChild(0).getText()).equals("int")) {
				expr += indentation + "iastore\n";
			} else if (symbolTable.getArrayInitType(ctx.getChild(0).getText()).equals("double")) {
				expr += indentation + "dastore\n";
			}

			expr += indentation + "aload " + symbolTable.getVarId(arrayName) + "\n";
			expr += newTexts.get(ctx.getChild(2));
			if (symbolTable.getArrayInitType(ctx.getChild(0).getText()).equals("int")) {
				expr += indentation + "iaload\n";
			} else if (symbolTable.getArrayInitType(ctx.getChild(0).getText()).equals("double")) {
				expr += indentation + "daload\n";
			}

			// symbolTable.editArrayValIn(arrayName, ctx.getChild(2),
			// ctx.getChild(5).getText());
			// 진짜 값도 바꿔주기 <--필요없을것같음
		} else {
		}
		newTexts.put(ctx, expr);
	}

	private String handleUnaryExpr(MiniCParser.ExprContext ctx, String expr) {
		String indentation = tab(func_count);
		String l1 = symbolTable.newLabel();
		String l2 = symbolTable.newLabel();
		String lend = symbolTable.newLabel();

		expr += newTexts.get(ctx.expr(0));
		switch (ctx.getChild(0).getText()) {
		case "-":
			expr += "           ineg \n";
			break;
		case "--":
			expr += indentation + "ldc 1" + "\n" + indentation + "isub" + "\n";
			break;
		case "++":
			expr += indentation + "ldc 1" + "\n" + indentation + "iadd" + "\n";
			break;
		case "!":
			expr += indentation + "ifeq " + l2 + "\n" + indentation + l1 + ": \n" + indentation + "ldc 0" + "\n"
					+ indentation + "goto " + lend + "\n" + indentation + l2 + ": \n" + indentation + "ldc 1" + "\n"
					+ indentation + lend + ": " + "\n";
			break;
		}
		return expr;
	}

	private String handleBinExpr(MiniCParser.ExprContext ctx, String expr) {
		String indentation = tab(func_count);
		String l2 = symbolTable.newLabel();
		String lend = symbolTable.newLabel();

		expr += newTexts.get(ctx.expr(0));
		expr += newTexts.get(ctx.expr(1));

		switch (ctx.getChild(1).getText()) {
		case "*":
			expr += indentation + "imul \n";
			break;
		case "/":
			expr += indentation + "idiv \n";
			break;
		case "%":
			expr += indentation + "irem \n";
			break;
		case "+": // expr(0) expr(1) iadd
			expr += indentation + "iadd \n";
			break;
		case "-":
			expr += indentation + "isub \n";
			break;

		case "==":
			expr += indentation + "isub " + "\n" + indentation + "ifeq " + l2 + "\n" + indentation + "ldc 0" + "\n"
					+ indentation + "goto " + lend + "\n" + indentation + l2 + ": " + "\n" + indentation + "ldc 1"
					+ "\n" + indentation + lend + ": ";
			break;
		case "!=":
			expr += indentation + "isub " + "\n" + indentation + "ifne " + l2 + "\n" + indentation + "ldc 0" + "\n"
					+ indentation + "goto " + lend + "\n" + indentation + l2 + ": " + "\n" + indentation + "ldc 1"
					+ "\n" + indentation + lend + ": " + "\n";
			break;
		case "<=":
			// <(5) Fill here>
			expr += indentation + "if_icmple " + l2 + "\n" + indentation + "ldc 0" + "\n" + indentation + "goto " + lend
					+ "\n" + indentation + l2 + ": " + "\n" + indentation + "ldc 1" + "\n" + indentation + lend + ": "
					+ "\n";
			break;
		case "<":
			// <(6) Fill here>
			expr += indentation + "if_icmplt " + l2 + "\n" + indentation + "ldc 0" + "\n" + indentation + "goto " + lend
					+ "\n" + indentation + l2 + ": " + "\n" + indentation + "ldc 1" + "\n" + indentation + lend + ": "
					+ "\n";
			break;
		case ">=":
			// <(7) Fill here>
			expr += indentation + "if_icmpge " + l2 + "\n" + indentation + "ldc 0" + "\n" + indentation + "goto " + lend
					+ "\n" + indentation + l2 + ": " + "\n" + indentation + "ldc 1" + "\n" + indentation + lend + ": "
					+ "\n";
			break;
		case ">":
			// <(8) Fill here>
			expr += indentation + "if_icmpgt " + l2 + "\n" + indentation + "ldc 0" + "\n" + indentation + "goto " + lend
					+ "\n" + indentation + l2 + ": " + "\n" + indentation + "ldc 1" + "\n" + indentation + lend + ": "
					+ "\n";
			break;
		case "and":
			expr += indentation + "ifne " + lend + "\n" + indentation + "pop" + "\n" + indentation + "ldc 0" + "\n"
					+ indentation + lend + ": " + "\n";
			break;
		case "or":
			// <(9) Fill here>
			expr += indentation + "ior" + "\n" + indentation + "ifeq " + l2 + "\n" + indentation + "ldc 1" + "\n"
					+ indentation + "goto " + lend + "\n" + indentation + l2 + ": " + "\n" + indentation + "ldc 0"
					+ "\n" + indentation + lend + ": " + "\n";
			break;
		}
		return expr;
	}

	private String handleFunCall(MiniCParser.ExprContext ctx, String expr) {
		String indentation = tab(func_count);
		String fname = getFunName(ctx);

		if (fname.equals("_printI") || fname.equals("_printD") || fname.equals("_printC")) { // System.out.println
			expr = indentation + "getstatic java/lang/System/out Ljava/io/PrintStream; \n" + newTexts.get(ctx.args())
					+ indentation + "invokevirtual " + symbolTable.getFunSpecStr(fname) + "\n";
		} else {
			expr = newTexts.get(ctx.args()) + indentation + "invokestatic " + getCurrentClassName() + "/"
					+ symbolTable.getFunSpecStr(fname) + "\n";
		}
		return expr;
	}

	// args : expr (',' expr)* | ;
	@Override
	public void exitArgs(MiniCParser.ArgsContext ctx) {

		String argsStr = "";

		for (int i = 0; i < ctx.expr().size(); i++) {
			argsStr += newTexts.get(ctx.expr(i));
		}
		newTexts.put(ctx, argsStr);
	}
}
