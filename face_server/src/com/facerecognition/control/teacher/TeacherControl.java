package com.facerecognition.control.teacher;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.facerecognition.service.teacher.TeacherServiceDAO;
import com.framework.control.BaseControl;
@Controller(value="TeacherControl")
//这里还可以添加一个map来增加映射！
@RequestMapping(value = "/teacher")
public class TeacherControl extends BaseControl {
	
	@Autowired   //这样会自动实例化下面的对象
	private TeacherServiceDAO teacherService ; 
	
	@RequestMapping(value = "/login.json")
	@ModelAttribute("TeacherUserMap")
	//最后返回的整个结果是以一个键值对形式的JSONObject数据，其中这个键就是这里括号的String
	public Map<String,Object> checkLogin(@RequestParam Map<String, Object> paraMap) {
		Map<String,Object> result=null;
		try {
			result=teacherService.checkLogin(paraMap);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return result;
	}
	
	@RequestMapping(value = "/insertteacher.do")
	@ResponseBody
	public Integer insertTeacher(@RequestParam Map<String, Object> paraMap){
		Integer result = null;
		try {
			result=teacherService.insertTeacher(paraMap);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return result;
	}
	
	@RequestMapping(value="/findallteacher.json")
	@ModelAttribute("TeacherListMap")
	public List<Map<String, Object>> findAllTeacher(@RequestParam Map<String, Object> paraMap){
		List<Map<String, Object>> result = null;
		try {
			result = teacherService.findAllTeacher(paraMap);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
