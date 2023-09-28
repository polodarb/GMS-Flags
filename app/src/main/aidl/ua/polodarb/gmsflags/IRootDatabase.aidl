package ua.polodarb.gmsflags;

interface IRootDatabase {
    Map<String, String> getGmsPackages();
    List<String> getGooglePackages();

    Map<String, String> getBoolFlags(String pkgName);
    Map<String, String> getIntFlags(String pkgName);
    Map<String, String> getFloatFlags(String pkgName);
    Map<String, String> getStringFlags(String pkgName);

    Map<String, String> getOverriddenBoolFlagsByPackage(String pkgName);
    Map<String, String> getOverriddenIntFlagsByPackage(String pkgName);
    Map<String, String> getOverriddenFloatFlagsByPackage(String pkgName);
    Map<String, String> getOverriddenStringFlagsByPackage(String pkgName);

    Map<String, String> getAllOverriddenBoolFlags();

    List<String> getListByPackages(String pkgName);

    String androidPackage(String pkgName);
    List<String> getUsers();

    void deleteAllOverriddenFlagsFromGMS();
    void deleteAllOverriddenFlagsFromPlayStore();
    void deleteRowByFlagName(String packageName, String name);
    void deleteOverriddenFlagByPackage(String packageName);

    void overrideFlag(
        String packageName,
        String user,
        String name,
        int flagType,
        String intVal,
        String boolVal,
        String floatVal,
        String stringVal,
        String extensionVal,
        int committed
    );
}