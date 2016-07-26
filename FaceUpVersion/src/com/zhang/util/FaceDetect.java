package com.zhang.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.faceplusplus.api.FaceDetecter;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;


public class FaceDetect {
	final private String apiKey = "b1d472e2239f5fc6e449fd1443c5f7e3" ; 
	final private String apiSecret = "rWAOtP5VcghFNY5WqzzgpeFI8UKMshaG" ; 
	HttpRequests httpRequests = null ; 
	private Context mContext = null;
	
	private HashMap<Integer, String> studentsNameIDMap = null ; 
	
	HandlerThread detectThread = null ; 
    Handler detectHandler = null ; 
    FaceDetecter detecter = null ; 
	
	public FaceDetect(Context context) {
		this.mContext = context;
		httpRequests = new HttpRequests(apiKey , apiSecret, true, true);
		
		detectThread = new HandlerThread("detect");
        detectThread.start();
        detectHandler = new Handler(detectThread.getLooper());
        detecter = new FaceDetecter();
        detecter.init(this.mContext, apiKey);
		
        studentsNameIDMap = new HashMap<Integer, String>();
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public String getApiSecret() {
		return apiSecret;
	}
	
	public HttpRequests getHttpRequests(){
		return httpRequests ; 
	}
	
	public Handler getHandler(){
		return detectHandler;
	}
	
    public FaceDetecter getDetecter(){
    	return detecter;
    }
    
	/**
	 * 返回图片中人脸数
	 */
	public int getFaceNumber(JSONObject result){
		int number = 0 ; 
		if(result != null){
			try {
				number = result.getJSONArray("face").length();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return number;
	}
	
	/**
	 * 通过person_name删除Face++中一个person的所有人脸信息等
	 * @return
	 */
	public int deletePerson(String person_name){
		int number = 0; 
		try {
			
			PostParameters postParameters_person = new PostParameters().setPersonName( person_name ); 
			JSONObject result = httpRequests.personDelete(postParameters_person);
			number = result.getInt("deleted") ; 
		} catch (Exception e) {
			
		}
		return number ; 
	}
	
	/**
	 * 通过face_id向Face++中添加一张人脸
	 */
	public int personAddFace(String person_name, String face_id){
		int number = 0 ;
		try {
			JSONArray person_List = httpRequests.infoGetPersonList().getJSONArray("person"); 
			int is_Person_Exist = 0 ; 
			for(int i = 0 ; i < person_List.length() ; i++){
				String name = person_List.getJSONObject(i).getString("person_name") ; 
//				System.out.println(name);
				if( name.equals( person_name ) ){
					//说明存在相同的person，不用创建
					is_Person_Exist = 1 ; 
//					System.out.println("存在相同名字的Person！") ; 
					break ; 
				}
			}
			
			PostParameters postParameters_person = new PostParameters().setPersonName( person_name ); 
			if(is_Person_Exist == 0){
				httpRequests.personCreate( postParameters_person ) ; //不存在同名的人 ，创建Person
			}
			JSONObject result = httpRequests.personAddFace( postParameters_person.setFaceId( face_id ) );
			number = result.getInt("added") ; 
		} catch (Exception e) {
			
		}
		return number ; 
	}
	
	
	public int deleteGroup(String group_name){
		int number = 0; 
		try {
			
			PostParameters postParameters_group = new PostParameters().setGroupName( group_name ) ;
			JSONObject result = httpRequests.groupDelete(postParameters_group);
			number = result.getInt("deleted") ; 
		} catch (Exception e) {
			
		}
		return number ; 
	}
	/**
	 * 创建一个group，并向其中添加person
	 */
	public int groupAddPerson(String group_name, String person_name){
		int number = 0 ; 
		try {
			JSONArray group_List = httpRequests.infoGetGroupList().getJSONArray("group") ; 
			int is_Group_Exist = 0 ; 
			for(int i = 0 ; i < group_List.length() ; i++){
				String name = group_List.getJSONObject(i).getString("group_name") ; 
				if( name.equals(group_name) ){
					//说明存在相同的group，不用创建
					is_Group_Exist = 1 ;
					break ; 
				}
			}
			PostParameters postParameters_group = new PostParameters().setGroupName( group_name ) ;
			if(is_Group_Exist == 0){
				httpRequests.groupCreate( postParameters_group );
			}
			JSONObject result = httpRequests.groupAddPerson( postParameters_group.setPersonName(person_name) ) ; 
			//{"added": 2, "success": true }
			number = result.getInt("added") ; 
		} catch (Exception e) {
			
		}
		return number ; 
	}
	
	/**
	 * 创建一个FaceSet，并向其中添加face
	 */
	public int facesetAddFace(String faceset_name, String face_id){
		int number = 0 ;
		try {
			JSONArray faceset_List = httpRequests.infoGetFacesetList().getJSONArray("faceset") ; 
			int is_Faceset_Exist = 0 ; 
			for(int i = 0 ; i < faceset_List.length() ; i++){
				String name = faceset_List.getJSONObject(i).getString("faceset_name") ; 
				if( name.equals(faceset_name) ){
					is_Faceset_Exist = 1 ;
					break ; 
				}
			}
			PostParameters postParameters_faceset = new PostParameters().setFacesetName( faceset_name ) ;
			if(is_Faceset_Exist == 0){
				httpRequests.facesetCreate( postParameters_faceset );
			}
			JSONObject result = httpRequests.facesetAddFace( postParameters_faceset.setFaceId(face_id) ) ; 
			number = result.getInt("added") ; 
		} catch (Exception e) {
			
		}
		return number ; 
	}
	
	/**
	 * /train/search，以用来在该Faceset内搜索最相似的Face
	 */
	public String trainSearch(String faceSet_name){
		String result_Search = "" ; 
		try {
			JSONObject search = httpRequests.trainSearch( new PostParameters().setFacesetName( faceSet_name ) ) ; 
			System.out.println(search) ;
			result_Search = httpRequests.getSessionSync( search.getString("session_id") ).getJSONObject("result").getString("success") ;
			System.out.println(result_Search) ;
		} catch (Exception e) {
			
		}
		return result_Search ; 
	}
	
	/**
	 * /train/identify，对一个Group进行训练，用来在一个Group中查询最相似的Person
	 */
	public String trainIdentify(String group_name){
		String result_Identify = "" ; 
		try {
			JSONObject identify = httpRequests.trainIdentify( new PostParameters().setGroupName( group_name ) ) ; 
			result_Identify = httpRequests.getSessionSync( identify.getString("session_id") ).getJSONObject("result").getString("success") ;
			MyDeBug.L("result_Identify的结果------" + result_Identify);
		} catch (Exception e) {
			
		}
		return result_Identify ; 
	}
	
	/**
	 * /train/verify，对一个person进行训练，用来辨别一个Face和一个Person，是否是同一个人的判断以及置信度。
	 */
	public String trainVerify(String person_name){
		String result_Verify = "" ; 
		try {
			JSONObject verify = httpRequests.trainVerify( new PostParameters().setPersonName( person_name ) );
			result_Verify = httpRequests.getSessionSync( verify.getString("session_id") ).getJSONObject("result").getString("success") ;
			MyDeBug.L("trainVerify的结果------" + result_Verify);
		} catch (Exception e) {
			
		}
		return result_Verify ; 
	}
	
	/**
	 * 得到传入的result中所有人脸对应的人名，以及对应置信度
	 */
	public void findAllFaceName(JSONObject result, int number, String classGroupName) throws Exception{
		
		for(int i = 0 ; i < number ; i++){
			//逐个识别
			//图片中每个人都对应返回多个识别出的人名！
			String faceID = result.getJSONArray( "face" ).getJSONObject(i).getString("face_id") ; 
			MyDeBug.L( "resultFaceID = " + faceID );
			//recognition
			JSONArray recognition_Identify = httpRequests.recognitionIdentify( new PostParameters().setGroupName( classGroupName ).setKeyFaceId(faceID) ).getJSONArray("face") ; 
			MyDeBug.L( "recognition_Identify = " + recognition_Identify );
			
			MyDeBug.L( "recognition_length = " + recognition_Identify.length() );//这里的recognition_length每次都只有一个"face"的数组
			MyDeBug.L( "recognition_candidate_length = " + recognition_Identify.getJSONObject(0).getJSONArray("candidate").length() );//指一张人脸对应的人脸，一般为3
			
			MyDeBug.L( "=============识别=================" );
			//这里我们只选择其中最大的一个存储！
			
			String person_name = recognition_Identify.getJSONObject(0).getJSONArray( "candidate" ).getJSONObject(0).getString( "person_name" ) ; 
			MyDeBug.L(person_name);
			
			int confid = recognition_Identify.getJSONObject(0).getJSONArray( "candidate" ).getJSONObject(0).getInt( "confidence" ) ; 
			MyDeBug.L("置信度----" + confid);
			MyDeBug.L( "=============结果=================" );
			
//			String person_name = null ; //每个识别出的人名
//			int confidence = 0 ; //对应人名的识别的置信度
//			int j=0 ; 
//			int len = recognition_Identify.getJSONObject(0).getJSONArray("candidate").length();
//			for(; j<len; j++){
//				person_name = recognition_Identify.getJSONObject(0).getJSONArray( "candidate" ).getJSONObject(j).getString( "person_name" ) ; 
//				confidence = recognition_Identify.getJSONObject(0).getJSONArray( "candidate" ).getJSONObject(j).getInt( "confidence" ) ; 
//				System.out.println(person_name);
//				System.out.println(confidence);
//				JSONObject recognition_Verify = httpRequests.recognitionVerify( new PostParameters().setPersonName( person_name ).setFaceId(faceID) );
//				System.out.println(recognition_Verify);
//				boolean isSamePerson = recognition_Verify.getBoolean("is_same_person") ; 
//				if(isSamePerson == true){
//					break ; 
//				}
//			}
			
//			JSONObject recognition_search= httpRequests.recognitionSearch( new PostParameters().setFacesetName("my_face_set").setKeyFaceId(faceID) ) ; 
//			System.out.println("recognition_search = " + recognition_search);
//			JSONArray search = recognition_search.getJSONArray("candidate") ; 
//			for(int x=0; x<search.length(); x++){
//				System.out.println("similarity" + search.getJSONObject(x).getString("similarity"));
//				System.out.println("tag" + search.getJSONObject(x).getString("tag"));
//			}
//			person_name = recognition_Identify.getJSONArray( "face" ).getJSONObject(0).getJSONArray( "candidate" ).getJSONObject(0).getString( "person_name" ) ; 
//			confidence = recognition_Identify.getJSONArray( "face" ).getJSONObject(0).getJSONArray( "candidate" ).getJSONObject(0).getInt( "confidence" ) ; 
//			System.out.println(person_name);
//			System.out.println(confidence);
//			if( j == len){
//				//如果根据人脸识别出的置信度在35以下，则判断此人不在Face++数据库中。
//				person_name = "未知" ; 
//			}
			if(confid >= 30){
				//这个函数得到的名字为：classID_studentID_studentName，那么要对String进行处理
				Pattern pattern = Pattern.compile("_");
				String[] strs = pattern.split(person_name);
				
				this.studentsNameIDMap.put(Integer.parseInt(strs[1]), strs[2]);
			}
		}
	}

	public HashMap<Integer, String> getStudentsNameIDMap() {
		return studentsNameIDMap;
	}
}
