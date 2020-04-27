package C_2011.Listener;

import listener.main.Obfuscator;

public class Invariant_Listener extends Listener {
    public Invariant_Listener(int count) {
        super(count);
    }

    @Override
    public String obfus_opaque(String stmt) {
        return Obfuscator.invariant(stmt);
    }
}
