package hu.progmasters.fundraiser.dto.fund;

public class CategoryOption {

    private final String name;

    private final String displayName;

    public CategoryOption(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
