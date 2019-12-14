package listener.main;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;

import generated.MiniCParser;
import generated.MiniCParser.Fun_declContext;
import generated.MiniCParser.Local_declContext;
import generated.MiniCParser.ParamsContext;
import generated.MiniCParser.Type_specContext;
import generated.MiniCParser.Var_declContext;
import listener.main.SymbolTable.Type;
import static listener.main.BytecodeGenListenerHelper.*;

public class SymbolTable {
	enum Type {
		INT, INTARRAY, VOID, ERROR
	}

	static public class VarInfo {
		Type type;
		int id;
		int initVal;
		Object arrayVal;

		public VarInfo(Type type, int id, int initVal) {
			this.type = type;
			this.id = id;
			this.initVal = initVal;
		}

		public VarInfo(Type type, int id) {
			this.type = type;
			this.id = id;
			this.initVal = 0;
		}

		public void addArrayVal(Object arrayVal) {
			this.arrayVal = arrayVal;
		}
	}

	// ???(int)배열은 생성하면서 배열의 정보를 가지는 클래스를 하나 더 생성
	static public class IntArrayVal {
		int id;
		int length;
		int[] val;

		public IntArrayVal(int id, int length) {
			this.id = id;
			this.length = length;
			this.val = new int[length];
		}

		// ???일단 아래거는 이따가
		public IntArrayVal(int id, int length, int[] val) {
			this.id = id;
			this.length = length;
			this.val = val;
		}
	}

	static public class FInfo {
		public String sigStr;
	}

	private Map<String, VarInfo> _lsymtable = new HashMap<>(); // local v.
	private Map<String, VarInfo> _gsymtable = new HashMap<>(); // global v.
	private Map<String, FInfo> _fsymtable = new HashMap<>(); // function

	private int _globalVarID = 0;
	private int _localVarID = 0;
	private int _labelID = 0;
	private int _tempVarID = 0;

	SymbolTable() {
		initFunDecl();
		initFunTable();
	}

	void initFunDecl() { // at each func decl
		_lsymtable.clear();
		_localVarID = 0;
		_labelID = 0;
		_tempVarID = 32;
	}

	void putLocalVar(String varname, Type type) {
		// <Fill here>
		_lsymtable.put(varname, new VarInfo(type, _localVarID++));
	}

	void putArray(String varname, String arrayLength) {
		VarInfo currentVar = _lsymtable.get(varname);
		if (currentVar.type == Type.INTARRAY) {
			currentVar.addArrayVal(new IntArrayVal(currentVar.id, Integer.parseInt(arrayLength)));
		}
		// 타입이 무엇인지 판단하고 (지금은)intArray로 이동
	}// int intArray[5];

	void putArrayInit(String varname, String arrayLength, ParseTree arrayInitVal) {
		VarInfo currentVar = _lsymtable.get(varname);
		if (arrayInitVal instanceof MiniCParser.Array_init_valContext) {
			if (currentVar.type == Type.INTARRAY) {
				int thisArrayLength = Integer.parseInt(arrayLength);
				int[] thisInitValArray = new int[thisArrayLength];
				for (int i = 0; i < arrayInitVal.getChildCount(); i = i + 2) {
					thisInitValArray[i / 2] = Integer.parseInt(arrayInitVal.getChild(i).getText());
				}
				currentVar.addArrayVal(new IntArrayVal(currentVar.id, thisArrayLength, thisInitValArray));
			}
		}
		// 타입이 무엇인지 판단하고 intArray로 이동
	}// int intArray[]={0,1,2,3};, int intArray[4]={0,1,2,3};, int
		// intArray[10]={0,1,2,3};
	
//	void 어레이의 해당 값을 수정하는 코드

	void putGlobalVar(String varname, Type type) {
		// <Fill here>
		_gsymtable.put(varname, new VarInfo(type, _globalVarID++));
	}

	void putLocalVarWithInitVal(String varname, Type type, int initVar) {
		// <Fill here>
		_lsymtable.put(varname, new VarInfo(type, _localVarID++, initVar));
	}

	Object getArrayInitVal(String varname) {
		VarInfo arrayVarInfo = _lsymtable.get(varname);
		if (arrayVarInfo.type == Type.INTARRAY) {
			if (arrayVarInfo.arrayVal instanceof IntArrayVal) {
				IntArrayVal iav = ((IntArrayVal) arrayVarInfo.arrayVal);
				return iav.val;
			}
		}
		return null;

	}

	void putGlobalVarWithInitVal(String varname, Type type, int initVar) {
		// <Fill here>
		_gsymtable.put(varname, new VarInfo(type, _globalVarID++, initVar));
	}

	void putParams(MiniCParser.ParamsContext params) {
		for (int i = 0; i < params.param().size(); i++) {
			// <Fill here>
			MiniCParser.ParamContext param = params.param(i);
			String pname = getParamName(param);
			putLocalVar(pname, Type.INT);
		}
	}

	private void initFunTable() {
		FInfo printlninfo = new FInfo();
		printlninfo.sigStr = "java/io/PrintStream/println(I)V";

		FInfo maininfo = new FInfo();
		maininfo.sigStr = "main([Ljava/lang/String;)V";
		_fsymtable.put("_print", printlninfo);
		_fsymtable.put("main", maininfo);
	}

	public String getFunSpecStr(String fname) {
		// <Fill here>
		FInfo finfo = _fsymtable.get(fname);
		return finfo.sigStr;
	}

	public String getFunSpecStr(Fun_declContext ctx) {
		// <Fill here>
		return getFunSpecStr(getFunName(ctx));
	}

	public String putFunSpecStr(Fun_declContext ctx) {
		String fname = getFunName(ctx);
		MiniCParser.ParamsContext params = ctx.params();
		String argtype = getParamTypesText(params);
		String rtype = getTypeText(ctx.type_spec());

		String res = fname + "(" + argtype + ")" + rtype;

		FInfo finfo = new FInfo();
		finfo.sigStr = res;
		_fsymtable.put(fname, finfo);

		return res;
	}

	String getVarId(String name) {
		// <Fill here>
		VarInfo lvar = (VarInfo) _lsymtable.get(name);
		return Integer.toString(lvar.id);
	}

	Type getVarType(String name) {
		VarInfo lvar = (VarInfo) _lsymtable.get(name);
		if (lvar != null) {
			return lvar.type;
		}

		VarInfo gvar = (VarInfo) _gsymtable.get(name);
		if (gvar != null) {
			return gvar.type;
		}

		return Type.ERROR;
	}

	String newLabel() {
		return "label" + _labelID++;
	}

	String newTempVar() {
		String id = "";
		return id + _tempVarID--;
	}

	// global
	public String getVarId(Var_declContext ctx) {
		// <Fill here>
		VarInfo gvar = (VarInfo) _gsymtable.get(ctx.IDENT().toString());
		return Integer.toString(gvar.id);
	}

	// local
	public String getVarId(Local_declContext ctx) {
		String sname = "";
		sname += getVarId(ctx.IDENT().getText());
		return sname;
	}

}
