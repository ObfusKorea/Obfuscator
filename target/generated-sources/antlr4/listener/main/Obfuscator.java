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
                + tabs(2) + "result = result - 1;\n" + tabs(1) + "} else {\n"
                + tabs(2) + "result = result + 1;\n" + tabs(1) + "}\n";
        String front2 = tabs(1) + "if (para * para < 0) {\n"
                + tabs(2) + "result = result - 1;\n"
                + tabs(1) + "} else {\n"
                + tabs(2) + "result = result + 1;\n"
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
        String result = String.format("(%s | %s) + %s - (~%s & %s)", x, y, y,x,y);
        return result;
    }

    private static String plusOP_2(String x, String y){
        return String.format("(%s | %s) + (%s & %s)", x, y, x,y);
    }

    private static String xorOP(String x, String y){
        return String.format("(%s | %s) - %s + (~%s & %s)", x, y, y,x,y);
    }

    private static String andOP(String x, String y){
        return String.format("(~%s | %s) - (~%s)", x, y, x);
    }
    private static String orOP(String x, String y){
        return String.format("(%s ^ %s) + %s - (~%s & %s)", x, y, y, x, y);
    }

    public static String MBAExp(String x, String y, String op){
        switch (op){
            case "+":
                return plusOP_1(x,y);
            case "^":
                return xorOP(x,y);
            case "&":
                return andOP(x, y);
            case "|":
                return orOP(x, y);
        }
        return x+op+y;
    }

    public static String dynamicASM(String a, String b){
        String c = "int a = %s;\n" +
                "int b = %s;\n" +
                "int temp_a = %s;\n" +
                "\n" +
                "__asm {\n" +
                "\tmov ecx, b;\n" +
                "\tcmp ecx, 0;\n" +
                "\tjnz A;\n" +
                "\tmov temp_a, 0;\n" +
                "\tmov eax, b;\n" +
                "\tadd temp_a, eax;\n" +
                "\tjmp not_A;\n" +
                "}\n" +
                "A:\n" +
                "__asm {\n" +
                "mov temp_a, 0;\n" +
                "jmp not_A;\n" +
                "}\n" +
                "return;\n" +
                "\n" +
                "not_A:\n" +
                "__asm {\n" +
                "mov ecx, b;\n" +
                "cmp ecx, 0;\n" +
                "jnz B;\n" +
                "mov eax, temp_a;\n" +
                "mov a, eax;\n" +
                "}\n" +
                "return;\n" +
                "\n" +
                "B:\n" +
                "__asm {\n" +
                "mov eax, b;\n" +
                "add temp_a, eax;\n" +
                "mov eax, temp_a;\n" +
                "mov a, eax;\n" +
                "}\n" +
                "return;";
        return String.format(c,a,b,"111");

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

    public static String getVMInit(int rmax,int codemax,int stackmax){
        String init =
                "#include <stdio.h>\n" +
                "\n" +
                "// 0. for maximal values : to be updated!\n" +
                "#define MAX\t"+codemax+"\n" +
                "#define S_MAX\t"+stackmax+"\n" +
                "#define R_MAX\t"+(rmax)+
                "\n" +
                "\n" +
                "// 1. code : array of bytes (general, reusable)\n" +
                "struct code {\n" +
                "\tint arr[MAX];\n" +
                "\tint pc;\n" +
                "};\n" +
                "\n" +
                "int readNext(struct code * _this) {\n" +
                "\t//return _this->arr[_this->pc];\n" +
                "\t//_this->pc++;\n" +
                "\tif (_this->pc == MAX) \n" +
                "\t\treturn -1;\n" +
                "\telse{\n" +
                "\t//\tprintf(\"%d, %d\\n\",_this->pc, _this->arr[_this->pc]);\n" +
                "\t\tint tmp = _this->pc;\n" +
                "\t\t_this->pc++;\t\t\n" +
                "\t\treturn _this->arr[tmp];\n" +
                "\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "// 2. operand stack  (general, reusable)\n" +
                "struct mstack {\n" +
                "\tint arr[S_MAX];\n" +
                "\tint top;\n" +
                "} mst;\n" +
                "\n" +
                "\n" +
                "void push(struct mstack * _this, int data ) {\n" +
                "\t_this->arr[_this->top] = data;\n" +
                "\t// printf(\"push %d, at %d\\n\",_this->arr[_this->top],_this->top);\n" +
                "\t_this->top++;\n" +
                "}\n" +
                "\n" +
                "int pop(struct mstack * _this) {\n" +
                "\t//_this->top--;\n" +
                "\tint tmp = _this->arr[--_this->top];\n" +
                "\t_this->arr[_this->top]=0;\n" +
                "\t// printf(\"pop %d\\n\",_this->top-1);\n" +
                "\treturn tmp;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "// 3. op code of VM (general, reusable)\n" +
                "#define  VPUSH\t-1\n" +
                "#define  VADD\t-2 \n" +
                "#define  VSUB\t-3\n" +
                "#define  VMULT\t-4\n" +
                "#define  VASSGN\t-5\n" +
                "#define  VLOAD\t-6\n" +
                "\n" +
                "\n" +
                "// 4. vm engine (general, so reusable)\n" +
                "// for y+(x*z)\n" +
                "//-> push y push x push z mult add assgn w\n" +
                "// push 1 push 0 push 2 mult add assgn 3\n" +
                "// 0,1,2 are variables' id\n" +
                "\n" +
                "void run_on_vm(struct code * bytes, int (* tvars)[] ) {\n" +
                "\n" +
                "\tint i = 0;\n" +
                "\tint onebyte; \n" +
                "\tint arg1, arg2;\n" +
                "\t// printf(\"run_on_vm\\n\");\n" +
                "\twhile( (onebyte = readNext(bytes)) != 0) {\n" +
                "\t\t// printf(\"%d\\n\", onebyte);\n" +
                "\t\tswitch (onebyte) {\n" +
                "\t\t\tcase VPUSH : // push Constant\n" +
                "\t\t\t\tif( (arg1=readNext(bytes)) == -1) return;\n" +
                "\t\t\t\t//printf(\"push %d\\n\",(*tvars)[args1]);\n" +
                "\t\t\t\tpush(&mst, arg1);\n" +
                "\t\t\t\tbreak;\n" +
                "\t\t\tcase VLOAD: // load Variable\n" +
                "\t\t\t\tif( (arg1=readNext(bytes)) == -1) return;\n" +
                "\t\t\t\t//printf(\"push %d\\n\",(*tvars)[args1]);\n" +
                "\t\t\t\tpush(&mst, (*tvars)[arg1]);\n" +
                "\t\t\t\tbreak;\n"+
                "\t\t\tcase VADD:\n" +
                "\t\t\t\targ1 = pop(&mst);\n" +
                "\t\t\t\targ2 = pop(&mst);\n" +
                "\t\t\t\t// printf(\"add %d %d\\n\",arg1, arg2);\n" +
                "\t\t\t\tpush(&mst, arg2 + arg1);\n" +
                "\t\t\t\tbreak;\n" +
                "\t\t\tcase VSUB:\n" +
                "\t\t\t\targ1 = pop(&mst);\n" +
                "\t\t\t\targ2 = pop(&mst);\n" +
                "\t\t\t\tpush(&mst, arg2-arg1);\n" +
                "\t\t\t\tbreak;\n" +
                "\t\t\tcase VMULT:\n" +
                "\t\t\t\targ1 = pop(&mst);\n" +
                "\t\t\t\targ2 = pop(&mst);\n" +
                "\t\t\t\t// printf(\"mult %d %d\\n\",arg1, arg2);\n" +
                "\t\t\t\tpush(&mst, arg2*arg1);\n" +
                "\t\t\t\tbreak;\n" +
                "\t\t\tcase VASSGN:\n" +
                "\t\t\t\targ1=readNext(bytes);\n" +
                "\t\t\t\tif( (arg2=readNext(bytes)) == -1) return;\n" +
                "\t\t\t\t(*tvars)[arg2] = pop(&mst);\n" +
                "\t\t\t\tbreak;\n" +
                "\t\t} \n" +
                "\t}\n" +
                "\n" +
                "}\n" +
                "\n" +
                "void add(struct code * bytes, int op){\n" +
                "\tbytes->arr[bytes->pc++] = op;\n" +
                "}\n" +
                "\n" +
                "void begin_add(struct code * bytes){\n" +
                "\tbytes->pc = 0;\n" +
                "\tfor(int i = 0; i<MAX ; i++){\n" +
                "\t\tbytes->arr[i] = 0;\t\t\n" +
                "\t}\n" +
                "\t// *mst.top = 0;\n" +
                "\tmst.top = 0;\n" +
                "}\n" +
                "\n" +
                "void end_add(struct code * bytes){\n" +
                "\tbytes->pc = 0;\n" +
                "}\n\n\n\n";
        return init;
    }

}
