-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# https://developer.android.com/jetpack/androidx/releases/compose-runtime#1.2.0-alpha03
-assumenosideeffects public class androidx.compose.runtime.ComposerKt {
        boolean isTraceInProgress();
        void traceEventStart(int,java.lang.String);
        void traceEventEnd();
}