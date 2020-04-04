package listener.main;

import java.util.HashMap;

public class Obfuscator {
	
	// 변수명 매핑을 위한 맵
	public static final HashMap<String, String> map = new HashMap<String, String>();
	static String underBars = "";	// 난독화를 위한 변수
	
	// invariant opaque : exitStmt함수에서 return stmt를 제외한 모든 stmt에 붙여 해당 코드를 난독화하는데 사용
	public static String invariant(String input) {
		String front = "if (a * a >= 0) {\n"
				+ tabs(2);
		String end = tabs(1) + "} else {\n"
				+ tabs(2) + "int temp = 0;\r\n"
				+ tabs(2) + "result = temp;\r\n"
				+ tabs(1) + "}\n";
		return front + input + end;
	}

	// contextual opaque : exitStmt함수에서 return stmt를 제외한 모든 stmt에 붙여 해당 코드를 난독화하는데 사용
	public static String contextual(String input) {
		String front = "int para = 2;\n"
				+ tabs(1) + "if (para * para >= 0) {\n"
				+ tabs(2);
		String end = tabs(1) + "} else {\n"
				+ tabs(2) + "int temp = 0;\r\n"
				+ tabs(2) + "result = temp;\r\n"
				+ tabs(1) + "}\n";
		return front + input + end;
	}

	// dynamic opaque : exitStmt함수에서 return stmt에 붙여 해당 코드를 난독화하는데 사용
	public static String dynamic(String input) {
		String front1 = "int para = rand() % 9;\n"
				+ tabs(1) + "if (para * para >= 0) {\n"
				+ tabs(2) + "result--;\n" + tabs(1) + "} else {\n"
				+ tabs(2) + "result++;\n" + tabs(1) + "}\n";
		String front2 = tabs(1) + "if (para * para < 0) {\n"
				+ tabs(2) + "result++;\n"
				+ tabs(1) + "} else {\n"
				+ tabs(2) + "result--;\n"
				+ tabs(1) + "}\n"
				+ tabs(1) + "";
		return front1 + front2 + input;
	}
	
	// ident가 map에 있는지 판별
	public static boolean isObfuscated(String ident) {
		if(map.get(ident) == null) return false;
		else return true;
 	}
	
	// ident를 key로 value(난독화 후의 변수명)를 반환, expr에서 사용
	public static String getObfIdent(String ident) {
		return map.get(ident);
	}
	
	// ident를 key, '_'로 이루어진 문자열을 value로 map에 삽입 - 난독화
	public static void obfuscateIdent(String ident) {
		underBars += "_";
		map.put(ident, underBars);
	}
	
	// 함수 내부에서, 매개변수의 값을 가지는 새로운 변수를 할당하는 부분
	public static String reAssign(String[] before, int t) {
		String b = null;
		String ret = "";
		for(int i=0; i<before.length; i++) {
			b = before[i];
			ret += tabs(t)+"int "+map.get(b)+" = 0;\n";
		}
		for(int i=0; i<before.length; i++) {
			b = before[i];
			ret += tabs(t)+"for(int i = 0; i < "+b+"; i++)\n"
					+tabs(t)+"{\n"
					+tabs(t+1)+map.get(b)+"++;\n"
					+tabs(t)+"}\n";
		}
		return ret;
	}
	
	// 형변환 난독화
	public static String changingTypes(String s1, String op, String s2) {
		String dummy01 = "0;\n" +
				tabs(1)+"char str[10] = \"1001qwer\";\n" + 
				tabs(1)+"char str2[10] = \"qsd92sds\";\n" + 
				tabs(1)+"sprintf( str, \"%d\", "+s1+" );\n" + 
				tabs(1)+"sprintf( str2, \"%d\", "+s2+" );\n\n" + 
				tabs(1)+"char str3[10] = \"thsisres\";\n" + 
				tabs(1)+"int value = 9;\n" + 
				tabs(1)+"value = atoi(str) "+op+" atoi(str2);\n" + 
				tabs(1)+"sprintf( str3, \"%d\", value );\n" + 
				tabs(1)+"result = atoi(str3)";
		return dummy01;
	}

	private static String plusOP_1(String x, String y){
		String result = String.format("(%s or %s) + %s - ((~%s) and %s)", x, y, y,x,y);
		return result;
	}

	private static String plusOP_2(String x, String y){
		return String.format("(%s or %s) + (%s and %s)", x, y, x,y);
	}

	private static String xorOP(String x, String y){
		return String.format("(%s or %s) - %s + ((~%s) and %s)", x, y, y,x,y);
	}

	private static String andOP(String x, String y){
		return String.format("((~%s) or %s) - (~%s)", x, y, x);
	}
	private static String orOP(String x, String y){
		return String.format("(%s ^ %s) + %s - ((~%s) and %s))", x, y, y, x, y);
	}

	public static String MBAExp(String x, String y, String op){
		switch (op){
			case "+":
				return plusOP_1(x,y);
			case "^":
				return xorOP(x,y);
			case "and":
				return andOP(x, y);
			case "or":
				return orOP(x, y);
		}
		return x+op+y;
	}
	
	// 점 찍는거
	private static String tabs(int n) {
		int i = 0;
		String tabs = "";
		while (i < n) {
			tabs = tabs + "\t";
			i++;
		}
		return tabs;
	}
	
}
