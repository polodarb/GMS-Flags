package ua.polodarb.gmsflags;

interface IRootDatabase {
    Map<String, String> getGmsPackages();
    List<String> getGooglePackages();

    Map<String, String> getBoolFlags(String pkgName);
    Map<String, String> getIntFlags(String pkgName);
    Map<String, String> getFloatFlags(String pkgName);
    Map<String, String> getStringFlags(String pkgName);

    Map<String, String> getOverriddenBoolFlags(String pkgName);
    Map<String, String> getOverriddenIntFlags(String pkgName);
    Map<String, String> getOverriddenFloatFlags(String pkgName);
    Map<String, String> getOverriddenStringFlags(String pkgName);

    List<String> getListByPackages(String pkgName);

    String androidPackage(String pkgName);
    List<String> getUsers();

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