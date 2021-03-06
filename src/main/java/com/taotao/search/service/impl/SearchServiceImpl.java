package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;
	@Override
	public SearchResult getALlSearch(String queryString, int page, int rows)
			throws Exception {
		//创建查询对象
		SolrQuery query=new SolrQuery();
		
		query.setQuery(queryString);
		//设置分页
		query.setStart((page-1)*rows);
		query.setRows(rows);
		
		//设置默认搜素域
		query.set("df", "item_keywords");
		//设置高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		//执行查询

		SearchResult allSearch = searchDao.getAllSearch(query);
		
		//计算查询结果总页数
		long recordCount = allSearch.getRecordCount();
		/*long pageCount = recordCount / rows;
		if (recordCount % rows > 0) {
			pageCount++;
		}*/
		long pageCount=recordCount % rows > 0?recordCount / rows+1:recordCount / rows;
		allSearch.setPageCount(pageCount);
		allSearch.setCurPage(page);

		return allSearch;
	}

}
