package zerql;

import java.util.Objects;

public final class TranslationOptions {
    private final boolean preserveCase;
    private final boolean validateResult;

    public TranslationOptions(boolean preserveCase, boolean validateResult) {
        this.preserveCase = preserveCase;
        this.validateResult = validateResult;
    }

    public boolean preserveCase() {
        return preserveCase;
    }

    public boolean validateResult() {
        return validateResult;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TranslationOptions that)) {
            return false;
        }
        return preserveCase == that.preserveCase && validateResult == that.validateResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(preserveCase, validateResult);
    }
}
