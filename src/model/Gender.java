package model;

public enum Gender {
    MALE("male"),
    FEALE("female");

    private final String text;

    /**
     * @param text
     */
    private Gender(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
