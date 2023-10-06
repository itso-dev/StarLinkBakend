package com.jamie.home.api.service;

import com.jamie.home.api.model.BOOKING;
import com.jamie.home.api.model.INFO;
import com.jamie.home.api.model.INTERPRETER;
import com.jamie.home.api.model.REPORT;
import com.jamie.home.api.model.common.FIELD;
import com.jamie.home.api.model.common.MEMBER;
import com.jamie.home.api.model.common.SEARCH;
import com.jamie.home.util.FileUtils;
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
        BOOKING ori_booking = bookingDao.getBooking(booking);
        // 상태값 변경에 따른 알림
        FIELD param = new FIELD();
        param.setType_name("booking_state");
        param.setValue("cancel");
        FIELD cancelField = fieldDao.getField(param);
        param.setValue("booking");
        FIELD bookingField = fieldDao.getField(param);

        if(booking.getState() == cancelField.getField()){
            INTERPRETER interpreter = interDao.getInterpreter(new INTERPRETER(ori_booking.getInterpreter()));
            MEMBER member = memberDao.getMember(new MEMBER(ori_booking.getMember()));
            // 알림 TYPE booking_cancel : 예약 취소
            memberDao.insertMemberInfo(new INFO(interpreter.getMember(), booking.getBooking(), "booking_cancel", "예약이 취소되었습니다.",member.getName()+"님의 예약이 취소되었습니다."));
        } else if(booking.getState() == bookingField.getField()){
            INTERPRETER interpreter = interDao.getInterpreter(new INTERPRETER(ori_booking.getInterpreter()));
            MEMBER member = memberDao.getMember(new MEMBER(ori_booking.getMember()));
            // 알림 TYPE booking_booking : 예약 완료
            memberDao.insertMemberInfo(new INFO(interpreter.getMember(), booking.getBooking(), "booking_booking", "새로운 예약이 신청되었습니다.",member.getName()+"님이 예약 신청하였습니다."));
        }
        return bookingDao.updateBooking(booking);
    }

    public Integer remove(BOOKING booking) {
        return bookingDao.deleteBooking(booking);
    }

    public REPORT getReport(REPORT report) {
        return bookingDao.getBookingReport(report);
    }

    public Integer saveReport(REPORT report) {
        report.setFiles(
                FileUtils.saveFiles(
                        report.getFiles_new(),
                        uploadDir
                )
        );
        return bookingDao.insertBookingReport(report);
    }

    public Integer modifyReport(REPORT report) {
        REPORT ori_report = bookingDao.getBookingReport(report);
        try {
            report.setFiles(
                    FileUtils.modiFiles(
                            ori_report.getFiles(),
                            report.getFiles_del(),
                            report.getFiles_new(),
                            uploadDir
                    )
            );
        } catch (Exception e) {
            report.setFiles(null);
        }
        return bookingDao.updateBookingReport(report);
    }
}
