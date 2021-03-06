package com.cable.rest.controller;

import lombok.extern.log4j.Log4j;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cable.rest.dto.PaymentDetailDto;
import com.cable.rest.response.ResponseResource;
import com.cable.rest.search.PaymentSearch;

@RestController
@RequestMapping("/payment")
@Log4j
public class PaymentController extends BaseController {
	
	
	@RequestMapping(value="/savepayment",method=RequestMethod.POST)
	public Object savePayment(@RequestBody PaymentDetailDto reqObject){
		return  sendtoMQ(reqObject, "savePayment", "paymentService");
	}
	
	@RequestMapping(value="/paymentlist",method=RequestMethod.POST)
	public Object getPaymentList(@RequestBody PaymentSearch searchObject){
		return sendtoMQ(searchObject, "getPaymentList", "projectService");
	}
	
	@RequestMapping(value="/deletepayment",method=RequestMethod.POST)
	public ResponseResource deletePayment(@RequestBody PaymentSearch searchObject){
		return (ResponseResource) sendtoMQ(searchObject, "deletePayment", "projectService");
	}
}
