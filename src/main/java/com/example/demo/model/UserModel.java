/**
 * 
 */
package com.example.demo.model;

/**
 * @author tangqichang
 *
 * 2019年1月25日-下午4:58:38
 * 在对数据传输层操作的数据其实并不是我们业务逻辑层所操作的对象   我们只是将这些数据形式按不同的表去存储。
 */
public class UserModel {
	private Integer id;
	private String name;
	private Byte gender;
	private String age;
	private String telephone;
	private String registerMode;
	private String thirdPartyId;
	
	private String encrpt_password;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	
	public String getRegisterMode() {
		return registerMode;
	}

	public void setRegisterMode(String registerMode) {
		this.registerMode = registerMode;
	}

	public String getThirdPartyId() {
		return thirdPartyId;
	}

	public void setThirdPartyId(String thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}

	public String getEncrpt_password() {
		return encrpt_password;
	}

	public void setEncrpt_password(String encrpt_password) {
		this.encrpt_password = encrpt_password;
	}
	
	
}
