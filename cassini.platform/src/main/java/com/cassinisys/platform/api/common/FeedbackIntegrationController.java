package com.cassinisys.platform.api.common;


import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.service.common.FeedbackIntegrationService;
import com.cassinisys.platform.util.Feedback;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;


@RestController
@RequestMapping("/common/feedback")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
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

	/*@RequestMapping(method = RequestMethod.GET)
	public Page<Feedback> getAll(PageRequest page) {
		Pageable pageable = pageRequestConverter.convert(page);
		return null;//feedbackService.findAll(pageable);
	}*/
	@RequestMapping(value = "/getAllOpenTickets/report/{reportId}/page/{pageNumber}", method = RequestMethod.GET)
	public List<Feedback> getAllOpenTickets(@PathVariable("reportId") String reportId, @PathVariable("pageNumber") Integer pageNumber) {
		return feedbackService.getAllOpenTickets(reportId, pageNumber);
	}

	@RequestMapping(value = "/getAllClosedTickets/report/{reportId}/page/{pageNumber}", method = RequestMethod.GET)
	public List<Feedback> getAllClosedTickets(@PathVariable("reportId") String reportId, @PathVariable("pageNumber") Integer pageNumber) {
		return feedbackService.getAllClosedTickets(reportId,pageNumber);
	}


}
