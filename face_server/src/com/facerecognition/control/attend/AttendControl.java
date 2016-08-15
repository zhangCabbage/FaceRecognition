package com.facerecognition.control.attend;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.facerecognition.service.attend.AttendServiceDAO;
import com.framework.control.BaseControl;

@Controller(value="AttendControl")
@RequestMapping(value="/attend")
public class AttendControl extends BaseControl{

	@Autowired
	public AttendServiceDAO attendService;
	
	@RequestMapping(value="/findstuattend.json")
	@ModelAttribute(value="attendList")
	public List< Map<String, Object> > findStuAttend(@RequestParam Map<String, Object> paraMap){
		//验证：
		List< Map<String, Object> > result = null;
		try {
			result = attendService.findStuAttend(paraMap);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@RequestMapping(value="/findallstuofclassattend.json")
	@ModelAttribute(value="attendList")
	public List< Map<String, Object> > findAllStuOfClassAttend(@RequestParam Map<String, Object> paraMap){
		//验证：
		List< Map<String, Object> > result = null;
		try {
			result = attendService.findAllStuOfClassAttend(paraMap);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@RequestMapping(value="/findclassattend.json")
	@ModelAttribute(value="attendList")
	public List< Map<String, Object> > findClassAttend(@RequestParam Map<String, Object> paraMap){
		//验证：
		List< Map<String, Object> > result = null;
		try {
			result = attendService.findClassAttend(paraMap);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@RequestMapping(value="/insertattend.do")
	@ResponseBody
	public Integer insertAttend(@RequestParam Map<String, Object> paraMap){
		//验证：student/insertstudent.do?studentName=郭发阳&studentGender=男&studentPhone=15671628737&classID=1
		//插入在验证的时候会在本地数据库中连续插入两次，并且对中文还会出现乱码
		Integer result = null;
		try {
			result = attendService.insertAttend(paraMap);
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
