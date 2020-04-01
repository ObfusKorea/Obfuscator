package listener.main;

import java.util.HashMap;

public class Obfuscator {
	
	// 변수명 매핑을 위한 맵
	public static final HashMap<String, String> map = new HashMap<String, String>();
	static String underBars = "";	// 난독화를 위한 변수
	
	// invariant opaque : exitStmt함수에서 return stmt를 제외한 모든 stmt에 붙여 해당 코드를 난독화하는데 사용
	public static String invariant(String input) {
		String front = "if (a * a >= 0) {\n"
				+ dots(2);
		String end = dots(1) + "} else {\n"
				+ dots(2) + "int temp = 0;\r\n"
				+ dots(2) + "result = temp;\r\n"
				+ dots(1) + "}\n";
		return front + input + end;
	}

	// contextual opaque : exitStmt함수에서 return stmt를 제외한 모든 stmt에 붙여 해당 코드를 난독화하는데 사용
	public static String contextual(String input) {
		String front = "int para = 2;\n"
				+ dots(1) + "if (para * para >= 0) {\n"
				+ dots(2);
		String end = dots(1) + "} else {\n"
				+ dots(2) + "int temp = 0;\r\n"
				+ dots(2) + "result = temp;\r\n"
				+ dots(1) + "}\n";
		return front + input + end;
	}

	// dynamic opaque : exitStmt함수에서 return stmt에 붙여 해당 코드를 난독화하는데 사용
	public static String dynamic(String input) {
		String front1 = "int para = rand() % 9;\n"
				+ dots(1) + "if (para * para >= 0) {\n"
				+ dots(2) + "result--;\n" + dots(1) + "} else {\n"
				+ dots(2) + "result++;\n" + dots(1) + "}\n";
		String front2 = dots(1) + "if (para * para < 0) {\n"
				+ dots(2) + "result++;\n"
				+ dots(1) + "} else {\n"
				+ dots(2) + "result--;\n"
				+ dots(1) + "}\n"
				+ dots(1) + "";
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
			ret += dots(t)+"int "+map.get(b)+" = 0;\n";
		}
		for(int i=0; i<before.length; i++) {
			b = before[i];
			ret += dots(t)+"for(int i = 0; i < "+b+"; i++)\n"
					+dots(t)+"{\n"
					+dots(t+1)+map.get(b)+"++;\n"
					+dots(t)+"}\n";
		}
		return ret;
	}
	
	// 형변환 난독화
	public static String changingTypes(String s1, String op, String s2) {
		String dummy01 = "0;\n"+
				dots(1)+"char temp[10] = \"qwerqwerq\";\n" + 
				dots(1)+"temp[0] = "+s1+"+'0';\n" + 
				dots(1)+"temp[1] = "+s2+"+'0';\n" + 
				dots(1)+"temp[2] = temp[0]-'0' + temp[1]-'0';\n" + 
				dots(1)+"result = temp[2]-'0';";
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
	private static String dots(int n) {
		int i = 0;
		String dots = "";
		while (i < n) {
			dots = dots + "....";
			i++;
		}
		return dots;
	}
	
}
