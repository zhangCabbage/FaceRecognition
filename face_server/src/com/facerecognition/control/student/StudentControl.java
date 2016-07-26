package com.facerecognition.control.student;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.facerecognition.service.student.StudentServiceDAO;
import com.framework.control.BaseControl;

@Controller(value="StudentControl")
@RequestMapping(value="/student")
public class StudentControl extends BaseControl{
	
	@Autowired
	public StudentServiceDAO studentService;
	
	@RequestMapping(value="/findallstudent.json")
	@ModelAttribute(value="studentList")
	public List< Map<String, Object> > findAllStudent(@RequestParam Map<String, Object> paraMap){
		//验证：
		List< Map<String, Object> > result = null;
		try {
			result = studentService.findAllStudent(paraMap);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@RequestMapping(value="/insertstudent.do")
	@ResponseBody
	public Integer InsertStudent(@RequestParam Map<String, Object> paraMap){
		//验证：student/insertstudent.do?studentName=郭发阳&studentGender=男&studentPhone=15671628737&classID=1
		//插入在验证的时候会在本地数据库中连续插入两次，并且对中文还会出现乱码
		Integer result = null;
		try {
			result = studentService.insertStudent(paraMap);
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@RequestMapping(value="/deletestudent.do")
	@ResponseBody
	public Integer deleteStudent(@RequestParam Map<String, Object> paraMap){
		//验证方法:student/deletestudent.do?studentID=4
		Integer result = null;
		try {
			result = studentService.deleteStudent(paraMap);
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result ;
	}
}
