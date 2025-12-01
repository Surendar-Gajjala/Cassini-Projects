package com.cassinisys.erp.model.api.paging;

/**
 * 
 * @author lakshmi
 *
 */
public class ERPPageRequest {

	private Integer page = 0;
	private Integer size = 20;
	private String sort;

	public ERPPageRequest() {
	}

	public ERPPageRequest(Integer page) {
		this.page = page;
	}

	public ERPPageRequest(Integer page, Integer size) {
		this(page);
		this.size = size;
	}

	public ERPPageRequest(Integer page, Integer size, String sort) {
		this(page, size);
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "Page [page=" + page + ", size=" + size + ", sort=" + sort + "]";
	}

}
