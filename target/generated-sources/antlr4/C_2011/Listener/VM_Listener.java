package C_2011.Listener;

import listener.main.Obfuscator;

import java.util.*;

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
    SymbolTable symbolTable = new SymbolTable();

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

class SymbolTable {
    class Var {
        int id;
        String name;
        int value;

        public Var(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private Map<String, Var> _lsymtable = new HashMap<>();
    private int _localVarID = 0;

    void putLocalVar(String varName) {
        if (_lsymtable.get(varName) == null) {
            _lsymtable.put(varName, new Var(_localVarID++, varName));
        }
    }

    int getLocalVarID(String varName) {
        Var lVar = _lsymtable.get(varName);
        if (lVar != null) {
            return lVar.id;
        }
        return -1;
    }
}