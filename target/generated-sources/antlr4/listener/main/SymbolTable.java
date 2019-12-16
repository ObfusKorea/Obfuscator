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
		INT, INTARRAY, VOID, ERROR, CHAR, DOUBLE
	}

	static public class VarInfo {
		Type type;
		int id;
		int initVal;
		double d_initVal; // double형 변수를 위함
		Object arrayVal;

		public VarInfo(Type type, int id, int initVal) {
			this.type = type;
			this.id = id;
			this.initVal = initVal;
		}

		public VarInfo(Type type, int id, double initVal) {
			this.type = type;
			this.id = id;
			this.d_initVal = initVal;
		}

		public VarInfo(Type type, int id) {
			this.type = type;
			this.id = id;
			if (type == Type.INT) {
				this.initVal = 0;
			}
			if (type == Type.DOUBLE) {
				this.d_initVal = 0.0;
			}
		}

		public void addArrayVal(Object arrayVal) {
			this.arrayVal = arrayVal;
		}
	}

	// (int)배열은 생성하면서 배열의 정보를 가지는 클래스를 하나 더 생성
	static public class IntArrayVal {
		int id;
		int length;
		int[] val;

		public IntArrayVal(int id, int length) {
			this.id = id;
			this.length = length;
			this.val = new int[length];
		}

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

	void putArrayInitVal(String varname, String arrayLength, ParseTree arrayInitVal) {
		VarInfo currentVar = _lsymtable.get(varname);		//현재 array의 초기값을 넣어줄 어레이 var정보를 가져옴
		int thisArrayLength = 0;	//array의 초기값의 길이 지정

		if (arrayInitVal instanceof MiniCParser.Array_init_valContext) {
			if (currentVar.type == Type.INTARRAY) {
				if (arrayLength == "") {
					thisArrayLength = (arrayInitVal.getChildCount())/2+1;
				} else {
					thisArrayLength = Integer.parseInt(arrayLength);
				}
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

//	void editArrayValIn(String varname, ParseTree indexTree, String inputVal) {
//		VarInfo currentVar = _lsymtable.get(varname);
////		int indexVal = Integer.parseInt(indexTree);
//		if (currentVar.type == Type.INTARRAY) {
//			int inputIntVal = Integer.parseInt(inputVal);
//
//			int[] oldArray = ((IntArrayVal)currentVar.arrayVal).val;
//			oldArray[0] = inputIntVal;
//
//			//수정된 IntArrayVal을 넣기
//			currentVar.addArrayVal(new IntArrayVal(currentVar.id, oldArray.length, oldArray));
//		}
//
//	}
	// 어레이의 해당 값을 수정하는 코드

	void putGlobalVar(String varname, Type type) {
		// <Fill here>
		_gsymtable.put(varname, new VarInfo(type, _globalVarID++));
	}

	void putLocalVarWithInitVal(String varname, Type type, int initVar) {
		// <Fill here>
		_lsymtable.put(varname, new VarInfo(type, _localVarID++, initVar));
	}

	// double type 로컬 변수를 초기 값과 함께 생성, 로컬 심벌 테이블에 삽입
	void putLocalVarWithInitVal(String varname, Type type, double initVar) {
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

	// double type 전역 변수를 초기 값과 함께 생성, 글로벌 심벌 테이블에 삽입
	void putGlobalVarWithInitVal(String varname, Type type, double initVar) {
		// <Fill here>
		_gsymtable.put(varname, new VarInfo(type, _globalVarID++, initVar));
	}

	void putParams(MiniCParser.ParamsContext params) {
		for (int i = 0; i < params.param().size(); i++) {
			// <Fill here>
			MiniCParser.ParamContext param = params.param(i);
			String pname = getParamName(param);
			putLocalVar(pname, getType(param.type_spec()));
		}
	}

	private void initFunTable() {
		FInfo printlninfo = new FInfo();
		printlninfo.sigStr = "java/io/PrintStream/println(I)V";

		FInfo printDoubleInfo = new FInfo();
		printDoubleInfo.sigStr = "java/io/PrintStream/println(D)V";

		FInfo printCharInfo = new FInfo();
		printCharInfo.sigStr = "java/io/PrintStream/println(C)V";

		FInfo maininfo = new FInfo();
		maininfo.sigStr = "main([Ljava/lang/String;)V";

		// println 함수가 출력하는것을 구분하기 위해서 기존의 _print를 I,D,C로 나누었음 !!!
		_fsymtable.put("_printI", printlninfo);
		_fsymtable.put("_printD", printDoubleInfo);
		_fsymtable.put("_printC", printCharInfo);
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
