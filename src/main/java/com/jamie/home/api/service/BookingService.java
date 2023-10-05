package com.jamie.home.api.service;

import com.jamie.home.api.model.BOOKING;
import com.jamie.home.api.model.common.SEARCH;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookingService extends BasicService{
    public List<BOOKING> list(SEARCH search) {
        return bookingDao.getListBooking(search);
    }

    public Integer listCnt(SEARCH search) {
        return bookingDao.getListBookingCnt(search);
    }

    public BOOKING get(BOOKING booking){
        return bookingDao.getBooking(booking);
    }

    public Integer save(BOOKING booking) {
        Integer result = bookingDao.insertBooking(booking);
        // TODO 채팅 생성이 되어야함. 채팅으로 예약이 감.
        return result;
    }

    public Integer modify(BOOKING booking) {
        return bookingDao.updateBooking(booking);
    }

    public Integer remove(BOOKING booking) {
        return bookingDao.deleteBooking(booking);
    }
}
