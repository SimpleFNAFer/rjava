package zerql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class QueryGenerator {
    private static final QueryLanguage[] LANGUAGES = QueryLanguage.values();
    private static final String[] AUTHORS = {"Alice", "Bob", "Carol", "David"};
    private static final String[] SYSTEMS = {"billing", "analytics", "support", "warehouse"};
    private static final String[] FIELDS = {"id", "name", "status", "created_at", "amount", "region"};

    private QueryGenerator() {
    }

    public static List<Query> generate(int count, long seed) {
        if (count < 0) {
            throw new IllegalArgumentException("Count must not be negative");
        }
        Random random = new Random(seed);
        List<Query> queries = new ArrayList<>(count);
        for (int index = 0; index < count; index++) {
            queries.add(createQuery(index + 1L, random));
        }
        return queries;
    }

    private static Query createQuery(long id, Random random) {
        QueryLanguage language = LANGUAGES[random.nextInt(LANGUAGES.length)];
        QueryLanguage targetLanguage = LANGUAGES[random.nextInt(LANGUAGES.length)];
        List<String> fields = createFields(random);
        String text = createText(language, fields);
        QueryMetadata metadata = new QueryMetadata(
                AUTHORS[random.nextInt(AUTHORS.length)],
                SYSTEMS[random.nextInt(SYSTEMS.length)],
                random.nextInt(5) + 1);
        TranslationOptions options = new TranslationOptions(
                random.nextBoolean(),
                random.nextBoolean());
        return new Query(
                id,
                text,
                LocalDateTime.of(2024, 1, 1, 0, 0).plusMinutes(random.nextInt(1_000_000)),
                language,
                new TranslationTarget(targetLanguage, targetLanguage.name().toLowerCase()),
                metadata,
                fields,
                options);
    }

    private static List<String> createFields(Random random) {
        int fieldCount = random.nextInt(FIELDS.length) + 1;
        List<String> fields = new ArrayList<>(fieldCount);
        for (int index = 0; index < fieldCount; index++) {
            fields.add(FIELDS[random.nextInt(FIELDS.length)]);
        }
        return fields;
    }

    private static String createText(QueryLanguage language, List<String> fields) {
        return switch (language) {
            case SQL -> "SELECT " + String.join(", ", fields) + " FROM events";
            case EQL -> "FROM events | KEEP " + String.join(", ", fields);
            case KQL -> "events | project " + String.join(", ", fields);
        };
    }
}
