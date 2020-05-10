package C_2011.Listener;

import listener.main.Obfuscator;

public class DynamicOpaque_Listener extends Listener {

    public DynamicOpaque_Listener(int count){
        super(count);
    }

    @Override
    public String Obfus_RtStmt(String s1) {
        return Obfuscator.dynamic(s1);
    }
}
