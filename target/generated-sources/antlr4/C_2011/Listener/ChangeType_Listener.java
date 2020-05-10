package C_2011.Listener;

import listener.main.Obfuscator;

public class ChangeType_Listener extends Listener{

    public ChangeType_Listener(int count){
        super(count);
    }

    @Override
    public String obfus_binaryExp(String s1, String op, String s2) {
        return Obfuscator.changingTypes(s1, op, s2);
    }
}
