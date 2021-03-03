package hu.progmasters.fundraiser.dto.fund;

import java.util.List;

public class EnumData {

    private final List<CategoryOption> categoryOptions;

    private final List<StatusOption> statusOptions;

    public EnumData(List<CategoryOption> categoryOptions, List<StatusOption> statusOptions) {
        this.categoryOptions = categoryOptions;
        this.statusOptions = statusOptions;
    }

    public List<CategoryOption> getCategoryOptions() {
        return categoryOptions;
    }

    public List<StatusOption> getStatusOptions() {
        return statusOptions;
    }
}
