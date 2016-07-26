package com.zhang.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;

import com.zhang.util.MyClass;
import com.zhang.util.MyDeBug;
import com.zhang.util.Parameter;
import com.zhang.util.Teacher;


public class TeacherService {
	private String HOST = SyncHttp.host;
	private SyncHttp syncHttp = new SyncHttp();
	private static TeacherService teacherService = null;
	
	private TeacherService() {
		//构造方法私有化，外部不能实例
	}
	
	public static TeacherService getInstance(){
		if(teacherService == null){
			teacherService = new TeacherService();
		}
		return teacherService;
	}
	
	//登录时检查用户名和密码
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public Teacher checkUser(List<Parameter> params){
		String url = HOST + "/teacher/login.json";
		System.out.println( "url为" + url );
		Teacher teacher = null;
		try {
			String result = syncHttp.httpPost(url, params);
			System.out.println( "checkUserResult" + result );
			//这里得到的result本来就是一个字符串形式的JSON数据
			if(!result.isEmpty() && result!=null){
				//此时说明登陆成功！这个时候就获取返回的具体用户信息，赋值给Teacher类对象
				teacher = new Teacher();
				JSONObject resultJsonObject = new JSONObject(result);
				//把JSONobject型的String数据再转化成JSONobject数据
				JSONObject object = resultJsonObject.getJSONObject("TeacherUserMap");
				
				String teacherID = object.get("teacherID").toString();
				String teacherName = object.get("teacherName").toString();
				String teacherPassword = object.get("teacherPassword").toString();
				teacher.setTeacherID(teacherID);
				teacher.setTeacherName(teacherName);
				teacher.setPassword(teacherPassword);
				
			}
			
		} catch (Exception e) {
			System.out.println( e.getMessage() );
		}
		System.out.println(teacher);
		return teacher;
	}
	
	@SuppressLint("NewApi")
	public Integer insertTeacher(List<Parameter> params){
		String url = HOST + "/teacher/insertteacher.do";
		System.out.println( "url为" + url );
		Integer result = null;
		try {
			String res = syncHttp.httpPost(url, params);
			System.out.println( "插入用户的结果为：" + res );
			//这里得到的result本来就是一个字符串形式的JSON数据
			if(!res.isEmpty() && res!=null){
				//此时说明插入成功！这个时候就获取返回的具体用户信息，赋值给Teacher类对象
				result = Integer.parseInt( res );
				
			}
			
		} catch (Exception e) {
			System.out.println( e.getMessage() );
		}
		return result;
	}
	
	@SuppressLint("NewApi")
	public boolean findTeacherMailExist(String myTeacherMail){
		boolean flag = false ; //默认没有！
		String url = HOST + "/teacher/findallteacher.json";
		System.out.println( "url为" + url );
		try {
			String res = syncHttp.httpGet(url, "");
			System.out.println( "查找所有用户的结果为：" + res );
			if(!res.isEmpty() && res!=null){
				//此时说明插入成功！这个时候就获取返回的具体用户信息，赋值给Teacher类对象
				JSONObject jsonObject = new JSONObject(res);
				JSONArray jsonArray = jsonObject.getJSONArray("TeacherListMap");
				
				for(int i=0; i<jsonArray.length(); i++){
					JSONObject object = jsonArray.getJSONObject(i);

					String teacherMail = object.getString("teacherMail");
					MyDeBug.L("teacherMail-----" + teacherMail);
					
					if( myTeacherMail.equals(teacherMail) ){
						flag = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return flag;
	}
	
}
