package C_2011.Listener;

public class VM_Listener extends Listener{

    public VM_Listener(int count) {
        super(count);
    }

    @Override
    public String insertINIT(String input){
        String init = "// TO: Generated program\n" +
                "// simply put 0.~4. and generate 5.\n" +
                "\n" +
                "#include <stdio.h>\n" +
                "\n" +
                "// 0. for maximal values : to be updated!\n" +
                "#define MAX\t100\n" +
                "\n" +
                "\n" +
                "// 1. code : array of bytes (general, reusable)\n" +
                "struct code {\n" +
                "\tchar arr[MAX];\n" +
                "\tint pc;\n" +
                "};\n" +
                "\n" +
                "char readNext(struct code * _this) {\n" +
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
                "\tint arr[MAX];\n" +
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
                "#define  VASSGN\t-5\n\n\n\n\n\n";

        return init+input;
    }
}
