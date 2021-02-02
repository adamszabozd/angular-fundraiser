package hu.progmasters.fundraiser.dto;

public class TargetAccountOption {

    private long id;
    private String goal;

    public TargetAccountOption(AccountDetails accountDetails) {
        this.id = accountDetails.getId();
 //       this.goal = accountDetails.getGoal();
    }

    public long getId() {
        return id;
    }

    public String getGoal() {
        return goal;
    }
}
