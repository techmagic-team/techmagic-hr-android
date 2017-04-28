package co.techmagic.hr.presentation.ui.adapter.calendar;


public interface IGuideXItem {

    void setHoliday(boolean holiday);

    boolean isHoliday();

    void setVacation(boolean vacation);

    boolean isVacation();

    void setDayOff();

    boolean isDayOff();

    void setIllness(boolean illness);

    boolean isIllness();

    void setRequested(boolean requested);

    boolean isRequested();
}
