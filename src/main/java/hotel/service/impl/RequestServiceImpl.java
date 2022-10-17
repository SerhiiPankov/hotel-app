package hotel.service.impl;

import hotel.dao.RequestDao;
import hotel.dto.list.RequestsDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Inject;
import hotel.lib.Service;
import hotel.model.Request;
import hotel.service.RequestService;

@Service
public class RequestServiceImpl implements RequestService {
    @Inject
    private RequestDao requestDao;

    @Override
    public RequestsDto getAllDto(int page, int recordsPerPage, boolean isProcessed)
            throws DataProcessingException {
        int startRecord = (page - 1) * recordsPerPage;
        return requestDao.getAllDto(startRecord, recordsPerPage, isProcessed);
    }

    @Override
    public RequestsDto getAllDtoByCustomerId(Long customerId, int page,
                                             int recordsPerPage, boolean isProcessed)
            throws DataProcessingException {
        int startRecord = (page - 1) * recordsPerPage;
        return requestDao.getAllDtoByCustomerId(
                customerId, startRecord, recordsPerPage, isProcessed);
    }

    @Override
    public Request get(Long bookingRequestId)
            throws DataProcessingException, WrongDataException {
        return requestDao.get(bookingRequestId).orElseThrow(() ->
                new WrongDataException("Can't get booking request with id: " + bookingRequestId));
    }

    @Override
    public int getNumberOfBookingRequestsPerCustomer(Long customerId)
            throws DataProcessingException {
        return requestDao.getNumberOfBookingRequestsPerCustomer(customerId);
    }

    @Override
    public int getNumberOfBookingRequests() throws DataProcessingException {
        return requestDao.getNumberOfBookingRequests();
    }

    @Override
    public Request create(Request request) throws DataProcessingException {
        return requestDao.create(request);
    }

    @Override
    public boolean delete(Long id) throws DataProcessingException {
        return requestDao.delete(id);
    }

    @Override
    public void setIsProcessing(long requestId) throws DataProcessingException {
        requestDao.setIsProcessing(requestId);
    }
}
