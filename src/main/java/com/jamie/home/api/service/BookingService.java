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

import java.text.SimpleDateFormat;
import java.util.Date;
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
        BOOKING result = bookingDao.getBooking(booking);
        setDetailInfo(result);
        return result;
    }

    private void setDetailInfo(BOOKING booking) {
        INTERPRETER interpreter = interDao.getInterpreter(new INTERPRETER(booking.getInterpreter()));
        interpreter.getOther_info().put("member_info",memberDao.getMember(new MEMBER(interpreter.getMember())));
        booking.getOther_info().put("interpreter_info", interpreter);
        booking.getOther_info().put("member_info", memberDao.getMember(new MEMBER(booking.getMember())));
    }

    public Integer save(BOOKING booking) {
        Integer result = bookingDao.insertBooking(booking);

        INTERPRETER interpreter = interDao.getInterpreter(new INTERPRETER(booking.getInterpreter()));
        MEMBER member = memberDao.getMember(new MEMBER(booking.getMember()));
        // 알림 TYPE booking_booking : 예약 완료
        memberDao.insertMemberInfo(new INFO(interpreter.getMember(), booking.getBooking(), "booking_booking", "새로운 예약이 신청되었습니다.",member.getName()+"님이 예약 신청하였습니다."));

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
        }
        return bookingDao.updateBooking(booking);
    }

    public Integer remove(BOOKING booking) {
        return bookingDao.deleteBooking(booking);
    }

    public REPORT getReport(REPORT report) {
        REPORT result = bookingDao.getBookingReport(report);
        setDetailReportInfo(result);
        return result;
    }

    private void setDetailReportInfo(REPORT report) {
        BOOKING booking = bookingDao.getBooking(new BOOKING(report.getBooking()));
        setDetailInfo(booking);
        report.getOther_info().put("booking_info", booking);
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

    public List<REPORT> listReport(SEARCH search) {
        List<REPORT> result = bookingDao.getListBookingReport(search);
        for(int i=0; i<result.size(); i++){
            setDetailReportInfo(result.get(i));
        }
        return result;
    }

    public Integer listReportCnt(SEARCH search) {
        return bookingDao.getListBookingReportCnt(search);
    }

    public Integer modifyReportStop(REPORT report, INTERPRETER interpreter) {
        Integer result = bookingDao.updateBookingReport(report);
        INTERPRETER ori_interpreter = interDao.getInterpreter(interpreter);
        interpreter.setState((Integer) report.getOther_info().get("state"));
        interpreter.setStop_date(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        // 알림 TYPE interpreter_reject : 통역사 정지
        memberDao.insertMemberInfo(new INFO(ori_interpreter.getMember(), ori_interpreter.getMember(), "interpreter_stop", "통역사 자격이 정지되었습니다.","7일간 통역사 활동을 할 수 없습니다."));
        interDao.updateInterpreter(interpreter);
        return result;
    }

    public Integer modifyReportRemove(REPORT report, INTERPRETER interpreter) {
        Integer result = bookingDao.updateBookingReport(report);
        interDao.deleteInterpreter(interpreter);
        return result;
    }
}
