package com.zhang.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.zhang.util.MyAttend;
import com.zhang.util.MyClass;
import com.zhang.util.MyDeBug;
import com.zhang.util.MyStudent;
import com.zhang.util.Parameter;

public class AttendService {
	private final String HOST = SyncHttp.host; 
	private SyncHttp syncHttp = new SyncHttp();
	
	private static AttendService attendService = null;
	private AttendService(){
	}
	
	public static AttendService instance(){
		if(attendService == null){
			attendService = new AttendService();
		}
		return attendService;
	}
	
	public List<MyAttend> findAllStuAttend( List<Parameter> params ){
		String url = HOST + "/attend/findstuattend.json" ; 
		MyDeBug.L("url为" + url);
		List<MyAttend> stuAttendList = new ArrayList<MyAttend>();
		try {
			String result = syncHttp.httpPost(url, params);
			MyDeBug.L("result:" + result);
			
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("attendList");
			for(int i=0; i<jsonArray.length(); i++){
				MyAttend myAttend = new MyAttend();
//				stuID, stuName, stuGender, stuPhone
				long time = jsonArray.getJSONObject(i).getLong("time");
				boolean flag = jsonArray.getJSONObject(i).getInt("flag") == 1 ? true : false;
				
				myAttend.setTime(time);
				myAttend.setFlag(flag);
				
				stuAttendList.add(myAttend);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return stuAttendList ; 
	}
	
	public List<MyStudent> findAllStuOfClassAttend( List<Parameter> params ){
		String url = HOST + "/attend/findallstuofclassattend.json" ; 
		MyDeBug.L("url为" + url);
		List<MyStudent> stuAttendList = new ArrayList<MyStudent>();
		try {
			String result = syncHttp.httpPost(url, params);
			MyDeBug.L("result:" + result);
			
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("attendList");
			for(int i=0; i<jsonArray.length(); i++){
				MyStudent stuAttend = new MyStudent();
//				stuID, stuName, stuGender, stuPhone
				String stuName = jsonArray.getJSONObject(i).getString("stuName");
				int flag = jsonArray.getJSONObject(i).getInt("flag");
				
				stuAttend.setStudentName(stuName);
				stuAttend.setFlag(flag);
				
				stuAttendList.add(stuAttend);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return stuAttendList ; 
	}
	
	public List<MyAttend> findclassAttend( List<Parameter> params ){
		String url = HOST + "/attend/findclassattend.json" ; 
		MyDeBug.L("url为" + url);
		List<MyAttend> stuAttendList = new ArrayList<MyAttend>();
		try {
			String result = syncHttp.httpPost(url, params);
			MyDeBug.L("result:" + result);
			
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("attendList");
			for(int i=0; i<jsonArray.length(); i++){
				MyAttend myAttend = new MyAttend();
//				stuID, stuName, stuGender, stuPhone
				long time = jsonArray.getJSONObject(i).getLong("time");
				int totalNum = jsonArray.getJSONObject(i).getInt("total");
				
				myAttend.setTime(time);
				myAttend.setTotalNum(totalNum);
				
				stuAttendList.add(myAttend);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return stuAttendList ; 
	}
	
	@SuppressLint("NewApi")
	public Integer insertAttend( List<Parameter> params ){
		Integer result = null;
		String url = HOST + "/attend/insertattend.do" ; 
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
}
