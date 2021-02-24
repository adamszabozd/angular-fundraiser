package hu.progmasters.fundraiser.dto.fund;

public class StatusOption {


    private final String name;
    private final String displayName;

    public StatusOption(String name, String displayName) {
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
