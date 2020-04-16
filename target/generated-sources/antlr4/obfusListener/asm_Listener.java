package obfusListener;

import listener.main.Obfuscator;

public class asm_Listener extends Listener {


    public asm_Listener(int count) {
        super(count);
    }

    @Override
    public String applyObfus_assign(String a, String b){
        return Obfuscator.dynamicASM(a,b);
    }
}
