package com.mutecsoft.healthvision.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class DatatableUtil {

	public Map<String, Object> makeDatatablePagingMap(PageImpl<?> pageObj, int draw) {
		
		Map<String, Object> resultMap = new HashMap<>();
		
    	resultMap.put("draw", draw);
    	resultMap.put("recordsTotal", pageObj.getTotalElements());
    	resultMap.put("recordsFiltered", pageObj.getTotalElements());
    	resultMap.put("data", pageObj.getContent());
		
		return resultMap;
	}
}
