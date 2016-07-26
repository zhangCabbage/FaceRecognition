package com.zhang.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import com.zhang.util.MyClass;
import com.zhang.util.MyDeBug;
import com.zhang.util.Parameter;

/**
 * 表示对class类进行的操作
 * @author zhang
 *
 */
public class ClassService {
	private String HOST = SyncHttp.host;
	private SyncHttp syncHttp = new SyncHttp();
	private static ClassService classService = null;
	
	private ClassService() {
		//构造方法私有化，外部不能实例
	}
	
	public static ClassService getInstance(){
		if(classService == null){
			classService = new ClassService();
		}
		return classService;
	}
	
	/**
	 * 返回当前数据库中所有的class以及对应class内的人数
	 * @return
	 */
	public List<MyClass> findAllClass( List<Parameter> params ){
		String url = HOST + "/class/findclass.json";
		MyDeBug.L("url为" + url);
		
		List<MyClass> myClassList = new ArrayList<MyClass>() ; 
		try {
			String result = syncHttp.httpPost(url, params);
			MyDeBug.L("result为" + result);
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("classList");
			
			for(int i=0; i<jsonArray.length(); i++){
				MyClass myClass = new MyClass();
				JSONObject object = jsonArray.getJSONObject(i);

				int classID = object.getInt("classID");
				String className = object.getString("className");
				int classStuNum = object.getInt("classStuNum");
				MyDeBug.L("classID-----" + classID);
				MyDeBug.L("className-----" + className);
				MyDeBug.L("classStuNum-----" + classStuNum);
				
				myClass.setClassID(classID);
				myClass.setClassName(className);
				myClass.setClassStuNumber(classStuNum);
				
				myClassList.add(myClass);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		MyDeBug.L("结果");
		MyDeBug.L("classlist-----" + myClassList.toString());
		return myClassList;
	}
	
	/**
	 * 向数据库中添加一个班级，此时默认的班级学生人数为0
	 */
	@SuppressLint("NewApi")
	public Integer insertClass(List<Parameter> params){
		String url = HOST + "/class/insertclass.do" ; 
		MyDeBug.L("url为" + url);
		Integer result = null; 
		
		try {
			String re_String = syncHttp.httpPost(url, params);
			if( re_String != null && !re_String.isEmpty() ){
				result = Integer.parseInt(re_String);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result ; 
	}
	
	/**
	 * 删除一个班级，并且删除掉此班级下的所有学生
	 */
	@SuppressLint("NewApi")
	public Integer deleteClass(List<Parameter> parmas){
		Integer result = null;
		String url = HOST + "/class/deleteclass.do";
		MyDeBug.L(url);
		try {
			String re_String = syncHttp.httpPost(url, parmas);
			if( re_String != null && !re_String.isEmpty() ){
				result = Integer.parseInt(re_String);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@SuppressLint("NewApi")
	public Integer addUpdateClass(List<Parameter> parmas){
		Integer result = null;
		String url = HOST + "/class/addupdateclass.do";
		MyDeBug.L(url);
		try {
			String re_String = syncHttp.httpPost(url, parmas);
			if( re_String != null && !re_String.isEmpty() ){
				result = Integer.parseInt(re_String);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@SuppressLint("NewApi")
	public Integer deleteUpdateClass(List<Parameter> parmas){
		Integer result = null;
		String url = HOST + "/class/deleteupdateclass.do";
		MyDeBug.L(url);
		try {
			String re_String = syncHttp.httpPost(url, parmas);
			if( re_String != null && !re_String.isEmpty() ){
				result = Integer.parseInt(re_String);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		MyDeBug.L("删除更新的结果："+result);
		return result;
	}
	
}
