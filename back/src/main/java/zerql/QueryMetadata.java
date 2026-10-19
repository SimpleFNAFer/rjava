package zerql;

import java.util.Objects;

public record QueryMetadata(String author, String sourceSystem, int priority) {
    public QueryMetadata {
        Objects.requireNonNull(author);
        Objects.requireNonNull(sourceSystem);
        if (author.isBlank() || sourceSystem.isBlank()) {
            throw new IllegalArgumentException("Author and source system must not be blank");
        }
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("Priority must be between 1 and 5");
        }
    }
}
