/**
 * 
 */
package com.example.demo.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;


/**
 * @author tangqichang
 *
 * 2019年1月25日-下午4:58:38
 * 在对数据传输层操作的数据其实并不是我们业务逻辑层所操作的对象   我们只是将这些数据形式按不同的表去存储。
 */
public class UserModel{
	
	private Integer id;
	@NotBlank(message = "用户名不能为空")
	private String name;
	@NotNull(message="性别不能不填写")
	private Byte gender;
	@NotNull(message="年龄不能不填写")
	@Min(value=0,message="年龄必须大于0岁")
	@Max(value=150,message="年龄不能大于150岁")
	private String age;
	@NotBlank(message="手机号不得为空")
	private String telephone;
	private String registerMode;
	private String thirdPartyId;
	@NotBlank(message="密码不得为空")
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
