package demo.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.niulx.spring.framework.annotation.Service;
import demo.service.IQueryService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Service
@Slf4j
public class QueryService implements IQueryService {

	public String query(String name) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
		log.info("this is ：" + json);
		return json;
	}

}
