package zerql;

import java.util.List;

public final class Main {
    private static final int[] SIZES = {5_000, 50_000, 250_000};

    private Main() {
    }

    public static void main(String[] args) {
        for (int size : SIZES) {
            List<Query> queries = QueryGenerator.generate(size, size);
            System.out.println("Размер коллекции: " + size);
            printMeasurement("Итерационный цикл", queries, QueryStatisticsCalculator::iterative);
            printMeasurement("Stream API и стандартные коллекторы", queries, QueryStatisticsCalculator::standardStream);
            printMeasurement("Stream API и собственный коллектор", queries, QueryStatisticsCalculator::customStream);
            System.out.println();
        }
    }

    private static void printMeasurement(
            String methodName,
            List<Query> queries,
            java.util.function.Function<List<Query>, QueryStatistics> calculator) {
        long start = System.nanoTime();
        QueryStatistics statistics = calculator.apply(queries);
        long end = System.nanoTime();
        System.out.printf(
                "%s: %d нс, запросов=%d, средняя длина=%.2f, языки=%s, приоритетных=%d%n",
                methodName,
                end - start,
                statistics.queryCount(),
                statistics.averageTextLength(),
                statistics.languageCounts(),
                statistics.highPriorityQueryCount());
    }
}
