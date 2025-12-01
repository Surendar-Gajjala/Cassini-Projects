package com.cassinisys.platform.util;

/**
 * @author reddy
 */
public class PageRequest {

	private Integer page = 0;
	private Integer size = 20;
	private String sort;

	public PageRequest() {
	}

	public PageRequest(Integer page) {
		this.page = page;
	}

	public PageRequest(Integer page, Integer size) {
		this(page);
		this.size = size;
	}

	public PageRequest(Integer page, Integer size, String sort) {
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
