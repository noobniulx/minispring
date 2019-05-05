package demo.service.impl;

import com.niulx.spring.framework.annotation.Service;
import demo.service.IModifyService;

@Service
public class ModifyService implements IModifyService {

	/**
	 * add
	 */
	public String add(String name,String addr) throws Exception {
		throw new Exception("this is a willfully！！");
		//return "modifyService add,name=" + name + ",addr=" + addr;
	}

	/**
	 * edit
	 */
	public String edit(Integer id,String name) {
		return "modifyService edit,id=" + id + ",name=" + name;
	}

	/**
	 * remove
	 */
	public String remove(Integer id) {
		return "modifyService id=" + id;
	}
	
}
