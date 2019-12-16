package generated;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class MiniCPrintListener extends MiniCBaseListener {
	// 들여쓰기 횟수를 세기 위한 변수들
	int fun_decl_count = 0;
	int while_count = 0;
	int if_count = 0;
	int for_count = 0;

	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();

	@Override
	public void exitProgram(MiniCParser.ProgramContext ctx) {
		String program = null;
		int size = ctx.decl().size();

		if (size > 0) {
			program = "";
			for (int i = 0; i < size; i++) {
				program = program + newTexts.get(ctx.decl(i));
			}
		}
		newTexts.put(ctx, program);
		System.out.println(program);
	}

	@Override
	public void exitDecl(MiniCParser.DeclContext ctx) {
		String decl = null;

		if (ctx.getChild(0) instanceof MiniCParser.Var_declContext) { // case : var_decl
			decl = newTexts.get(ctx.var_decl());
		} else { // case : fun_decl
			decl = newTexts.get(ctx.fun_decl());
		}
		newTexts.put(ctx, decl);
	}

	@Override
	public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		String typeStr = null, identStr = null, valStr = null, nl = "\n";
		int count = ctx.getChildCount();

		typeStr = newTexts.get(ctx.type_spec()); // type_spec
		identStr = ctx.getChild(1).getText(); // IDENT
		switch (count) {
		case 3: // case : type_spec IDENT ';'
			newTexts.put(ctx, typeStr + " " + identStr + ";" + nl);
			break;
		case 5:
			if (ctx.CHARACTER() != null) {// case : type_spec IDENT '=' CHARACTER ';'
				valStr = Character.toString(ctx.getChild(3).getText().charAt(1)); // 'x'형태에서 x만 가져옴
				newTexts.put(ctx, typeStr + " " + identStr + " = \'" + valStr + "\';" + nl);
				break;
			} else {// case : type_spec IDENT '=' (LITERAL|Double_Lit) ';'
				valStr = ctx.getChild(3).getText(); // LITERAL|Double_Lit
				newTexts.put(ctx, typeStr + " " + identStr + " = " + valStr + ";" + nl);
				break;
			}
		case 6: // case : type_spec IDENT '[' LITERAL ']' ';'
			valStr = ctx.getChild(3).getText(); // LITERAL
			newTexts.put(ctx, typeStr + " " + identStr + "[" + valStr + "];" + nl);
			break;
		}
	}

	@Override
	public void exitType_spec(MiniCParser.Type_specContext ctx) {
		String s1 = null;

		s1 = ctx.getChild(0).getText();
		newTexts.put(ctx, s1);
	}

	@Override
	public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
		fun_decl_count++;
	}

	@Override
	public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
		String s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null;

		s1 = newTexts.get(ctx.type_spec()); // type_spec
		s2 = ctx.getChild(1).getText(); // IDENT
		s3 = ctx.getChild(2).getText(); // '('
		s4 = newTexts.get(ctx.params()); // params
		s5 = ctx.getChild(4).getText(); // ')'
		s6 = newTexts.get(ctx.compound_stmt()); // compound_stmt
		newTexts.put(ctx, s1 + " " + s2 + s3 + s4 + s5 + "\n" + s6);
		fun_decl_count--;
	}

	@Override
	public void exitParams(MiniCParser.ParamsContext ctx) {
		String s1 = null;
		int size = ctx.param().size(); // param의 수를 센다

		if (size == 0) { // param이 0개인 경우
			if (ctx.getChild(0) == null) { // case : empty
				s1 = "";
			} else { // case : VOID
				s1 = ctx.getChild(0).getText();
			}
			newTexts.put(ctx, s1);
		} else if (size == 1) { // case : param (파라미터가 1개)
			s1 = newTexts.get(ctx.param(0));
			newTexts.put(ctx, s1);
		} else { // case : param (',' param)* (파라미터가 1개 이상)
			s1 = newTexts.get(ctx.param(0));
			for (int i = 1; i < size; i++) {
				s1 = s1 + ctx.getChild(2 * i - 1).getText() + " " + newTexts.get(ctx.param(i));
				// ctx.getChild(2 * i - 1)은 param(i)의 앞에 나오는 ','에 해당한다
				newTexts.put(ctx, s1);
			}
		}
	}

	@Override
	public void exitParam(MiniCParser.ParamContext ctx) {
		String s1 = null, s2 = null, s3 = null, s4 = null;

		s1 = newTexts.get(ctx.type_spec()); // type_spec
		s2 = ctx.getChild(1).getText(); // IDENT
		if (ctx.getChildCount() == 4) { // case : type_spec IDENT '[' ']'
			s3 = ctx.getChild(2).getText(); // '['
			s4 = ctx.getChild(3).getText(); // ']'
			newTexts.put(ctx, s1 + " " + s2 + s3 + s4);
		} else { // case : type_spec IDENT
			newTexts.put(ctx, s1 + " " + s2);
		}
	}

	@Override
	public void exitStmt(MiniCParser.StmtContext ctx) {
		String indentation = null, s1 = null;
		indentation = dots(if_count + while_count + fun_decl_count + for_count);
		/*
		 * stmt의 들여쓰기에 해당하는 문자열 if, while과 같은 절 혹은 함수의 내부에 있는 문장에 대해 들여쓰기 처리를 하기 때문에 현재
		 * ctx가 if, while, 함수 내부로 enter한 횟수를 세서 처리한다
		 */

		if (ctx.getChild(0) instanceof MiniCParser.Expr_stmtContext) { // case : expr_stmt
			s1 = newTexts.get(ctx.expr_stmt());
		} else if (ctx.getChild(0) instanceof MiniCParser.Compound_stmtContext) { // case : compound_stmt
			s1 = newTexts.get(ctx.compound_stmt());
		} else if (ctx.getChild(0) instanceof MiniCParser.If_stmtContext) { // case : if_stmt
			s1 = newTexts.get(ctx.if_stmt());
		} else if (ctx.getChild(0) instanceof MiniCParser.While_stmtContext) { // case : while_stmt
			s1 = newTexts.get(ctx.while_stmt());
		} else if (ctx.getChild(0) instanceof MiniCParser.For_stmtContext) {
			s1 = newTexts.get(ctx.for_stmt());
		} else { // case : return_stmt
			s1 = newTexts.get(ctx.return_stmt());
		}

		if (ctx.getChild(0) instanceof MiniCParser.Compound_stmtContext) {
			/*
			 * compound_stmt일 경우 indentation을 붙일 경우 중괄호의 시작 부분에 부적절한 들여쓰기 처리를 하게 되므로
			 * indentation을 붙이지 않고 다른 메소드에서 처리한다
			 */
			newTexts.put(ctx, s1);
		} else {
			newTexts.put(ctx, indentation + s1);
		}
	}

	@Override
	public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		String s1 = null, s2 = null;

		s1 = newTexts.get(ctx.expr()); // expr
		s2 = ctx.getChild(1).getText(); // ';'
		newTexts.put(ctx, s1 + s2 + "\n");
	}

	@Override
	public void enterWhile_stmt(MiniCParser.While_stmtContext ctx) {
		while_count++;
	}

	@Override
	public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		String s1 = null, s2 = null, s3 = null, s4 = null, s5 = null;

		s1 = ctx.getChild(0).getText(); // WHILE
		s2 = ctx.getChild(1).getText(); // '('
		s3 = newTexts.get(ctx.expr()); // expr
		s4 = ctx.getChild(3).getText(); // ')'
		s5 = newTexts.get(ctx.stmt()); // stmt
		newTexts.put(ctx, s1 + " " + s2 + s3 + s4 + "\n" + s5);
		while_count--;
	}

	@Override
	public void enterFor_stmt(MiniCParser.For_stmtContext ctx) {
		for_count++;
	}

	@Override
	public void exitFor_stmt(MiniCParser.For_stmtContext ctx) {
		String s1 = null, s2 = null, s3 = null, s4 = null, s5 = null;

		s1 = ctx.getChild(0).getText();
		switch (ctx.getChildCount()) {
		case 7:
			if (ctx.getChild(3) instanceof MiniCParser.ExprContext) {
				// FOR '(' local_decl expr ';' ')' stmt; 증감식 생략된 for문
				s2 = newTexts.get(ctx.local_decl());
				s3 = newTexts.get(ctx.expr(0));
				s4 = newTexts.get(ctx.stmt());
				newTexts.put(ctx, s1 + " (" + s2 + " " + s3 + "; ) " + "\n" + s4);
				break;
			}
		case 8:
			if (ctx.getChild(2) instanceof MiniCParser.Local_declContext) {
				// FOR '(' local_decl expr ';' expr ')' stmt;
				s2 = newTexts.get(ctx.local_decl());
				s3 = newTexts.get(ctx.expr(0));
				s4 = newTexts.get(ctx.expr(1));
				s5 = newTexts.get(ctx.stmt());
				newTexts.put(ctx, s1 + " (" + s2 + " " + s3 + "; " + s4 + ") " + "\n" + s5);
			} else {
				// FOR '(' ';' expr ';' expr ')' stmt; //초기화식 생략된 for문
				s2 = newTexts.get(ctx.expr(0));
				s3 = newTexts.get(ctx.expr(1));
				s4 = newTexts.get(ctx.stmt());
				newTexts.put(ctx, s1 + " (;" + s2 + "; " + s3 + ") " + "\n" + s4);
			}
			break;
		}
		for_count--;
	}

	@Override
	public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		String s1 = null, l_decl = null, stmt = null, s2 = null;
		int l_decl_size = ctx.local_decl().size(); // 복합문 내부 local decl 갯수
		int stmt_size = ctx.stmt().size(); // 복합문 내부 stmt 갯수
		int i = 0;

		s1 = ctx.getChild(0).getText(); // '{'
		// local decl과 stmt를 갯수만큼 get
		if (l_decl_size == 0) {
			l_decl = "";
		} else {
			l_decl = "";
			for (i = 0; i < l_decl_size; i++) {
				l_decl = l_decl + newTexts.get(ctx.local_decl(i));
			}
		}
		if (stmt_size == 0) {
			stmt = "";
		} else {
			stmt = "";
			for (i = 0; i < stmt_size; i++) {
				stmt = stmt + newTexts.get(ctx.stmt(i));
			}
		}
		s2 = ctx.getChild(ctx.getChildCount() - 1).getText(); // '}'

		newTexts.put(ctx, dots(if_count + while_count + fun_decl_count + for_count - 1) // 여는 중괄호 앞의 indentation
				+ s1 + "\n" + l_decl + stmt + dots(if_count + while_count + fun_decl_count + for_count - 1) // 닫는 중괄호 앞의
																											// indentation
				+ s2 + "\n");
		/*
		 * dots(if_count + while_count + fun_decl_count - 1)은 복합문을 여닫는 중괄호 앞에 출력되는
		 * indentation에 해당한다 중괄호의 경우 자신이 속해있는 특수절이나 함수와 들여쓰기 정도가 같으므로 절, 함수 enter 합에 대해
		 * 1을 빼서 들여쓰기 단계를 맞춘다
		 */
	}

	@Override
	public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
		String indentation = null, typeStr = null, identStr = null, valStr = null, nl = "\n";
		int count = ctx.getChildCount();

		indentation = dots(if_count + while_count + fun_decl_count + for_count); // Local_decl의 indentation
		if (ctx.parent instanceof MiniCParser.For_stmtContext) {
			indentation = dots(0);
			nl = "";
		}
		typeStr = newTexts.get(ctx.type_spec()); // type_spec
		identStr = ctx.getChild(1).getText(); // IDENT
		switch (count) {
		case 3: // case : type_spec IDENT ';'
			newTexts.put(ctx, indentation + typeStr + " " + identStr + ";" + nl);
			break;
		case 5:
			if (ctx.CHARACTER() != null) {// case : type_spec IDENT '=' CHARACTER ';'
				valStr = Character.toString(ctx.getChild(3).getText().charAt(1)); // 'x'형태에서 x만 가져옴
				newTexts.put(ctx, indentation + typeStr + " " + identStr + " = \'" + valStr + "\';" + nl);
				break;
			} else {// case : type_spec IDENT '=' (LITERAL|Double_Lit) ';'
				valStr = ctx.getChild(3).getText(); // LITERAL|Double_Lit
				newTexts.put(ctx, indentation + typeStr + " " + identStr + " = " + valStr + ";" + nl);
				break;
			}
		case 6: // case : type_spec IDENT '[' LITERAL ']' ';'
			valStr = ctx.getChild(3).getText(); // LITERAL
			newTexts.put(ctx, indentation + typeStr + " " + identStr + "[" + valStr + "];" + nl);
			break;
		}
	}

	@Override
	public void enterIf_stmt(MiniCParser.If_stmtContext ctx) {
		if_count++;
	}

	@Override
	public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
		String s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null;

		s1 = ctx.getChild(0).getText(); // IF
		s2 = ctx.getChild(1).getText(); // '('
		s3 = newTexts.get(ctx.expr()); // expr
		s4 = ctx.getChild(3).getText(); // ')'
		s5 = newTexts.get(ctx.stmt(0)); // stmt

		if (ctx.getChildCount() == 5) { // case : IF '(' expr ')' stmt
			newTexts.put(ctx, s1 + " " + s2 + s3 + s4 + "\n" + s5);
		}
		if (ctx.getChildCount() == 7) { // case : IF '(' expr ')' stmt ELSE stmt
			s6 = ctx.getChild(5).getText(); // ELSE
			s7 = newTexts.get(ctx.stmt(1)); // stmt
			newTexts.put(ctx,
					s1 + " " + s2 + s3 + s4 + "\n" + s5 + dots(if_count + fun_decl_count + while_count + for_count - 1) // else
					// 키워드의
					// indentation
							+ s6 + "\n" + s7);
		}
		if_count--;
	}

	@Override
	public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		String s1 = null, s2 = null, s3 = null;

		s1 = ctx.getChild(0).getText(); // RETURN

		if (ctx.getChildCount() == 2) { // case : RETURN ';'
			s2 = ctx.getChild(1).getText(); // ';'
			newTexts.put(ctx, s1 + s2 + "\n");
		} else { // case : RETURN expr ';'
			s2 = newTexts.get(ctx.expr()); // expr
			s3 = ctx.getChild(2).getText(); // ';'
			newTexts.put(ctx, s1 + " " + s2 + s3 + "\n");
		}
	}

	@Override
	public void exitExpr(MiniCParser.ExprContext ctx) {
		String s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, op = null;

		if (ctx.getChildCount() == 1) { // LITERAL or IDENT
			s1 = ctx.getChild(0).getText();
			newTexts.put(ctx, s1);
		} else if (ctx.getChildCount() == 2) { // 전위연산
			s1 = ctx.getChild(0).getText(); // operator
			s2 = newTexts.get(ctx.expr(0)); // expr
			newTexts.put(ctx, s1 + s2); // 띄우지 않고 출력
		} else if (ctx.getChildCount() == 3) {
			if (ctx.getChild(1) instanceof MiniCParser.ExprContext) { // case : '(' expr ')'
				s1 = ctx.getChild(0).getText(); // '('
				s2 = newTexts.get(ctx.expr(0)); // expr
				s3 = ctx.getChild(2).getText(); // ')'
				newTexts.put(ctx, s1 + s2 + s3); // 괄호와 expression 띄우지 않고 출력
			} else if (ctx.getChild(0) == ctx.IDENT()) { // case : IDENT '=' expr
				s1 = ctx.getChild(0).getText(); // INDENT
				s2 = ctx.getChild(1).getText(); // '='
				s3 = newTexts.get(ctx.expr(0)); // expr
				newTexts.put(ctx, s1 + " " + s2 + " " + s3);
			} else { // ex : expr '+' expr
				s1 = newTexts.get(ctx.expr(0)); // expr
				s2 = newTexts.get(ctx.expr(1)); // expr
				op = ctx.getChild(1).getText(); // operator
				newTexts.put(ctx, s1 + " " + op + " " + s2);
			}
		} else if (ctx.getChildCount() == 4) {
			if (ctx.getChild(2) == ctx.expr()) { // case : IDENT '[' expr ']'
				s1 = ctx.getChild(0).getText(); // IDENT
				s2 = ctx.getChild(1).getText(); // '['
				s3 = newTexts.get(ctx.expr(0)); // expr
				s4 = ctx.getChild(3).getText(); // ']'
			} else { // case : IDENT '(' args ')'
				s1 = ctx.getChild(0).getText(); // INDENT
				s2 = ctx.getChild(1).getText(); // '('
				s3 = newTexts.get(ctx.args()); // args
				s4 = ctx.getChild(3).getText(); // ')'
			}
			newTexts.put(ctx, s1 + s2 + s3 + s4);
		} else { // case : IDENT '[' expr ']' '=' expr
			s1 = ctx.getChild(0).getText(); // IDENT
			s2 = ctx.getChild(1).getText(); // '['
			s3 = newTexts.get(ctx.expr(0)); // expr
			s4 = ctx.getChild(3).getText(); // ']'
			s5 = ctx.getChild(4).getText(); // '='
			s6 = newTexts.get(ctx.expr(1)); // expr
			newTexts.put(ctx, s1 + s2 + s3 + s4 + " " + s5 + " " + s6);
		}
	}

	@Override
	public void exitArgs(MiniCParser.ArgsContext ctx) {
		String s1 = null;
		int size = ctx.expr().size();

		if (size == 0) { // case : nothing
			if (ctx.getChild(0) == null) {
				s1 = "";
			}
		} else if (size == 1) { // expr이 하나인 경우
			s1 = newTexts.get(ctx.expr(0));
		} else { // expr이 하나 이상인 경우
			s1 = newTexts.get(ctx.expr(0));
			for (int i = 1; i < size; i++) {
				s1 = s1 + ctx.getChild(2 * i - 1).getText() + " " + newTexts.get(ctx.expr(i));
			}
		}
		newTexts.put(ctx, s1);
	}

	// n 개의 "...."이 이어진 문자열을 반환
	public String dots(int n) {
		int i = 0;
		String dots = "";
		while (i < n) {
			dots = dots + "....";
			i++;
		}
		return dots;
	}
}
