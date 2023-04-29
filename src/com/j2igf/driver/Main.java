package com.j2igf.driver;

import com.j2igf.framework.core.J2IGF;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        J2IGF.setTitle("Test");
        J2IGF.setWidth(1280);
        J2IGF.setHeight(720);
        J2IGF.toggleFlags(J2IGF.FLAG_DEBUG_MODE);
        J2IGF.initialize();
        J2IGF.run();
    }
}
