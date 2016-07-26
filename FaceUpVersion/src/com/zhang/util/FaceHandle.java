package com.zhang.util;

import org.json.JSONObject;

public class FaceHandle {
	
	private int number = 0;
	private Face[] faceList;
	public FaceHandle(){
		
	}
	
	public FaceHandle(JSONObject result, int number){
		this.number = number;
		faceList = new Face[number];
		try {
			for(int i=0; i<number; i++){
				JSONObject faceObject = result.getJSONArray( "face" ).getJSONObject(i);
				
				JSONObject attrObject = faceObject.getJSONObject("attribute");  //attribute  
				JSONObject posObject = faceObject.getJSONObject("position");  //position  
				
				faceList[i] = new Face();  
				faceList[i].setFaceId(faceObject.getString("face_id"));  
				faceList[i].setAgeValue(attrObject.getJSONObject("age").getInt("value"));  
				faceList[i].setAgeRange(attrObject.getJSONObject("age").getInt("range"));  
				faceList[i].setGenderValue(genderConvert(attrObject.getJSONObject("gender").getString("value")));  
				faceList[i].setGenderConfidence(attrObject.getJSONObject("gender").getDouble("confidence"));  
				faceList[i].setRaceValue(raceConvert(attrObject.getJSONObject("race").getString("value")));  
				faceList[i].setRaceConfidence(attrObject.getJSONObject("race").getDouble("confidence"));  
				faceList[i].setSmilingValue(attrObject.getJSONObject("smiling").getDouble("value"));  
				faceList[i].setCenterX(posObject.getJSONObject("center").getDouble("x"));  
				faceList[i].setCenterY(posObject.getJSONObject("center").getDouble("y"));  
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/**
	 * 性别转换（英文->中文）
	 * @param gender
	 * @return
	 */
	private static String genderConvert(String gender){
		String result = "男性";  
		if ("Female".equals(gender))
			result = "女性";
		return result;
	}

	private static String raceConvert(String race){
		String result = "黄色";
		if( "White".equals(race) ){
			result = "白色";
		}else if( "Black".equals(race) ){
			result = "黑色";
		}
		return result;
	}
	
	public Face[] getFaceList(){
		return faceList;
	}
	
	public String makeMessage(){
		StringBuffer resultBuffer = new StringBuffer();
		resultBuffer.append("共检测到").append(this.number).append("张人脸").append("\n");
		for(int i=0; i<this.number; i++){
			resultBuffer.append(faceList[i].getRaceValue()).append("人种，");
			resultBuffer.append(faceList[i].getGenderValue()).append("，");
			resultBuffer.append(faceList[i].getAgeValue()).append("岁左右\n");
		}
		return resultBuffer.toString();
	}
	
	public String makeSpecialMessage(){
		StringBuffer result = new StringBuffer();
		
		for(int i=0; i<this.number; i++){
			Face face = faceList[i];
			result.append(face.getRaceValue()).append("人种，");
			result.append(faceList[i].getGenderValue()).append("，");
			result.append( face.getAgeValue() ).append("岁，");
			if( face.getGenderValue().equals("女性") ){
				MyDeBug.L("女性");
				result.append( InfoConstant.Girl[face.getAgeValue()] );
			}else{
				result.append( InfoConstant.BOY[face.getAgeValue()] );
			}
		}
		
		return result.toString();
	}
	
	
}
