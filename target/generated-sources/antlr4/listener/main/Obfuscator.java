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
		String dummy01 = "0;\n"+
				tabs(1)+"char temp[10] = \"qwerqwerq\";\n" + 
				tabs(1)+"temp[0] = "+s1+"+'0';\n" + 
				tabs(1)+"temp[1] = "+s2+"+'0';\n" + 
				tabs(1)+"temp[2] = temp[0]-'0' + temp[1]-'0';\n" + 
				tabs(1)+"result = temp[2]-'0';";
		return dummy01;
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
