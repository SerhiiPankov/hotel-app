package hotel.dto.list;

import hotel.dto.RequestDto;
import java.util.ArrayList;
import java.util.List;

public class RequestsDto {
    private List<RequestDto> requests;
    private int numberOfRequests;

    public RequestsDto() {
        requests = new ArrayList<>();
    }

    public List<RequestDto> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestDto> requests) {
        this.requests = requests;
    }

    public int getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(int numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }
}
