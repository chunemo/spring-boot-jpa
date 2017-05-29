package com.qiqi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 用户表
 * 
 * @author qc_zhong
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue
	private Long id;
	
	// 姓名
	private String name;
	
	// 年龄
	private Integer age;
}