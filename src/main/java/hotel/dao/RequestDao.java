package hotel.dao;

import hotel.dto.list.RequestsDto;
import hotel.exception.DataProcessingException;
import hotel.model.Request;
import java.util.Optional;

public interface RequestDao {
    RequestsDto getAllDto(int startRecord, int recordsPerPage, boolean isProcessed)
            throws DataProcessingException;

    RequestsDto getAllDtoByCustomerId(Long customerId, int startRecord,
                                      int recordsPerPage, boolean isProcessed)
            throws DataProcessingException;

    Optional<Request> get(Long bookingRequestId) throws DataProcessingException;

    int getNumberOfBookingRequestsPerCustomer(Long customerId) throws DataProcessingException;

    int getNumberOfBookingRequests() throws DataProcessingException;

    Request create(Request request) throws DataProcessingException;

    boolean delete(Long bookingRequestId) throws DataProcessingException;

    void setIsProcessing(long requestId) throws DataProcessingException;
}
