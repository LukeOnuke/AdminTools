package app.admintools.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Version {
    private final String VERSION_NUMBER = "7.1.0";
    /**
     * Snapshot version, only used if isDevelopmentVersion is true
     *
     * Version number goes like 23d12m2020y001v .  So its [Day Of Release]d[Month Of Release]m[Year Of Release]y[Snapshot Number]v
     */
    private final String SNAPSHOT_VER = "17d3m2022y000v";
    private final boolean isDevelopmentVersion = false;
    private static Version instance = null;

    public static Version getInstance() {
        if(instance == null){
            instance = new Version();
        }
        return instance;
    }

    public String getStrippedVersion(){
        return VERSION_NUMBER.substring(0, 5);
    }

    public boolean isDevelopmentVersion() {
        return isDevelopmentVersion;
    }

    public String getSnapshotVersion() {
        return SNAPSHOT_VER;
    }

    public String getFullVersionNumber() {
        return VERSION_NUMBER;
    }

    public static int getVersionAsInt(String versionNumber){
        versionNumber = versionNumber.replace("v", "");
        ArrayList<String> splitVer = new ArrayList<>(Arrays.asList(versionNumber.split("[.]")));
        final AtomicInteger multiplier = new AtomicInteger(10000);
        final AtomicInteger result = new AtomicInteger(0);
        splitVer.forEach((verSegment) -> {
            result.getAndAdd(Integer.parseInt(verSegment) * multiplier.get());
            multiplier.set(multiplier.get() / 10);
        });
        return result.get();
    }
}
