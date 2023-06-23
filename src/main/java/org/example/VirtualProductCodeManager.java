package org.example;

import java.util.HashSet;
import java.util.Set;

public class VirtualProductCodeManager {
    private static VirtualProductCodeManager instance;
    private Set<String> usedCodes;

    private VirtualProductCodeManager() {
        usedCodes = new HashSet<>();
    }

    public static synchronized VirtualProductCodeManager getInstance() {
        if (instance == null) {
            instance = new VirtualProductCodeManager();
        }
        return instance;
    }

    public void useCode(String code) {
        usedCodes.add(code);
    }

    public boolean isCodeUsed(String code) {
        return usedCodes.contains(code);
    }
}
