package com.wander.danmu.danmu;

import java.io.Serializable;
/**
 * 评论实体
 * 
 * @author 王端晴
 * 
 */
public class CommentNew implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1001L;
	// "id": 1, //评论ID
	// "wid": 9, //作品id
	// "uid": 146298433, //评论者id
	// "avatar": "http%3A%2F%2Fimage.kuwo.cn%2Fus%2Fsecrets.gif", //评论者头像
	// "nick": "zhuhongfa10", //评论者昵称
	// "richlevel": 0, //评论者财富等级
	// "subcmt": 0, //该评论子评论条数
	// "content": "verynice", //评论内容
	// "tm": 1407313654 //评论时间
	// "touid": 147262628, //被回复者uid,若不是回复评论，则无该节点
	// "touname": "E9%87%8A" //被回复者用户名，若不是回复评论，则无该节点
	private String id;
	private String wid;
	private String uid;
	private String avatar;
	private String nick;
	private int richlevel;
	private int subcmt;
	private String content = "";
	private long tm;
	private String touid;
	private String touname;

	public String getTouid() {
		return touid;
	}

	public void setTouid(String touid) {
		this.touid = touid;
	}

	public String getTouname() {
		return touname;
	}

	public void setTouname(String touname) {
		this.touname = decodeUTF8(touname);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWid() {
		return wid;
	}

	public void setWid(String wid) {
		this.wid = wid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = decodeUTF8(avatar);
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = decodeUTF8(nick);
	}

	public int getRichlevel() {
		return richlevel;
	}

	public void setRichlevel(int richlevel) {
		this.richlevel = richlevel;
	}

	public int getSubcmt() {
		return subcmt;
	}

	public void setSubcmt(int subcmt) {
		this.subcmt = subcmt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = decodeUTF8(content);
	}

	public long getTm() {
		return tm;
	}

	public void setTm(long tm) {
		this.tm = tm;
	}

	private String decodeUTF8(String source) {
		return source;
	}

	@Override
	public String toString() {
		return "CommentNew{" +
				"wid='" + wid + '\'' +
				", uid='" + uid + '\'' +
				", avatar='" + avatar + '\'' +
				", nick='" + nick + '\'' +
				", content='" + content + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if(((CommentNew)o).getId().equals(getId())){
			return true;
		}else {
			return false;
		}
	}
}
