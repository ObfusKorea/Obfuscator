package obfusListener;

import listener.main.Obfuscator;

public class MBA_Listener extends Listener {

    public MBA_Listener(int count) {
        super(count);
    }

    // s1 + s2에 대한 입력에 대하여 난독화하여 리턴핟록 오버라이드
    @Override
    public String applyObfus_binary(String s1, String op, String s2){
        return Obfuscator.MBAExp(s1, s2, op);
    }
}
