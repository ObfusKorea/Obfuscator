package obfusListener;
import listener.main.Obfuscator;
public class changeType_Listener extends Listener{

    public changeType_Listener(int count){
        super(count);
    }

    @Override
    public String applyObfus_binary(String s1, String op, String s2) {
        return Obfuscator.changingTypes(s1, op, s2);
    }
}
