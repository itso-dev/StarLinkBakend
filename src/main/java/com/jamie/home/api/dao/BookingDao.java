package com.jamie.home.api.dao;

import com.jamie.home.api.model.BOOKING;
import com.jamie.home.api.model.common.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookingDao {
    List<BOOKING> getListBooking(SEARCH search);
    Integer getListBookingCnt(SEARCH search);
    BOOKING getBooking(BOOKING booking);
    Integer insertBooking(BOOKING booking);
    Integer updateBooking(BOOKING booking);
    Integer deleteBooking(BOOKING booking);
}
