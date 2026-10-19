package zerql;

import java.util.Map;

public record QueryStatistics(
        long queryCount,
        long totalTextLength,
        double averageTextLength,
        Map<QueryLanguage, Long> languageCounts,
        long highPriorityQueryCount) {
    public QueryStatistics {
        languageCounts = Map.copyOf(languageCounts);
    }
}
