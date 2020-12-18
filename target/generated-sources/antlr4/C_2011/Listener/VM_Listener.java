package C_2011.Listener;

import listener.main.Obfuscator;
public class VM_Listener extends Listener{

    public VM_Listener(int count) {
        super(count);
    }

    @Override
    public String insertINIT(String input){
        String init = Obfuscator.getVMInit();
        return init+input;
    }
}
