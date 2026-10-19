package zerql;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class QueryStatisticsCalculator {
    private QueryStatisticsCalculator() {
    }

    public static QueryStatistics iterative(List<Query> queries) {
        long totalTextLength = 0;
        long highPriorityQueryCount = 0;
        Map<QueryLanguage, Long> languageCounts = new EnumMap<>(QueryLanguage.class);
        for (Query query : queries) {
            totalTextLength += query.text().length();
            languageCounts.merge(query.sourceLanguage(), 1L, Long::sum);
            if (query.metadata().priority() >= 4) {
                highPriorityQueryCount++;
            }
        }
        return createStatistics(queries.size(), totalTextLength, languageCounts, highPriorityQueryCount);
    }

    // Teeing collector -> speed up
    public static QueryStatistics standardStream(List<Query> queries) {
        Map<QueryLanguage, Long> languageCounts = queries.stream()
                .collect(Collectors.groupingBy(Query::sourceLanguage, () -> new EnumMap<>(QueryLanguage.class), Collectors.counting()));
        long totalTextLength = queries.stream().mapToLong(query -> query.text().length()).sum();
        long highPriorityQueryCount = queries.stream()
                .filter(query -> query.metadata().priority() >= 4)
                .count();
        return createStatistics(queries.size(), totalTextLength, languageCounts, highPriorityQueryCount);
    }

    public static QueryStatistics customStream(List<Query> queries) {
        return queries.stream().collect(new QueryStatisticsCollector());
    }

    private static QueryStatistics createStatistics(
            long queryCount,
            long totalTextLength,
            Map<QueryLanguage, Long> languageCounts,
            long highPriorityQueryCount) {
        double average = queryCount == 0 ? 0 : (double) totalTextLength / queryCount;
        return new QueryStatistics(queryCount, totalTextLength, average, languageCounts, highPriorityQueryCount);
    }
}
