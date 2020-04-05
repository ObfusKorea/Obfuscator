package obfusListener;

import listener.main.Obfuscator;

public class contextual_Listener extends Listener {
    public contextual_Listener(int count){
        super(count);
    }

    @Override
    public String applyObfus_exceptRtStmt(String s1) {
        return Obfuscator.contextual(s1);
    }
}
