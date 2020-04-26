package C_2011.Listener;

import listener.main.Obfuscator;

public class changeType_Listener extends Listener{

    public changeType_Listener(int count){
        super(count);
    }

    @Override
    public String Obfus_AdditiveExp(String s1, String op, String s2) {
        return Obfuscator.changingTypes(s1, op, s2);
    }
}
