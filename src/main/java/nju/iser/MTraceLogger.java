package nju.iser;

public class MTraceLogger {


    private static final String WRITE = "W";
    private static final String READ = "R";

    public static String getPath() {
        return MTraceLogger.class.getCanonicalName().replace(".", "/");
    }

    public static void logVisitField(boolean isRead, String owner, String name, String desc) {
        String operation = isRead ? READ : WRITE;
        String fullFieldName = String.join("/", owner, name).replace("/", ".");
        log(operation, System.identityHashCode(owner + name + desc), fullFieldName);
    }

    public static void logXALOAD(Object ref, int index) {
        log(READ, System.identityHashCode(ref.hashCode() + index), convertName(ref, index));
    }

    public static void logXASTORE(Object ref, int index) {
        log(WRITE, System.identityHashCode(ref.hashCode() + index), convertName(ref, index));
    }

    private static String convertName(Object arrayRef, int index) {
        String canonicalName = arrayRef.getClass().getCanonicalName();
        String componentName = canonicalName.substring(0, canonicalName.lastIndexOf('['));
        return String.format("%s[%d]", componentName, index);
    }

    private static void log(String operation, int hashCode, String name) {
        System.out.printf("%s %d %016x %s\n", operation, Thread.currentThread().getId(), hashCode, name);
    }
}
