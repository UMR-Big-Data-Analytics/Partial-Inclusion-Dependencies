package Algorithms;

import Utils.pIND;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface Solver {

    default Map<String, String> parseArgs(String[] args) {
        // TODO: 28/10/2023 implement default args parser
        return null;
    }

    default void safeResult(List<pIND> pINDs, Path p, boolean append) {
        // TODO: 28/10/2023 implement output writing
    }
}
