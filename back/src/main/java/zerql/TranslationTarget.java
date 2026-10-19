package zerql;

import java.util.Objects;

public final class TranslationTarget {
    private final QueryLanguage language;
    private final String dialect;

    public TranslationTarget(QueryLanguage language, String dialect) {
        this.language = Objects.requireNonNull(language);
        if (dialect == null || dialect.isBlank()) {
            throw new IllegalArgumentException("Dialect must not be blank");
        }
        this.dialect = dialect;
    }

    public QueryLanguage language() {
        return language;
    }

    public String dialect() {
        return dialect;
    }
}
