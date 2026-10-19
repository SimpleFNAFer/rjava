package zerql;

import java.time.LocalDateTime;
import java.util.List;

public final class Query {
    private final long id;
    private final String text;
    private final LocalDateTime createdAt;
    private final QueryLanguage sourceLanguage;
    private final TranslationTarget target;
    private final QueryMetadata metadata;
    private final List<String> requestedFields;
    private final TranslationOptions options;

    public Query(
            long id,
            String text,
            LocalDateTime createdAt,
            QueryLanguage sourceLanguage,
            TranslationTarget target,
            QueryMetadata metadata,
            List<String> requestedFields,
            TranslationOptions options) {
        if (id < 1) {
            throw new IllegalArgumentException("Id must be positive");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Query text must not be blank");
        }
        if (createdAt == null || sourceLanguage == null || target == null || metadata == null || options == null) {
            throw new NullPointerException("Query fields must not be null");
        }
        if (requestedFields == null || requestedFields.isEmpty()
                || requestedFields.stream().anyMatch(field -> field == null || field.isBlank())) {
            throw new IllegalArgumentException("Requested fields must contain non-blank values");
        }
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.sourceLanguage = sourceLanguage;
        this.target = target;
        this.metadata = metadata;
        this.requestedFields = List.copyOf(requestedFields);
        this.options = options;
    }

    public long id() {
        return id;
    }

    public String text() {
        return text;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public QueryLanguage sourceLanguage() {
        return sourceLanguage;
    }

    public TranslationTarget target() {
        return target;
    }

    public QueryMetadata metadata() {
        return metadata;
    }

    public List<String> requestedFields() {
        return requestedFields;
    }

    public TranslationOptions options() {
        return options;
    }
}
