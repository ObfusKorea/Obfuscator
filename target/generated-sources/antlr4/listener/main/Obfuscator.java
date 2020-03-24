package listener.main;

import java.util.HashMap;

public class Obfuscator {
	
	// 변수명 매핑을 위한 맵
	public static final HashMap<String, String> map = new HashMap<String, String>();
	static String underBars = "";	// 난독화를 위한 변수
	
	// invariant opaque : exitStmt함수에서 return stmt를 제외한 모든 stmt를 난독화하는 데 사용
	public static String invariant(String input) {
		String front = "if (a * a >= 0) {\n"
				+ dots(2);
		String end = dots(1) + "} else {\n"
				+ dots(2) + "int temp = 0;\r\n"
				+ dots(2) + "result = temp;\r\n"
				+ dots(1) + "}\n";
		return front + input + end;
	}

	// contextual opaque : exitStmt함수에서 return stmt를 제외한 모든 stmt를 난독화하는 데 사용
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

	// dynamic opaque : exitStmt함수에서 return stmt를 난독화하는데 사용
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
	
	// 점 찍는거
	public static String dots(int n) {
		int i = 0;
		String dots = "";
		while (i < n) {
			dots = dots + "....";
			i++;
		}
		return dots;
	}
	
}
