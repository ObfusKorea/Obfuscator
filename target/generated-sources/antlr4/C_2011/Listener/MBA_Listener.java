package C_2011.Listener;

import listener.main.Obfuscator;

public class MBA_Listener extends Listener {

    public MBA_Listener(int count) {
        super(count);
    }

    // s1 + s2에 대한 입력에 대하여 난독화하여 리턴핟록 오버라이드
    @Override
    public String Obfus_AdditiveExp(String additive, String op, String multi) {
        return Obfuscator.MBAExp(additive, multi, op);
    }
}
