package co.techmagic.hr.presentation.ui.view.timetable;

import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class EmployeePlanItem extends AbstractGridItem {

    private String employeeName, projectName;
    private String planName = "-";
    private TimeRange timeRange;

    public EmployeePlanItem() {}

    public EmployeePlanItem(String employeeName, String projectName, Date planStart, Date planEnd) {
        this.employeeName = employeeName;
        this.projectName = projectName;
        this.timeRange = new TimeRange(planStart, planEnd);
    }

    public static EmployeePlanItem generateSample() {
        final String[] firstNameSamples = {"Kristeen", "Carran", "Lillie", "Marje", "Edith", "Steve", "Henry", "Kyle", "Terrence"};
        final String[] lastNameSamples = {"Woodham", "Boatwright", "Lovel", "Dennel", "Wilkerson", "Irvin", "Aston", "Presley"};
        final String[] projectNames = {"Roof Renovation", "Mall Construction", "Demolition old Hallway"};

        // Generate a tvMonthAndDate range between now and 30 days
        Random rand = new Random();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        int r1 = -rand.nextInt(12);
        int r2 = rand.nextInt(12);
        start.add(Calendar.DATE, r1);
        end.add(Calendar.DATE, r2);

        return new EmployeePlanItem(firstNameSamples[rand.nextInt(firstNameSamples.length)] + " " +
                lastNameSamples[rand.nextInt(lastNameSamples.length)],
                projectNames[rand.nextInt(projectNames.length)],
                start.getTime(),
                end.getTime());
    }

    @Nullable
    @Override
    public TimeRange getTimeRange() {
        return timeRange;
    }

    @Override
    public String getName() {
        return planName;
    }

    @Override
    public String getPersonName() {
        return employeeName;
    }
}