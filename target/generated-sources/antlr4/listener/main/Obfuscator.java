package listener.main;

public class Obfuscator {
	
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
