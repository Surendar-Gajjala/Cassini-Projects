package com.cassinisys.is.api.store;

import com.cassinisys.is.filtering.ISLoanCriteria;
import com.cassinisys.is.model.store.ISLoan;
import com.cassinisys.is.model.store.ISLoanIssueItem;
import com.cassinisys.is.model.store.ISLoanReturnItemIssued;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.is.service.store.ISLoanService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by swapna on 10/08/18.
 */
@RestController
@RequestMapping("is/stores/{storeId}/loans")
public class ISLoanController extends BaseController {

    @Autowired
    private ISLoanService loanService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    private ISLoan createLoan(@RequestBody ISLoan isLoan) {
        return loanService.createLoan(isLoan);
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISLoan> getAllLoans(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return loanService.getAllLoans(pageable);
    }

    @RequestMapping(value = "/issued", method = RequestMethod.GET)
    private List<ISLoan> getLoansIssuedByStore(@PathVariable("storeId") Integer storeId) {
        return loanService.getLoansIssuedByStore(storeId);
    }

    @RequestMapping(value = "issued/pageable", method = RequestMethod.GET)
    public Page<ISLoan> getPagedLoansIssued(@PathVariable("storeId") Integer storeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return loanService.getPagedLoansIssued(storeId, pageable);
    }

    @RequestMapping(value = "received/pageable", method = RequestMethod.GET)
    public Page<ISLoan> getPagedLoansReceived(@PathVariable("storeId") Integer storeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return loanService.getPagedLoansReceived(storeId, pageable);
    }

    @RequestMapping(value = "/received", method = RequestMethod.GET)
    private List<ISLoan> getLoansReceivedByStore(@PathVariable("storeId") Integer storeId) {
        return loanService.getLoansReceivedByStore(storeId);
    }

    @RequestMapping(value = "/{loanId}", method = RequestMethod.GET)
    private ISLoan getLoanById(@PathVariable("loanId") Integer loanId) {
        return loanService.getLoanById(loanId);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ISLoan update(@RequestBody ISLoan isLoan) {
        return loanService.update(isLoan);
    }

    @RequestMapping(value = "/items/toStore/{toStore}/toProject/{toProject}", method = RequestMethod.POST)
    private void createLoanIssueItems(@PathVariable("storeId") Integer fromStore, @PathVariable("toStore") Integer toStore,
                                      @PathVariable("toProject") Integer toProject, @RequestBody List<ISLoanIssueItem> loanIssueItems) {
        loanService.createLoanIssueItems(fromStore, toStore, toProject, loanIssueItems);
    }

    @RequestMapping(value = "/{loanId}/loanItems/pageable", method = RequestMethod.GET)
    private Page<ISLoanIssueItem> getLoanItems(@PathVariable("loanId") Integer loanId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        List<ISLoanIssueItem> loanIssueItemList = loanService.getLoanItems(loanId);
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > loanIssueItemList.size() ? loanIssueItemList.size() : (start + pageable.getPageSize());
        return new PageImpl<ISLoanIssueItem>(loanIssueItemList.subList(start, end), pageable, loanIssueItemList.size());
    }

    @RequestMapping(value = "/{loanId}/returnItems/pageable", method = RequestMethod.GET)
    private Page<ItemDTO> getLoanReturnItemsDetails(@PathVariable("storeId") Integer storeId, @PathVariable("loanId") Integer loanId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        List<ItemDTO> itemDTOList = loanService.getLoanReturnItemsDetails(storeId, loanId);
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > itemDTOList.size() ? itemDTOList.size() : (start + pageable.getPageSize());
        return new PageImpl<ItemDTO>(itemDTOList.subList(start, end), pageable, itemDTOList.size());
    }

    @RequestMapping(value = "/{loanId}/returnItems", method = RequestMethod.POST)
    private List<ISLoanReturnItemIssued> createLoanReturnItems(@PathVariable("loanId") Integer loanId, @RequestBody List<ISLoanReturnItemIssued> loanReturnItemIssueds) {
        return loanService.createLoanReturnItems(loanId, loanReturnItemIssueds);
    }

    @RequestMapping(value = "/{loanId}/itemHistory/{itemId}", method = RequestMethod.GET)
    private List<ISLoanReturnItemIssued> getLoanReturnItemHistory(@PathVariable("loanId") Integer loanId, @PathVariable("itemId") Integer itemId) {
        return loanService.getLoanReturnItemHistory(loanId, itemId);
    }

    @RequestMapping(value = "/{loanId}/printLoanIssueChallan", method = RequestMethod.GET, produces = "text/html")
    public String printLoanChallan(@PathVariable("loanId") Integer loanId, HttpServletRequest request, HttpServletResponse response) {
        String fileName = loanService.printLoanChallan(loanId, request, response);
        return fileName;
    }

    @RequestMapping(value = "/file/{fileName}/download", method = RequestMethod.GET)
    public void downloadExportFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        loanService.downloadExportFile(fileName, response);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<ISLoan> freeTextSearch(PageRequest pageRequest, ISLoanCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISLoan> roadChalans = loanService.freeTextSearch(pageable, criteria);
        return roadChalans;
    }
}

