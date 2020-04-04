package obfusListener;

import listener.main.Obfuscator;

public class MBA_Listener extends Listener {

    public MBA_Listener(int count) {
        super(count);
    }

    @Override
    public String applyObfus_binary(String s1, String op, String s2){
        return Obfuscator.MBAExp(s1, s2, op);
    }
}
