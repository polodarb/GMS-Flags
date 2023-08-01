package ua.polodarb.gmsflags;

interface IRootDatabase {
    List<String> getGmsPackages();
    List<String> getBoolFlags(String pkgName);
    List<String> getIntFlags(String pkgName);
    List<String> getFloatFlags(String pkgName);
    List<String> getStringFlags(String pkgName);
}