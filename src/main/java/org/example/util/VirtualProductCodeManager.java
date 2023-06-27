package org.example.util;

import java.util.HashSet;
import java.util.Set;

public class VirtualProductCodeManager {
    private static VirtualProductCodeManager instance;
    private static Set<String> usedCodes;

    public static synchronized VirtualProductCodeManager getInstance() {
        usedCodes = new HashSet<>();
        if (instance == null) {
            instance = new VirtualProductCodeManager();
        }
        return instance;
    }

    public synchronized void useCode(String code) {
        usedCodes.add(code);
    }

    public boolean isCodeUsed(String code) {
        return usedCodes.contains(code);
    }
}
