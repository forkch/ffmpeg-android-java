package com.github.hiteshsondhi88.libffmpeg;

import android.os.Build;

class CpuArchHelper {

    @SuppressWarnings("deprecation") static CpuArch getCpuArch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (String abi : Build.SUPPORTED_ABIS) {
                CpuArch arch = checkABI(abi);
                if (arch != CpuArch.NONE) return arch;
            }
        } else {
            CpuArch arch = checkABI(Build.CPU_ABI);
            if (arch != CpuArch.NONE) return arch;
            return checkABI(Build.CPU_ABI2);
        }
        return CpuArch.NONE;
    }

    private static CpuArch checkABI(String abi) {
        if (abi.equals(getx86CpuAbi())) {
            return CpuArch.x86;
        } else {
            // check if device is armeabi
            if (abi.equals(getArmeabiv7CpuAbi())) {
                ArmArchHelper cpuNativeArchHelper = new ArmArchHelper();
                String archInfo = cpuNativeArchHelper.cpuArchFromJNI();
                // check if device is arm v7
                if (cpuNativeArchHelper.isARM_v7_CPU(archInfo)) {
                    // check if device is neon
                    if (cpuNativeArchHelper.isNeonSupported(archInfo)) {
                        return CpuArch.ARMv7_NEON;
                    }
                    return CpuArch.ARMv7;
                }
            }
        }
        return CpuArch.NONE;
    }

    static String getx86CpuAbi() {
        return "x86";
    }

    static String getArmeabiv7CpuAbi() {
        return "armeabi-v7a";
    }
}
