package com.facerecognition.control.mclass;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.facerecognition.service.mclass.MClassServiceDAO;
import com.framework.control.BaseControl;

@Controller(value="ClassControl")
@RequestMapping(value="/class")
public class ClassControl extends BaseControl {
	
	@Autowired
	MClassServiceDAO mClassService ; 
	
	@RequestMapping(value="/findclass.json")
	@ModelAttribute("classList")
	public List<Map<String, Object>> findAllClass(@RequestParam Map<String, Object> paraMap) {
		List<Map<String, Object>> result = null;
		try {
			result = mClassService.findAllClass(paraMap);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@RequestMapping(value="/insertclass.do")
	@ResponseBody
	public Integer insertClass(@RequestParam Map<String, Object> paraMap){
		Integer result = null;
		try {
			result = mClassService.insertClass(paraMap);
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@RequestMapping(value="/deleteclass.do")
	@ResponseBody
	public Integer deleteClass(@RequestParam Map<String, Object> paraMap){
		Integer result = null ; 
		try {
			result = mClassService.deleteClass(paraMap);
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@RequestMapping(value="/addupdateclass.do")
	@ResponseBody
	public Integer addUpdateClass(@RequestParam Map<String, Object> paraMap){
		Integer result = null ; 
		try {
			result = mClassService.addUpdateClass(paraMap);
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@RequestMapping(value="/deleteupdateclass.do")
	@ResponseBody
	public Integer deleteUpdateClass(@RequestParam Map<String, Object> paraMap){
		Integer result = null ; 
		try {
			result = mClassService.deleteUpdateClass(paraMap);
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
