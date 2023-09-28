package Utils;

import java.time.Duration;

public class Utils {

    public static String printMillTime(long milliseconds) {
        Duration duration = Duration.ofMillis(milliseconds);
        long HH = duration.toHours();
        long MM = duration.toMinutesPart();
        long SS = duration.toSecondsPart();
        long MS = duration.toMillisPart();

        return String.format("%02dh %02dm %02ds %04dms", HH, MM, SS,MS);
    }
}
