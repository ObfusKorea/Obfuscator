package obfusListener;

import listener.main.Obfuscator;

public class invariant_Listener extends Listener {
    public invariant_Listener(int count) {
        super(count);
    }

    @Override
    public String applyObfus_exceptRtStmt(String s1) {
        return Obfuscator.invariant(s1);
    }
}
