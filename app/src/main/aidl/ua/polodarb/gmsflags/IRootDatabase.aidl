package ua.polodarb.gmsflags;

interface IRootDatabase {
    Map<String, String> getGmsPackages();
    Map<String, String> getBoolFlags(String pkgName);
    Map<String, String> getIntFlags(String pkgName);
    Map<String, String> getFloatFlags(String pkgName);
    Map<String, String> getStringFlags(String pkgName);

    String androidPackage(String pkgName);
    List<String> getUsers();

    void deleteRowByFlagName(String packageName, String name);

    void overrideFlag(
    String packageName,
    String user,
    String name,
    String flagType,
    String intVal,
    String boolVal,
    String floatVal,
    String stringVal,
    String extensionVal,
    String committed
    );
}