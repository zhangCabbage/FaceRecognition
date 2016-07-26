package com.zhang.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyStudent;
import com.zhang.util.Parameter;

/**
 * 表示对每个班的student类的操作
 * @author zhang
 *
 */
public class StudentService {
	
	private final String HOST = SyncHttp.host; 
	private SyncHttp syncHttp = new SyncHttp();
	private static StudentService studentService = null;
	private  StudentService(){
	}
	
	public static StudentService getInstance(){
		if(studentService == null){
			studentService = new StudentService();
		}
		return studentService;
	}
	
	public List<MyStudent> findAllStudent( List<Parameter> params ){
		String url = HOST + "/student/findallstudent.json" ; 
		MyDeBug.L("url为" + url);
		List<MyStudent> myStudentList = new ArrayList<MyStudent>();
		try {
			String result = syncHttp.httpPost(url, params);
			MyDeBug.L("result:" + result);
			
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("studentList");
			for(int i=0; i<jsonArray.length(); i++){
				MyStudent myStudent = new MyStudent();
//				stuID, stuName, stuGender, stuPhone
				int stuID = jsonArray.getJSONObject(i).getInt("stuID");
				String stuName = jsonArray.getJSONObject(i).getString("stuName");
				String stuGender = jsonArray.getJSONObject(i).getString("stuGender");
				String stuPhone = jsonArray.getJSONObject(i).getString("stuPhone");
				
				MyDeBug.L("stuID-----" + stuID);
				MyDeBug.L("stuName-----" + stuName);
				MyDeBug.L("stuGender-----" + stuGender);
				MyDeBug.L("stuPhone-----" + stuPhone);
				
				myStudent.setStudentID(stuID);
				myStudent.setStudentName(stuName);
				myStudent.setStudentGender(stuGender);
				myStudent.setStudentPhone(stuPhone);
				
				myStudentList.add(myStudent);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return myStudentList ; 
	}
	
	@SuppressLint("NewApi")
	public Integer insertStudent( List<Parameter> params ){
		Integer result = null;
		String url = HOST + "/student/insertstudent.do" ; 
		MyDeBug.L("url为" + url);
		try {
			String re_String = syncHttp.httpPost(url, params);
			if(re_String != null && !re_String.isEmpty()){
				result = Integer.parseInt( re_String );
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@SuppressLint("NewApi")
	public Integer deleteStudent( List<Parameter> params ){
		Integer result = null;
		String url = HOST + "/student/deletestudent.do" ; 
		MyDeBug.L("url为" + url);
		try {
			String re_String = syncHttp.httpPost(url, params);
			if(re_String != null && !re_String.isEmpty()){
				result = Integer.parseInt(re_String);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		MyDeBug.L("删除学生的结果为："+result);
		return result;
	}
}
