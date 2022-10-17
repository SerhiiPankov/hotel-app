package hotel.service;

import hotel.dto.list.RequestsDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.model.Request;

public interface RequestService {
    RequestsDto getAllDto(int page, int recordsPerPage, boolean isProcessed)
            throws DataProcessingException;

    RequestsDto getAllDtoByCustomerId(Long customerId, int page,
                                      int recordsPerPage, boolean isProcessed)
            throws DataProcessingException;

    Request get(Long id) throws DataProcessingException, WrongDataException;

    int getNumberOfBookingRequestsPerCustomer(Long customerId) throws DataProcessingException;

    int getNumberOfBookingRequests() throws DataProcessingException;

    Request create(Request request) throws DataProcessingException;

    boolean delete(Long id) throws DataProcessingException;

    void setIsProcessing(long requestId) throws DataProcessingException;
}
