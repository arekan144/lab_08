package carshow;

public enum ItemCondition {
    NEW("Nowy"),USED("Używany"),DAMAGED("Uszkodzony");
    private final String textRep;

    ItemCondition(String textRep) {
        this.textRep = textRep;
    }

    @Override public String toString() {
        return textRep;
    }
}
