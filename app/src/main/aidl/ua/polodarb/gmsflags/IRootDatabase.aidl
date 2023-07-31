package ua.polodarb.gmsflags;

interface IRootDatabase {
    List<String> getGmsPackages();
    List<String> getBoolFlags(String pkgName);
}