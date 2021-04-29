package com.fanfish.app.model;

public class Student {	
	private int pk;
	private String name;
	private int age;	
	
	public Student(int pk,String name,int age) {
		this.pk =pk;
		this.name=name;
		this.age=age;
	}
	
	public int getPk() {
		return pk;
	}

	public void setPk(int pk) {
		this.pk = pk;
	}

	@Override
	public String toString(){
		return "["+this.name+"  "+this.age+"]";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
