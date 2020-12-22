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

    public static String getVMInit(int rmax,int codemax,int stackmax,String op[]){
        String VPUSH = op[0], VADD = op[1], VSUB = op[2], VMULT = op[3], VASSGN = op[4], VLOAD = op[5];

        String code1 =
                "#include <stdio.h>\n" +
                "\n" +
                "#define MAX\t"+codemax+"\n" +
                "#define S_MAX\t"+stackmax+"\n" +
                "#define R_MAX\t"+(rmax)+
                "\n" +
                "\n" +
                "struct code {\n" +
                "\tint arr[MAX];\n" +
                "\tint pc;\n" +
                "};\n" +
                "\n" +
                "int readNext(struct code * _this) {\n" +
                "\tif (_this->pc == MAX) \n" +
                "\t\treturn -1;\n" +
                "\telse{\n" +
                "\t\tint tmp = _this->pc;\n" +
                "\t\t_this->pc++;\t\t\n" +
                "\t\treturn _this->arr[tmp];\n" +
                "\n" +
                "\t}\n" +
                "}\n" +
                "\n";

        String code2 =
                "// 2. operand stack  (general, reusable)\n" +
                "struct mstack {\n" +
                "\tint arr[S_MAX];\n" +
                "\tint top;\n" +
                "} mst;\n" +
                "\n" +
                "\n" +
                "void push(struct mstack * _this, int data ) {\n" +
                        "\tif(_this->top+1 >= S_MAX) return;\n" +
                        "\t_this->arr[++_this->top] = data;\n" +
                        "}\n" +
                        "\n" +
                        "int pop(struct mstack * _this) {\n" +
                        "\tif(_this->top<0) return -1;\n" +
                        "\tint tmp = _this->arr[_this->top];\n" +
                        "\t_this->arr[_this->top--]=0;\n" +
                        "\treturn tmp;\n" +
                        "}\n\n\n";

        String code3 = String.format("// 3. op code of VM (general, reusable)\n" +
                "#define  %s\t0\n" +
                "#define  %s\t1\n" +
                "#define  %s\t2\n" +
                "#define  %s\t3\n" +
                "#define  %s\t4\n" +
                "#define  %s\t5\n" +
                "\n" +
                "\n",
                VPUSH, VADD, VSUB, VMULT, VASSGN, VLOAD);

        String code4 = "// 4. vm engine (general, so reusable)\n" +
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
                "\tint isCnst = 0, isRead = 0;\n"+
                "\tgoto "+Label.F.getValue()+";\n"+

                getTotalMachineLabels()+

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

        String result = code1 + code2 + code3 + code4;
        return result;
    }

    private static String getTotalMachineLabels() {
        StringBuilder sb = new StringBuilder();
        Label[] labels = Label.values();
        for (int i = 0; i < labels.length; i++) {
            sb.append(getLabelBlock(labels[i]));
        }
        return sb.toString();
    }

    static String getLabelBlock(Label label){
        String result;
        switch (label){
            case F:
                result = "\t"+label.getValue()+":\n" +
                        "        if(isRead == 1){\n" +
                        "\n" +
                        "        }else if ((onebyte = readNext(bytes)) == -1) return;\n" +
                        "\n" +
                        "        if(onebyte == VPUSH){\n" +
                        "            isCnst = VPUSH;\n" +
                        "            goto CHECK;\n" +
                        "        }else if(onebyte == VLOAD){\n" +
                        "            isCnst = VLOAD;\n" +
                        "            goto CHECK;\n" +
                        "        }else if(onebyte == VADD){\n" +
                        "            isCnst = VADD;\n" +
                        "            goto ARG;\n" +
                        "        }else if(onebyte == VSUB){\n" +
                        "            isCnst = VSUB;\n" +
                        "            goto ARG;\n" +
                        "        }else if(onebyte == VMULT){\n" +
                        "            isCnst = VMULT;\n" +
                        "            goto ARG;\n" +
                        "        }else if(onebyte == VASSGN){\n" +
                        "            isCnst = VASSGN;\n" +
                        "            goto ASSGN;\n" +
                        "        }\n";
                break;
            case CHECK:
                result = "\tCHECK:\n" +
                        "\t    if( (arg1=readNext(bytes)) == -1) return;\n" +
                        "        if(isCnst == VLOAD){\n" +
                        "            goto LOAD;\n" +
                        "        }else{\n" +
                        "            goto PUSH;";
                break;
            case ARG:
                result = "    ARG:\n" +
                        "\t    arg1 = pop(&mst);\n" +
                        "    \targ2 = pop(&mst);\n" +
                        "        if(isCnst == VADD){\n" +
                        "            goto ADD;\n" +
                        "        }else if(isCnst == VSUB){\n" +
                        "            goto SUB;\n" +
                        "        }else if(isCnst == VMULT){\n" +
                        "            goto MULT;\n" +
                        "        }";
                break;
            case PUSH:
                result = "\tPUSH:\n" +
                        "\t    push(&mst, arg1);\n" +
                        "        goto F;\n";
                break;
            case LOAD:
                result = "\tLOAD:\n" +
                        "\t    push(&mst, (*tvars)[arg1]);\n" +
                        "        goto F;\n";
                break;
            case ADD:
                result = "\tADD:\n" +
                        "        push(&mst, arg2 + arg1);\n" +
                        "        if ((onebyte = readNext(bytes)) == -1) return;\n" +
                        "        if(onebyte == VPUSH){\n" +
                        "            isCnst = VPUSH;\n" +
                        "            goto CHECK;\n" +
                        "        }else if(onebyte == VLOAD){\n" +
                        "            isCnst = VLOAD;\n" +
                        "            goto CHECK;\n" +
                        "        }else if(onebyte == VADD){\n" +
                        "            isCnst = VADD;\n" +
                        "            goto ARG;\n" +
                        "        }else if(onebyte == VSUB){\n" +
                        "            isCnst = VSUB;\n" +
                        "            goto ARG;\n" +
                        "        }else if(onebyte == VMULT){\n" +
                        "            isCnst = VMULT;\n" +
                        "            goto ARG;\n" +
                        "        }else if(onebyte == VASSGN){\n" +
                        "            isCnst = VASSGN;\n" +
                        "            goto ASSGN;\n" +
                        "        }\n" +
                        "        goto F;\n";
                break;
            case SUB:
                result = "\tSUB:\n" +
                        "        push(&mst, arg2 - arg1);\n" +
                        "        goto F;\n";
                break;
            case MULT:
                result = "\tMULT:\n" +
                        "        push(&mst, arg2 * arg1);\n" +
                        "        goto F;\n";
                break;
            case ASSGN:
                result = "\tASSGN:\n" +
                        "\t    arg1=readNext(bytes);\n" +
                        "\t\tif( (arg2=readNext(bytes)) == -1) return;\n" +
                        "\t\t(*tvars)[arg2] = pop(&mst);\n" +
                        "        goto F;\n";
                break;
            default:
                result = "NULL";
        }

        return result;
    }

    enum Label{

        F("F"), CHECK("CHECK"), ARG("ARG"), PUSH("PUSH"), LOAD("LOAD"), ADD("ADD"), SUB("SUB"), MULT("MULT"), ASSGN("ASSGN");
        String value;

        Label(String value){
            this.value= value;
        }

        public String getValue(){
            return value;
        }

    }

}
