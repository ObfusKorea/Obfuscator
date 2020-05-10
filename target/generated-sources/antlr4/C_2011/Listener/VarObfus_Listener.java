package C_2011.Listener;

import listener.main.Obfuscator;

public class VarObfus_Listener extends Listener {

    private Obfuscator obfuscator = new Obfuscator();

    public VarObfus_Listener(int count) {
        super(count);
    }

    @Override
    public String obfus_ID(String id) {
        if (obfuscator.isObfuscated(id)) {
            return obfuscator.getObfIdent(id);
        }
        return id;
    }

    @Override
    public void obfuscateIdent(String ident) {
        obfuscator.obfuscateIdent(ident);
    }

//    @Override
//    public void obfus_arg(String arg) {
//        Obfuscator.reAssign()
//    }
}
