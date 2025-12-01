package com.cassinisys.erp.api.common;


import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.service.common.FeedbackIntegrationService;
import com.cassinisys.erp.util.Feedback;


@RestController
@Api(name = "Feedback", description = "Feedback endpoint",group = "COMMON")
@RequestMapping("common/feedback")
public class FeedbackIntegrationController extends BaseController {

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@Autowired
	private FeedbackIntegrationService feedbackService;

	@RequestMapping(method = RequestMethod.POST)
	public Feedback create(@RequestBody Feedback feedback) {
		return feedbackService.create(feedback);

	}

	@RequestMapping(value = "/{ticketId}/attachment",method = RequestMethod.POST)
	public Feedback createTicketAttachment(@PathVariable("ticketId") String ticketId, MultipartHttpServletRequest request) {
		return feedbackService.createTicket(null,ticketId, request.getFileMap());

	}

	@RequestMapping(value = "/{ticketId}", method = RequestMethod.PUT)
	public Feedback update(@PathVariable("ticketId") Integer ticketId,
			@RequestBody Feedback feedback) {
		feedback.setNumber(ticketId);
		return feedbackService.update(feedback);
	}

	@RequestMapping(value = "/{ticketId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("ticketId") Integer ticketId) {
		feedbackService.delete(ticketId);
	}

	@RequestMapping(value = "/{ticketId}", method = RequestMethod.GET)
	public Feedback get(@PathVariable("ticketId") Integer ticketId) {
		return feedbackService.get(ticketId);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Page<Feedback> getAll(PageRequest page) {
		//Pageable pageable = pageRequestConverter.convert(page);
		return null;//feedbackService.findAll(pageable);
	}

	

}
