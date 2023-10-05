package com.jamie.home.api.controller;

import com.jamie.home.api.model.BOOKING;
import com.jamie.home.api.model.REPORT;
import com.jamie.home.api.model.common.ResponseOverlays;
import com.jamie.home.api.model.common.SEARCH;
import com.jamie.home.api.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/booking/*")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    @RequestMapping(value="/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
        try {
            if(search.getPage() != null && search.getPage_block() != null){
                search.calStart();
            }
            List<BOOKING> list = bookingService.list(search);
            if(list != null){
                Integer cnt = bookingService.listCnt(search);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_BOOKING_SUCCESS", list, cnt);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BOOKING_NULL", null,0);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BOOKING_FAIL", false,0);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.GET)
    public ResponseOverlays get(@PathVariable("key") int key) {
        try {
            BOOKING result = bookingService.get(new BOOKING(key));
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_BOOKING_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BOOKING_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BOOKING_FAIL", null);
        }
    }

    @RequestMapping(value="/save", method= RequestMethod.POST)
    public ResponseOverlays save(@Validated @RequestBody BOOKING booking) {
        try {
            int result = bookingService.save(booking);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BOOKING_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_BOOKING_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BOOKING_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.PUT)
    public ResponseOverlays modify(@PathVariable("key") int key, @Validated @RequestBody BOOKING booking) {
        try {
            booking.setBooking(key);
            int result = bookingService.modify(booking);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BOOKING_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_BOOKING_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BOOKING_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.DELETE)
    public ResponseOverlays remove(@PathVariable("key") int key) {
        try {
            int result = bookingService.remove(new BOOKING(key));
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BOOKING_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_BOOKING_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BOOKING_FAIL", false);
        }
    }

    @RequestMapping(value="/report/{key}", method= RequestMethod.GET)
    public ResponseOverlays getReport(@PathVariable("key") int key) {
        try {
            REPORT result = bookingService.getReport(new REPORT(key));
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_BOOKING_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BOOKING_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BOOKING_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}/report/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays saveReport(@PathVariable("key") int key, @Validated @ModelAttribute REPORT report) {
        try {
            report.setBooking(key);
            int result = bookingService.saveReport(report);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BOOKING_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_BOOKING_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BOOKING_FAIL", false);
        }
    }

    @RequestMapping(value="/report/{key}", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays modifyReport(@PathVariable("key") int key, @Validated @ModelAttribute REPORT report) {
        try {
            report.setBooking_report(key);
            int result = bookingService.modifyReport(report);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BOOKING_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_BOOKING_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BOOKING_FAIL", false);
        }
    }
}