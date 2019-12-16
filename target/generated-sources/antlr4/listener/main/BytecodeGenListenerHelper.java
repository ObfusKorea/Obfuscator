package listener.main;

import java.util.Hashtable;

import generated.MiniCParser;
import generated.MiniCParser.ExprContext;
import generated.MiniCParser.Fun_declContext;
import generated.MiniCParser.If_stmtContext;
import generated.MiniCParser.Local_declContext;
import generated.MiniCParser.ParamContext;
import generated.MiniCParser.ParamsContext;
import generated.MiniCParser.Type_specContext;
import generated.MiniCParser.Var_declContext;
import listener.main.SymbolTable;
import listener.main.SymbolTable.Type;
import listener.main.SymbolTable.VarInfo;

public class BytecodeGenListenerHelper {

	// <boolean functions>

	static boolean isFunDecl(MiniCParser.ProgramContext ctx, int i) {
		return ctx.getChild(i).getChild(0) instanceof MiniCParser.Fun_declContext;
	}

	// type_spec IDENT '[' ']'
	static boolean isArrayParamDecl(ParamContext param) {
		return param.getChildCount() == 4;
	}

	// global vars
	static int initVal(Var_declContext ctx) {
		return Integer.parseInt(ctx.LITERAL().getText());
	}

	// var_decl : type_spec IDENT '=' LITERAL ';
	static boolean isDeclWithInit(Var_declContext ctx) {
		return ctx.getChildCount() == 5;
	}

	// var_decl : type_spec IDENT '[' LITERAL ']' ';'
	static boolean isArrayDecl(Var_declContext ctx) {
		return ctx.getChildCount() == 6;
	}

	// <local vars>
	// local_decl : type_spec IDENT '[' LITERAL ']' ';'
	static Object initVal(Local_declContext ctx) {
		if (getType(ctx.type_spec()) == Type.INT) {
			return Integer.parseInt(ctx.LITERAL().getText());
		}
		if (getType(ctx.type_spec()) == Type.DOUBLE) {
			return Double.parseDouble(ctx.DOUBLE_Lit().getText());
		}
		return null;
	}

	// type_spec IDENT '[' LITERAL ']' ';'
	static boolean isArrayDecl(Local_declContext ctx) {
		// 6이면서 3번째가 [인것
		return (ctx.getChildCount() == 6) && (ctx.getChild(2).getText().equals("["));
	}

	// type_spec IDENT '[' ']' '=' '{' array_init_val '}' ';' | type_spec IDENT '[' LITERAL ']' '=' '{' array_init_val '}' ';' 
	static boolean isArrayDeclWithInit(Local_declContext ctx) {
		// 6이상이면서 3번째가 [인것
		return (ctx.getChildCount() > 6) && (ctx.getChild(2).getText().equals("["));
	}

	// type_spec IDENT '=' (LITERAL|DOUBLE_Lit) ';'
	static boolean isDeclWithInit(Local_declContext ctx) {
		return ctx.getChildCount() == 5;
	}

	static boolean isVoidF(Fun_declContext ctx) {
		// <Fill in>
		String v = "void";
		return ctx.type_spec().getText().equals(v);
	}

	static boolean isIntReturn(MiniCParser.Return_stmtContext ctx) {
		return ctx.getChildCount() == 3;
	}

	static boolean isVoidReturn(MiniCParser.Return_stmtContext ctx) {
		return ctx.getChildCount() == 2;
	}

	// <information extraction>
	static String getStackSize(Fun_declContext ctx) {
		return "32";
	}

	static String getLocalVarSize(Fun_declContext ctx) {
		return "32";
	}

	static String getTypeText(Type_specContext typespec) {
		// <Fill in>
		if (getType(typespec) == Type.INT) {
			return "I";
		} else if (getType(typespec) == Type.DOUBLE) {
			return "D";
		} else {
			return "V";
		}
	}

	static String getTypeLowerCase(Type_specContext typespec) {
		if (getType(typespec) == Type.INT) {
			return "i";
		} else if (getType(typespec) == Type.DOUBLE) {
			return "d";
		} else {
			return "타입로워케이스";
		}
	}

	// type_spec과 일치하는 Type을 반환
	static Type getType(Type_specContext typespec) {
		if (typespec.getText().equals("int")) {
			return Type.INT;
		}
		if (typespec.getText().equals("char")) {
			return Type.CHAR;
		}
		if (typespec.getText().equals("double")) {
			return Type.DOUBLE;
		}
		return Type.ERROR;
	}

	// params
	static String getParamName(ParamContext param) {
		// <Fill in>
		return param.IDENT().getText();
	}

	static String getParamTypesText(ParamsContext params) {
		String typeText = "";

		for (int i = 0; i < params.param().size(); i++) {
			MiniCParser.Type_specContext typespec = (MiniCParser.Type_specContext) params.param(i).getChild(0);
			typeText += getTypeText(typespec); // + ";";
		}
		return typeText;
	}

	static String getLocalVarName(Local_declContext local_decl) {
		// <Fill in>
		return local_decl.IDENT().getText();
	}

	static String getFunName(Fun_declContext ctx) {
		// <Fill in>
		return ctx.IDENT().getText();
	}

	static String getFunName(ExprContext ctx) {
		// <Fill in>
		return ctx.IDENT().getText();
	}

	static String getArrayName(ExprContext ctx) {
		return ctx.getChild(0).getText();
	}

	static boolean noElse(If_stmtContext ctx) {
		return ctx.getChildCount() <= 5;
	}

	static String getFunProlog() {
		return ".class public Test" + "\n" + ".super java/lang/Object" + "\n" + ".method public <init>()V" + "\n"
				+ "aload_0" + "\n" + "invokenonvirtual java/lang/Object/<init>()V" + "\n" + "return" + "\n"
				+ ".end method" + "\n";
	}

	static String getCurrentClassName() {
		return "Test";
	}

	// 배열의 타입을 알아내는 메소드
	static SymbolTable.Type getArrayType(Type_specContext typespec) {
		if (typespec.getChild(0).getText().equals("int")) {
			return Type.INTARRAY;
		} else {
			// 임시로 int가 아닌 값이 들어올 경우 void타입을 반환
			return Type.VOID;
		}
	}

	// 배열의 타입(INTARRAY말고 int)알아내는 메소드
	static String getArrayElementType() {
		return "int";
	}
}
