package hotel.dto.list;

import hotel.dto.ScheduleDto;
import java.util.List;

public class SchedulesDto {
    private List<ScheduleDto> schedules;
    private int numberOfSchedules;

    public List<ScheduleDto> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleDto> schedules) {
        this.schedules = schedules;
    }

    public int getNumberOfSchedules() {
        return numberOfSchedules;
    }

    public void setNumberOfSchedules(int numberOfSchedules) {
        this.numberOfSchedules = numberOfSchedules;
    }
}
