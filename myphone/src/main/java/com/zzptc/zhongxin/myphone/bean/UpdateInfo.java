package com.zzptc.zhongxin.myphone.bean;

public class UpdateInfo implements java.io.Serializable {

	private int versionCode;//�汾��
	private String downloadUrl;//���ص�ַ
	private String versionName;//�汾���
	private String description;//�汾����
	
	
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
