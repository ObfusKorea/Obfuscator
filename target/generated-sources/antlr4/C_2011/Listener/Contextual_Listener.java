package C_2011.Listener;

import listener.main.Obfuscator;

public class Contextual_Listener extends Listener {

    public Contextual_Listener(int count) {
        super(count);
    }

    @Override
    public String obfus_opaque(String stmt) {
        return Obfuscator.contextual(stmt);
    }
}
