package obfusListener;

import listener.main.Obfuscator;

public class dynamicOpaque_Listener extends Listener {
    public dynamicOpaque_Listener(int count){
        super(count);
    }

    @Override
    public String applyObfus_RtStmt(String s1) {
        return Obfuscator.dynamic(s1);
    }
}
