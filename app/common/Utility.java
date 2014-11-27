package common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import play.Logger;
import models.filters.FilterBuilder;

public class Utility {
	public static List<Tuple> findBaseModelObject(FilterBuilder fb,
			String orderByColumn, boolean ascending, int pageNumber,
			int pageSize, EntityManager em) {
		TypedQuery<Tuple> tq = em.createQuery(fb.buildeQuery(
				em.getCriteriaBuilder(), orderByColumn, ascending));
		if (pageNumber != -1 && pageSize != -1) {
			pageNumber = pageNumber>0?pageNumber: 1;
			pageSize = pageSize>0?pageSize: 10;
			tq.setMaxResults(pageSize);
			tq.setFirstResult(pageSize * (pageNumber - 1));
		}
		return tq.getResultList();
	}

	public static String getQueryString(String url) {
		String[] splits = url.split("\\?");
		return splits.length > 1 ? splits[1] : "";
	}

	public static String replaceKeyword(String query, String keyword,
			String newKeyword) {
		try {
			return query.replaceFirst("keyword=" + URLEncoder.encode(keyword,"UTF-8"), "keyword=" + URLEncoder.encode(newKeyword,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return query;
		}
	}
	
	public static String getEventbriteDateFormat(Date date){
		Date someDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String s = df.format(someDate);
		return s;
	}

}
