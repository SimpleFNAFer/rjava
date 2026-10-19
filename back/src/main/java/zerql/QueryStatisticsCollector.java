package zerql;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class QueryStatisticsCollector
        implements Collector<Query, QueryStatisticsCollector.Accumulator, QueryStatistics> {
    @Override
    public Supplier<Accumulator> supplier() {
        return Accumulator::new;
    }

    @Override
    public BiConsumer<Accumulator, Query> accumulator() {
        return Accumulator::add;
    }

    @Override
    public BinaryOperator<Accumulator> combiner() {
        return Accumulator::combine;
    }

    @Override
    public Function<Accumulator, QueryStatistics> finisher() {
        return Accumulator::toStatistics;
    }

    @Override
    public java.util.Set<Characteristics> characteristics() {
        return java.util.Set.of();
    }

    static final class Accumulator {
        private long queryCount;
        private long totalTextLength;
        private long highPriorityQueryCount;
        private final Map<QueryLanguage, Long> languageCounts = new EnumMap<>(QueryLanguage.class);

        private void add(Query query) {
            queryCount++;
            totalTextLength += query.text().length();
            languageCounts.merge(query.sourceLanguage(), 1L, Long::sum);
            if (query.metadata().priority() >= 4) {
                highPriorityQueryCount++;
            }
        }

        private Accumulator combine(Accumulator other) {
            queryCount += other.queryCount;
            totalTextLength += other.totalTextLength;
            highPriorityQueryCount += other.highPriorityQueryCount;
            other.languageCounts.forEach((language, count) -> languageCounts.merge(language, count, Long::sum));
            return this;
        }

        private QueryStatistics toStatistics() {
            double average = queryCount == 0 ? 0 : (double) totalTextLength / queryCount;
            return new QueryStatistics(
                    queryCount,
                    totalTextLength,
                    average,
                    languageCounts,
                    highPriorityQueryCount);
        }
    }
}
