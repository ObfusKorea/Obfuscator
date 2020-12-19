package C_2011.Listener;

import listener.main.Obfuscator;
public class VM_Listener extends Listener {
    enum VCODE {
        VPUSH, VADD, VSUB, VMULT, VASSGN
    }

    String getVCode(VCODE c) {
        switch (c) {
            case VPUSH:
                return "VPUSH";
            case VADD:
                return "VADD";
            case VSUB:
                return "VSUB";
            case VMULT:
                return "VMULT";
            case VASSGN:
                return "VASSGN";
            default:
                return "NULL";
        }
    }

    String add = "add(&encoded_bytes, %s);\n";

    public VM_Listener(int count) {
        super(count);
    }

    @Override
    public String insertINIT(String input){
        String init = Obfuscator.getVMInit();
        return init+input;
    }

    String getAddCode(String s) {
        return String.format(add, s);
    }
}